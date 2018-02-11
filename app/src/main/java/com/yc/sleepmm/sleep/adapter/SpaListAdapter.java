package com.yc.sleepmm.sleep.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.sleepmm.R;
import com.yc.sleepmm.sleep.bean.SpaDataInfo;

import java.util.List;

/**
 * Created by admin on 2018/1/25.
 */

public class SpaListAdapter extends BaseQuickAdapter<SpaDataInfo, BaseViewHolder> {

    public SpaListAdapter(List<SpaDataInfo> datas) {
        super(R.layout.spa_list_item_content, datas);
    }

    @Override
    protected void convert(final BaseViewHolder helper, SpaDataInfo item) {
        final SpaDataInfo spaItemInfo = (SpaDataInfo) item;
        helper.setText(R.id.tv_spa_level_two, spaItemInfo.getTitle());
    }
}
