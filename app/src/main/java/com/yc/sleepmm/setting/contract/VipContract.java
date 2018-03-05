package com.yc.sleepmm.setting.contract;

import com.yc.sleepmm.base.view.IFinish;
import com.yc.sleepmm.base.view.IPresenter;
import com.yc.sleepmm.base.view.IView;
import com.yc.sleepmm.index.model.bean.UserInfo;

/**
 * Created by wanglin  on 2018/2/12 10:56.
 */

public interface VipContract {
    interface View extends IView ,IFinish{
        void showUserInfo(UserInfo data);
    }

    interface Presenter extends IPresenter {
        void getUserInfo(String user_id);
    }
}
