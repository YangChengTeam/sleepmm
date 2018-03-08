package com.yc.sleepmm.sleep.ui.fragment;


import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.view.BaseFragment;
import com.yc.sleepmm.base.view.SpaStateView;
import com.yc.sleepmm.sleep.adapter.SpaMainAdapter;
import com.yc.sleepmm.sleep.contract.SpaDataContract;
import com.yc.sleepmm.sleep.model.bean.SpaDataInfo;
import com.yc.sleepmm.sleep.model.bean.SpaItemInfo;
import com.yc.sleepmm.sleep.presenter.SpaDataPresenter;
import com.yc.sleepmm.sleep.ui.activity.SleepDetailActivity;

import java.util.List;

import butterknife.BindView;


/**
 * Created by wanglin  on 2018/1/10 17:18.
 */

public class SleepFragment extends BaseFragment<SpaDataPresenter> implements SpaDataContract.View {

    @BindView(R.id.stateView)
    SpaStateView stateView;
    @BindView(R.id.recyclerView_sleep)
    RecyclerView recyclerViewSleep;

    private int itemPage = 1;

    private int pageSize = 10;

    private List<SpaDataInfo> dataTypes;


    private SparseIntArray pages;
    private SparseBooleanArray booleanSparseArray;
    private SpaMainAdapter spaMainAdapter;
    private SparseBooleanArray booleanArray;//是否是首次点击


    @Override
    public int getLayoutId() {
        return R.layout.fragment_main_sleep;
    }

    @Override
    public void init() {

        mPresenter = new SpaDataPresenter(getActivity(), this);
        spaMainAdapter = new SpaMainAdapter(null);
        recyclerViewSleep.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewSleep.setAdapter(spaMainAdapter);
        pages = new SparseIntArray();

        booleanSparseArray = new SparseBooleanArray();
        booleanArray = new SparseBooleanArray();

        mPresenter.getSpaDataList();

        recyclerViewSleep.addItemDecoration(new MyDecoration());
        initListener();

    }

    private void initListener() {

        spaMainAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public boolean onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), SleepDetailActivity.class);
                SpaItemInfo spaItemInfo = spaMainAdapter.getItem(position).getFirst();
                intent.putExtra("pos", 1);
                intent.putExtra("type_id", spaItemInfo.getType_id());
                intent.putExtra("spa_id", spaItemInfo.getId());
                startActivity(intent);
                return false;

            }
        });
        spaMainAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
                TextView moreTextView = view.findViewById(R.id.tv_spa_more);
                Drawable downDrawable = getResources().getDrawable(R.mipmap.spa_more_down);
                Drawable upDrawable = getResources().getDrawable(R.mipmap.spa_more_up);
                boolean isGroup = booleanSparseArray.get(position);
                if (isGroup) {
                    downDrawable.setBounds(0, 0, downDrawable.getMinimumWidth(), downDrawable.getMinimumHeight());//非常重要，必须设置，否则图片不会显示
                    moreTextView.setCompoundDrawables(downDrawable, null, null, null);
                    if (dataTypes != null && dataTypes.size() > 0 && booleanArray.get(position)) {
                        booleanArray.put(position, false);
                        mPresenter.getSpaItemList(dataTypes.get(position).getId(), pages.get(position), pageSize, position);
                    }
                    spaMainAdapter.setVisable(true, position);

                    spaMainAdapter.getAdapter(position).setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                        @Override
                        public void onLoadMoreRequested() {
                            mPresenter.getSpaItemList(dataTypes.get(position).getId(), pages.get(position), pageSize, position);
                        }
                    }, spaMainAdapter.getView(position));

                    spaMainAdapter.getAdapter(position).setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int pos) {

                            SpaItemInfo spaItemInfo = spaMainAdapter.getAdapter(position).getItem(pos);
                            Intent intent = new Intent(getActivity(), SleepDetailActivity.class);
                            intent.putExtra("spa_id", spaItemInfo.getId());
                            intent.putExtra("type_id", spaItemInfo.getType_id());
                            intent.putExtra("pos", spaItemInfo.getGroupPos());
                            startActivity(intent);
                        }
                    });


                } else {
                    upDrawable.setBounds(0, 0, upDrawable.getMinimumWidth(), upDrawable.getMinimumHeight());//非常重要，必须设置，否则图片不会显示
                    moreTextView.setCompoundDrawables(upDrawable, null, null, null);
                    pages.put(position, itemPage);
                    spaMainAdapter.setVisable(false, position);
                }
                isGroup = !isGroup;
                booleanSparseArray.put(position, isGroup);
            }
        });


    }

    @Override
    public void hide() {
        stateView.hide();
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(recyclerViewSleep, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getSpaDataList();
            }
        });
    }

    @Override
    public void showLoading() {
        stateView.showLoading(recyclerViewSleep);
    }

    @Override
    public void showNoData() {
        stateView.showNoData(recyclerViewSleep);
    }

    @Override
    public void showSpaData(List<SpaDataInfo> datas) {
        if (datas != null) {
            dataTypes = datas;
            if (datas.size() > 0) {
                for (int i = 0; i < datas.size(); i++) {
                    pages.put(i, itemPage);
                    booleanSparseArray.put(i, true);
                    booleanArray.put(i, true);//是否是首次点击
                }
            }
            spaMainAdapter.setNewData(datas);

        }
    }

    @Override
    public void showSpaItemList(List<SpaItemInfo> itemInfos) {

    }

    @Override
    public void showSpaItemList(List<SpaItemInfo> itemInfos, int position) {


        if (itemInfos != null) {

            loadMore(itemInfos, position);
        }

    }


    private void loadMore(List<SpaItemInfo> itemInfos, int position) {

        int currentPage = pages.get(position);

        if (itemInfos.size() > 0) {
            //当前分类的页面
            int nextPage = currentPage + 1;
            pages.put(position, nextPage);
            spaMainAdapter.getAdapter(position).loadMoreComplete();
        } else {
            spaMainAdapter.getAdapter(position).loadMoreEnd();
        }

        if (currentPage == 1) {
            itemInfos.remove(0);
            spaMainAdapter.getAdapter(position).setNewData(itemInfos);
        } else {
            spaMainAdapter.getAdapter(position).addData(itemInfos);
        }


    }

    private class MyDecoration extends RecyclerView.ItemDecoration {

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0, 0, 0, SizeUtils.dp2px(10));
        }
    }


}
