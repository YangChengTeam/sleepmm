package com.yc.sleepmm.sleep.ui.fragment;


import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.kk.utils.LogUtil;
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.view.BaseFragment;
import com.yc.sleepmm.base.view.SpaStateView;
import com.yc.sleepmm.sleep.adapter.SpaListAdapter;
import com.yc.sleepmm.sleep.adapter.SpaTypeListViewAdapterNew;
import com.yc.sleepmm.sleep.contract.SpaDataContract;
import com.yc.sleepmm.sleep.model.bean.SpaDataInfo;
import com.yc.sleepmm.sleep.model.bean.SpaItemInfo;
import com.yc.sleepmm.sleep.presenter.SpaDataPresenter;

import java.util.List;

import butterknife.BindView;


/**
 * Created by wanglin  on 2018/1/10 17:18.
 */

public class SleepFragmentNew extends BaseFragment<SpaDataPresenter> implements SpaDataContract.View, SpaTypeListViewAdapterNew.OnMoreListener {

    @BindView(R.id.stateView)
    SpaStateView stateView;
    @BindView(R.id.expandablelistview)
    ExpandableListView expandablelistview;


    private SparseArray<List<SpaItemInfo>> dataSet;

    private SpaTypeListViewAdapterNew spaTypeListViewAdapter;


    private int itemPage = 1;

    private int pageSize = 5;

    private List<SpaDataInfo> dataTypes;


    private SpaListAdapter currentSpaListAdapter;

    private int lastGroupPosition = -1;

    private SparseIntArray pages;
    private SparseBooleanArray booleanSparseArray;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_sleep;
    }

    @Override
    public void init() {

        mPresenter = new SpaDataPresenter(getActivity(), this);
        spaTypeListViewAdapter = new SpaTypeListViewAdapterNew(getActivity(), null, null);
        expandablelistview.setAdapter(spaTypeListViewAdapter);
        pages = new SparseIntArray();
        dataSet = new SparseArray<>();
        booleanSparseArray = new SparseBooleanArray();
        mPresenter.getSpaDataList();
        spaTypeListViewAdapter.setOnMoreListener(SleepFragmentNew.this);
        initListener();

    }

    private void initListener() {
        expandablelistview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View view, final int groupPosition, long id) {

                if (lastGroupPosition > -1 && lastGroupPosition != groupPosition) {
                    /*if (currentSpaListAdapter != null) {
                        currentSpaListAdapter.setNewData(null);
                    }*/
                    currentSpaListAdapter = null;
                }
                lastGroupPosition = groupPosition;

                spaTypeListViewAdapter.setCurrentParentPosition(groupPosition);
                boolean isGroup = parent.isGroupExpanded(groupPosition);

                TextView moreTextView = view.findViewById(R.id.tv_spa_more);
                Drawable downDrawable = getResources().getDrawable(R.mipmap.spa_more_down);
                Drawable upDrawable = getResources().getDrawable(R.mipmap.spa_more_up);
                if (isGroup) {
                    downDrawable.setBounds(0, 0, downDrawable.getMinimumWidth(), downDrawable.getMinimumHeight());//非常重要，必须设置，否则图片不会显示
                    moreTextView.setCompoundDrawables(downDrawable, null, null, null);

                } else {
                    upDrawable.setBounds(0, 0, upDrawable.getMinimumWidth(), upDrawable.getMinimumHeight());//非常重要，必须设置，否则图片不会显示
                    moreTextView.setCompoundDrawables(upDrawable, null, null, null);
                    pages.put(groupPosition, itemPage);

                }

                //当前分类的页面
                int tempPage = pages.get(groupPosition);

                if ((dataTypes != null && dataTypes.get(groupPosition) != null) && tempPage == itemPage && booleanSparseArray.get(groupPosition)) {
                    booleanSparseArray.put(groupPosition, false);
                    String currentTypeId = dataTypes.get(groupPosition).getId();
                    mPresenter.getSpaItemList(currentTypeId, tempPage, pageSize, groupPosition);
                }
                return false;
            }
        });

    }

    @Override
    public void hide() {
        stateView.hide();
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(expandablelistview, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getSpaDataList();
            }
        });
    }

    @Override
    public void showLoading() {
        stateView.showLoading(expandablelistview);
    }

    @Override
    public void showNoData() {
        stateView.showNoData(expandablelistview);
    }

    @Override
    public void showSpaData(List<SpaDataInfo> datas) {
        if (datas != null) {
            dataTypes = datas;
            if (datas.size() > 0) {
                for (int i = 0; i < datas.size(); i++) {
                    pages.put(i, itemPage);
                    booleanSparseArray.put(i, true);//是否是首次点击
                }
            }
            spaTypeListViewAdapter.setSpaDataInfos(datas);

        }
    }

    @Override
    public void showSpaItemList(List<SpaItemInfo> itemInfos) {

    }

    @Override
    public void showSpaItemList(List<SpaItemInfo> itemInfos, int position) {


        if (itemInfos != null && itemInfos.size() > 0) {

            if (loadMore(itemInfos, position)) return;

            int tempCurrentPage = pages.get(position);
            if (tempCurrentPage == 1) {
                dataSet.clear();
                itemInfos.remove(itemInfos.get(0));
                dataSet.put(position, itemInfos);
                spaTypeListViewAdapter.setDataSet(dataSet);

                LogUtils.i("refresh data --->");

            }
        }

    }

    private boolean loadMore(List<SpaItemInfo> itemInfos, int position) {
        if (currentSpaListAdapter != null) {
            LogUtils.i("spaListAdapter fragment --->" + currentSpaListAdapter.hashCode());
            int currentPage = pages.get(position);

            if (itemInfos.size() == pageSize) {
                //当前分类的页面
                int nextPage = currentPage + 1;
                pages.put(position, nextPage);
                currentSpaListAdapter.loadMoreComplete();
            } else {
                currentSpaListAdapter.loadMoreEnd();
            }

            if (currentPage == 1) {
                itemInfos.remove(0);
                currentSpaListAdapter.setNewData(itemInfos);
            } else {
                currentSpaListAdapter.addData(itemInfos);
            }

            return true;
        }
        return false;
    }

    @Override
    public void loadMore(SpaListAdapter spaListAdapter, int position) {
        LogUtil.msg("TAG  " + "loadMore");
        currentSpaListAdapter = spaListAdapter;
        if (dataTypes != null && dataTypes.size() > 0) {
            mPresenter.getSpaItemList(dataTypes.get(position).getId(), pages.get(position), pageSize, position);
        }
    }

}
