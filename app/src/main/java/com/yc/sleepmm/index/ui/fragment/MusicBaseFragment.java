package com.yc.sleepmm.index.ui.fragment;

import android.view.View;

import com.yc.sleepmm.base.presenter.BasePresenter;
import com.yc.sleepmm.base.view.BaseFragment;

/**
 * TinyHung@Outlook.com
 * 2018/1/22.
 * 音乐列表统一父类，需要加载数据失败时候 重试功能，请实现onRefresh()方法
 */

public abstract class MusicBaseFragment<P extends BasePresenter> extends BaseFragment<P> {


    @Override
    public void init() {
        initViews();
    }

    protected abstract void initViews();

    /**
     * 在这里实现Fragment数据的缓加载.
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            onVisible();
        } else {
            onInvisible();
        }
    }

    protected void onInvisible() {

    }

    protected void onVisible() {

    }


    /**
     * 子类可实现此登录方法
     */
    protected void onLogin() {

    }


    /**
     * 加载失败后点击后的操作
     */
    protected void onRefresh() {

    }


    protected boolean isVisible(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

}
