package com.yc.sleepmm.base.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.sleepmm.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2018/2/7 17:31.
 * 状态图
 */

public class SpaStateView extends BaseView {
    @BindView(R.id.iv_loading)
    ImageView ivLoading;
    @BindView(R.id.tv_mess)
    TextView tvMess;
    @BindView(R.id.rl_container)
    RelativeLayout rlContainer;

    private View mContentView;

    public SpaStateView(@NonNull Context context) {
        super(context);
    }

    public SpaStateView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.base_spa_state_view;
    }


    @Override
    public void init() {
        super.init();
        Glide.with(this).load(R.mipmap.sleep_loading).into(ivLoading);
    }

    public void showLoading(View contentView, String mess) {
        mContentView = contentView;
        setVisibility(VISIBLE);
        tvMess.setText(mess);
        tvMess.setVisibility(GONE);
        mContentView.setVisibility(GONE);
    }

    public void showLoading(View contentView) {
        showLoading(contentView, "正在加载中，请稍候...");

    }

    public void showNoData(View contentView, String message) {

        mContentView = contentView;

        setVisibility(VISIBLE);
        tvMess.setText(message);
        tvMess.setVisibility(VISIBLE);
        mContentView.setVisibility(GONE);
        ivLoading.setVisibility(GONE);
    }

    public void showNoData(View contentView) {
        showNoData(contentView, "暂无数据");
    }

    public void showNoNet(View contentView, String message, final OnClickListener onClickListener) {

        mContentView = contentView;
        setVisibility(View.VISIBLE);
        mContentView.setVisibility(View.GONE);
        tvMess.setText(message);
        tvMess.setVisibility(VISIBLE);
        ivLoading.setVisibility(GONE);

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

}
