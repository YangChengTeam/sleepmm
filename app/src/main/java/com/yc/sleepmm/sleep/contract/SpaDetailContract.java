package com.yc.sleepmm.sleep.contract;

import com.music.player.lib.bean.MusicInfo;
import com.yc.sleepmm.base.view.IHide;
import com.yc.sleepmm.base.view.ILoading;
import com.yc.sleepmm.base.view.INoData;
import com.yc.sleepmm.base.view.INoNet;
import com.yc.sleepmm.base.view.IPresenter;
import com.yc.sleepmm.base.view.IView;

/**
 * Created by wanglin  on 2018/2/11 09:11.
 */

public interface SpaDetailContract {

    interface View extends IView, ILoading, INoData, INoNet, IHide {
        void showSpaDetailInfo(MusicInfo data);
    }

    interface Presenter extends IPresenter {
        void getSpaDetailInfo(String spa_id);
    }
}
