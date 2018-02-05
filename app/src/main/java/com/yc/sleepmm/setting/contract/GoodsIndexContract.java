package com.yc.sleepmm.setting.contract;

import com.yc.sleepmm.base.view.IPresenter;
import com.yc.sleepmm.base.view.IView;
import com.yc.sleepmm.setting.bean.GoodsInfo;

import java.util.List;

/**
 * Created by wanglin  on 2018/2/4 09:40.
 */

public interface GoodsIndexContract {

    interface View extends IView {
        void showGoodsInfos(List<GoodsInfo> goodsInfos);
    }

    interface Presenter extends IPresenter {

        void getGoodsInfoList(String type_id,int page,int limit);
    }
}
