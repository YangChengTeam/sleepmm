package com.yc.sleepmm.vip.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kk.utils.ScreenUtil;
import com.yc.sleepmm.R;
import com.yc.sleepmm.vip.bean.GoodInfo;

import java.util.List;

/**
 * Created by wanglin  on 2018/1/25 11:59.
 */

public class VipGoodAdapter extends BaseQuickAdapter<GoodInfo, BaseViewHolder> {

    private SparseArray<View> sparseArray;

    public VipGoodAdapter(List<GoodInfo> data) {
        super(R.layout.vip_good_item, data);
        sparseArray = new SparseArray<>();
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodInfo item) {
        helper.setText(R.id.tv_vip_price, item.getPrice()).setText(R.id.tv_vip_title, item.getName());
        int position = helper.getAdapterPosition();
        if (position == 0) {
            helper.setVisible(R.id.iv_experience, true);
            helper.itemView.setSelected(true);
        } else {
            helper.setVisible(R.id.iv_experience, false);
        }

        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) helper.itemView.getLayoutParams();
        layoutParams.width = (int) (ScreenUtil.getWidth(mContext) * 0.211);
        helper.itemView.setLayoutParams(layoutParams);

        sparseArray.put(position, helper.itemView);
    }

    public View getView(int position) {

        for (int i = 0; i < sparseArray.size(); i++) {
            sparseArray.get(i).setSelected(false);
        }

        return sparseArray.get(position);
    }
}
