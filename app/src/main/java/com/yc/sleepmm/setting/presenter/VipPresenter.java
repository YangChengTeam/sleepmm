package com.yc.sleepmm.setting.presenter;

import android.content.Context;

import com.hwangjr.rxbus.RxBus;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.sleepmm.base.APP;
import com.yc.sleepmm.base.presenter.BasePresenter;
import com.yc.sleepmm.index.constants.Constant;
import com.yc.sleepmm.index.model.bean.UserInfo;
import com.yc.sleepmm.setting.contract.VipContract;
import com.yc.sleepmm.setting.engine.VipEngine;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2018/2/12 10:55.
 */

public class VipPresenter extends BasePresenter<VipEngine, VipContract.View> implements VipContract.Presenter {
    public VipPresenter(Context mContext, VipContract.View view) {
        super(mContext, view);
        mEngine = new VipEngine(mContext);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }

    @Override
    public void getUserInfo(final String user_id) {
        Subscription subscription = mEngine.getUserInfo(user_id).subscribe(new Subscriber<ResultInfo<UserInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<UserInfo> userInfoResultInfo) {
                if (userInfoResultInfo != null && userInfoResultInfo.code == HttpConfig.STATUS_OK && userInfoResultInfo.data != null) {
                    APP.getInstance().setUserData(userInfoResultInfo.data, true);
                    RxBus.get().post(Constant.RX_LOGIN_SUCCESS, "form pay");
                    mView.finish();
                }
            }
        });
        mSubscriptions.add(subscription);
    }
}
