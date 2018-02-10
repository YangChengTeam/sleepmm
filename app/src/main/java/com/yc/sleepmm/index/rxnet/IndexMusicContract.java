package com.yc.sleepmm.index.rxnet;

import com.music.player.lib.bean.MusicInfo;
import com.yc.sleepmm.base.view.IPresenter;
import com.yc.sleepmm.base.view.IView;
import com.yc.sleepmm.index.model.bean.MusicTypeInfo;

import java.util.List;

/**
 * Created by wanglin  on 2018/2/8 10:27.
 */

public interface IndexMusicContract {
    interface View extends IView {
        void showMusicTypeInfo(List<MusicTypeInfo> data);

        void showRandomMusicInfo(MusicInfo data);

        void showCollectSucess(boolean isCollect);
    }

    interface Presenter extends IPresenter {
        void getMusicTypes();
    }
}
