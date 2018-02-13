package com.yc.sleepmm.index.ui.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.androidkun.xtablayout.XTabLayout;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.music.player.lib.bean.MusicInfo;
import com.music.player.lib.manager.MusicPlayerManager;
import com.music.player.lib.mode.PlayerSetyle;
import com.music.player.lib.mode.PlayerStatus;
import com.music.player.lib.util.PreferencesUtil;
import com.music.player.lib.view.MusicPlayerController;
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.APP;
import com.yc.sleepmm.base.view.BaseFragment;
import com.yc.sleepmm.index.adapter.AppFragmentPagerAdapter;
import com.yc.sleepmm.index.model.bean.MusicTypeInfo;
import com.yc.sleepmm.index.rxnet.IndexMusicContract;
import com.yc.sleepmm.index.ui.presenter.IndexMusicPresenter;
import com.yc.sleepmm.setting.constants.BusAction;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by wanglin  on 2018/1/10 17:17.
 */

public class IndexFragment extends BaseFragment<IndexMusicPresenter> implements IndexMusicContract.View {


    @BindView(R.id.music_player_controller)
    MusicPlayerController mPlayerController;
    @BindView(R.id.tab_layout)
    XTabLayout tab_layout;
    @BindView(R.id.view_pager)
    ViewPager mView_pager;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_index;
    }

    @Override
    public void init() {
        mPresenter = new IndexMusicPresenter(getActivity(), this);
        mPresenter.getMusicTypes();
        initViews();
    }


    private void initViews() {
        //设置播放器样式
        mPlayerController.setPlayerStyle(PlayerSetyle.PLAYER_STYLE_DEFAULT);
        //设置UI组件
        mPlayerController.setUIComponentType(HomeMusicListFragment.THIS_TOKIN);

        mPlayerController.setOnClickEventListener(new MusicPlayerController.OnClickEventListener() {
            //收藏
            @Override
            public void onEventCollect(MusicInfo musicInfo) {

                mPresenter.collectMusic(musicInfo != null ? musicInfo.getId() : "");
            }

            //随便听听
            @Override
            public void onEventRandomPlay() {
                //其他界面使用播放控制器示例
                if (!APP.getInstance().isGotoLogin(getActivity())) {
                    mPresenter.randomPlay();
                }
            }

            @Override
            public void onBack() {

            }

            @Override
            public void onPlayState(MusicInfo info) {
                String id = "";
                if (info != null) {
                    id = info.getId();
                    if (PlayerStatus.PLAYER_STATUS_PLAYING == info.getPlauStatus()) {
                        mPresenter.playStatistics(id);
                    }
                }
                boolean isCollect = PreferencesUtil.getInstance().getBoolean(id);
                mPlayerController.setCollectIcon(isCollect ? R.drawable.ic_player_collect_true : R.drawable.ic_player_collect, isCollect, id);

            }
        });
        //注册观察者
        MusicPlayerManager.getInstance().addObservable(mPlayerController);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mPlayerController) {
            MusicPlayerManager.getInstance().deleteObserver(mPlayerController);//移除观察者
            mPlayerController.onDestroy();
        }
    }

    /**
     * 返回当前正在显示的角标位置
     *
     * @return
     */
    public int getCurrentIndex() {
        if (null != mView_pager) {
            return mView_pager.getCurrentItem();
        }
        return 0;
    }

    private List<MusicTypeInfo> musicTypeInfos;


    @Override
    public void showMusicTypeInfo(List<MusicTypeInfo> data) {
        List<HomeMusicListFragment> fragments = new ArrayList<>();
        if (data.size() > 0) {
            musicTypeInfos = data;
            for (int i = 0; i < data.size(); i++) {

                fragments.add(HomeMusicListFragment.newInstance(data.get(i).id + "", i));
            }
            AppFragmentPagerAdapter fragmentPagerAdapter = new AppFragmentPagerAdapter(getChildFragmentManager(), fragments, data);
            mView_pager.setOffscreenPageLimit(data.size());
            mView_pager.setAdapter(fragmentPagerAdapter);
            mView_pager.setCurrentItem(0);
            tab_layout.setTabMode(TabLayout.MODE_SCROLLABLE);
            tab_layout.setupWithViewPager(mView_pager);
        }
    }


    @Subscribe(thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.MUSIC_INFO)
            })
    @Override
    public void showRandomMusicInfo(MusicInfo data) {
        int position = -1;
        MusicPlayerManager.getInstance().playMusic(data);
        if (musicTypeInfos != null && musicTypeInfos.size() > 0) {
            for (int i = 0; i < musicTypeInfos.size(); i++) {
                if (musicTypeInfos.get(i).id.equals(data.getType_id())) {
                    position = i;
                    break;
                }
            }
        }
        if (position >= 0) {
            mView_pager.setCurrentItem(position);
//            if (position < musicTypeInfos.size() && getFragment(position) != null) {
//                getFragment(position).scrollToposition(data);
//            }
        }

    }

    @Override
    public void showCollectSucess(boolean isCollect) {
        mPlayerController.setCollectIcon(isCollect ? R.drawable.ic_player_collect_true : R.drawable.ic_player_collect, isCollect);
    }


}

