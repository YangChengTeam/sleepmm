package com.yc.sleepmm.pay.ui.activity;

import android.icu.text.ListFormatter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.yc.sleepmm.R;
import com.yc.sleepmm.base.view.BaseActivity;
import com.yc.sleepmm.pay.bean.GoodInfo;
import com.yc.sleepmm.pay.contract.GoodInfoContract;
import com.yc.sleepmm.pay.presenter.GoodInfoPresenter;
import com.yc.sleepmm.pay.ui.adapter.VipGoodAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * Created by wanglin  on 2018/1/25 11:37.
 */

public class VipActivity extends BaseActivity<GoodInfoPresenter> implements GoodInfoContract.View {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_vip_protocol)
    TextView tvVipProtocol;
    @BindView(R.id.recyclerView_good)
    RecyclerView recyclerViewGood;
    @BindView(R.id.recyclerView_pay)
    RecyclerView recyclerViewPay;
    private VipGoodAdapter vipGoodAdapter;


    @Override
    public int getLayoutId() {
        return R.layout.activity_vip;
    }

    @Override
    public void init() {
        mPresenter =new GoodInfoPresenter(this,this);
        recyclerViewGood.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        vipGoodAdapter = new VipGoodAdapter(null);
        recyclerViewGood.setAdapter(vipGoodAdapter);

    }


    @Override
    public void showGoodInfos(List<GoodInfo> goodInfos) {
        vipGoodAdapter.setNewData(goodInfos);
    }
}
