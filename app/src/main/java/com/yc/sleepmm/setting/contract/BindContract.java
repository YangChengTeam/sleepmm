package com.yc.sleepmm.setting.contract;

import com.yc.sleepmm.base.view.IDialog;
import com.yc.sleepmm.base.view.IFinish;
import com.yc.sleepmm.base.view.IPresenter;
import com.yc.sleepmm.base.view.IView;

/**
 * Created by wanglin  on 2018/2/13 11:45.
 */

public interface BindContract {

    interface View extends IView, IDialog, IFinish {
        void showPhoneErrorView();

        void showRightView();

        void showCodeErrorView();
    }

    interface Presenter extends IPresenter {
        void bindPhone(String mobile, String user_id, String code);
    }
}
