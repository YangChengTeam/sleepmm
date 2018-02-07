package com.yc.sleepmm.setting.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.hwangjr.rxbus.RxBus;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.ToastUtil;
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.APP;
import com.yc.sleepmm.base.view.BaseActivity;
import com.yc.sleepmm.index.constants.Constant;
import com.yc.sleepmm.setting.constants.SpConstant;
import com.yc.sleepmm.setting.contract.SystemSettingContract;
import com.yc.sleepmm.setting.presenter.SystemSettingPresenter;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2018/1/26 15:07.
 */

public class SystemSettingActivity extends BaseActivity<SystemSettingPresenter> implements SystemSettingContract.View, CompoundButton.OnCheckedChangeListener {
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
    @BindView(R.id.btn_logout)
    Button btnLogout;
    @BindView(R.id.tv_cache)
    TextView tvCache;
    @BindView(R.id.rl_clear_cache)
    RelativeLayout rlClearCache;


    @Override
    public int getLayoutId() {
        return R.layout.activity_system_setting;
    }

    @Override
    public void init() {
        mPresenter = new SystemSettingPresenter(this, this);
        tvVipProtocol.setVisibility(View.GONE);

        switchTiming.setChecked(SPUtils.getInstance().getBoolean(SpConstant.OPEN_TIMING));
        switchOpen2g.setChecked(SPUtils.getInstance().getBoolean(SpConstant.OPEN_FLOW, true));
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
                ToastUtil.toast(SystemSettingActivity.this, "成功退出");
            }
        });

        RxView.clicks(rlClearCache).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (mPresenter.clearCache()) {
                    tvCache.setText("0KB");
                    ToastUtil.toast(SystemSettingActivity.this, "缓存清除成功");
                }

            }
        });

        switchTiming.setOnCheckedChangeListener(this);
        switchOpen2g.setOnCheckedChangeListener(this);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switch_timing://定时开启
                if (isChecked) {
                    sumTime();
                    SPUtils.getInstance().put(SpConstant.OPEN_TIMING, true);
                } else {
                    SPUtils.getInstance().put(SpConstant.OPEN_TIMING, false);
                }

                break;
            case R.id.switch_open2g://打开2g/3g/4g
                SPUtils.getInstance().put(SpConstant.OPEN_FLOW, isChecked);
                break;
        }
    }


    private void sumTime() {
        int hours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int delayHours = 22 - hours;
        ToastUtil.toast2(this, "将在" + delayHours + "小时后开启定时模式");

    }

    @Override
    public void showLogout(boolean b) {
        btnLogout.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showCacheSize(String cacheSize) {
        tvCache.setText(cacheSize);
    }

}
