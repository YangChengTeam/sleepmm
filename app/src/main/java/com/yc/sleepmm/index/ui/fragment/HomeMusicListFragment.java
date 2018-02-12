package com.yc.sleepmm.index.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.music.player.lib.bean.MusicInfo;
import com.music.player.lib.bean.MusicPlayerConfig;
import com.music.player.lib.listener.OnUserPlayerEventListener;
import com.music.player.lib.manager.MusicPlayerManager;
import com.music.player.lib.mode.PlayerStatus;
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.view.StateView;
import com.yc.sleepmm.index.adapter.HomeMusicListAdapter;
import com.yc.sleepmm.index.rxnet.IndexMusicTypeDetailContract;
import com.yc.sleepmm.index.ui.presenter.IndexMusicTypeDetailPresenter;

import java.util.List;

import butterknife.BindView;

/**
 * TinyHung@Outlook.com
 * 2018/1/21
 * 首页音乐列表
 */

public class HomeMusicListFragment extends MusicBaseFragment<IndexMusicTypeDetailPresenter> implements OnUserPlayerEventListener, IndexMusicTypeDetailContract.View {

    private static final String TAG = "HomeMusicListFragment";
    @BindView(R.id.stateView)
    StateView stateView;
    private HomeMusicListAdapter mHomeMusicListAdapter;
    public static int THIS_TOKIN = 0x10;//这个界面的标识，（这个界面是复用的）
    private String mMusic_id;
    private int page = 1;
    private int pageSize = 10;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void initViews() {
        mPresenter = new IndexMusicTypeDetailPresenter(getActivity(), this);

        getData(false);
        initAdapter();

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_music_list;
    }


    public static HomeMusicListFragment newInstance(String title, int i) {
        HomeMusicListFragment fragment = new HomeMusicListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("music_id", title);
        bundle.putInt("position", i);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mMusic_id = arguments.getString("music_id");
    }


    private void initAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
        mHomeMusicListAdapter = new HomeMusicListAdapter(null);


        mRecyclerView.setAdapter(mHomeMusicListAdapter);
        initListener();
        //注册观察者以刷新列表
        MusicPlayerManager.getInstance().addObservable(mHomeMusicListAdapter);
    }

    private void initListener() {

        mSwipeRefreshLayout.setColorSchemeResources(R.color.app_style);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getData(true);

            }
        });

        mHomeMusicListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //其他界面使用播放控制器示例
                MusicPlayerManager.getInstance().playPauseMusic(mHomeMusicListAdapter.getData(), position);
            }
        });
        mHomeMusicListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData(false);
            }
        }, mRecyclerView);
    }

//    @Override
//    protected void onVisible() {
//        super.onVisible();
//        if (null != mHomeMusicListAdapter && (null == mHomeMusicListAdapter.getData() || mHomeMusicListAdapter.getData().size() <= 0)) {
//            getData(false);
//        }
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mHomeMusicListAdapter) {
            MusicPlayerManager.getInstance().deleteObserver(mHomeMusicListAdapter);
        }
    }


    @Override
    protected void onRefresh() {
        super.onRefresh();
        page = 1;
        getData(false);
    }
    //========================================联网获取数据回调=======================================


    //========================================播放器状态发生变化=====================================

    /**
     * 播放器发生了变化
     *
     * @param musicInfo 当前播放的任务，未播放为空
     * @param stateCode 类别Code: 0：未播放 1：准备中 2：正在播放 3：暂停播放, 4：停止播放
     */
    @Override
    public void onMusicPlayerState(MusicInfo musicInfo, int stateCode) {

        switch (stateCode) {
            case PlayerStatus.PLAYER_STATUS_EMPOTY:
                break;
            case PlayerStatus.PLAYER_STATUS_ASYNCPREPARE:

                break;
            case PlayerStatus.PLAYER_STATUS_PLAYING:
                break;
            case PlayerStatus.PLAYER_STATUS_PAUSE:
//                mHomeMusicListAdapter
                break;
            case PlayerStatus.PLAYER_STATUS_STOP:
                break;
            case PlayerStatus.PLAYER_STATUS_ERROR:
                break;
        }
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
     * 请求接收这个事件，是在Home界面中注册的
     *
     * @param viewTupe
     * @param position
     */
    @Override
    public void autoStartNewPlayTasks(int viewTupe, int position) {
        //如果是自己，且正在显示，处理自动播放事件
        if (viewTupe == HomeMusicListFragment.THIS_TOKIN && getUserVisibleHint() && null != mHomeMusicListAdapter) {
            MusicPlayerManager.getInstance().playMusic(mHomeMusicListAdapter.getData(), 0);//默认播放第0个
        }
    }

    @Override
    public void taskRemmainTime(long durtion) {

    }

    @Override
    public void changeCollectResult(int icon, boolean isCollect, String musicID) {

    }

    private void getData(boolean isRefresh) {
        mPresenter.getMusicInfos(mMusic_id, page, pageSize, isRefresh);
    }

    @Override
    public void hide() {
        stateView.hide();
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(mSwipeRefreshLayout, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 1;
                getData(false);
            }
        });
    }

    @Override
    public void showLoading() {
        stateView.showLoading(mSwipeRefreshLayout);
    }

    @Override
    public void showNoData() {
        stateView.showNoData(mSwipeRefreshLayout);
    }

    @Override
    public void showMusicInfos(List<MusicInfo> data) {

        if (page == 1) {
            mHomeMusicListAdapter.setNewData(data);
        } else {
            mHomeMusicListAdapter.addData(data);
        }
        if (data.size() > 0 && data.size() == pageSize) {
            page++;
            mHomeMusicListAdapter.loadMoreComplete();
        } else {
            mHomeMusicListAdapter.loadMoreEnd();
        }
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        MusicPlayerManager.getInstance().onResumeChecked();//在刷新之后检查，防止列表为空，无法全局同步
    }


    public void scrollToposition(MusicInfo musicInfo) {
        if (null != mHomeMusicListAdapter && mHomeMusicListAdapter.getData() != null && mHomeMusicListAdapter.getData().size() > 0) {

            List<MusicInfo> musicInfos = mHomeMusicListAdapter.getData();
            int position = -1;
            for (int i = 0; i < musicInfos.size(); i++) {
                if (musicInfo.getId().equals(musicInfos.get(i).getId())) {
                    position = i;
                    break;
                }
            }
//            mRecyclerView.scrollToPosition(position);
            mRecyclerView.smoothScrollToPosition(position);


        }
    }

}
