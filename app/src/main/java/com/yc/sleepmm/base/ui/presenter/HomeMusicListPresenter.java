
package com.yc.sleepmm.base.ui.presenter;

import android.content.Context;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.music.player.lib.bean.MusicInfo;
import com.yc.sleepmm.base.rxnet.RxPresenter;
import com.yc.sleepmm.bean.MediaMusicCategoryList;
import com.yc.sleepmm.base.ui.contract.HomeMusicListContract;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * TinyHung@outlook.com
 * 2017/6/1 15:00
 * 获取音乐列表
 */
public class HomeMusicListPresenter extends RxPresenter<HomeMusicListContract.View> implements HomeMusicListContract.Presenter<HomeMusicListContract.View> {

    private boolean ieGetMusicList=false;

    public boolean isIeGetMusicList() {
        return ieGetMusicList;
    }

    public HomeMusicListPresenter(Context context){
        super(context);
    }

    /**
     * 根据ID获取音乐列表
     * @param musicID
     * @param page
     * @param pageSize
     */
    @Override
    public void getMusicList(String musicID, int page, int pageSize) {
        if(isIeGetMusicList()) return;
        ieGetMusicList=true;
        Map<String,String> params=new HashMap<>();
        params.put("user_id", "1065153");
        params.put("page", "1");
        params.put("page_size","10");
        HttpCoreEngin.get(mContext).rxpost("http://sc.wk2.com/Api/Appnq6/music_recommend", MediaMusicCategoryList.class, params,false,false,false).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<MediaMusicCategoryList>() {            @Override
            public void call(MediaMusicCategoryList data) {
            ieGetMusicList=false;
                if(null!=data&&1==data.getCode()&&null!=data.getData()&&data.getData().size()>0){
                    List<MusicInfo> musicInfos=new ArrayList<>();
                    for (MediaMusicCategoryList.DataBean dataBean : data.getData()) {
                        MusicInfo musicInfo=new MusicInfo();
                        musicInfo.setMusicID(dataBean.getId());
                        musicInfo.setMusicAlbum(dataBean.getTitle());
                        musicInfo.setMusicTitle(dataBean.getTitle());
                        musicInfo.setMusicDurtion(dataBean.getSeconds());
                        musicInfo.setMsuicAlbumIcon(dataBean.getCover());
                        musicInfo.setMusicCover(dataBean.getCover());
                        musicInfo.setMusicAuthor(dataBean.getAuthor());
                        musicInfo.setMusicAlbumTitle(dataBean.getTitle());
                        musicInfo.setMusicPath(dataBean.getUrl());
                        musicInfos.add(musicInfo);
                    }
                    if(null!=mView) mView.showMusicList(musicInfos);
                }else if(null!=data&&1==data.getCode()&&null!=data.getData()&&data.getData().size()<=0){
                    if(null!=mView) mView.showMusicListEmpty("没有更多数据了");
                }else{
                    if(null!=mView) mView.showMusicListError("加载失败");
                }
            }
        });
    }
}
