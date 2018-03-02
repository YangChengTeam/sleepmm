package com.yc.sleepmm.sleep.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kk.utils.LogUtil;
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.util.DateUtils;
import com.yc.sleepmm.sleep.model.bean.SpaDataInfo;
import com.yc.sleepmm.sleep.model.bean.SpaItemInfo;
import com.yc.sleepmm.sleep.ui.activity.SleepDetailActivity;

import java.util.List;

public class SpaTypeListViewAdapter extends BaseExpandableListAdapter {


    public Context mContext;

    private SparseArray<List<SpaItemInfo>> dataSet;

    private List<SpaDataInfo> spaDataInfos;


    public int currentParentPosition;

    public int lastParentPosition;

    public SparseArray<RecyclerView> sparseArray;

    public interface OnMoreListener {
        void loadMore(SpaListAdapter spaListAdapter, int position);

    }

    public OnMoreListener onMoreListener;

    public void setOnMoreListener(OnMoreListener onMoreListener) {
        this.onMoreListener = onMoreListener;
    }

    public void setDataSet(SparseArray<List<SpaItemInfo>> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    public void setSpaDataInfos(List<SpaDataInfo> spaDataInfos) {
        this.spaDataInfos = spaDataInfos;
        notifyDataSetChanged();
    }

    public void setCurrentParentPosition(int currentParentPosition) {
        this.currentParentPosition = currentParentPosition;
    }

    public SpaTypeListViewAdapter(Context context, SparseArray<List<SpaItemInfo>> dataset, List<SpaDataInfo> spaDataInfos) {
        this.mContext = context;
        this.dataSet = dataset;
        this.spaDataInfos = spaDataInfos;
        sparseArray = new SparseArray<>();
    }


    //  获得某个父项的某个子项
    @Override
    public Object getChild(int parentPos, int childPos) {
        return dataSet != null ? dataSet.get(parentPos).get(childPos) : null;
    }


    //  获得父项的数量
    @Override
    public int getGroupCount() {
        return spaDataInfos != null ? spaDataInfos.size() : 0;
    }

    //  获得某个父项的子项数目
    @Override
    public int getChildrenCount(int parentPos) {
        return 1;
    }

    //  获得某个父项
    @Override
    public Object getGroup(int parentPos) {
        return spaDataInfos != null ? spaDataInfos.get(parentPos) : null;
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
    public View getGroupView(final int parentPos, boolean isExpanded, View converView, ViewGroup parent) {
        LogUtil.msg("TAG getGroupView");
        View view = converView;
        GroupHolder groupHolder;
        if (view == null) {
            groupHolder = new GroupHolder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.spa_list_item_head, null);
            groupHolder.mTitleTv = view.findViewById(R.id.tv_spa_level_one);
            groupHolder.mSingUserTv = view.findViewById(R.id.tv_head_sing_user);
            groupHolder.mSingTimeTv = view.findViewById(R.id.tv_head_sing_time);
            groupHolder.mListenCountTv = view.findViewById(R.id.tv_head_listen_count);
            groupHolder.mHeadPlayIv = view.findViewById(R.id.iv_head_play);
            groupHolder.typeLayout = view.findViewById(R.id.type_layout);
            view.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) view.getTag();
        }


        if (spaDataInfos != null && spaDataInfos.get(parentPos) != null) {
            if (spaDataInfos.get(parentPos).getFirst() != null) {
                groupHolder.mTitleTv.setText(spaDataInfos.get(parentPos).getFirst().getTitle());
                groupHolder.mSingUserTv.setText(spaDataInfos.get(parentPos).getFirst().getAuthor_title());
                groupHolder.mListenCountTv.setText(StringUtils.isEmpty(spaDataInfos.get(parentPos).getFirst().getPlay_num()) ? "0" : spaDataInfos.get(parentPos).getFirst().getPlay_num() + "");
                if (!StringUtils.isEmpty(spaDataInfos.get(parentPos).getFirst().getTime())) {
                    groupHolder.mSingTimeTv.setText(DateUtils.getFormatDateInSecond(spaDataInfos.get(parentPos).getFirst().getTime()));
                }
            }

            final GroupHolder finalGroupHolder = groupHolder;
            Glide.with(mContext).asDrawable().load(spaDataInfos.get(parentPos).getImg()).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(Drawable drawable, Transition<? super Drawable> transition) {
                    finalGroupHolder.typeLayout.setBackground(drawable);
                }
            });
        }

        //头部的播放按钮
        groupHolder.mHeadPlayIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spaDataInfos.get(parentPos).getFirst() != null && !StringUtils.isEmpty(spaDataInfos.get(parentPos).getFirst().getId())) {
                    Intent intent = new Intent(mContext, SleepDetailActivity.class);
                    SpaItemInfo spaItemInfo = spaDataInfos.get(parentPos).getFirst();
                    intent.putExtra("pos", 1);
                    intent.putExtra("type_id", spaItemInfo.getType_id());
                    intent.putExtra("spa_id", spaItemInfo.getId());
                    mContext.startActivity(intent);
                } else {
                    ToastUtils.showLong("播放错误，请重试");
                }
            }
        });
        return view;
    }

    //  获得子项显示的view
    @Override
    public View getChildView(final int parentPos, int childPos, boolean b, View convertView, final ViewGroup viewGroup) {

        LogUtil.msg("TAG getChildView");
        LogUtils.i("getChildView currentParentPosition ---> " + currentParentPosition + "---parentPos" + parentPos);

        View childView = convertView;

        if (childView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            childView = inflater.inflate(R.layout.spa_list_item_child, null);
        }
        RecyclerView childRecyclerView = childView.findViewById(R.id.spa_child_list);


        if (dataSet != null && dataSet.get(parentPos) != null) {
            final SpaListAdapter spaListAdapter = new SpaListAdapter(dataSet.get(parentPos));

            childRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            childRecyclerView.setAdapter(spaListAdapter);
            spaListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    onMoreListener.loadMore(spaListAdapter, parentPos);
                }
            }, childRecyclerView);

            spaListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    Intent intent = new Intent(mContext, SleepDetailActivity.class);
                    intent.putExtra("spa_id", spaListAdapter.getData().get(position).getId());
                    mContext.startActivity(intent);
                }
            });

        } else {
            childRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            childRecyclerView.setAdapter(null);
        }

        return childView;
    }

    //  子项是否可选中，如果需要设置子项的点击事件，需要返回true
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    /**
     * 父类复用
     */
    public class GroupHolder {
        public TextView mTitleTv;
        public TextView mSingUserTv;
        public TextView mSingTimeTv;
        public TextView mListenCountTv;
        public ImageView mHeadPlayIv;
        public LinearLayout typeLayout;

    }


    public RecyclerView getView(int position) {
        return sparseArray.get(position);
    }

    public SpaListAdapter getAdapter(int position) {

        return ((SpaListAdapter) sparseArray.get(position).getAdapter());
    }

    public void setChildData(List<SpaItemInfo> infoList, int position) {
        getAdapter(position).setNewData(infoList);
    }


    public void addChildData(List<SpaItemInfo> infoList, int position) {
        getAdapter(position).addData(infoList);
    }
}