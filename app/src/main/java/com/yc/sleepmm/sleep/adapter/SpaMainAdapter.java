package com.yc.sleepmm.sleep.adapter;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kk.utils.LogUtil;
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.util.CommonInfoHelper;
import com.yc.sleepmm.base.util.DateUtils;
import com.yc.sleepmm.sleep.model.bean.SpaDataInfo;
import com.yc.sleepmm.sleep.model.bean.SpaItemInfo;

import java.util.List;

import butterknife.BindView;

/**
 * Created by wanglin  on 2018/2/28 15:33.
 */

public class SpaMainAdapter extends BaseQuickAdapter<SpaDataInfo, BaseViewHolder> {
    @BindView(R.id.iv_head_play)
    ImageView ivHeadPlay;
    @BindView(R.id.tv_spa_level_one)
    TextView tvSpaLevelOne;
    @BindView(R.id.tv_head_sing_user)
    TextView tvHeadSingUser;
    @BindView(R.id.tv_head_sing_time)
    TextView tvHeadSingTime;
    @BindView(R.id.tv_head_listen_count)
    TextView tvHeadListenCount;
    @BindView(R.id.tv_spa_more)
    TextView tvSpaMore;
    @BindView(R.id.type_layout)
    LinearLayout typeLayout;


    private SparseArray<RecyclerView> sparseArray;


    public SpaMainAdapter(List<SpaDataInfo> data) {
        super(R.layout.spa_list_item_head, data);
        sparseArray = new SparseArray<>();
    }

    @Override
    protected void convert(final BaseViewHolder helper, final SpaDataInfo item) {
        helper.setText(R.id.tv_spa_level_one, item.getFirst().getTitle()).
                setText(R.id.tv_head_sing_user, item.getFirst().getAuthor_title()).
                setText(R.id.tv_head_listen_count, StringUtils.isEmpty(item.getFirst().getPlay_num()) ? "0" : item.getFirst().getPlay_num() + "")
                .addOnClickListener(R.id.iv_head_play);
        if (!StringUtils.isEmpty(item.getFirst().getTime())) {
            helper.setText(R.id.tv_head_sing_time, DateUtils.getFormatDateInSecond(item.getFirst().getTime()));
        }

        Glide.with(mContext).asDrawable().load(item.getImg()).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable drawable, Transition<? super Drawable> transition) {
                helper.getView(R.id.type_layout).setBackground(drawable);
            }
        });

        RecyclerView recyclerView = helper.getView(R.id.recyclerView_list_detail);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        SpaListAdapter spaListAdapter = new SpaListAdapter(null);
        recyclerView.setAdapter(spaListAdapter);
        sparseArray.put(mData.indexOf(item), recyclerView);

    }

    public SpaListAdapter getAdapter(int position) {
        SpaListAdapter spaListAdapter = (SpaListAdapter) sparseArray.get(position).getAdapter();

        List<SpaItemInfo> spaItemInfos = spaListAdapter.getData();
        if (spaItemInfos != null && spaItemInfos.size() > 0) {
            CommonInfoHelper.setO(mContext, spaItemInfos, position + "");
        }

        return spaListAdapter;
    }

    public RecyclerView getView(int position) {
        return sparseArray.get(position);

    }


    public void setVisable(boolean flag, final int position) {
        sparseArray.get(position).setVisibility(flag ? View.VISIBLE : View.GONE);

        if (flag) {

            CommonInfoHelper.getO(mContext, position + "", new TypeReference<List<SpaItemInfo>>() {
            }.getType(), new CommonInfoHelper.onParseListener<List<SpaItemInfo>>() {
                @Override
                public void onParse(List<SpaItemInfo> data) {
                    getAdapter(position).setNewData(data);
                    getAdapter(position).loadMoreEnd();
                }
            });

            LogUtil.msg("TAG " + getAdapter(position).getData().size());
        }
    }

//    public void setParent(ViewGroup parent, int position) {
//        getView(position).setNestParent(parent);
//    }

}
