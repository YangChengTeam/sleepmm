package com.yc.sleepmm.sleep.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.util.DateUtils;
import com.yc.sleepmm.sleep.bean.SpaItemInfoWrapper;
import com.yc.sleepmm.sleep.model.bean.SpaDataInfo;
import com.yc.sleepmm.sleep.ui.activity.SleepDetailActivity;

import java.util.List;
import java.util.Map;

public class SpaTypeListViewAdapter extends BaseExpandableListAdapter {

    public Context mContext;

    private Map<Integer, SpaItemInfoWrapper> dataSet;

    private List<SpaDataInfo> spaDataInfos;

    private Handler handler;

    public interface OnMoreListener {
        void loadMore(SpaListAdapter spaListAdapter);
    }

    public OnMoreListener onMoreListener;

    public void setOnMoreListener(OnMoreListener onMoreListener) {
        this.onMoreListener = onMoreListener;
    }

    public void setDataSet(Map<Integer, SpaItemInfoWrapper> dataSet) {
        this.dataSet = dataSet;
    }

    public void setSpaDataInfos(List<SpaDataInfo> spaDataInfos) {
        this.spaDataInfos = spaDataInfos;
    }


    public SpaTypeListViewAdapter(Context context, Map<Integer, SpaItemInfoWrapper> dataset, List<SpaDataInfo> spaDataInfos) {
        this.mContext = context;
        this.dataSet = dataset;
        this.spaDataInfos = spaDataInfos;
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                notifyDataSetChanged();
                super.handleMessage(msg);
            }
        };
    }

    public void refresh() {
        handler.sendMessage(new Message());
    }

    //  获得某个父项的某个子项
    @Override
    public Object getChild(int parentPos, int childPos) {
        return dataSet != null ? dataSet.get(parentPos).getList() : null;
    }

    //  获得父项的数量
    @Override
    public int getGroupCount() {
        return dataSet != null ? dataSet.size() : 0;
    }

    //  获得某个父项的子项数目
    @Override
    public int getChildrenCount(int parentPos) {
        return 1;
    }

    //  获得某个父项
    @Override
    public Object getGroup(int parentPos) {
        return dataSet != null ? dataSet.get(parentPos) : 0;
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
    public View getGroupView(final int parentPos, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.spa_list_item_head, null);
        }
        view.setTag(R.layout.spa_list_item_head, parentPos);
        view.setTag(R.layout.spa_list_item_content, -1);
        final LinearLayout typeLayout = view.findViewById(R.id.type_layout);

        TextView mTitleTv = (TextView) view.findViewById(R.id.tv_spa_level_one);
        TextView mSingUserTv = (TextView) view.findViewById(R.id.tv_head_sing_user);
        TextView mSingTimeTv = (TextView) view.findViewById(R.id.tv_head_sing_time);
        TextView mListenCountTv = (TextView) view.findViewById(R.id.tv_head_listen_count);
        ImageView mHeadPlayIv = (ImageView) view.findViewById(R.id.iv_head_play);
        if (spaDataInfos != null && spaDataInfos.get(parentPos) != null) {
            if (spaDataInfos.get(parentPos).getFirst() != null) {
                mTitleTv.setText(spaDataInfos.get(parentPos).getFirst().getTitle());
                mSingUserTv.setText(spaDataInfos.get(parentPos).getFirst().getAuthor_title());
                mListenCountTv.setText(spaDataInfos.get(parentPos).getFirst().getPlay_num()+"");
                if (!StringUtils.isEmpty(spaDataInfos.get(parentPos).getFirst().getTime())) {
                    mSingTimeTv.setText(DateUtils.getFormatDateInSecond(spaDataInfos.get(parentPos).getFirst().getTime()));
                }
            }

            Glide.with(mContext).asDrawable().load(spaDataInfos.get(parentPos).getImg()).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(Drawable drawable, Transition<? super Drawable> transition) {
                    typeLayout.setBackground(drawable);
                }
            });
        }

        //头部的播放按钮
        mHeadPlayIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spaDataInfos.get(parentPos).getFirst() != null) {
                    Intent intent = new Intent(mContext, SleepDetailActivity.class);
                    intent.putExtra("spa_id", spaDataInfos.get(parentPos).getFirst().getId());
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
    public View getChildView(int parentPos, int childPos, boolean b, View childView, final ViewGroup viewGroup) {

        if (childView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            childView = inflater.inflate(R.layout.spa_list_item_child, null);
        }

        childView.setTag(R.layout.spa_list_item_head, parentPos);
        childView.setTag(R.layout.spa_list_item_child, childPos);

        RecyclerView childRecyclerView = childView.findViewById(R.id.spa_child_list);
        if (dataSet.get(parentPos) != null && dataSet.get(parentPos).getList() != null) {
            final SpaListAdapter spaListAdapter = new SpaListAdapter(dataSet.get(parentPos).getList());

            LogUtils.i("spaListAdapter adapter --->" + spaListAdapter.hashCode());

            childRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            childRecyclerView.setAdapter(spaListAdapter);
            spaListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    onMoreListener.loadMore(spaListAdapter);
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
}