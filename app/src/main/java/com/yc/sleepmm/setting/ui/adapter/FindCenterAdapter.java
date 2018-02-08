package com.yc.sleepmm.setting.ui.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
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
        helper.setText(R.id.tv_download_title, item.getTitle())
                .setText(R.id.tv_download_introduce, item.getName()).addOnClickListener(R.id.iv_download);

        Glide.with(mContext).load(item.getIco()).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA)
                .error(R.mipmap.download_app_icon).placeholder(R.mipmap.download_app_icon)).into((ImageView) helper.getView(R.id.iv_download_icon));


        int position = helper.getAdapterPosition();
        if (position == mData.size() - 1) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) helper.itemView.getLayoutParams();
            layoutParams.bottomMargin = ScreenUtil.dip2px(mContext, 15);
            helper.itemView.setLayoutParams(layoutParams);

        }
    }
}
