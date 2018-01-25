package com.yc.sleepmm.pay.contract;

import com.yc.sleepmm.base.view.IPresenter;
import com.yc.sleepmm.base.view.IView;
import com.yc.sleepmm.pay.bean.GoodInfo;

import java.util.List;

/**
 * Created by wanglin  on 2018/1/25 13:37.
 */

public interface GoodInfoContract {

    interface View extends IView {
        void showGoodInfos(List<GoodInfo> goodInfos);
    }

    interface Presenter extends IPresenter {

      void getGoodInfos();

    }
}
