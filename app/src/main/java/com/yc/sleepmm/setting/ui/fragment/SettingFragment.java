package com.yc.sleepmm.setting.ui.fragment;

import android.content.Intent;
import android.widget.ImageView;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.view.BaseFragment;
import com.yc.sleepmm.setting.ui.activity.FindCenterActivity;
import com.yc.sleepmm.setting.ui.activity.OptionFeedbackActivity;
import com.yc.sleepmm.setting.ui.activity.SkinActivity;
import com.yc.sleepmm.setting.ui.activity.SystemSettingActivity;
import com.yc.sleepmm.setting.ui.activity.UserKeepActivity;
import com.yc.sleepmm.setting.widget.BaseSettingView;
import com.yc.sleepmm.vip.ui.activity.VipActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
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
                Intent intent = new Intent(getActivity(), VipActivity.class);
                startActivity(intent);
            }
        });
        RxView.clicks(baseSettingViewFind).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), FindCenterActivity.class);
                startActivity(intent);
            }
        });
        RxView.clicks(baseSettingViewPerson).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), UserKeepActivity.class);
                startActivity(intent);
            }
        });
        RxView.clicks(baseSettingViewAlarm).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                AlarmFragment alarmFragment = new AlarmFragment();
                alarmFragment.show(getFragmentManager(), null);
            }
        });
        RxView.clicks(baseSettingViewSkin).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), SkinActivity.class);
                startActivity(intent);
            }
        });
        RxView.clicks(baseSettingViewFeedback).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), OptionFeedbackActivity.class);
                startActivity(intent);
            }
        });
        RxView.clicks(baseSettingViewSystem).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), SystemSettingActivity.class);
                startActivity(intent);
            }
        });

    }
}
