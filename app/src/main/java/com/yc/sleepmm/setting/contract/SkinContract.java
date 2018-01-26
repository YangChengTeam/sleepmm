package com.yc.sleepmm.setting.contract;

import com.yc.sleepmm.base.view.IPresenter;
import com.yc.sleepmm.base.view.IView;
import com.yc.sleepmm.setting.bean.SkinInfo;

import java.util.List;

/**
 * Created by wanglin  on 2018/1/26 13:46.
 */

public interface SkinContract {

    interface View extends IView {
        void showSkinInfos(List<SkinInfo> skinInfos);
    }

    interface Presenter extends IPresenter {
        void getSkinInfos();
    }
}
