package com.yc.sleepmm.setting.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.sleepmm.R;
import com.yc.sleepmm.setting.bean.PresideInfo;

import java.util.List;

/**
 * Created by wanglin  on 2018/3/1 16:40.
 */

public class PresideIntroduceAdapter extends BaseQuickAdapter<PresideInfo, BaseViewHolder> {
    public PresideIntroduceAdapter(List<PresideInfo> data) {
        super(R.layout.activity_preside_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PresideInfo item) {
        helper.setText(R.id.tv_name, item.getName())
                .setText(R.id.tv_introduce, item.getDesc())
                .setImageResource(R.id.iv_preside, item.getImgId());
    }
}
