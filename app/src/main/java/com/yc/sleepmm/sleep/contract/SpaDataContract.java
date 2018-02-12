package com.yc.sleepmm.sleep.contract;

import com.yc.sleepmm.base.view.IPresenter;
import com.yc.sleepmm.base.view.IView;
import com.yc.sleepmm.sleep.model.bean.SpaDataInfo;
import com.yc.sleepmm.sleep.model.bean.SpaItemInfo;

import java.util.List;

/**
 * Created by wanglin  on 2018/2/11 09:11.
 */

public interface SpaDataContract {

    interface View extends IView {
        void showSpaData(List<SpaDataInfo> datas);
        void showSpaItemList(List<SpaItemInfo> itemInfos);
    }

    interface Presenter extends IPresenter {
        void getSpaDataList();
        void getSpaItemList(String typeId,int page,int limit);
    }
}
