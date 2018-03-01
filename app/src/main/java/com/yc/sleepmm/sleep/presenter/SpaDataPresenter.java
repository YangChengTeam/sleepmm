package com.yc.sleepmm.sleep.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.sleepmm.base.presenter.BasePresenter;
import com.yc.sleepmm.sleep.contract.SpaDataContract;
import com.yc.sleepmm.sleep.model.bean.SpaDataInfo;
import com.yc.sleepmm.sleep.model.bean.SpaItemInfo;
import com.yc.sleepmm.sleep.model.engine.SpaDataInfoEngine;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;


public class SpaDataPresenter extends BasePresenter<SpaDataInfoEngine, SpaDataContract.View> implements SpaDataContract.Presenter {

    public SpaDataPresenter(Context mContext, SpaDataContract.View view) {
        super(mContext, view);
        mEngine = new SpaDataInfoEngine(mContext);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }

    @Override
    public void getSpaDataList() {
        mView.showLoading();
        Subscription subscription = mEngine.getSpaDataInfo().subscribe(new Subscriber<ResultInfo<List<SpaDataInfo>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showNoNet();
            }

            @Override
            public void onNext(ResultInfo<List<SpaDataInfo>> spaDetailInfoResultInfo) {
                if (spaDetailInfoResultInfo != null && spaDetailInfoResultInfo.code == HttpConfig.STATUS_OK) {
                    if (spaDetailInfoResultInfo.data != null && spaDetailInfoResultInfo.data.size() > 0) {
                        mView.hide();
                    } else {
                        mView.showNoData();
                    }
                    mView.showSpaData(spaDetailInfoResultInfo.data);
                } else {
                    mView.showNoData();
                }
            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void getSpaItemList(String typeId, int page, int limit) {
        Subscription subscription = mEngine.getSpaItemList(typeId, page, limit).subscribe(new Subscriber<ResultInfo<List<SpaItemInfo>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<List<SpaItemInfo>> spaDetailInfoResultInfo) {
                mView.showSpaItemList(spaDetailInfoResultInfo.data);
            }
        });
        mSubscriptions.add(subscription);
    }


    public void getSpaItemList(String typeId, int page, int limit, final int position) {
        Subscription subscription = mEngine.getSpaItemList(typeId, page, limit).subscribe(new Subscriber<ResultInfo<List<SpaItemInfo>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<List<SpaItemInfo>> spaDetailInfoResultInfo) {
                mView.showSpaItemList(spaDetailInfoResultInfo.data, position);
            }
        });
        mSubscriptions.add(subscription);
    }
}
