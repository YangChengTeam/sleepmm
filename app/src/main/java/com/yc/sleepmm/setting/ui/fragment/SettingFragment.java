package com.yc.sleepmm.setting.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;

import com.kk.utils.ToastUtil;
import com.vondear.rxtools.RxPhotoTool;

import com.yc.sleepmm.R;
import com.yc.sleepmm.base.view.BaseFragment;
import com.yc.sleepmm.setting.constants.BusAction;
import com.yc.sleepmm.setting.ui.activity.FindCenterActivity;
import com.yc.sleepmm.setting.ui.activity.OptionFeedbackActivity;
import com.yc.sleepmm.setting.ui.activity.SkinActivity;
import com.yc.sleepmm.setting.ui.activity.SystemSettingActivity;

import com.yc.sleepmm.setting.ui.activity.UserKeepActivity;


import com.yc.sleepmm.setting.widget.BaseSettingView;
import com.yc.sleepmm.vip.ui.activity.VipActivity;

import java.io.File;
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
    private SelectPicFragment selectPicFragment;


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

        RxView.clicks(ivAvatar).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                selectPicFragment = new SelectPicFragment();
                selectPicFragment.show(getFragmentManager(), null);
            }
        });
    }


    //从Uri中加载图片 并将其转化成File文件返回
    public File roadImageView(Uri uri, ImageView imageView) {
        Glide.with(this).
                load(uri).
                apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA).circleCrop().placeholder(R.mipmap.default_avatar).
                        error(R.mipmap.default_avatar).
                        fallback(R.mipmap.default_avatar)).
                thumbnail(0.5f).
                into(imageView);

        return (new File(RxPhotoTool.getImageAbsolutePath(getActivity(), uri)));
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.GET_PICTURE)
            }
    )
    public void getPicture(Uri uri) {
        roadImageView(uri, ivAvatar);
    }

}
