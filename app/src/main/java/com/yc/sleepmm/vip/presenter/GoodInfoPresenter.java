package com.yc.sleepmm.vip.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.sleepmm.base.presenter.BasePresenter;
import com.yc.sleepmm.vip.bean.GoodsInfo;
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
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;
        getGoodInfos("1", 1, 10);
    }


    @Override
    public void getGoodInfos(String type_id, int page, final int limit) {

        Subscription subscription = mEngine.getGoodInfoList(type_id, page, limit).subscribe(new Subscriber<ResultInfo<List<GoodsInfo>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<List<GoodsInfo>> listResultInfo) {
                if (listResultInfo != null && listResultInfo.code == HttpConfig.STATUS_OK && listResultInfo.data != null) {
                    mView.showGoodInfos(listResultInfo.data);
                }
            }
        });
        mSubscriptions.add(subscription);
    }


}
