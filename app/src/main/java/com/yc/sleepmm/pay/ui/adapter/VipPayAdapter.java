package com.yc.sleepmm.pay.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.sleepmm.pay.bean.PayInfo;

import java.util.List;

/**
 * Created by wanglin  on 2018/1/25 14:38.
 */

public class VipPayAdapter extends BaseQuickAdapter<PayInfo ,BaseViewHolder> {
    public VipPayAdapter(List<PayInfo> data) {
        super(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PayInfo item) {

    }
}
