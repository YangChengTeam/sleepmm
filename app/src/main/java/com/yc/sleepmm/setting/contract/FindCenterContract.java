package com.yc.sleepmm.setting.contract;

import com.yc.sleepmm.base.view.IHide;
import com.yc.sleepmm.base.view.ILoading;
import com.yc.sleepmm.base.view.INoData;
import com.yc.sleepmm.base.view.INoNet;
import com.yc.sleepmm.base.view.IPresenter;
import com.yc.sleepmm.base.view.IView;
import com.yc.sleepmm.setting.bean.FindCenterInfo;

import java.util.List;

/**
 * Created by wanglin  on 2018/1/26 09:16.
 */

public interface FindCenterContract {

    interface View extends IView, ILoading, INoData, INoNet ,IHide{
        void showFindCenterInfos(List<FindCenterInfo> findCenterInfos);
    }

    interface Presenter extends IPresenter {

        void getFindcenterInfos(int page, int limit);
    }
}
