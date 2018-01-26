package com.yc.sleepmm.vip.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kk.utils.ScreenUtil;
import com.yc.sleepmm.R;
import com.yc.sleepmm.vip.bean.PayInfo;

import java.util.List;

/**
 * Created by wanglin  on 2018/1/25 14:38.
 */

public class VipPayAdapter extends BaseQuickAdapter<PayInfo, BaseViewHolder> {
    private SparseArray<ImageView> sparseArray;

    public VipPayAdapter(List<PayInfo> data) {
        super(R.layout.vip_pay_item, data);
        sparseArray = new SparseArray<>(2);
    }

    @Override
    protected void convert(BaseViewHolder helper, PayInfo item) {
        ImageView ib = helper.getView(R.id.ib_pay);
        if (item.getPayway().equals("alipay")) {
            ib.setImageResource(R.drawable.alipay_selector);
        } else {
            ib.setImageResource(R.drawable.wxpay_selector);
        }

        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) helper.itemView.getLayoutParams();
        layoutParams.width = (int) (ScreenUtil.getWidth(mContext) * 0.45);
        helper.itemView.setLayoutParams(layoutParams);
        int position = helper.getAdapterPosition();

        sparseArray.put(position, ib);

        getView(0).setSelected(true);

    }


    public ImageView getView(int position) {
        for (int i = 0; i < sparseArray.size(); i++) {
            sparseArray.get(i).setSelected(false);
        }
        return sparseArray.get(position);
    }
}
