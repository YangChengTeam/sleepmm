package com.yc.sleepmm.sleep.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.ToastUtil;
import com.music.player.lib.bean.MusicInfo;
import com.music.player.lib.util.PreferencesUtil;
import com.yc.sleepmm.base.APP;
import com.yc.sleepmm.base.presenter.BasePresenter;
import com.yc.sleepmm.index.model.engine.EngineUtils;
import com.yc.sleepmm.sleep.contract.SpaDetailContract;
import com.yc.sleepmm.sleep.model.engine.SpaDetailEngine;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2018/2/11 09:10.
 */

public class SpaDetailPresenter extends BasePresenter<SpaDetailEngine, SpaDetailContract.View> implements SpaDetailContract.Presenter {
    public SpaDetailPresenter(Context mContext, SpaDetailContract.View view) {
        super(mContext, view);
        mEngine = new SpaDetailEngine(mContext);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }


//    @Override
//    public void getSpaDetailInfo(String spa_id) {
//        mView.showLoading();
//        Subscription subscription = mEngine.getSpaDetailInfo(spa_id).subscribe(new Subscriber<ResultInfo<MusicInfo>>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                mView.showNoNet();
//            }
//
//            @Override
//            public void onNext(ResultInfo<MusicInfo> spaDetailInfoResultInfo) {
//                if (spaDetailInfoResultInfo != null && spaDetailInfoResultInfo.code == HttpConfig.STATUS_OK && spaDetailInfoResultInfo.data != null) {
//                    mView.showSpaDetailInfo(spaDetailInfoResultInfo.data, false);
//                    mView.hide();
//                } else {
//                    mView.showNoData();
//                }
//            }
//        });
//        mSubscriptions.add(subscription);
//    }

    @Override
    public void randomSpaInfo(String type_id) {
        Subscription subscription = mEngine.randomSpaInfo(type_id).subscribe(new Subscriber<ResultInfo<List<MusicInfo>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<List<MusicInfo>> musicInfoResultInfo) {
                if (musicInfoResultInfo != null && musicInfoResultInfo.code == HttpConfig.STATUS_OK && musicInfoResultInfo.data != null) {
                    mView.showSpaDetailInfo(musicInfoResultInfo.data, true);
                }
            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void spaPlay(String music_id) {
        Subscription subscription = mEngine.spaPlay(music_id).subscribe(new Subscriber<ResultInfo<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<String> musicInfoResultInfo) {
                if (musicInfoResultInfo != null && musicInfoResultInfo.code == HttpConfig.STATUS_OK && musicInfoResultInfo.data != null) {

                }
            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void collectSpa(final String spa_id) {
        if (!APP.getInstance().isGotoLogin(mContext)) {

            if (TextUtils.isEmpty(spa_id)) {
                ToastUtil.toast(mContext, "请先选择一首歌曲收藏");
                return;
            }

            Subscription subscription = mEngine.collectSpa(APP.getInstance().getUserData().getId(), spa_id).subscribe(new Subscriber<ResultInfo<String>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(ResultInfo<String> stringResultInfo) {
                    boolean isCollect = PreferencesUtil.getInstance().getBoolean(spa_id);
                    if (stringResultInfo != null && stringResultInfo.code == HttpConfig.STATUS_OK) {

                        isCollect = !isCollect;
                        PreferencesUtil.getInstance().putBoolean(spa_id, isCollect);

                        mView.showCollectSucess(isCollect);
                    }
                }
            });
            mSubscriptions.add(subscription);
        }
    }

    public void getSpaDetailList(String typeId, final int page, final int limit, final String spaId) {
        if (page == 1)
            mView.showLoading();
        Subscription subscription = EngineUtils.getSpaItemList(mContext, typeId, page, limit).subscribe(new Subscriber<ResultInfo<List<MusicInfo>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (page == 1)
                    mView.showNoNet();
            }

            @Override
            public void onNext(ResultInfo<List<MusicInfo>> spaDetailInfoResultInfo) {
                if (spaDetailInfoResultInfo != null && spaDetailInfoResultInfo.code == HttpConfig.STATUS_OK) {
                    if (spaDetailInfoResultInfo.data != null && spaDetailInfoResultInfo.data.size() > 0) {
                        mView.hide();
                        if (page == 1) {
                            mView.showSpaDetailList(filterData(spaDetailInfoResultInfo.data, spaId));
                        } else {
                            mView.showSpaDetailList(spaDetailInfoResultInfo.data);
                        }
                    } else {
                        if (page == 1)
                            mView.showNoData();
                    }
                } else {
                    if (page == 1)
                        mView.showNoNet();
                }
            }
        });
        mSubscriptions.add(subscription);
    }


    private List<MusicInfo> filterData(List<MusicInfo> list, String spaId) {

        MusicInfo currentInfo = null;
        if (list.size() > 0) {
            for (MusicInfo musicInfo : list) {
                if (musicInfo.getId().equals(spaId)) {
                    currentInfo = musicInfo;
                    break;
                }
            }
            if (currentInfo != null) {
                list.remove(currentInfo);
                list.add(0, currentInfo);
            }
        }
        return list;

    }
}
