package com.yc.sleepmm.setting.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.hwangjr.rxbus.RxBus;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.music.player.lib.util.ToastUtils;
import com.yc.sleepmm.base.APP;
import com.yc.sleepmm.base.presenter.BasePresenter;
import com.yc.sleepmm.base.util.UIUtils;
import com.yc.sleepmm.index.constants.Constant;
import com.yc.sleepmm.index.model.bean.UserInfo;
import com.yc.sleepmm.index.model.engine.EngineUtils;
import com.yc.sleepmm.setting.contract.BindContract;
import com.yc.sleepmm.setting.engine.BindPhoneEngine;
import com.yc.sleepmm.setting.utils.Utils;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2018/2/13 11:39.
 */

public class BindPhonePresenter extends BasePresenter<BindPhoneEngine, BindContract.View> implements BindContract.Presenter {
    public BindPhonePresenter(Context mContext, BindContract.View view) {
        super(mContext, view);
        mEngine = new BindPhoneEngine(mContext);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }

    @Override
    public void bindPhone(String mobile, String user_id, String code) {
        if (TextUtils.isEmpty(mobile)) {
            ToastUtils.showCenterToast("手机号不能为空");
            mView.showPhoneErrorView();
            return;
        }
        if (!Utils.isPhoneNumber(mobile)) {
            ToastUtils.showCenterToast("手机号码格式不正确");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            ToastUtils.showCenterToast("验证码不能为空");
            mView.showCodeErrorView();
            return;
        }

        mView.showLoadingProgressDialog("绑定手机号中，请稍候", true);

        Subscription subscription = mEngine.bindPhone(mobile, user_id, code).subscribe(new Subscriber<ResultInfo<UserInfo>>() {
            @Override
            public void onCompleted() {
                mView.dismissProgressDialog();
            }

            @Override
            public void onError(Throwable e) {
                mView.dismissProgressDialog();
            }

            @Override
            public void onNext(final ResultInfo<UserInfo> userInfoResultInfo) {
                if (userInfoResultInfo != null) {
                    if (userInfoResultInfo.code == HttpConfig.STATUS_OK) {
                        APP.getInstance().setUserData(userInfoResultInfo.data, true);
                        RxBus.get().post(Constant.RX_LOGIN_SUCCESS, "bind success");
                        mView.finish();
                    } else {
                        UIUtils.post(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showCenterToast(userInfoResultInfo.message);
                            }
                        });

                    }
                }
            }
        });
        mSubscriptions.add(subscription);
    }


    public void getCode(String phone) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showCenterToast("手机号不能为空");
            mView.showPhoneErrorView();
            return;
        }
        if (!Utils.isPhoneNumber(phone)) {
            ToastUtils.showCenterToast("手机号码格式不正确");
            return;
        }

        mView.showRightView();

        mView.showLoadingProgressDialog("获取验证码中...", true);
        Subscription subscription = EngineUtils.getCode(mContext, phone).subscribe(new Subscriber<ResultInfo<String>>() {
            @Override
            public void onCompleted() {
                mView.dismissProgressDialog();
            }

            @Override
            public void onError(Throwable e) {
                mView.dismissProgressDialog();
            }

            @Override
            public void onNext(final ResultInfo<String> stringResultInfo) {
                UIUtils.post(new Runnable() {
                    @Override
                    public void run() {
                        if (stringResultInfo != null) {
                            if (stringResultInfo.code == HttpConfig.STATUS_OK) {
                                ToastUtils.showCenterToast(stringResultInfo.data);
                            } else {
                                ToastUtils.showCenterToast(stringResultInfo.message);
                            }
                        }
                    }
                });


            }
        });

        mSubscriptions.add(subscription);
    }

}
