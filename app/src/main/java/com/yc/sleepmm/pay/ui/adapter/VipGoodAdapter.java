package com.yc.sleepmm.pay.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.sleepmm.R;
import com.yc.sleepmm.pay.bean.GoodInfo;

import java.util.List;

/**
 * Created by wanglin  on 2018/1/25 11:59.
 */

public class VipGoodAdapter extends BaseQuickAdapter<GoodInfo, BaseViewHolder> {
    public VipGoodAdapter(List<GoodInfo> data) {
        super(R.layout.vip_good_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodInfo item) {
        helper.setText(R.id.tv_vip_price, item.getPrice()).setText(R.id.tv_vip_title, item.getName());
        int position = helper.getAdapterPosition();
        if (position == 0) {
            helper.setVisible(R.id.iv_experience, true);
            helper.itemView.setBackgroundResource(R.drawable.vip_selector_bg);
        } else {
            helper.setVisible(R.id.iv_experience, false);
            helper.itemView.setBackgroundResource(R.drawable.vip_normal_bg);
        }
    }
}
