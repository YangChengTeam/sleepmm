package com.yc.sleepmm.vip.ui.activity;

import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.ScreenUtil;
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.view.BaseActivity;
import com.yc.sleepmm.vip.bean.GoodInfo;
import com.yc.sleepmm.vip.bean.PayInfo;
import com.yc.sleepmm.vip.contract.GoodInfoContract;
import com.yc.sleepmm.vip.presenter.GoodInfoPresenter;
import com.yc.sleepmm.vip.ui.adapter.VipGoodAdapter;
import com.yc.sleepmm.vip.ui.adapter.VipPayAdapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

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
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    private VipGoodAdapter vipGoodAdapter;
    private VipPayAdapter vipPayAdapter;


    @Override
    public int getLayoutId() {
        return R.layout.activity_vip;
    }

    @Override
    public void init() {

        tvTitle.setText(getString(R.string.vip_miemie));
        mPresenter = new GoodInfoPresenter(this, this);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) nestedScrollView.getLayoutParams();
        layoutParams.height = RelativeLayout.LayoutParams.MATCH_PARENT - ScreenUtil.dip2px(this, 40);
        nestedScrollView.setLayoutParams(layoutParams);

        recyclerViewGood.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        vipGoodAdapter = new VipGoodAdapter(null);
        recyclerViewGood.setAdapter(vipGoodAdapter);

        recyclerViewPay.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        vipPayAdapter = new VipPayAdapter(null);
        recyclerViewPay.setAdapter(vipPayAdapter);

        initListener();

    }

    private void initListener() {

        RxView.clicks(ivBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });

        RxView.clicks(tvVipProtocol).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(VipActivity.this, VipProtocolActivity.class);
                startActivity(intent);
            }
        });

        vipGoodAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                vipGoodAdapter.getView(position).setSelected(true);
            }
        });

        vipPayAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                vipPayAdapter.getView(position).setSelected(true);
            }
        });
    }


    @Override
    public void showGoodInfos(List<GoodInfo> goodInfos) {
        vipGoodAdapter.setNewData(goodInfos);
    }

    @Override
    public void showPayInfos(List<PayInfo> payInfos) {
        vipPayAdapter.setNewData(payInfos);
    }

}
