package com.yc.sleepmm.setting.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

import com.yc.sleepmm.R;
import com.yc.sleepmm.base.view.BaseView;

import butterknife.BindView;

/**
 * Created by wanglin  on 2018/1/25 09:25.
 */

public class BaseSettingView extends BaseView {
    @BindView(R.id.iv_setting_icon)
    ImageView ivSettingIcon;
    @BindView(R.id.tv_setting_title)
    TextView tvSettingTitle;
    private Drawable mDrawable;
    private String mTitle;

    public BaseSettingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.base_setting);

        try {
            mDrawable = ta.getDrawable(R.styleable.base_setting_icon);
            mTitle = ta.getString(R.styleable.base_setting_title);
            if (mDrawable != null) {
                ivSettingIcon.setImageDrawable(mDrawable);
            }
            if (!TextUtils.isEmpty(mTitle)) {
                tvSettingTitle.setText(mTitle);
            }

        } finally {
            ta.recycle();
        }


    }

    @Override
    public int getLayoutId() {
        return R.layout.base_setting_view;
    }

}
