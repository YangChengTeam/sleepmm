
package com.yc.sleepmm.index.ui.contract;


import com.yc.sleepmm.index.bean.UserDataInfo;
import com.yc.sleepmm.index.bean.UserInfo;
import com.yc.sleepmm.index.rxnet.BaseContract;


/**
 * @time 2017/5/23 10:50
 * @des 用户登录
 */
public interface LoginContract {

    interface View extends BaseContract.BaseView {
        void showLoginOtherResult(String data);
        void showLoginAccountResult(UserInfo data);
        void showRegisterAccountResult(UserInfo data);
        void showFindPasswordResult(UserInfo data);
        void showGetCodeResult(String data);
        void showRequstError(String data);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        //快速登录
        void loginOther(UserDataInfo userDataInfo);
        //账号登录
        void loginAccount(String account, String password);
        //注册用户
        void registerAccount(String account,String password,String code);
        //修改密码
        void findPassword(String phoneNumber, String code, String newPassword);
        //获取验证码
        void getCode(String phoneNumber);
    }
}
