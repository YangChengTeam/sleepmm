package com.yc.sleepmm.setting.ui.activity;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.ScreenUtil;
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.view.BaseActivity;
import com.yc.sleepmm.setting.bean.FindCenterInfo;
import com.yc.sleepmm.setting.contract.FindCenterContract;
import com.yc.sleepmm.setting.presenter.FindCenterPresenter;
import com.yc.sleepmm.setting.ui.adapter.FindCenterAdapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2018/1/25 18:20.
 */

public class FindCenterActivity extends BaseActivity<FindCenterPresenter> implements FindCenterContract.View {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_vip_protocol)
    TextView tvVipProtocol;
    @BindView(R.id.recyclerView_find_vip)
    RecyclerView recyclerViewFindVip;
    private FindCenterAdapter findCenterAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_find_center;
    }

    @Override
    public void init() {
        tvTitle.setText(getString(R.string.find_center));
        tvVipProtocol.setVisibility(View.GONE);


        mPresenter = new FindCenterPresenter(this, this);
        recyclerViewFindVip.setLayoutManager(new LinearLayoutManager(this));
        findCenterAdapter = new FindCenterAdapter(null);
        recyclerViewFindVip.setAdapter(findCenterAdapter);
        initListener();

    }

    private void initListener() {
        RxView.clicks(ivBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });
        findCenterAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public boolean onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.iv_download) {
                    //todo 下载
                }
                return false;
            }
        });
    }


    @Override
    public void showFindCenterInfos(List<FindCenterInfo> findCenterInfos) {
        findCenterAdapter.setNewData(findCenterInfos);
    }
}
