package com.yc.sleepmm.sleep.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yc.sleepmm.R;
import com.yc.sleepmm.sleep.bean.SpaDataInfo;
import com.yc.sleepmm.sleep.bean.SpaItemInfo;

import java.util.List;

/**
 * Created by admin on 2018/1/25.
 */

public class SpaListAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;

    LinearLayout.LayoutParams headParams;

    LinearLayout.LayoutParams itemParams;

    public SpaListAdapter(List<MultiItemEntity> datas) {
        super(datas);
        addItemType(TYPE_LEVEL_0, R.layout.spa_list_item_head);
        addItemType(TYPE_LEVEL_1, R.layout.spa_list_item_content);
        headParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(102));
        headParams.setMargins(SizeUtils.dp2px(10), SizeUtils.dp2px(10), SizeUtils.dp2px(10), 0);

        itemParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(64));
        itemParams.setMargins(SizeUtils.dp2px(10), 0, SizeUtils.dp2px(10), 0);
    }

    @Override
    protected void convert(final BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case TYPE_LEVEL_0:
                helper.itemView.setLayoutParams(headParams);
                final SpaDataInfo spaDataInfo = (SpaDataInfo) item;
                helper.setText(R.id.tv_spa_level_one, spaDataInfo.getTitle());

                helper.itemView.findViewById(R.id.tv_spa_more).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = helper.getAdapterPosition();
                        if (spaDataInfo.isExpanded()) {
                            collapse(pos);
                        } else {
                            expand(pos);
                        }
                        LogUtils.i("parent item position--->" + helper.getAdapterPosition());
                        LogUtils.i("sub size --->" + spaDataInfo.getSubItems().size());

                    }
                });
                break;
            case TYPE_LEVEL_1:
                LogUtils.i("sub item position--->" + helper.getAdapterPosition());
                helper.itemView.setLayoutParams(itemParams);
                final SpaItemInfo spaItemInfo = (SpaItemInfo) item;
                helper.setText(R.id.tv_spa_level_two, spaItemInfo.getTitle());
                break;
            default:
                break;
        }
    }
}
