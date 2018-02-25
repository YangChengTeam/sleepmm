package com.yc.sleepmm.setting.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.music.player.lib.util.ToastUtils;
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.APP;
import com.yc.sleepmm.base.view.BaseActivity;
import com.yc.sleepmm.index.constants.Constant;
import com.yc.sleepmm.index.model.bean.UserInfo;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2018/2/13 11:06.
 */

public class AccountActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_vip_protocol)
    TextView tvVipProtocol;

    @BindView(R.id.tv_bind)
    TextView tvBind;
    @BindView(R.id.rl_phone)
    RelativeLayout rlPhone;
    @BindView(R.id.iv_arrow)
    ImageView ivArrow;
    @BindView(R.id.rl_change_pwd)
    RelativeLayout rlChangePwd;
    @BindView(R.id.divider)
    View divider;


    private String mobile = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    public void init() {
        tvTitle.setText(getString(R.string.account_safe));
        tvVipProtocol.setVisibility(View.GONE);

        initView();

        initListener();
    }

    private void initView() {
        if (APP.getInstance().isLogin()) {
            UserInfo userInfo = APP.getInstance().getUserData();
            if (!TextUtils.isEmpty(userInfo.getMobile())) {
                mobile = "已绑定";
                rlChangePwd.setVisibility(View.VISIBLE);
                divider.setVisibility(View.VISIBLE);
            } else {
                mobile = "未绑定";
                rlChangePwd.setVisibility(View.GONE);
                divider.setVisibility(View.GONE);
            }
        }
        tvBind.setText(mobile);
    }

    private void initListener() {
        RxView.clicks(ivBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });

        RxView.clicks(rlPhone).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (!APP.getInstance().isGotoLogin(AccountActivity.this)) {
                    if (TextUtils.equals(mobile, "未绑定")) {
                        startActivity(new Intent(AccountActivity.this, BindPhoneActivity.class));
                    } else {
                        ToastUtils.showCenterToast("手机号已绑定，不能修改");
                    }
                }
            }
        });

        RxView.clicks(rlChangePwd).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(AccountActivity.this, ChangePwdActivity.class));
            }
        });
    }


    @Subscribe(thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(Constant.RX_LOGIN_SUCCESS)
            })

    public void bindPhone(String login) {
        initView();
    }


}
