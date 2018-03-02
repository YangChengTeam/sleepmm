package com.yc.sleepmm.base.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.sleepmm.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2018/2/7 17:31.
 * 状态图
 */

public class StateView extends BaseView {
    @BindView(R.id.iv_loading)
    ImageView ivLoading;
    @BindView(R.id.tv_mess)
    TextView tvMess;
    @BindView(R.id.rl_container)
    RelativeLayout rlContainer;

    private View mContentView;
    private AnimationDrawable animationDrawable;


    public StateView(@NonNull Context context) {
        super(context);
    }

    public StateView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StateView);
        try {
            int gravity = ta.getInt(R.styleable.StateView_gravity, 5);
            setGravity(gravity);

        } finally {
            ta.recycle();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.base_state_view;
    }


    @Override
    public void init() {
        super.init();
        animationDrawable = (AnimationDrawable) ivLoading.getDrawable();
    }

    public void showLoading(View contentView, String mess) {
        mContentView = contentView;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivLoading.getLayoutParams();
        layoutParams.width = SizeUtils.dp2px(160 / 2);
        layoutParams.height = SizeUtils.dp2px(160 / 2);
        ivLoading.setLayoutParams(layoutParams);
        setVisibility(VISIBLE);
        tvMess.setText(mess);
        ivLoading.setVisibility(VISIBLE);
//        Glide.with(this).load(R.drawable.progress_anim).into(ivLoading);

        ivLoading.setBackgroundResource(R.drawable.progress_anim);
        animationDrawable = (AnimationDrawable) ivLoading.getBackground();
        mContentView.setVisibility(GONE);
        if (animationDrawable != null)
            animationDrawable.start();

    }


    public void showLoading(View contentView) {
        showLoading(contentView, "正在加载中，请稍候...");

    }

    public void showNoData(View contentView, String message) {
        if (animationDrawable != null) animationDrawable.stop();
        mContentView = contentView;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivLoading.getLayoutParams();
        layoutParams.width = SizeUtils.dp2px(344 / 3);
        layoutParams.height = SizeUtils.dp2px(276 / 3);
        ivLoading.setLayoutParams(layoutParams);
        setVisibility(VISIBLE);
        tvMess.setText(message);
        mContentView.setVisibility(GONE);
        ivLoading.setVisibility(GONE);
        if (animationDrawable != null)
            animationDrawable.stop();
        ivLoading.setBackgroundResource(R.drawable.ic_list_empty_icon);
//        Glide.with(this).load(R.drawable.ic_list_empty_icon).into(ivLoading);
    }

    public void showNoData(View contentView) {
        showNoData(contentView, "暂无数据");
    }


    public void showNoNet(View contentView, String message, final OnClickListener onClickListener) {
        if (animationDrawable != null) animationDrawable.stop();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivLoading.getLayoutParams();
        layoutParams.width = SizeUtils.dp2px(256 / 3);
        layoutParams.height = SizeUtils.dp2px(256 / 3);
        ivLoading.setLayoutParams(layoutParams);
        mContentView = contentView;
        setVisibility(View.VISIBLE);
        mContentView.setVisibility(View.GONE);
        tvMess.setText(message);
        ivLoading.setVisibility(GONE);
        if (animationDrawable != null)
            animationDrawable.stop();
        ivLoading.setBackgroundResource(R.mipmap.base_no_wifi);
//        Glide.with(this).load(R.mipmap.base_no_wifi).into(ivLoading);

        RxView.clicks(rlContainer).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                onClickListener.onClick(rlContainer);
            }
        });
    }

    public void hide() {
        setVisibility(GONE);
        if (mContentView != null)
            mContentView.setVisibility(VISIBLE);
    }

    public void showNoNet(View contentView, final OnClickListener onClickListener) {
        showNoNet(contentView, "加载失败，点击重试", onClickListener);
    }

    public void setGravity(int gravity) {
        FrameLayout.LayoutParams layoutParams = (LayoutParams) rlContainer.getLayoutParams();
//            layoutParams.topMargin = SizeUtils.dp2px(30);
//            rlContainer.setLayoutParams(layoutParams);
        switch (gravity) {
            case 1://top
                layoutParams.gravity = Gravity.TOP;
                break;
            case 2://left
                layoutParams.gravity = Gravity.LEFT;
                break;
            case 3://right
                layoutParams.gravity = Gravity.RIGHT;
                break;
            case 4://Bottom
                layoutParams.gravity = Gravity.BOTTOM;
                break;
            case 5://Center
                layoutParams.gravity = Gravity.CENTER;
                break;
        }
        rlContainer.setLayoutParams(layoutParams);
    }
}
