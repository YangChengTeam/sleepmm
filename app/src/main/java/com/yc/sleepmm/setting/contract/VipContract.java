package com.yc.sleepmm.setting.contract;

import com.yc.sleepmm.base.view.IFinish;
import com.yc.sleepmm.base.view.IPresenter;
import com.yc.sleepmm.base.view.IView;

/**
 * Created by wanglin  on 2018/2/12 10:56.
 */

public interface VipContract {
    interface View extends IView ,IFinish{
    }

    interface Presenter extends IPresenter {
        void getUserInfo(String user_id);
    }
}
