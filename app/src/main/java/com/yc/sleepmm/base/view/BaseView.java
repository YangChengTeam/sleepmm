package com.yc.sleepmm.base.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.kk.utils.LogUtil;

import butterknife.ButterKnife;

/**
 * Created by wanglin  on 2018/1/24 17:02.
 */

public abstract class BaseView extends FrameLayout implements IView {
    public BaseView(@NonNull Context context) {
        super(context);
        LayoutInflater.from(context).inflate(getLayoutId(), this);
        try {
            ButterKnife.bind(this);
        } catch (Exception e) {
            LogUtil.msg("--> 初始化失败  " + e.getMessage());
        }

        init();
    }

    public BaseView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(getLayoutId(), this);
        try {
            ButterKnife.bind(this, view);
        } catch (Exception e) {
            LogUtil.msg("--> 初始化失败  " + e.getMessage());
        }

        init();
    }

    @Override
    public void init() {

    }
}
