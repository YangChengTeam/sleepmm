package com.yc.sleepmm.index.rxnet;

import com.music.player.lib.bean.MusicInfo;
import com.yc.sleepmm.base.view.IHide;
import com.yc.sleepmm.base.view.ILoading;
import com.yc.sleepmm.base.view.INoData;
import com.yc.sleepmm.base.view.INoNet;
import com.yc.sleepmm.base.view.IPresenter;
import com.yc.sleepmm.base.view.IView;

import java.util.List;

/**
 * Created by wanglin  on 2018/2/8 11:17.
 */

public interface IndexMusicTypeDetailContract {

    interface View extends IView, ILoading, INoNet, INoData, IHide {
        void showMusicInfos(List<MusicInfo> data);
    }

    interface Presenter extends IPresenter {
        void getMusicInfos(String type_id, int page, int limit,boolean isRefresh);
    }
}
