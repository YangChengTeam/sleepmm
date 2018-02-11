package com.yc.sleepmm.sleep.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.music.player.lib.bean.MusicInfo;
import com.yc.sleepmm.base.presenter.BasePresenter;
import com.yc.sleepmm.sleep.contract.SpaDetailContract;
import com.yc.sleepmm.sleep.model.engine.SpaDetailEngine;

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


    @Override
    public void getSpaDetailInfo(String spa_id) {
        mView.showLoading();
        Subscription subscription = mEngine.getSpaDetailInfo(spa_id).subscribe(new Subscriber<ResultInfo<MusicInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showNoNet();
            }

            @Override
            public void onNext(ResultInfo<MusicInfo> spaDetailInfoResultInfo) {
                if (spaDetailInfoResultInfo != null && spaDetailInfoResultInfo.code == HttpConfig.STATUS_OK && spaDetailInfoResultInfo.data != null) {
                    mView.showSpaDetailInfo(spaDetailInfoResultInfo.data);
                    mView.hide();
                } else {
                    mView.showNoData();
                }
            }
        });
        mSubscriptions.add(subscription);
    }
}
