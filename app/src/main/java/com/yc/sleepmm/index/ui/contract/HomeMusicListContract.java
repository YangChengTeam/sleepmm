
package com.yc.sleepmm.index.ui.contract;


import com.music.player.lib.bean.MusicInfo;
import com.yc.sleepmm.index.rxnet.BaseContract;
import java.util.List;

/**
 * TinyHung@Outlook.com
 * 2017/5/22.
 * 获取音乐列表
 */
public interface HomeMusicListContract {

    interface View extends BaseContract.BaseView {
        void showMusicList(List<MusicInfo> data);
        void showMusicListEmpty(String data);
        void showMusicListError(String data);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void getMusicList(String musicID,int page,int pageSize);//获取音乐列表
    }
}
