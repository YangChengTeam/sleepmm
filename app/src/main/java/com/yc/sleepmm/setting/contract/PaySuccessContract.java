package com.yc.sleepmm.setting.contract;

import com.yc.sleepmm.base.view.IDialog;
import com.yc.sleepmm.base.view.IPresenter;
import com.yc.sleepmm.base.view.IView;

/**
 * Created by wanglin  on 2018/3/5 11:59.
 */

public interface PaySuccessContract {
    interface View extends IView, IDialog {
        void showUploadResult(String data);
    }

    interface Presenter extends IPresenter {
        void uploadPhone(String phone,String orderSn);
    }

}
