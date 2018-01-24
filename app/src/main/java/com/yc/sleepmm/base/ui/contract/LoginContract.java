
package com.yc.sleepmm.base.ui.contract;


import com.yc.sleepmm.base.rxnet.BaseContract;
import com.yc.sleepmm.bean.UserDataInfo;

/**
 * @time 2017/5/23 10:50
 * @des 用户登录
 */
public interface LoginContract {

    interface View extends BaseContract.BaseView {
        void showLoginOtherResult(String data);
        void showLoginAccountResult(String data);
        void showRegisterAccountResult(String data);
        void showMakePasswordResult(String data);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        //快速登录
        void loginOther(UserDataInfo userDataInfo);
        //账号登录
        void loginAccount(String account, String password);
        //注册用户
        void registerAccount(String account,String password,String code);
        //修改密码
        void makePassword(String account,String password,String code);
    }
}
