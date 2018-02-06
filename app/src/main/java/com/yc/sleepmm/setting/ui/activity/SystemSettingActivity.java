package com.yc.sleepmm.setting.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.APP;
import com.yc.sleepmm.base.view.BaseActivity;
import com.yc.sleepmm.index.constants.Constant;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2018/1/26 15:07.
 */

public class SystemSettingActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_vip_protocol)
    TextView tvVipProtocol;
    @BindView(R.id.switch_timing)
    Switch switchTiming;
    @BindView(R.id.switch_open2g)
    Switch switchOpen2g;
    @BindView(R.id.iv_arrow)
    ImageView ivArrow;
    @BindView(R.id.btn_logout)
    Button btnLogout;


    @Override
    public int getLayoutId() {
        return R.layout.activity_system_setting;
    }

    @Override
    public void init() {
        tvVipProtocol.setVisibility(View.GONE);
        initListener();
    }

    private void initListener() {
        RxView.clicks(ivBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });

        RxView.clicks(btnLogout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                APP.getInstance().setUserData(null, true);
                RxBus.get().post(Constant.RX_LOGIN_SUCCESS, "logout");
                btnLogout.setVisibility(View.GONE);
            }
        });
    }


}
