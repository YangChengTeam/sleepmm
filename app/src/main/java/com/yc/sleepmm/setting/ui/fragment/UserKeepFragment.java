package com.yc.sleepmm.setting.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.RxBus;
import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.music.player.lib.bean.MusicInfo;
import com.music.player.lib.bean.MusicPlayerConfig;
import com.music.player.lib.listener.OnUserPlayerEventListener;
import com.music.player.lib.manager.MusicPlayerManager;
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.view.BaseFragment;
import com.yc.sleepmm.base.view.StateView;
import com.yc.sleepmm.main.ui.activity.MainActivity;
import com.yc.sleepmm.setting.constants.BusAction;
import com.yc.sleepmm.setting.contract.CollectContract;
import com.yc.sleepmm.setting.presenter.CollectPresenter;
import com.yc.sleepmm.setting.ui.adapter.UserKeepListAdapter;
import com.yc.sleepmm.sleep.ui.activity.SleepDetailActivity;

import java.util.List;

import butterknife.BindView;

/**
 * Created by admin on 2018/1/26.
 */

public class UserKeepFragment extends BaseFragment<CollectPresenter> implements OnUserPlayerEventListener, CollectContract.View {

    private static final String EXTRA_TYPE = "content";

    @BindView(R.id.keep_recycler_view)
    RecyclerView mKeepRecyclerView;


    @BindView(R.id.stateView)
    StateView stateView;

    UserKeepListAdapter mAdapter;
    private int MUSIC_PAGE = 1;
    private int SPA_PAGE = 1;

    private int LIMIT = 10;
    private int type = 0;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_user_music;
    }


    @Override
    public void init() {
        type = getArguments().getInt(EXTRA_TYPE);
        mPresenter = new CollectPresenter(getActivity(), this);

        getData();
        mAdapter = new UserKeepListAdapter(null);
        mKeepRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mKeepRecyclerView.setAdapter(mAdapter);
        MusicPlayerManager.getInstance().addObservable(mAdapter);
        initListener();
    }

    private void initListener() {
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData();
            }
        }, mKeepRecyclerView);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MusicInfo musicInfo = mAdapter.getItem(position);
                Intent intent;
                if (type == 1) {
                    intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("position", 0);
                    RxBus.get().post(BusAction.MUSIC_INFO, musicInfo);
                    startActivity(intent);
                } else if (type == 2) {
                    intent = new Intent(getActivity(), SleepDetailActivity.class);
                    intent.putExtra("spa_id", musicInfo.getId());
                    startActivity(intent);
                }

            }

        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public boolean onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                MusicPlayerManager.getInstance().playPauseMusic(mAdapter.getData(), position);
                return true;
            }
        });

    }

    public static UserKeepFragment newInstance(int type) {
        Bundle arguments = new Bundle();
        arguments.putInt(EXTRA_TYPE, type);
        UserKeepFragment userKeepFragment = new UserKeepFragment();
        userKeepFragment.setArguments(arguments);
        return userKeepFragment;
    }

    @Override
    public void hide() {
        stateView.hide();
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(mKeepRecyclerView, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    @Override
    public void showLoading() {
        stateView.showLoading(mKeepRecyclerView);
    }

    @Override
    public void showNoData() {
        stateView.showNoData(mKeepRecyclerView);
    }


    @Override
    public void showSpaCollectList(List<MusicInfo> data) {

        if (SPA_PAGE == 1) {
            mAdapter.setNewData(data);
        } else {
            mAdapter.addData(data);
        }

        if (data != null && data.size() == LIMIT) {
            SPA_PAGE++;
            mAdapter.loadMoreComplete();
        } else {
            mAdapter.loadMoreEnd();
        }
        MusicPlayerManager.getInstance().onResumeChecked();//在刷新之后检查，防止列表为空，无法全局同步

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mAdapter) {
            MusicPlayerManager.getInstance().deleteObserver(mAdapter);
        }
    }

    @Override
    public void showMusicCollectList(List<MusicInfo> data) {

        if (MUSIC_PAGE == 1) {
            mAdapter.setNewData(data);
        } else {
            mAdapter.addData(data);
        }

        if (data != null && data.size() == LIMIT) {

            MUSIC_PAGE++;
            mAdapter.loadMoreComplete();
        } else {
            mAdapter.loadMoreEnd();
        }
        MusicPlayerManager.getInstance().onResumeChecked();//在刷新之后检查，防止列表为空，无法全局同步
    }


    private void getMusicFavoriteList() {

        if (type == 1)
            mPresenter.getMusicFavoriteList(MUSIC_PAGE, LIMIT);
    }

    public void getSpaFavoriteList() {
        if (type == 2)
            mPresenter.getSpaFavoriteList(SPA_PAGE, LIMIT);
    }


    private void getData() {

        getMusicFavoriteList();

        getSpaFavoriteList();

    }

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

    @Override
    public void autoStartNewPlayTasks(int viewTupe, int position) {

    }

    @Override
    public void taskRemmainTime(long durtion) {

    }

    @Override
    public void changeCollectResult(int icon, boolean isCollect, String musicID) {

    }
}
