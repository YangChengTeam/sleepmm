
package com.yc.sleepmm.index.ui.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.music.player.lib.util.ToastUtils;
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.presenter.BasePresenter;
import com.yc.sleepmm.base.util.UIUtils;
import com.yc.sleepmm.index.model.bean.UserDataInfo;
import com.yc.sleepmm.index.model.bean.UserInfo;
import com.yc.sleepmm.index.model.engine.EngineUtils;
import com.yc.sleepmm.index.model.engine.LoginGroupEngine;
import com.yc.sleepmm.index.ui.contract.LoginContract;

import rx.Subscriber;
import rx.Subscription;


/**
 * TinyHung@outlook.com
 * 2017/5/23 10:53
 * 注册、登录、找回密码Presenter
 */

public class LoginPresenter extends BasePresenter<LoginGroupEngine, LoginContract.View> implements LoginContract.Presenter {

    private static final String TAG = "LoginPresenter";


    private boolean isLogin = false;
    private boolean isRegister = false;
    private boolean isMakePassword = false;

    public LoginPresenter(Context context, LoginContract.View view) {
        super(context, view);
        mEngine = new LoginGroupEngine(context);
    }

    //是否正在修改密码
    public boolean isMakePassword() {
        return isMakePassword;
    }

    //是否正在登录
    public boolean isLogin() {
        return isLogin;
    }

    //是否正在注册
    public boolean isRegister() {
        return isRegister;
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }

    /**
     * 第三方账号快速登录
     *
     * @param userDataInfo
     */
    @Override
    public void loginOther(UserDataInfo userDataInfo) {
        if (isLogin) return;
        isLogin = true;

        Subscription subscription = mEngine.snsLogin(userDataInfo.getAccessToken(), userDataInfo.getLoginType(), userDataInfo.getNickname(), userDataInfo.getIconUrl()).subscribe(new Subscriber<ResultInfo<UserInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.dismissProgressDialog();
            }

            @Override
            public void onNext(ResultInfo<UserInfo> userInfoResultInfo) {
                mView.dismissProgressDialog();
                if (null != userInfoResultInfo) {
                    if (HttpConfig.STATUS_OK == userInfoResultInfo.code && null != userInfoResultInfo.data) {
                        if (null != mView)
                            mView.showAccountResult(userInfoResultInfo.data, mContext.getString(R.string.login));
                    } else {
                        if (null != mView) mView.showRequstError(userInfoResultInfo.message);
                    }
                }
            }
        });
        mSubscriptions.add(subscription);

    }

    /**
     * 账号密码登录
     *
     * @param account
     * @param password
     */
    @Override
    public void loginAccount(String account, String password) {
        if (isLogin) return;
        isLogin = true;
        mView.showLoadingProgressDialog("登录中，请稍后...", true);
        Subscription subscription = mEngine.loginAccount(account, password).subscribe(new Subscriber<ResultInfo<UserInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.dismissProgressDialog();
            }

            @Override
            public void onNext(ResultInfo<UserInfo> userInfoResultInfo) {

                mView.dismissProgressDialog();
                isLogin = false;
                if (null != userInfoResultInfo) {
                    if (1 == userInfoResultInfo.code && null != userInfoResultInfo.data) {
                        if (null != mView)
                            mView.showAccountResult(userInfoResultInfo.data, mContext.getString(R.string.login));
                    } else {
                        if (null != mView) mView.showRequstError(userInfoResultInfo.message);
                    }
                }
            }
        });
        mSubscriptions.add(subscription);
    }


    /**
     * 注册账户
     *
     * @param account
     * @param password
     * @param code
     */
    @Override
    public void registerAccount(String account, String password, String code) {
        if (isRegister) return;
        isRegister = true;

        mView.showLoadingProgressDialog("提交注册中...", true);
        Subscription subscription = mEngine.registerAccount(account, password, code).subscribe(new Subscriber<ResultInfo<UserInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.dismissProgressDialog();
            }

            @Override
            public void onNext(ResultInfo<UserInfo> userInfoResultInfo) {
                mView.dismissProgressDialog();
                isRegister = false;
                if (null != userInfoResultInfo) {
                    if (1 == userInfoResultInfo.code && null != userInfoResultInfo.data) {
                        if (null != mView)
                            mView.showAccountResult(userInfoResultInfo.data, mContext.getString(R.string.register));
                    } else {
                        if (null != mView) mView.showRequstError(userInfoResultInfo.message);
                    }
                }
            }
        });
        mSubscriptions.add(subscription);

    }

    /**
     * 找回密码\修改密码
     *
     * @param phoneNumber
     * @param code
     * @param newPassword
     */
    @Override
    public void findPassword(String phoneNumber, String code, String newPassword) {
        if (isMakePassword) return;
        isMakePassword = true;
        mView.showLoadingProgressDialog("修改密码中...", true);
        Subscription subscription = mEngine.findPassword(phoneNumber, code, newPassword).subscribe(new Subscriber<ResultInfo<UserInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.dismissProgressDialog();
            }

            @Override
            public void onNext(ResultInfo<UserInfo> userInfoResultInfo) {
                mView.dismissProgressDialog();
                isMakePassword = false;
                if (null != userInfoResultInfo) {
                    if (HttpConfig.STATUS_OK == userInfoResultInfo.code && null != userInfoResultInfo.data) {
                        if (null != mView)
                            mView.showAccountResult(userInfoResultInfo.data, mContext.getString(R.string.change_password));
                    } else {
                        if (null != mView) mView.showRequstError(userInfoResultInfo.message);
                    }
                }
            }
        });

        mSubscriptions.add(subscription);


    }


    /**
     * 获取验证码
     *
     * @param phoneNumber
     */
    @Override
    public void getCode(String phoneNumber) {

        mView.showLoadingProgressDialog("获取验证码中...", true);
        Subscription subscription = EngineUtils.getCode(mContext, phoneNumber).subscribe(new Subscriber<ResultInfo<String>>() {
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
                        if (null != stringResultInfo) {
                            if (HttpConfig.STATUS_OK == stringResultInfo.code && stringResultInfo.data != null) {
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
