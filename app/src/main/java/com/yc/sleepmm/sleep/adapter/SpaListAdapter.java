package com.yc.sleepmm.sleep.adapter;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.util.DateUtils;
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
        if (item != null) {
            helper.setText(R.id.tv_spa_level_two, item.getTitle());
            helper.setText(R.id.tv_spa_number, (helper.getAdapterPosition() + 2) + "");
            helper.setText(R.id.tv_spa_sing_user, item.getAuthor_title());
            helper.setText(R.id.tv_spa_listen_count, StringUtils.isEmpty(item.getPlay_num()) ? "0" : item.getPlay_num() + "");
            if (!StringUtils.isEmpty(item.getTime())) {
                helper.setText(R.id.tv_spa_sing_time, DateUtils.getFormatDateInSecond(item.getTime()));
            }
        }
    }
}
