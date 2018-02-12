package com.yc.sleepmm.setting.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
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
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
    }

    @Override
    public void getFindcenterInfos(final int page, final int limit) {
        if (page == 1) {
            mView.showLoading();
        }
        Subscription subscription = mEngine.getFindCenterInfo(page, limit).subscribe(new Subscriber<ResultInfo<List<FindCenterInfo>>>() {
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
            public void onNext(ResultInfo<List<FindCenterInfo>> listResultInfo) {
                if (listResultInfo != null && listResultInfo.code == HttpConfig.STATUS_OK) {
                    if (listResultInfo.data != null && listResultInfo.data.size() > 0) {
                        mView.showFindCenterInfos(listResultInfo.data);
                        mView.hide();
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
}
