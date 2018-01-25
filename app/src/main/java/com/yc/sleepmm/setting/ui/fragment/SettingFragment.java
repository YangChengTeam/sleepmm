package com.yc.sleepmm.setting.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.ToastUtil;
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.view.BaseFragment;
import com.yc.sleepmm.pay.ui.activity.VipActivity;
import com.yc.sleepmm.setting.widget.BaseSettingView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2018/1/10 17:18.
 */

public class SettingFragment extends BaseFragment {
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.baseSettingView_vip)
    BaseSettingView baseSettingViewVip;
    @BindView(R.id.baseSettingView_find)
    BaseSettingView baseSettingViewFind;
    @BindView(R.id.baseSettingView_person)
    BaseSettingView baseSettingViewPerson;
    @BindView(R.id.baseSettingView_alarm)
    BaseSettingView baseSettingViewAlarm;
    @BindView(R.id.baseSettingView_skin)
    BaseSettingView baseSettingViewSkin;
    @BindView(R.id.baseSettingView_feedback)
    BaseSettingView baseSettingViewFeedback;
    @BindView(R.id.baseSettingView_system)
    BaseSettingView baseSettingViewSystem;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    public void init() {


        initListener();
    }

    private void initListener() {
        RxView.clicks(baseSettingViewVip).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                ToastUtil.toast(getActivity(), "会员中心");
                Intent intent = new Intent(getActivity(), VipActivity.class);
                startActivity(intent);
            }
        });
        RxView.clicks(baseSettingViewFind).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                ToastUtil.toast(getActivity(), "发现中心");
            }
        });
        RxView.clicks(baseSettingViewPerson).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                ToastUtil.toast(getActivity(), "个人收藏");
            }
        });
        RxView.clicks(baseSettingViewAlarm).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                ToastUtil.toast(getActivity(), "闹钟设置");
            }
        });
        RxView.clicks(baseSettingViewSkin).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                ToastUtil.toast(getActivity(), "个性皮肤");
            }
        });
        RxView.clicks(baseSettingViewFeedback).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                ToastUtil.toast(getActivity(), "意见反馈");
            }
        });
        RxView.clicks(baseSettingViewSystem).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                ToastUtil.toast(getActivity(), "系统设置");
            }
        });


    }


}
