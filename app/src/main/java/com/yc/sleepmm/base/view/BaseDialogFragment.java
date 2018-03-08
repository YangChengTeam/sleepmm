package com.yc.sleepmm.base.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.hwangjr.rxbus.RxBus;
import com.umeng.analytics.MobclickAgent;
import com.vondear.rxtools.RxLogTool;
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.presenter.BasePresenter;
import com.yc.sleepmm.base.util.EmptyUtils;

import butterknife.ButterKnife;

/**
 * Created by wanglin  on 2018/3/5 11:24.
 */

public abstract class BaseDialogFragment<P extends BasePresenter> extends DialogFragment implements IView {

    protected P mPresenter;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RxBus.get().register(this);
        Window window = getDialog().getWindow();

        if (rootView == null) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            rootView = inflater.inflate(getLayoutId(), container, false);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//注意此处
            window.setLayout(ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight() * 2 / 3 + SizeUtils.dp2px(50));//这2行,和上面的一样,注意顺序就行;
            window.setWindowAnimations(R.style.pay_style);
        }
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        try {
            ButterKnife.bind(this, rootView);
        } catch (Exception e) {
            RxLogTool.e("--> " + "初始化失败");
        }
        init();


        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (EmptyUtils.isNotEmpty(mPresenter))
            mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EmptyUtils.isNotEmpty(mPresenter))
            mPresenter.unsubscribe();
        RxBus.get().unregister(this);
    }


}
