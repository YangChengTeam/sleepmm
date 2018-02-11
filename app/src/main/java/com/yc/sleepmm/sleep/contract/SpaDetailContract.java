package com.yc.sleepmm.sleep.contract;

import com.yc.sleepmm.base.view.IPresenter;
import com.yc.sleepmm.base.view.IView;

/**
 * Created by wanglin  on 2018/2/11 09:11.
 */

public interface SpaDetailContract {

    interface View extends IView {
    }

    interface Presenter extends IPresenter {
        void getSpaDetailInfo(String spa_id);
    }
}
