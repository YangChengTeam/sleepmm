package com.yc.sleepmm.sleep.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.sleepmm.R;
import com.yc.sleepmm.sleep.model.bean.SpaItemInfo;

import java.util.List;

/**
 * Created by admin on 2018/1/25.
 */

public class SpaListAdapter extends BaseQuickAdapter<SpaItemInfo, BaseViewHolder> {

    public SpaListAdapter(List<SpaItemInfo> datas) {
        super(R.layout.spa_list_item_content, datas);
    }

    @Override
    protected void convert(final BaseViewHolder helper, SpaItemInfo item) {
        final SpaItemInfo spaItemInfo = (SpaItemInfo) item;
        if (spaItemInfo != null) {
            helper.setText(R.id.tv_spa_level_two, spaItemInfo.getTitle());
        }
    }
}
