
package com.yc.sleepmm.base.ui.presenter;

import android.content.Context;
import com.yc.sleepmm.base.rxnet.RxPresenter;
import com.yc.sleepmm.base.ui.contract.LoginContract;
import com.yc.sleepmm.index.bean.UserDataInfo;

/**
 * TinyHung@outlook.com
 * 2017/5/23 10:53
 * 注册、登录、找回密码Presenter
 */

public class LoginPresenter extends RxPresenter<LoginContract.View> implements LoginContract.Presenter<LoginContract.View> {

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
     * 快速登录
     * @param userDataInfo
     */
    @Override
    public void loginOther(UserDataInfo userDataInfo) {

    }

    /**
     * 账号密码登录
     * @param account
     * @param password
     */
    @Override
    public void loginAccount(String account, String password) {

    }


    /**
     * 注册账户
     * @param account
     * @param password
     * @param code
     */
    @Override
    public void registerAccount(String account, String password, String code) {

    }

    /**
     * 修改密码
     * @param account
     * @param password
     * @param code
     */
    @Override
    public void makePassword(String account,String password, String code) {

    }
}
