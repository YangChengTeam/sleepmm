package com.yc.sleepmm.setting.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.music.player.lib.bean.MusicInfo;
import com.yc.sleepmm.base.presenter.BasePresenter;
import com.yc.sleepmm.setting.contract.CollectContract;
import com.yc.sleepmm.setting.engine.CollectEngine;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2018/2/11 09:50.
 */

public class CollectPresenter extends BasePresenter<CollectEngine, CollectContract.View> implements CollectContract.Presenter {
    public CollectPresenter(Context mContext, CollectContract.View view) {
        super(mContext, view);
        mEngine = new CollectEngine(mContext);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }

    @Override
    public void getSpaFavoriteList(final int page, int limit) {
        if (page == 1)
            mView.showLoading();
        Subscription subscription = mEngine.getSpaFavoriteList(page, limit).subscribe(new Subscriber<ResultInfo<List<MusicInfo>>>() {
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
                if (listResultInfo != null && listResultInfo.code == HttpConfig.STATUS_OK && listResultInfo.data != null) {

                    mView.showSpaCollectList(listResultInfo.data);
                    if (page == 1) {
                        mView.hide();
                    }
                } else {
                    if (page == 1) {
                        mView.showNoData();
                    }
                }

            }
        });

        mSubscriptions.add(subscription);

    }

    @Override
    public void getMusicFavoriteList(final int page, int limit) {
        if (page == 1)
            mView.showLoading();
        Subscription subscription = mEngine.getMusicFavoriteList(page, limit).subscribe(new Subscriber<ResultInfo<List<MusicInfo>>>() {
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
                if (listResultInfo != null && listResultInfo.code == HttpConfig.STATUS_OK && listResultInfo.data != null) {
                    mView.showMusicCollectList(listResultInfo.data);
                    if (page == 1) {
                        mView.hide();
                    }
                } else {
                    if (page == 1) {
                        mView.showNoData();
                    }
                }
            }
        });
        mSubscriptions.add(subscription);
    }
}
