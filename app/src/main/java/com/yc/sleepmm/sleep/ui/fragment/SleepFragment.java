package com.yc.sleepmm.sleep.ui.fragment;


import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.view.BaseFragment;
import com.yc.sleepmm.sleep.adapter.SpaListAdapter;
import com.yc.sleepmm.sleep.adapter.SpaTypeListViewAdapter;
import com.yc.sleepmm.sleep.bean.SpaItemInfoWrapper;
import com.yc.sleepmm.sleep.contract.SpaDataContract;
import com.yc.sleepmm.sleep.model.bean.SpaDataInfo;
import com.yc.sleepmm.sleep.model.bean.SpaItemInfo;
import com.yc.sleepmm.sleep.presenter.SpaDataPresenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;


/**
 * Created by wanglin  on 2018/1/10 17:18.
 */

public class SleepFragment extends BaseFragment<SpaDataPresenter> implements SpaDataContract.View, SpaTypeListViewAdapter.OnMoreListener {

    @BindView(R.id.expandablelistview)
    ExpandableListView expandablelistview;

    private Map<Integer, SpaItemInfoWrapper> dataSet = new HashMap<>();

    private SpaTypeListViewAdapter spaTypeListViewAdapter;

    private String currentTypeId;

    private int itemPage = 1;

    private int pageSize = 10;

    private List<SpaDataInfo> dataTypes;

    private int currentGroupPosition;

    private Map<Integer, Integer> typePageMaps = new HashMap<Integer, Integer>();

    private SpaListAdapter currentSpaListAdapter;

    private View currentView;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_sleep;
    }

    @Override
    public void init() {
        //spaItemInfoWrapper = new SpaItemInfoWrapper();
        //spaItemInfoWrapper.setList(list);
        /*adapter = new SpaListAdapter(list);
        mSpaRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSpaRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), SleepDetailActivity.class);
                startActivity(intent);
            }
        });*/

        mPresenter = new SpaDataPresenter(getActivity(), this);

        spaTypeListViewAdapter = new SpaTypeListViewAdapter(getActivity(), null, null);

        expandablelistview.setAdapter(spaTypeListViewAdapter);

        expandablelistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });

        expandablelistview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                currentView = parent.getChildAt(groupPosition);

                LogUtils.i("onGroupClick--->" + groupPosition);

                //当前分类的页面
                int tempPage = typePageMaps.get(groupPosition) != null ? typePageMaps.get(groupPosition) : 1;

                if ((dataTypes != null && dataTypes.get(groupPosition) != null) && tempPage == itemPage) {

                    currentGroupPosition = groupPosition;
                    currentTypeId = dataTypes.get(groupPosition).getId();
                    mPresenter.getSpaItemList(currentTypeId, tempPage, pageSize);
                }
                return false;
            }
        });

        expandablelistview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(getActivity(), "onChildClick" + childPosition, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        expandablelistview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String content = "";
                if ((int) view.getTag(R.layout.spa_list_item_head) == -1) {
                    content = "父类第" + view.getTag(R.layout.spa_list_item_content) + "项" + "被长按了";
                } else {
                    content = "父类第" + view.getTag(R.layout.spa_list_item_head) + "项" + "中的"
                            + "子类第" + view.getTag(R.layout.spa_list_item_content) + "项" + "被长按了";
                }
                Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        mPresenter.getSpaDataList();
        spaTypeListViewAdapter.setOnMoreListener(SleepFragment.this);
    }

    @Override
    public void showSpaData(List<SpaDataInfo> datas) {
        if (datas != null) {
            dataTypes = datas;
            for (int i = 0; i < datas.size(); i++) {
                dataSet.put(i, null);
                typePageMaps.put(i, itemPage);
            }
            spaTypeListViewAdapter.setDataSet(dataSet);
            spaTypeListViewAdapter.setSpaDataInfos(datas);
            spaTypeListViewAdapter.refresh();
        }

    }

    @Override
    public void showSpaItemList(List<SpaItemInfo> itemInfos) {
        if (dataSet != null) {
            if (itemInfos != null && itemInfos.size() > 0) {
                if (currentSpaListAdapter != null) {
                    LogUtils.i("spaListAdapter fragment --->" + currentSpaListAdapter.hashCode());

                    currentSpaListAdapter.addData(itemInfos);
                    if (itemInfos.size() == pageSize) {
                        currentSpaListAdapter.loadMoreComplete();
                    } else {
                        currentSpaListAdapter.loadMoreEnd();
                    }
                }

                int tempPage = typePageMaps.get(currentGroupPosition) != null ? typePageMaps.get(currentGroupPosition) : 1;
                if (tempPage == 1) {
                    SpaItemInfoWrapper spaItemInfoWrapper;
                    if (dataSet.get(currentGroupPosition) != null) {
                        spaItemInfoWrapper = dataSet.get(currentGroupPosition);
                        spaItemInfoWrapper.setAddList(itemInfos);
                    } else {
                        spaItemInfoWrapper = new SpaItemInfoWrapper();
                        spaItemInfoWrapper.setList(itemInfos);
                    }

                    dataSet.put(currentGroupPosition, spaItemInfoWrapper);

                    spaTypeListViewAdapter.setDataSet(dataSet);
                    spaTypeListViewAdapter.refresh();

                }
            }else{
                currentSpaListAdapter.loadMoreEnd();
            }
        }
    }


    @Override
    public void loadMore(SpaListAdapter spaListAdapter) {
        currentSpaListAdapter = spaListAdapter;
        //当前分类的页面
        int tempPage = typePageMaps.get(currentGroupPosition) != null ? typePageMaps.get(currentGroupPosition) : 1;
        int nextPage = tempPage + 1;
        typePageMaps.put(currentGroupPosition, nextPage);

        //RecyclerView recyclerView = currentView.findViewById(R.id.spa_child_list);
        //LogUtils.i("current item adapter --->" + recyclerView.getAdapter());

        mPresenter.getSpaItemList(currentTypeId, nextPage, pageSize);
    }
}
