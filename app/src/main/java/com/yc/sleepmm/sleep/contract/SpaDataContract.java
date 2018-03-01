package com.yc.sleepmm.sleep.contract;

import com.yc.sleepmm.base.view.IHide;
import com.yc.sleepmm.base.view.ILoading;
import com.yc.sleepmm.base.view.INoData;
import com.yc.sleepmm.base.view.INoNet;
import com.yc.sleepmm.base.view.IPresenter;
import com.yc.sleepmm.base.view.IView;
import com.yc.sleepmm.sleep.model.bean.SpaDataInfo;
import com.yc.sleepmm.sleep.model.bean.SpaItemInfo;

import java.util.List;

/**
 * Created by wanglin  on 2018/2/11 09:11.
 */

public interface SpaDataContract {

    interface View extends IView, ILoading, IHide, INoNet, INoData {
        void showSpaData(List<SpaDataInfo> datas);

        void showSpaItemList(List<SpaItemInfo> itemInfos);

        void showSpaItemList(List<SpaItemInfo> itemInfos, int postion);
    }

    interface Presenter extends IPresenter {
        void getSpaDataList();

        void getSpaItemList(String typeId, int page, int limit);
    }
}
