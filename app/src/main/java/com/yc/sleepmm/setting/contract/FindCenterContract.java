package com.yc.sleepmm.setting.contract;

import com.yc.sleepmm.base.view.IPresenter;
import com.yc.sleepmm.base.view.IView;
import com.yc.sleepmm.setting.bean.FindCenterInfo;

import java.util.List;

/**
 * Created by wanglin  on 2018/1/26 09:16.
 */

public interface FindCenterContract {

    interface View extends IView {
        void showFindCenterInfos(List<FindCenterInfo> findCenterInfos);
    }

    interface Presenter extends IPresenter {

        void getFindcenterInfos();
    }
}
