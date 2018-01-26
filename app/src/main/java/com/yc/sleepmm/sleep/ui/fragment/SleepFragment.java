package com.yc.sleepmm.sleep.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.view.BaseFragment;
import com.yc.sleepmm.sleep.adapter.SpaListAdapter;
import com.yc.sleepmm.sleep.bean.SpaDataInfo;
import com.yc.sleepmm.sleep.bean.SpaItemInfo;
import com.yc.sleepmm.sleep.ui.SleepDetailActivity;

import java.util.ArrayList;

import butterknife.BindView;


/**
 * Created by wanglin  on 2018/1/10 17:18.
 */

public class SleepFragment extends BaseFragment {

    @BindView(R.id.spa_list)
    RecyclerView mSpaRecyclerView;

    ArrayList<MultiItemEntity> list;

    SpaListAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_sleep;
    }

    @Override
    public void init() {
        list = generateData();

        adapter = new SpaListAdapter(list);
        mSpaRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSpaRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), SleepDetailActivity.class);
                startActivity(intent);
            }
        });

    }

    private ArrayList<MultiItemEntity> generateData() {
        int lv0Count = 9;
        int lv1Count = 3;

        ArrayList<MultiItemEntity> res = new ArrayList<>();
        for (int i = 0; i < lv0Count; i++) {
            SpaDataInfo lv0 = new SpaDataInfo("小睡眠测试数据SPA");
            for (int j = 0; j < lv1Count; j++) {
                SpaItemInfo lv1 = new SpaItemInfo("item" + j + 1);
                lv0.addSubItem(lv1);
            }
            res.add(lv0);
        }
        return res;
    }
}
