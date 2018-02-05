package com.yc.sleepmm.setting.presenter;

import android.content.Context;

import com.kk.pay.OrderInfo;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.sleepmm.base.presenter.BasePresenter;
import com.yc.sleepmm.setting.contract.CreateOrderContract;
import com.yc.sleepmm.setting.engine.CreateOrderEngine;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2018/2/4 10:44.
 */

public class CreateOrderPresenter extends BasePresenter<CreateOrderEngine, CreateOrderContract.View> implements CreateOrderContract.Presenter {
    public CreateOrderPresenter(Context mContext, CreateOrderContract.View view) {
        super(mContext, view);
        mEngine = new CreateOrderEngine(mContext);
    }


    @Override
    protected void loadData(boolean forceUpdate) {

    }

    @Override
    public void getOrderInfo(String user_id, String title, String money, String pay_way_name) {
        Subscription subscription = mEngine.getOrderInfos(user_id, title, money, pay_way_name).subscribe(new Subscriber<ResultInfo<OrderInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<OrderInfo> orderInfoResultInfo) {
                if (orderInfoResultInfo != null && orderInfoResultInfo.code == HttpConfig.STATUS_OK && orderInfoResultInfo.data != null) {
                    mView.showOrderInfos(orderInfoResultInfo.data);
                }
            }
        });

        mSubscriptions.add(subscription);

    }
}
