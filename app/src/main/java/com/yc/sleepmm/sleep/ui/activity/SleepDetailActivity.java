package com.yc.sleepmm.sleep.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.music.player.lib.bean.MusicInfo;
import com.music.player.lib.bean.MusicPlayerConfig;
import com.music.player.lib.constants.Constants;
import com.music.player.lib.listener.OnUserPlayerEventListener;
import com.music.player.lib.manager.MusicPlayerManager;
import com.music.player.lib.mode.PlayerSetyle;
import com.music.player.lib.mode.PlayerStatus;
import com.music.player.lib.util.PreferencesUtil;
import com.music.player.lib.view.MusicPlayerController;
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.APP;
import com.yc.sleepmm.base.view.BaseActivity;
import com.yc.sleepmm.base.view.StateView;
import com.yc.sleepmm.sleep.adapter.UserSleepAdapter;
import com.yc.sleepmm.sleep.contract.SpaDetailContract;
import com.yc.sleepmm.sleep.presenter.SpaDetailPresenter;

import butterknife.BindView;

/**
 * Created by admin on 2018/1/26.
 */

public class SleepDetailActivity extends BaseActivity<SpaDetailPresenter> implements OnUserPlayerEventListener, SpaDetailContract.View {

    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.music_player_controller)
    MusicPlayerController mMusicPlayerController;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;


    private UserSleepAdapter userSleepAdapter;
    private String spaId = "";
    private TextView tvWordDes;
    private ImageView ivUserHead;
    private TextView tvAutor;
    private TextView tvAutorDes;

    @Override
    public int getLayoutId() {
        return R.layout.activity_sleep_detail;
    }

    @Override
    public void init() {
        mPresenter = new SpaDetailPresenter(this, this);

        Intent intent = getIntent();
        if (intent != null && intent.getStringExtra("spa_id") != null)
            spaId = intent.getStringExtra("spa_id");

        getData();
        initViews();
        initAdapter();

    }

    /**
     * 初始设置示例
     */
    private void initViews() {
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


        //注册事件回调
        mMusicPlayerController.setOnClickEventListener(new MusicPlayerController.OnClickEventListener() {
            //收藏事件触发了
            @Override
            public void onEventCollect(MusicInfo musicInfo) {
                mPresenter.collectSpa(musicInfo != null ? musicInfo.getId() : "");
            }

            //随机播放触发了
            @Override
            public void onEventRandomPlay() {
                //其他界面使用播放控制器示例
                if (!APP.getInstance().isGotoLogin(SleepDetailActivity.this)) {
                    mPresenter.randomSpaInfo();
                }
            }

            //返回事件
            @Override
            public void onBack() {
                SleepDetailActivity.this.onBackPressed();
            }

            @Override
            public void onPlayState(MusicInfo info) {
                String id = "";
                if (info != null) {
                    id = info.getId();
                    if (PlayerStatus.PLAYER_STATUS_PLAYING == info.getPlauStatus()) {
                        mPresenter.spaPlay(id);
                    }
                }
                boolean isCollect = PreferencesUtil.getInstance().getBoolean(id);
                mMusicPlayerController.setCollectIcon(isCollect ? R.drawable.ic_player_collect_true : R.drawable.ic_player_collect, isCollect, id);
            }
        });
        //注册到被观察者中
        MusicPlayerManager.getInstance().addObservable(mMusicPlayerController);
//        //注册播放变化监听
        MusicPlayerManager.getInstance().addPlayerStateListener(this);
//        MusicPlayerManager.getInstance().onResumeChecked();//先让播放器刷新起来
    }

    /**
     * 配合播放列表示例
     */
    private void initAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(SleepDetailActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        //如列表需要实时更新播放动态的话，Adapter需要实现Observer接口
        userSleepAdapter = new UserSleepAdapter(null);
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
        tvWordDes = haedView.findViewById(R.id.tv_word_des);
        ivUserHead = haedView.findViewById(R.id.iv_user_head);
        tvAutor = haedView.findViewById(R.id.tv_autor);
        tvAutorDes = haedView.findViewById(R.id.tv_autor_des);

        userSleepAdapter.addHeaderView(haedView);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //必须注销所有已注册的监听
//        MusicPlayerManager.getInstance().detelePlayerStateListener(this);
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


    public void getData() {
        mPresenter.getSpaDetailInfo(spaId);
    }


    @Override
    public void hide() {
        stateView.hide();
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(llContainer, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    @Override
    public void showLoading() {
        stateView.showLoading(llContainer);
    }

    @Override
    public void showNoData() {
        stateView.showNoData(llContainer);
    }



    @Override
    public void showSpaDetailInfo(MusicInfo data, boolean isRandom) {

        if (null != userSleepAdapter && data != null && data.getLists() != null && data.getLists().size() > 0) {

            showCollectSucess(data.getLists().get(0).getIs_favorite() == 1);
            userSleepAdapter.setNewData(data.getLists());
            tvWordDes.setText(data.getTitle());
            tvAutor.setText(data.getAuthor_title());
            tvAutorDes.setText(data.getAuthor_desp());
            Glide.with(SleepDetailActivity.this).load(data.getAuthor_img()).apply(new RequestOptions().error(R.mipmap.default_avatar).circleCrop()).into(ivUserHead);
            if (isRandom) {
                int position = (int) (Math.random() * data.getLists().size());
                MusicPlayerManager.getInstance().playMusic(data.getLists(), position);
            }

        }
        MusicPlayerManager.getInstance().onResumeChecked();//在刷新之后检查，防止列表为空，无法全局同步
    }

    @Override
    public void showCollectSucess(boolean isCollect) {
        mMusicPlayerController.setCollectIcon(isCollect ? R.drawable.ic_player_collect_true : R.drawable.ic_player_collect, isCollect);
    }


}