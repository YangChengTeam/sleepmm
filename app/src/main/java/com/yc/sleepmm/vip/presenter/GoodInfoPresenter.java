package com.yc.sleepmm.vip.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.sleepmm.base.presenter.BasePresenter;
import com.yc.sleepmm.vip.bean.GoodInfo;
import com.yc.sleepmm.vip.bean.PayInfo;
import com.yc.sleepmm.vip.contract.GoodInfoContract;
import com.yc.sleepmm.vip.engine.GoodInfoEngine;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2018/1/25 13:35.
 */

public class GoodInfoPresenter extends BasePresenter<GoodInfoEngine, GoodInfoContract.View> implements GoodInfoContract.Presenter {
    public GoodInfoPresenter(Context context, GoodInfoContract.View view) {
        super(context, view);
        mEngine = new GoodInfoEngine(context);
    }

    @Override
    protected void loadData(boolean forceUpdate) {
        if (!forceUpdate) return;
        getGoodInfos();
        getPayInfos();
    }


    @Override
    public void getGoodInfos() {
        Subscription subscription = mEngine.getGoodInfoList().subscribe(new Subscriber<List<GoodInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<GoodInfo> goodInfos) {
                mView.showGoodInfos(goodInfos);
            }
        });
        mSubscriptions.add(subscription);

    }

    @Override
    public void getPayInfos() {
        Subscription subscription = mEngine.getPayInfos().subscribe(new Subscriber<ResultInfo<List<PayInfo>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<List<PayInfo>> listResultInfo) {
                if (listResultInfo != null && listResultInfo.code == HttpConfig.STATUS_OK && listResultInfo.data != null) {
                    mView.showPayInfos(listResultInfo.data);
                }
            }
        });
        mSubscriptions.add(subscription);
    }
}
