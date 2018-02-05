
package com.yc.sleepmm.index.ui.presenter;

import android.content.Context;
import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.orhanobut.logger.Logger;
import com.yc.sleepmm.index.bean.UserDataInfo;
import com.yc.sleepmm.index.bean.UserInfo;
import com.yc.sleepmm.index.constants.NetContants;
import com.yc.sleepmm.index.rxnet.RxPresenter;
import com.yc.sleepmm.index.ui.contract.LoginContract;
import java.util.HashMap;
import java.util.Map;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


/**
 * TinyHung@outlook.com
 * 2017/5/23 10:53
 * 注册、登录、找回密码Presenter
 */

public class LoginPresenter extends RxPresenter<LoginContract.View> implements LoginContract.Presenter<LoginContract.View> {

    private static final String TAG = "LoginPresenter";

    public LoginPresenter(Context context) {
        super(context);
    }

    private boolean isLogin=false;
    private boolean isRegister=false;
    private boolean isMakePassword=false;

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

    /**
     * 第三方账号快速登录
     * @param userDataInfo
     */
    @Override
    public void loginOther(UserDataInfo userDataInfo) {
        Logger.d(TAG,"getOpenid="+userDataInfo.getOpenid());
        Logger.d(TAG,"getGender="+userDataInfo.getGender());
        Logger.d(TAG,"getNickname="+userDataInfo.getNickname());
    }

    /**
     * 账号密码登录
     * @param account
     * @param password
     */
    @Override
    public void loginAccount(String account, String password) {
        if(isLogin) return;
        isLogin=true;
        Map<String,String> params=new HashMap<>();
        params.put("mobile", account);
        params.put("password", password);
        Subscription subscribe = HttpCoreEngin.get(mContext).rxpost(NetContants.DEBUG_HOST + NetContants.HOST_USER_LOGIN, new TypeReference<ResultInfo<UserInfo>>() {
        }.getType(), params, true, true, true).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<ResultInfo<UserInfo>>() {
            @Override
            public void call(ResultInfo<UserInfo> data) {
                isLogin=false;
                if(null!=data){
                    if(1==data.code&&null!=data.data){
                        if(null!=mView)mView.showLoginAccountResult(data.data);
                    }else{
                        if(null!=mView)mView.showRequstError(data.message);
                    }
                }else{
                    if(null!=mView)mView.showErrorView();
                }
            }
        });
        addSubscrebe(subscribe);
    }


    /**
     * 注册账户
     * @param account
     * @param password
     * @param code
     */
    @Override
    public void registerAccount(String account, String password, String code) {
        if(isRegister) return;
        isRegister=true;
        Map<String,String> params=new HashMap<>();
        params.put("mobile", account);
        params.put("code", code);
        params.put("password", password);
        Subscription subscribe = HttpCoreEngin.get(mContext).rxpost(NetContants.DEBUG_HOST + NetContants.HOST_USER_REGISTER, new TypeReference<ResultInfo<UserInfo>>() {
        }.getType(), params, true, true, true).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<ResultInfo<UserInfo>>() {
            @Override
            public void call(ResultInfo<UserInfo> data) {
                isRegister=false;
                if(null!=data){
                    if(1==data.code&&null!=data.data){
                        if(null!=mView)mView.showRegisterAccountResult(data.data);
                    }else{
                        if(null!=mView)mView.showRequstError(data.message);
                    }
                }else{
                    if(null!=mView)mView.showErrorView();
                }
            }
        });
        addSubscrebe(subscribe);
    }

    /**
     * 找回密码\修改密码
     * @param phoneNumber
     * @param code
     * @param newPassword
     */
    @Override
    public void findPassword(String phoneNumber, String code, String newPassword) {
        if(isMakePassword) return;
        isMakePassword=true;
        Map<String,String> params=new HashMap<>();
        params.put("mobile", phoneNumber);
        params.put("code", code);
        params.put("new_password", newPassword);
        Subscription subscribe = HttpCoreEngin.get(mContext).rxpost(NetContants.DEBUG_HOST + NetContants.HOST_USER_FIND_PASSWORD, new TypeReference<ResultInfo<UserInfo>>() {
        }.getType(), params, true, true, true).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<ResultInfo<UserInfo>>() {
            @Override
            public void call(ResultInfo<UserInfo> data) {
                isMakePassword=false;
                if(null!=data){
                    if(1==data.code&&null!=data.data){
                        if(null!=mView)mView.showFindPasswordResult(data.data);
                    }else{
                        if(null!=mView)mView.showRequstError(data.message);
                    }
                }else{
                    if(null!=mView)mView.showErrorView();
                }
            }
        });
        addSubscrebe(subscribe);
    }


    /**
     * 获取验证码
     * @param phoneNumber
     */
    @Override
    public void getCode(String phoneNumber) {
        Map<String,String> params=new HashMap<>();
        params.put("mobile", phoneNumber);
        params.put("user_id", "0");
        Subscription subscribe = HttpCoreEngin.get(mContext).rxpost(NetContants.DEBUG_HOST + NetContants.HOST_USER_GET_CODE, new TypeReference<ResultInfo<String>>() {
        }.getType(), params, true, true, true).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<ResultInfo<String>>() {
            @Override
            public void call(ResultInfo<String> data) {
                Logger.d(TAG,"data="+data.data);
                if(null!=data){
                    if(1==data.code){
                        if(null!=mView)mView.showGetCodeResult(data.data);
                    }else{
                        if(null!=mView)mView.showRequstError(data.message);
                    }
                }else{
                    if(null!=mView)mView.showErrorView();
                }
            }
        });
        addSubscrebe(subscribe);
    }
}
