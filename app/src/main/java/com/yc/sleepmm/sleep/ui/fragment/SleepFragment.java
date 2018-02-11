package com.yc.sleepmm.sleep.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.view.BaseFragment;
import com.yc.sleepmm.sleep.adapter.SpaListAdapter;
import com.yc.sleepmm.sleep.bean.SpaItemInfoWrapper;
import com.yc.sleepmm.sleep.model.bean.SpaDataInfo;
import com.yc.sleepmm.sleep.ui.activity.SleepDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;


/**
 * Created by wanglin  on 2018/1/10 17:18.
 */

public class SleepFragment extends BaseFragment {

    /*@BindView(R.id.spa_list)
    RecyclerView mSpaRecyclerView;*/

    @BindView(R.id.expandablelistview)
    ExpandableListView expandablelistview;

    private SpaItemInfoWrapper spaItemInfoWrapper;

    private ArrayList<SpaDataInfo> list;

    private SpaListAdapter adapter;

    private Map<Integer, SpaItemInfoWrapper> dataset = new HashMap<>();

    private String[] parentList = new String[]{"first", "second", "third"};

    @Override
    public int getLayoutId() {
        return R.layout.fragment_sleep;
    }

    @Override
    public void init() {
        list = generateData();
        spaItemInfoWrapper = new SpaItemInfoWrapper();
        spaItemInfoWrapper.setList(list);
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

        initialData();

        expandablelistview.setAdapter(new MyExpandableListViewAdapter());

        expandablelistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "onItemClickListener--" + position, Toast.LENGTH_SHORT).show();

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
    }

    private void initialData() {
        dataset.put(0, spaItemInfoWrapper);
        dataset.put(1, spaItemInfoWrapper);
        dataset.put(2, spaItemInfoWrapper);
    }

    private class MyExpandableListViewAdapter extends BaseExpandableListAdapter {

        //  获得某个父项的某个子项
        @Override
        public Object getChild(int parentPos, int childPos) {
            return dataset.get(parentPos).getList();
        }

        //  获得父项的数量
        @Override
        public int getGroupCount() {
            return dataset.size();
        }

        //  获得某个父项的子项数目
        @Override
        public int getChildrenCount(int parentPos) {
            return 1;
        }

        //  获得某个父项
        @Override
        public Object getGroup(int parentPos) {
            return dataset.get(parentPos);
        }

        //  获得某个父项的id
        @Override
        public long getGroupId(int parentPos) {
            return parentPos;
        }

        //  获得某个父项的某个子项的id
        @Override
        public long getChildId(int parentPos, int childPos) {
            return childPos;
        }

        //  按函数的名字来理解应该是是否具有稳定的id，这个方法目前一直都是返回false，没有去改动过
        @Override
        public boolean hasStableIds() {
            return false;
        }

        //  获得父项显示的view
        @Override
        public View getGroupView(int parentPos, boolean b, View view, ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.spa_list_item_head, null);
            }
            view.setTag(R.layout.spa_list_item_head, parentPos);
            view.setTag(R.layout.spa_list_item_content, -1);
            TextView text = (TextView) view.findViewById(R.id.tv_spa_level_one);
            text.setText(parentList[parentPos]);
            return view;
        }

        //  获得子项显示的view
        @Override
        public View getChildView(int parentPos, int childPos, boolean b, View childView, ViewGroup viewGroup) {
            if (childView == null) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                childView = inflater.inflate(R.layout.spa_list_item_child, null);
            }

            /*childView.setTag(R.layout.spa_list_item_head, parentPos);
            childView.setTag(R.layout.spa_list_item_content, childPos);
            TextView text = (TextView) childView.findViewById(R.id.tv_spa_level_two);
            text.setText(((SpaItemInfo) dataset.get(parentPos).get(childPos)).getTitle());
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "点到了内置的textview", Toast.LENGTH_SHORT).show();
                }
            });*/
            childView.setTag(R.layout.spa_list_item_head, parentPos);
            childView.setTag(R.layout.spa_list_item_content, childPos);

            RecyclerView recyclerView = (RecyclerView)childView.findViewById(R.id.spa_child_list);
            adapter = new SpaListAdapter(dataset.get(parentPos).getList());
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    Intent intent = new Intent(getActivity(), SleepDetailActivity.class);
                    startActivity(intent);
                }
            });

            return childView;
        }

        //  子项是否可选中，如果需要设置子项的点击事件，需要返回true
        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }

    private ArrayList<SpaDataInfo> generateData() {
        int lv0Count = 20;

        ArrayList<SpaDataInfo> res = new ArrayList<>();
        for (int i = 0; i < lv0Count; i++) {
            SpaDataInfo lv1 = new SpaDataInfo("item" + i + 1);
            res.add(lv1);
        }
        return res;
    }
}
