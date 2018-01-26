package com.yc.sleepmm.setting.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.sleepmm.R;
import com.yc.sleepmm.setting.bean.SkinInfo;

import java.util.List;

/**
 * Created by wanglin  on 2018/1/26 13:38.
 */

public class SkinAdapter extends BaseQuickAdapter<SkinInfo, BaseViewHolder> {
    public SkinAdapter(List<SkinInfo> data) {
        super(R.layout.activity_skin_item, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, SkinInfo item) {
        helper.setImageResource(R.id.iv_skin, item.getImgId())
                .setText(R.id.btn_confirm, item.isIdUsed() ? "使用中" : mContext.getString(R.string.confirm))
                .addOnClickListener(R.id.btn_confirm);

    }
}
