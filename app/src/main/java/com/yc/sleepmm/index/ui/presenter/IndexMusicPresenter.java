package com.yc.sleepmm.index.ui.presenter;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.music.player.lib.bean.MusicInfo;
import com.music.player.lib.util.ToastUtils;
import com.yc.sleepmm.base.APP;
import com.yc.sleepmm.base.presenter.BasePresenter;
import com.yc.sleepmm.base.util.CommonInfoHelper;
import com.yc.sleepmm.index.constants.SpConstant;
import com.yc.sleepmm.index.model.bean.MusicTypeInfo;
import com.yc.sleepmm.index.model.bean.UserInfo;
import com.yc.sleepmm.index.model.engine.EngineUtils;
import com.yc.sleepmm.index.model.engine.IndexMusicEngine;
import com.yc.sleepmm.index.rxnet.IndexMusicContract;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2018/2/8 10:21.
 */

public class IndexMusicPresenter extends BasePresenter<IndexMusicEngine, IndexMusicContract.View> implements IndexMusicContract.Presenter {
    public IndexMusicPresenter(Context mContext, IndexMusicContract.View view) {
        super(mContext, view);
        mEngine = new IndexMusicEngine(mContext);

    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;
        getMusicTypes();
    }

    @Override
    public void getMusicTypes() {

        CommonInfoHelper.getO(mContext, SpConstant.MUSIC_TYPE_INFO, new TypeReference<List<MusicTypeInfo>>() {
        }.getType(), new CommonInfoHelper.onParseListener<List<MusicTypeInfo>>() {
            @Override
            public void onParse(List<MusicTypeInfo> list) {

                mView.showMusicTypeInfo(list);
            }
        });

        Subscription subscription = mEngine.getMusicTypes().subscribe(new Subscriber<ResultInfo<List<MusicTypeInfo>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<List<MusicTypeInfo>> listResultInfo) {
                if (listResultInfo != null && listResultInfo.code == HttpConfig.STATUS_OK && listResultInfo.data != null) {
                    mView.showMusicTypeInfo(listResultInfo.data);
//                    CacheUtils.writeCache(mContext, SpConstant.MUSIC_TYPE_INFO, JSON.toJSONString(listResultInfo.data));
                    CommonInfoHelper.setO(mContext, listResultInfo.data, SpConstant.MUSIC_TYPE_INFO);

                }
            }
        });
        mSubscriptions.add(subscription);
    }


    public void randomPlay() {

        Subscription subscription = EngineUtils.randomPlay(mContext).subscribe(new Subscriber<ResultInfo<MusicInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<MusicInfo> musicInfoResultInfo) {
                if (musicInfoResultInfo != null && musicInfoResultInfo.code == HttpConfig.STATUS_OK && musicInfoResultInfo.data != null) {
                    mView.showRandomMusicInfo(musicInfoResultInfo.data);

                }
            }
        });
        mSubscriptions.add(subscription);
    }


    /**
     * 音乐收藏
     *
     * @param
     * @param musicInfo
     */
    public void collectMusic(final MusicInfo musicInfo) {


        if (!APP.getInstance().isGotoLogin(mContext)) {

            if (musicInfo == null) {
                ToastUtils.showCenterToast("请先选择一首歌曲收藏");
                return;
            }


            if (musicInfo.getType() == 2) {
                ToastUtils.showCenterToast("当前播放的是spa音乐，不能" + (musicInfo.getIs_favorite() == 1 ? "取消收藏" : "收藏"));
                return;
            }

            Subscription subscription = EngineUtils.collectMusic(mContext, APP.getInstance().getUserData().getId(), musicInfo.getId()).subscribe(new Subscriber<ResultInfo<String>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(ResultInfo<String> stringResultInfo) {
                    boolean isCollect = musicInfo.getIs_favorite() == 1;
                    if (stringResultInfo != null && stringResultInfo.code == HttpConfig.STATUS_OK) {

                        isCollect = !isCollect;
                        musicInfo.setIs_favorite(isCollect ? 1 : 0);
                        mView.showCollectSucess(isCollect);
                    }
                }
            });
            mSubscriptions.add(subscription);
        }
    }

    public void playStatistics(String music_id) {
        Subscription subscription = EngineUtils.playStatistics(mContext, music_id).subscribe(new Subscriber<ResultInfo<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<String> stringResultInfo) {

            }
        });

        mSubscriptions.add(subscription);

    }


    public void getUserInfo() {
        Subscription subscription = EngineUtils.getUserInfo(mContext, APP.getInstance().getUid()).subscribe(new Subscriber<ResultInfo<UserInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<UserInfo> userInfoResultInfo) {
                if (userInfoResultInfo != null && userInfoResultInfo.code == HttpConfig.STATUS_OK && userInfoResultInfo.data != null) {
                    APP.getInstance().setUserData(userInfoResultInfo.data, true);
                }
            }
        });
        mSubscriptions.add(subscription);

    }
}
