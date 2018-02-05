package com.yc.sleepmm.setting.contract;

import com.kk.pay.OrderInfo;
import com.yc.sleepmm.base.view.IPresenter;
import com.yc.sleepmm.base.view.IView;

/**
 * Created by wanglin  on 2018/2/4 10:44.
 */

public interface CreateOrderContract {

    interface View extends IView {
        void showOrderInfos(OrderInfo data);
    }

    interface Presenter extends IPresenter {

        void getOrderInfo(String user_id, String title, String money, String pay_way_name);
    }
}
