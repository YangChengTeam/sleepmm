
package com.yc.sleepmm.index.ui.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.ToastUtil;
import com.yc.sleepmm.base.presenter.BasePresenter;
import com.yc.sleepmm.index.model.bean.UserDataInfo;
import com.yc.sleepmm.index.model.bean.UserInfo;
import com.yc.sleepmm.index.model.engine.LoginGroupEngine;
import com.yc.sleepmm.index.ui.contract.LoginContract;

import rx.Subscriber;
import rx.Subscription;


/**
 * TinyHung@outlook.com
 * 2017/5/23 10:53
 * 注册、登录、找回密码Presenter
 */

public class LoginGroupPresenter extends BasePresenter<LoginGroupEngine, LoginContract.View> implements LoginContract.Presenter {

    private static final String TAG = "LoginGroupPresenter";


    private boolean isLogin = false;
    private boolean isRegister = false;
    private boolean isMakePassword = false;

    public LoginGroupPresenter(Context mContext, LoginContract.View view) {
        super(mContext, view);
        mEngine = new LoginGroupEngine(mContext);
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
//        Map<String,String> params=new HashMap<>();
//        params.put("nickname",userDataInfo.getNickname());
//        params.put("icon_url",userDataInfo.getIconUrl());
//        params.put("open_id",userDataInfo.getOpenid());
//        params.put("login_type",userDataInfo.getLoginType());
//        Subscription subscribe = HttpCoreEngin.get(mContext).rxpost(NetContants.DEBUG_HOST + NetContants.HOST_USER_OTHER_LOGIN, new TypeReference<ResultInfo<UserInfo>>() {
//        }.getType(), params, true, true, true).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<ResultInfo<UserInfo>>() {
//            @Override
//            public void call(ResultInfo<UserInfo> data) {
//                isLogin=false;
//                if(null!=data){
//                    if(1==data.code&&null!=data.data){
//                        if(null!=mView)mView.showLoginOtherResult(data.data);
//                    }else{
//                        if(null!=mView)mView.showRequstError(data.message);
//                    }
//                }else{
//                    if(null!=mView)mView.showErrorView();
//                }
//            }
//        });
//        addSubscrebe(subscribe);
    }

    /**
     * 账号密码登录
     *
     * @param account
     * @param password
     */
    @Override
    public void loginAccount(String account, String password) {


        Subscription subscription = mEngine.loginAccount(account, password).subscribe(new Subscriber<ResultInfo<UserInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<UserInfo> userInfoResultInfo) {
                if (null != userInfoResultInfo) {
                    if (HttpConfig.STATUS_OK == userInfoResultInfo.code && null != userInfoResultInfo.data) {
                        if (null != mView) mView.showLoginAccountResult(userInfoResultInfo.data);
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
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password) || TextUtils.isEmpty(code)) {
            ToastUtil.toast(mContext, "用户名、密码、验证码不能为空");
            return;
        }

        Subscription subscribe = mEngine.registerAccount(account, password, code).subscribe(new Subscriber<ResultInfo<UserInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<UserInfo> userInfoResultInfo) {
                if (null != userInfoResultInfo) {
                    if (HttpConfig.STATUS_OK == userInfoResultInfo.code && null != userInfoResultInfo.data) {
                        mView.showRegisterAccountResult(userInfoResultInfo.data);
                    } else {
                        if (null != mView) mView.showRequstError(userInfoResultInfo.message);
                    }
                }
            }
        });
        mSubscriptions.add(subscribe);
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
        mView.showLoadingDialog("修改密码中...");
        Subscription subscription = mEngine.findPassword(phoneNumber, code, newPassword).subscribe(new Subscriber<ResultInfo<UserInfo>>() {
            @Override
            public void onCompleted() {
                mView.dismissDialog();
            }

            @Override
            public void onError(Throwable e) {
                mView.dismissDialog();
            }

            @Override
            public void onNext(ResultInfo<UserInfo> userInfoResultInfo) {
                if (null != userInfoResultInfo) {
                    if (HttpConfig.STATUS_OK == userInfoResultInfo.code && null != userInfoResultInfo.data) {
                        if (null != mView) mView.showFindPasswordResult(userInfoResultInfo.data);
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
        mView.showLoadingDialog("获取验证码中...");
        Subscription subscription = mEngine.getCode(phoneNumber).subscribe(new Subscriber<ResultInfo<String>>() {
            @Override
            public void onCompleted() {
                mView.dismissDialog();
            }

            @Override
            public void onError(Throwable e) {
                mView.dismissDialog();
            }

            @Override
            public void onNext(ResultInfo<String> stringResultInfo) {
                if (null != stringResultInfo) {
                    if (HttpConfig.STATUS_OK == stringResultInfo.code) {
                        if (null != mView) mView.showGetCodeResult(stringResultInfo.data);
                    } else {
                        if (null != mView) mView.showRequstError(stringResultInfo.message);
                    }
                }
            }
        });
        mSubscriptions.add(subscription);
    }


}
