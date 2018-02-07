package com.yc.sleepmm.setting.contract;

import com.yc.sleepmm.base.view.IDialog;
import com.yc.sleepmm.base.view.IFinish;
import com.yc.sleepmm.base.view.IPresenter;
import com.yc.sleepmm.base.view.IView;

/**
 * Created by wanglin  on 2018/2/7 14:36.
 */

public interface FeedbackContract {
    interface View extends IView, IDialog, IFinish {
    }

    interface Presenter extends IPresenter {
        void createSuggest(String content, String user_id);
    }
}
