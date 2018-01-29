package com.yc.sleepmm.setting.ui.adapter;

import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kk.utils.ScreenUtil;
import com.yc.sleepmm.R;
import com.yc.sleepmm.setting.bean.FindCenterInfo;

import java.util.List;

/**
 * Created by wanglin  on 2018/1/25 18:22.
 */

public class FindCenterAdapter extends BaseQuickAdapter<FindCenterInfo, BaseViewHolder> {
    public FindCenterAdapter(List<FindCenterInfo> data) {
        super(R.layout.activity_find_center_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FindCenterInfo item) {
        helper.setText(R.id.tv_download_title, item.getName())
                .setText(R.id.tv_download_introduce, item.getDesc()).addOnClickListener(R.id.iv_download);

        helper.setImageResource(R.id.iv_download_icon, item.getImgId());

        int position = helper.getAdapterPosition();
        if (position == mData.size() - 1) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) helper.itemView.getLayoutParams();
            layoutParams.bottomMargin = ScreenUtil.dip2px(mContext, 15);
            helper.itemView.setLayoutParams(layoutParams);

        }
    }
}
