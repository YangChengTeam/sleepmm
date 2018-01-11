package com.yc.sleepmm.base.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by wanglin  on 2018/1/10 17:18.
 */

public abstract class BaseFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        try {
            ButterKnife.bind(this, view);
        } catch (Exception e) {

        }
        init();

        return view;
    }

    protected abstract int getLayoutId();

    protected abstract void init();
}
