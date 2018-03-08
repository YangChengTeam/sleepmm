package com.yc.sleepmm.index.ui.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.music.player.lib.bean.MusicInfo;
import com.yc.sleepmm.base.presenter.BasePresenter;
import com.yc.sleepmm.index.model.engine.IndexMusicTypeDetailEngine;
import com.yc.sleepmm.index.rxnet.IndexMusicTypeDetailContract;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2018/2/8 11:02.
 */

public class IndexMusicTypeDetailPresenter extends BasePresenter<IndexMusicTypeDetailEngine, IndexMusicTypeDetailContract.View> implements IndexMusicTypeDetailContract.Presenter {
    public IndexMusicTypeDetailPresenter(Context mContext, IndexMusicTypeDetailContract.View view) {
        super(mContext, view);
        mEngine = new IndexMusicTypeDetailEngine(mContext);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }

    @Override
    public void getMusicInfos(final String type_id, final int page, int limit, boolean isRefresh) {

        if (page == 1 && !isRefresh) {
            mView.showLoading();
        }
        Subscription subscription = mEngine.getMusicInfos(type_id, page, limit).subscribe(new Subscriber<ResultInfo<List<MusicInfo>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (page == 1) {
                    mView.showNoNet();
                }
            }

            @Override
            public void onNext(ResultInfo<List<MusicInfo>> listResultInfo) {
                if (listResultInfo != null && listResultInfo.code == HttpConfig.STATUS_OK) {
                    if (listResultInfo.data != null && listResultInfo.data.size() > 0) {
                        mView.hide();
                        setTypeInfo(listResultInfo.data);
                    } else {
                        if (page == 1) {
                            mView.showNoData();
                        }
                    }
                } else {
                    if (page == 1) {
                        mView.showNoNet();
                    }
                }
            }
        });
        mSubscriptions.add(subscription);
    }

    private void setTypeInfo(List<MusicInfo> infos) {
        if (infos != null && infos.size() > 0) {
            for (MusicInfo info : infos) {
                info.setType(1);
            }
        }
        mView.showMusicInfos(infos);

    }


}
