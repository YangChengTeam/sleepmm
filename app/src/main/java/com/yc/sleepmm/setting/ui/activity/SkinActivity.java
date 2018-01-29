package com.yc.sleepmm.setting.ui.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.view.BaseActivity;
import com.yc.sleepmm.setting.bean.SkinInfo;
import com.yc.sleepmm.setting.contract.SkinContract;
import com.yc.sleepmm.setting.presenter.SkinPresenter;
import com.yc.sleepmm.setting.ui.adapter.SkinAdapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2018/1/26 12:03.
 */

public class SkinActivity extends BaseActivity<SkinPresenter> implements SkinContract.View {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_vip_protocol)
    TextView tvVipProtocol;
    @BindView(R.id.recyclerView_skin)
    RecyclerView recyclerViewSkin;
    private SkinAdapter skinAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_skin;
    }

    @Override
    public void init() {
        mPresenter = new SkinPresenter(this, this);
        tvTitle.setText(getString(R.string.individuality_skin));
        tvVipProtocol.setVisibility(View.GONE);
        recyclerViewSkin.setLayoutManager(new GridLayoutManager(this, 3));
        skinAdapter = new SkinAdapter(null);
        recyclerViewSkin.setAdapter(skinAdapter);
        initListener();

    }

    private void initListener() {
        RxView.clicks(ivBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });

        skinAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public boolean onItemChildClick(BaseQuickAdapter adapter, View view, int position) {


                return false;

            }
        });
    }


    @Override
    public void showSkinInfos(List<SkinInfo> skinInfos) {
        skinAdapter.setNewData(skinInfos);
    }
}
