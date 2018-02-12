package com.yc.sleepmm.setting.contract;

import com.music.player.lib.bean.MusicInfo;
import com.yc.sleepmm.base.view.IHide;
import com.yc.sleepmm.base.view.ILoading;
import com.yc.sleepmm.base.view.INoData;
import com.yc.sleepmm.base.view.INoNet;
import com.yc.sleepmm.base.view.IPresenter;
import com.yc.sleepmm.base.view.IView;

import java.util.List;

/**
 * Created by wanglin  on 2018/2/11 10:01.
 */

public interface CollectContract {
    interface View extends IView, ILoading, IHide, INoNet, INoData {
        void showSpaCollectList(List<MusicInfo> data);

        void showMusicCollectList(List<MusicInfo> data);
    }

    interface Presenter extends IPresenter {
        void getSpaFavoriteList( int page, int limit);

        void getMusicFavoriteList( int page, int limit);
    }
}
