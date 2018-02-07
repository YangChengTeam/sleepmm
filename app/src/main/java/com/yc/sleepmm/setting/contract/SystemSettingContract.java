package com.yc.sleepmm.setting.contract;

import com.yc.sleepmm.base.view.IPresenter;
import com.yc.sleepmm.base.view.IView;

/**
 * Created by wanglin  on 2018/2/7 10:11.
 */

public interface SystemSettingContract {

    interface View extends IView{
        void showLogout(boolean b);

        void showCacheSize(String cacheSize);
    }
    interface Presenter extends IPresenter{}
}
