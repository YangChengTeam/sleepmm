package com.yc.sleepmm.setting.presenter;

import android.content.Context;

import com.yc.sleepmm.base.presenter.BasePresenter;
import com.yc.sleepmm.setting.bean.FindCenterInfo;
import com.yc.sleepmm.setting.contract.FindCenterContract;
import com.yc.sleepmm.setting.engine.FindCenterEngine;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2018/1/26 09:15.
 */

public class FindCenterPresenter extends BasePresenter<FindCenterEngine, FindCenterContract.View> implements FindCenterContract.Presenter {
    public FindCenterPresenter(Context mContext, FindCenterContract.View view) {
        super(mContext, view);
        mEngine = new FindCenterEngine(mContext);
    }

    @Override
    protected void loadData(boolean forceUpdate) {
        if (!forceUpdate) return;
        getFindcenterInfos();
    }

    @Override
    public void getFindcenterInfos() {
        Subscription subscription = mEngine.getFindCenterInfo().subscribe(new Subscriber<List<FindCenterInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<FindCenterInfo> findCenterInfos) {
                mView.showFindCenterInfos(findCenterInfos);
            }
        });
        mSubscriptions.add(subscription);
    }
}
