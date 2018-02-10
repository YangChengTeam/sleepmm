package com.yc.sleepmm.sleep.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.music.player.lib.bean.MusicInfo;
import com.music.player.lib.bean.MusicPlayerConfig;
import com.music.player.lib.constants.Constants;
import com.music.player.lib.listener.OnUserPlayerEventListener;
import com.music.player.lib.manager.MusicPlayerManager;
import com.music.player.lib.mode.PlayerSetyle;
import com.music.player.lib.util.ToastUtils;
import com.music.player.lib.view.MusicPlayerController;
import com.yc.sleepmm.R;
import com.yc.sleepmm.index.model.bean.MediaMusicCategoryList;
import com.yc.sleepmm.sleep.adapter.UserSleepAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by admin on 2018/1/26.
 */

public class SleepDetailActivity extends AppCompatActivity implements OnUserPlayerEventListener {

    private MusicPlayerController mMusicPlayerController;
    private boolean isCollect = false;//是否收藏，需要调用者维护
    private UserSleepAdapter userSleepAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_detail);
        initViews();
        initAdapter();
        loadMusicList();//加载音乐列表
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 初始设置示例
     */
    private void initViews() {
        //音乐播放控制器
        mMusicPlayerController = (MusicPlayerController) findViewById(R.id.music_player_controller);
        //设置播放器样式，不设置默认首页样式，这里以黑色为例
        mMusicPlayerController.setPlayerStyle(PlayerSetyle.PLAYER_STYLE_BLACK);
        //调用此方法目的在于当播放列表为空，会回调至持有播放控制器的所有UI组件，设置Type就是标识UI组件的身份，用来判断是是否处理 回调方法事件autoStartNewPlayTasks()，
        //参数可自定义，需要和回调的autoStartNewPlayTasks（type）对应,
        mMusicPlayerController.setUIComponentType(Constants.UI_TYPE_DETAILS);
        //设置是否显示返回按钮
        mMusicPlayerController.setBackButtonVisibility(true);
        //设置闹钟最大定时时间
//        mMusicPlayerController.setAlarmSeekBarProgressMax(1000);
        //设置闹钟初始的定时时间
//        mMusicPlayerController.setAlarmSeekBarProgress(60);
        //是否点赞,默认false
        mMusicPlayerController.setCollectIcon(R.drawable.ic_player_collect, isCollect);//相反，未收藏：R.drawable.ic_player_collect,false
        //注册事件回调
        mMusicPlayerController.setOnClickEventListener(new MusicPlayerController.OnClickEventListener() {
            //收藏事件触发了
            @Override
            public void onEventCollect(MusicInfo musicInfo) {
                isCollect = !isCollect;
                //设置是否收藏示例
                mMusicPlayerController.setCollectIcon(isCollect ? R.drawable.ic_player_collect_true : R.drawable.ic_player_collect, isCollect);
            }

            //随机播放触发了
            @Override
            public void onEventRandomPlay() {
                ToastUtils.showCenterToast("点击了来一首");
                //调用以下任意方法触发音乐播放
//                MusicPlayerManager.getInstance().playMusic("音乐列表","position");
//                MusicPlayerManager.getInstance().playMusic("position");
//                MusicPlayerManager.getInstance().playMusic("单个音乐对象");
            }

            //返回事件
            @Override
            public void onBack() {
                SleepDetailActivity.this.onBackPressed();
            }

            @Override
            public void onPlayState(MusicInfo info) {

            }
        });
        //注册到被观察者中
        MusicPlayerManager.getInstance().addObservable(mMusicPlayerController);
        //注册播放变化监听
        MusicPlayerManager.getInstance().addPlayerStateListener(this);
        MusicPlayerManager.getInstance().onResumeChecked();//先让播放器刷新起来
    }

    /**
     * 配合播放列表示例
     */
    private void initAdapter() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(SleepDetailActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        //如列表需要实时更新播放动态的话，Adapter需要实现Observer接口
        userSleepAdapter = new UserSleepAdapter(SleepDetailActivity.this, null);
        userSleepAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MusicPlayerManager.getInstance().playPauseMusic(userSleepAdapter.getData(), position);
            }
        });
        recyclerView.setAdapter(userSleepAdapter);
        //注册观察者以刷新列表
        MusicPlayerManager.getInstance().addObservable(userSleepAdapter);

        View haedView = LayoutInflater.from(this).inflate(R.layout.sleep_detail_head, null);
        userSleepAdapter.addHeaderView(haedView);
    }

    private void loadMusicList() {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", "1065153");
        params.put("page", "1");
        params.put("page_size", "10");
        HttpCoreEngin.get(SleepDetailActivity.this).rxpost("http://sc.wk2.com/Api/Appnq6/music_recommend", MediaMusicCategoryList.class, params, false, false, false).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<MediaMusicCategoryList>() {
            @Override
            public void call(MediaMusicCategoryList data) {
                if (null != data && 1 == data.getCode() && null != data.getData() && data.getData().size() > 0) {
                    List<MusicInfo> musicInfos = new ArrayList<>();
                    for (MediaMusicCategoryList.DataBean dataBean : data.getData()) {
                        MusicInfo musicInfo = new MusicInfo();
                        musicInfo.setId(dataBean.getId());
                        musicInfo.setTitle(dataBean.getTitle());

                        musicInfo.setTime(dataBean.getSeconds());
//                        musicInfo.setMusicCover(dataBean.getCover());
//                        musicInfo.setMusicCover(dataBean.getCover());
//                        musicInfo.setMusicAuthor(dataBean.getAuthor());
//                        musicInfo.setMusicAlbumTitle(dataBean.getTitle());
                        musicInfo.setFile(dataBean.getUrl());
                        musicInfos.add(musicInfo);
                    }
                    if (null != userSleepAdapter) {
                        userSleepAdapter.setNewData(musicInfos);
                        MusicPlayerManager.getInstance().onResumeChecked();
                    }
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //必须注销所有已注册的监听
        MusicPlayerManager.getInstance().detelePlayerStateListener(this);
        if (null != mMusicPlayerController) {
            MusicPlayerManager.getInstance().deleteObserver(mMusicPlayerController);
            mMusicPlayerController.onDestroy();
        }
        if (null != userSleepAdapter) {
            MusicPlayerManager.getInstance().deleteObserver(userSleepAdapter);
        }
    }

    //========================================播放器发生了变化========================================

    @Override
    public void onMusicPlayerState(MusicInfo musicInfo, int stateCode) {

    }

    @Override
    public void checkedPlayTaskResult(MusicInfo musicInfo, KSYMediaPlayer mediaPlayer) {
    }

    @Override
    public void changePlayModelResult(int playModel) {

    }

    @Override
    public void changeAlarmModelResult(int model) {

    }

    @Override
    public void onMusicPlayerConfig(MusicPlayerConfig musicPlayerConfig) {

    }

    @Override
    public void onBufferingUpdate(int percent) {

    }

    @Override
    public void onPrepared(IMediaPlayer mediaPlayer) {

    }

    /**
     * 在这里响应当播放器列表为空 是否播放新的歌曲事件
     *
     * @param viewTupe UI组件身份ID
     * @param position
     */
    @Override
    public void autoStartNewPlayTasks(int viewTupe, int position) {
        if (Constants.UI_TYPE_DETAILS == viewTupe && null != userSleepAdapter) {
            MusicPlayerManager.getInstance().playMusic(userSleepAdapter.getData(), 0);//这个position默认是0，油控制器传出
        }
    }

    @Override
    public void taskRemmainTime(long durtion) {

    }

    @Override
    public void changeCollectResult(int icon, boolean isCollect, String musicID) {

    }
}