package com.yc.sleepmm.index.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.music.player.lib.bean.MusicInfo;
import com.music.player.lib.bean.MusicPlayerConfig;
import com.music.player.lib.listener.OnUserPlayerEventListener;
import com.music.player.lib.manager.MusicPlayerManager;
import com.music.player.lib.util.Logger;
import com.yc.sleepmm.R;
import com.yc.sleepmm.index.ui.activity.MusicPlayerSample;
import com.yc.sleepmm.index.adapter.HomeMusicListAdapter;
import com.yc.sleepmm.index.ui.contract.HomeMusicListContract;
import com.yc.sleepmm.index.ui.presenter.HomeMusicListPresenter;
import java.util.List;
import butterknife.BindView;

/**
 * TinyHung@Outlook.com
 * 2018/1/21
 * 首页音乐列表
 */

public class HomeMusicListFragment  extends MusicBaseFragment implements OnUserPlayerEventListener, HomeMusicListContract.View, BaseQuickAdapter.RequestLoadMoreListener {

    private static final String TAG = "HomeMusicListFragment";
    private HomeMusicListAdapter mHomeMusicListAdapter;
    public static int THIS_TOKIN=0x10;//这个界面的标识，（这个界面是复用的）
    private String mMusic_id;
    private HomeMusicListPresenter mMusicListPresenter;
    private int page=0;
    private int pageSize=10;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void initViews() {
        initAdapter();
        mSwipeRefreshLayout.setColorSchemeResources(R.color.app_style);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(null!=mMusicListPresenter&&!mMusicListPresenter.isIeGetMusicList()){
                    page=0;
                    loadMusicList();
                }else{
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_music_list;
    }

    public static Fragment newInstance(String title) {
        HomeMusicListFragment fragment=new HomeMusicListFragment();
        Bundle bundle=new Bundle();
        bundle.putString("music_id",title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mMusic_id = arguments.getString("music_id");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MusicPlayerManager.getInstance().addPlayerStateListener(this);
        mMusicListPresenter = new HomeMusicListPresenter(getActivity());
        mMusicListPresenter.attachView(this);
        IndexFragment parentFragment = (IndexFragment) getParentFragment();
        if(null!=parentFragment&&0==parentFragment.getCurrentIndex()){
            loadMusicList();
        }
    }


    private void initAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
        mRecyclerView.setHasFixedSize(true);
        mHomeMusicListAdapter = new HomeMusicListAdapter(null);
        View emptyView = View.inflate(getActivity(), R.layout.item_re_music_list_empty_layout, null);
        ((TextView) emptyView.findViewById(R.id.tvEmptyView)).setText("暂时没有音乐呢~");
        mHomeMusicListAdapter.setEmptyView(emptyView);
        mHomeMusicListAdapter.setOnItemClickListener(new HomeMusicListAdapter.OnItemClickListener() {
            //播放/暂停
            @Override
            public void onPlayMusic(int position, View view) {
                MusicPlayerManager.getInstance().playPauseMusic(mHomeMusicListAdapter.getData(),position);
            }
            //条目
            @Override
            public void onDetails(String musicID) {
                //其他界面使用播放控制器示例
                startActivity(new Intent(getActivity(),MusicPlayerSample.class));
            }
        });

        mHomeMusicListAdapter.setOnLoadMoreListener(this);
        mRecyclerView.setAdapter(mHomeMusicListAdapter);
        //注册观察者以刷新列表
        MusicPlayerManager.getInstance().addObservable(mHomeMusicListAdapter);
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        if(null!=mMusicListPresenter&&!mMusicListPresenter.isIeGetMusicList()&&null!=mHomeMusicListAdapter&&(null==mHomeMusicListAdapter.getData()||mHomeMusicListAdapter.getData().size()<=0)){
            loadMusicList();
        }
    }

    private void loadMusicList() {
        if(null!=mHomeMusicListAdapter&&null!=mMusicListPresenter&&!mMusicListPresenter.isIeGetMusicList()){
            page++;
            mMusicListPresenter.getMusicList(mMusic_id,page,pageSize);
        }
    }

    @Override
    public void onDestroy() {
        if(null!=mMusicListPresenter) mMusicListPresenter.detachView();
        if(null!=mHomeMusicListAdapter){
            MusicPlayerManager.getInstance().deleteObserver(mHomeMusicListAdapter);
        }
        super.onDestroy();
    }


    @Override
    public void onLoadMoreRequested() {
        if(null!=mHomeMusicListAdapter){
            mHomeMusicListAdapter.setEnableLoadMore(true);
            loadMusicList();
        }
    }

    @Override
    protected void onRefresh() {
        super.onRefresh();
        showLoadingView();
        page=0;
        loadMusicList();
    }
    //========================================联网获取数据回调=======================================


    @Override
    public void showErrorView() {

    }

    @Override
    public void complete() {

    }

    @Override
    public void showMusicList(List<MusicInfo> data) {
        if(null!=mSwipeRefreshLayout) mSwipeRefreshLayout.setRefreshing(false);
        showContentView();
        if(null!=mHomeMusicListAdapter){
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mHomeMusicListAdapter.loadMoreComplete();
                }
            });
            if(1==page){
                if(null!= mHomeMusicListAdapter){
                    mHomeMusicListAdapter.setNewData(data);
                }
            }else{
                if(null!= mHomeMusicListAdapter){
                    mHomeMusicListAdapter.addData(data);
                }
            }
            MusicPlayerManager.getInstance().onResumeChecked();//在刷新之后检查，防止列表为空，无法全局同步
        }
    }

    @Override
    public void showMusicListEmpty(String data) {
        if(null!=mSwipeRefreshLayout) mSwipeRefreshLayout.setRefreshing(false);
        showContentView();
        if(null!=mHomeMusicListAdapter){
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mHomeMusicListAdapter.loadMoreEnd();
                }
            });
        }
    }

    @Override
    public void showMusicListError(String data) {
        if(null!=mSwipeRefreshLayout) mSwipeRefreshLayout.setRefreshing(false);
        if(null!=mHomeMusicListAdapter){
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mHomeMusicListAdapter.loadMoreFail();
                }
            });
            if(1==page&&null==mHomeMusicListAdapter.getData()||mHomeMusicListAdapter.getData().size()<=0){
                showLoadingErrorView();
            }
        }
        if(page>0){
            page--;
        }
    }

    //========================================播放器状态发生变化=====================================

    /**
     * 播放器发生了变化
     * @param musicInfo 当前播放的任务，未播放为空
     * @param stateCode 类别Code: 0：未播放 1：准备中 2：正在播放 3：暂停播放, 4：停止播放
     */
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
     * 请求接收这个事件，是在Home界面中注册的
     * @param viewTupe
     * @param position
     */
    @Override
    public void autoStartNewPlayTasks(int viewTupe, int position) {
        //如果是自己，且正在显示，处理自动播放事件
        if(viewTupe==HomeMusicListFragment.THIS_TOKIN&&getUserVisibleHint()&&null!=mHomeMusicListAdapter){
            MusicPlayerManager.getInstance().playMusic(mHomeMusicListAdapter.getData(),0);//默认播放第0个
        }
    }

    @Override
    public void taskRemmainTime(long durtion) {

    }

    @Override
    public void changeCollectResult(int icon, boolean isCollect, String musicID) {

    }
}
