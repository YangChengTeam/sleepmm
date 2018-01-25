package com.yc.sleepmm.index.ui.fragment;

import com.androidkun.xtablayout.XTabLayout;
import com.yc.sleepmm.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.music.player.lib.manager.MusicPlayerManager;
import com.music.player.lib.mode.PlayerSetyle;
import com.music.player.lib.view.MusicPlayerController;
import com.yc.sleepmm.index.ui.activity.LoginGroupActivity;
import com.yc.sleepmm.index.adapter.AppFragmentPagerAdapter;
import com.yc.sleepmm.base.view.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by wanglin  on 2018/1/10 17:17.
 */

public class IndexFragment extends BaseFragment {


    @BindView(R.id.music_player_controller)
    MusicPlayerController mPlayerController;
    private ViewPager mView_pager;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        initAdapter();
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_index;
    }

    @Override
    public void init() {

    }

    private boolean isCollect = false;

    private void initViews() {
        //设置播放器样式
        mPlayerController.setPlayerStyle(PlayerSetyle.PLAYER_STYLE_DEFAULT);
        //设置UI组件
        mPlayerController.setUIComponentType(HomeMusicListFragment.THIS_TOKIN);
        mPlayerController.setOnClickEventListener(new MusicPlayerController.OnClickEventListener() {
            //收藏
            @Override
            public void onEventCollect() {
                isCollect = !isCollect;
                mPlayerController.setCollectIcon(isCollect ? R.drawable.ic_player_collect_true : R.drawable.ic_player_collect, isCollect);
            }

            //随便听听
            @Override
            public void onEventRandomPlay() {
                //其他界面使用播放控制器示例
                startActivity(new Intent(getActivity(), LoginGroupActivity.class));
                getActivity().overridePendingTransition(R.anim.menu_enter, 0);//进场动画
            }

            @Override
            public void onBack() {

            }
        });
        //注册观察者
        MusicPlayerManager.getInstance().addObservable(mPlayerController);
    }

    /**
     * 初始化分类列表
     */
    private void initAdapter() {
        XTabLayout tab_layout = (XTabLayout) getView().findViewById(R.id.tab_layout);
        mView_pager = (ViewPager) getView().findViewById(R.id.view_pager);
        List<String> titles = new ArrayList<>();
        titles.add("自然音效");
        titles.add("上课音");
        titles.add("轻音乐");
        titles.add("钢琴声");
        titles.add("古典声");
        titles.add("语文课");
        titles.add("物理课");
        titles.add("英语课");
        titles.add("数学课");
        titles.add("化学课");
        titles.add("影视");
        titles.add("汽车");
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < titles.size(); i++) {
            fragments.add(HomeMusicListFragment.newInstance(titles.get(i)));
        }
        AppFragmentPagerAdapter fragmentPagerAdapter = new AppFragmentPagerAdapter(getChildFragmentManager(), fragments, titles);
        mView_pager.setOffscreenPageLimit(1);
        mView_pager.setAdapter(fragmentPagerAdapter);
        mView_pager.setCurrentItem(0);
        tab_layout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tab_layout.setupWithViewPager(mView_pager);
    }

    @Override
    public void onResume() {
        super.onResume();
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


}

