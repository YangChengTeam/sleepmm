package com.yc.sleepmm.vip.contract;

import com.yc.sleepmm.base.view.IPresenter;
import com.yc.sleepmm.base.view.IView;
import com.yc.sleepmm.vip.bean.GoodsInfo;

import java.util.List;

/**
 * Created by wanglin  on 2018/1/25 13:37.
 */

public interface GoodInfoContract {

    interface View extends IView {
        void showGoodInfos(List<GoodsInfo> goodInfos);

    }

    interface Presenter extends IPresenter {

        void getGoodInfos(String type_id, int page, int limit);


    }
}
