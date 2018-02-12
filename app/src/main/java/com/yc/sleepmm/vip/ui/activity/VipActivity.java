package com.yc.sleepmm.vip.ui.activity;

import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.RxBus;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.pay.I1PayAbs;
import com.kk.pay.IPayAbs;
import com.kk.pay.IPayCallback;
import com.kk.pay.OrderGood;
import com.kk.pay.OrderInfo;
import com.kk.pay.OrderParamsInfo;
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.APP;
import com.yc.sleepmm.base.view.BaseActivity;
import com.yc.sleepmm.index.model.bean.UserInfo;
import com.yc.sleepmm.setting.constants.BusAction;
import com.yc.sleepmm.setting.constants.Config;
import com.yc.sleepmm.setting.constants.NetConstant;
import com.yc.sleepmm.vip.bean.GoodsInfo;
import com.yc.sleepmm.vip.bean.PayInfo;
import com.yc.sleepmm.vip.ui.adapter.VipGoodAdapter;
import com.yc.sleepmm.vip.ui.adapter.VipPayAdapter;
import com.yc.sleepmm.vip.utils.GoodsInfoHelper;
import com.yc.sleepmm.vip.utils.PaywayHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2018/1/25 11:37.
 */

public class VipActivity extends BaseActivity {
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
    @BindView(R.id.btn_charge)
    Button btnCharge;
    @BindView(R.id.ll_pay)
    LinearLayout llPay;
    private VipGoodAdapter vipGoodAdapter;
    private VipPayAdapter vipPayAdapter;
    private GoodsInfo goodsInfo;
    private PayInfo payInfo;
    private IPayAbs iPayAbs;


    @Override
    public int getLayoutId() {
        return R.layout.activity_vip;
    }

    @Override
    public void init() {

        tvTitle.setText(getString(R.string.vip_miemie));

        iPayAbs = new I1PayAbs(this);
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) nestedScrollView.getLayoutParams();
//        layoutParams.height = RelativeLayout.LayoutParams.MATCH_PARENT - ScreenUtil.dip2px(this, 50);
//        nestedScrollView.setLayoutParams(layoutParams);

        recyclerViewGood.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        vipGoodAdapter = new VipGoodAdapter(GoodsInfoHelper.getGoodsInfoList());

        recyclerViewGood.setAdapter(vipGoodAdapter);
        if (GoodsInfoHelper.getGoodsInfoList() != null && GoodsInfoHelper.getGoodsInfoList().size() > 0) {
            goodsInfo = GoodsInfoHelper.getGoodsInfoList().get(0);
        }
        recyclerViewPay.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        vipPayAdapter = new VipPayAdapter(PaywayHelper.getmPaywayInfo());
        recyclerViewPay.setAdapter(vipPayAdapter);
        if (PaywayHelper.getmPaywayInfo() != null && PaywayHelper.getmPaywayInfo().size() > 0) {
            payInfo = PaywayHelper.getmPaywayInfo().get(0);
        }
        setVip();
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
        RxView.clicks(btnCharge).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                UserInfo userInfo = APP.getInstance().getUserData();
                OrderParamsInfo orderParams = new OrderParamsInfo();
                orderParams.setPay_url(NetConstant.orders_init_url);
                orderParams.setUser_id(userInfo.getId());
                orderParams.setTitle(goodsInfo.name);
                orderParams.setMoney(goodsInfo.pay_price);
                orderParams.setPay_way_name(payInfo.getPayway());
                List<OrderGood> list = new ArrayList<>();
                OrderGood orderGood = new OrderGood(String.valueOf(goodsInfo.id), 1);
                list.add(orderGood);
                orderParams.setGoods_list(list);
                iPayAbs.pay(orderParams, new IPayCallback() {
                    @Override
                    public void onSuccess(OrderInfo orderInfo) {
                        updateSuccessData();
                    }

                    @Override
                    public void onFailure(OrderInfo orderInfo) {

                    }
                });

            }
        });

        vipGoodAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                vipGoodAdapter.getView(position).setSelected(true);
                goodsInfo = (GoodsInfo) adapter.getItem(position);
            }
        });

        vipPayAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                vipPayAdapter.getView(position).setSelected(true);
                payInfo = (PayInfo) adapter.getItem(position);
            }
        });
    }

    private void setVip() {
        if (APP.getInstance().getUserData().getVip() == 1) {
            llPay.setVisibility(View.GONE);
            btnCharge.setVisibility(View.GONE);
        } else {
            llPay.setVisibility(View.VISIBLE);
            btnCharge.setVisibility(View.VISIBLE);
        }
    }

    private void updateSuccessData() {
        UserInfo userInfo = APP.getInstance().getUserData();
        userInfo.setVip(1);
        Date date = new Date();
        if (goodsInfo.use_time_limit != null) {
            int use_time_Limit = 0;
            try {
                use_time_Limit = Integer.parseInt(goodsInfo.use_time_limit);
            } catch (Exception e) {
                use_time_Limit = 0;
            }

            long vip_end_time = date.getTime() + use_time_Limit * 30 * (Config.MS_IN_A_DAY);
            userInfo.setVip_end_time(String.valueOf(vip_end_time / 1000));
        }
        APP.getInstance().setUserData(userInfo, true);

        RxBus.get().post(BusAction.PAY_SUCCESS, "form pay");
        finish();

    }
}
