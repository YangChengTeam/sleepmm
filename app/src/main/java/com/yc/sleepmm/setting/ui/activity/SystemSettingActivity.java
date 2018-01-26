package com.yc.sleepmm.setting.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.view.BaseActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    @BindView(R.id.switch1)
    Switch switch1;


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
    }


}
