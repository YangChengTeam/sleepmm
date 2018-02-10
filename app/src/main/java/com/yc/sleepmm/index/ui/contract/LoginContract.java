
package com.yc.sleepmm.index.ui.contract;


import com.yc.sleepmm.base.view.IDialog;
import com.yc.sleepmm.base.view.IPresenter;
import com.yc.sleepmm.base.view.IView;
import com.yc.sleepmm.index.model.bean.UserDataInfo;
import com.yc.sleepmm.index.model.bean.UserInfo;


/**
 * @time 2017/5/23 10:50
 * @des 用户登录
 */
public interface LoginContract {

    interface View extends IView, IDialog {

        void showAccountResult(UserInfo data, String tint);

        void showRequstError(String data);
    }

    interface Presenter extends IPresenter {
        //快速登录
        void loginOther(UserDataInfo userDataInfo);

        //账号登录
        void loginAccount(String account, String password);

        //注册用户
        void registerAccount(String account, String password, String code);

        //修改密码
        void findPassword(String phoneNumber, String code, String newPassword);

        //获取验证码
        void getCode(String phoneNumber);

    }
}
