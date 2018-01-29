package com.yc.sleepmm.setting.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.music.player.lib.bean.MusicInfo;
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.view.BaseFragment;
import com.yc.sleepmm.setting.ui.adapter.UserKeepListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by admin on 2018/1/26.
 */

public class UserKeepFragment extends BaseFragment {

    private static final String EXTRA_TYPE = "content";

    @BindView(R.id.keep_recycler_view)
    RecyclerView mKeepRecyclerView;

    UserKeepListAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_user_music;
    }

    @Override
    public void init() {
        List<MusicInfo> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MusicInfo musicInfo = new MusicInfo();
            musicInfo.setMusicTitle("test"+i);
            musicInfo.setUpTime(com.blankj.utilcode.util.TimeUtils.getNowString());
            list.add(musicInfo);
        }

        mAdapter = new UserKeepListAdapter(list);
        mKeepRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mKeepRecyclerView.setAdapter(mAdapter);
    }

    public static UserKeepFragment newInstance(int type) {
        Bundle arguments = new Bundle();
        arguments.putInt(EXTRA_TYPE, type);
        UserKeepFragment userKeepFragment = new UserKeepFragment();
        userKeepFragment.setArguments(arguments);
        return userKeepFragment;
    }

}
