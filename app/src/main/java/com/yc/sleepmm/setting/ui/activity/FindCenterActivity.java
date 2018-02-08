package com.yc.sleepmm.setting.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.view.BaseActivity;
import com.yc.sleepmm.base.view.StateView;
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
    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    private FindCenterAdapter findCenterAdapter;

    private int PAGE = 1;
    private int LIMIT = 10;

    @Override
    public int getLayoutId() {
        return R.layout.activity_find_center;
    }

    @Override
    public void init() {
        tvTitle.setText(getString(R.string.find_center));
        tvVipProtocol.setVisibility(View.GONE);
        mPresenter = new FindCenterPresenter(this, this);
        getData();
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
                    FindCenterInfo info = (FindCenterInfo) adapter.getItem(position);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(info.getFile_path()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
                return false;
            }
        });
        findCenterAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                PAGE++;
                getData();
            }
        }, recyclerViewFindVip);
    }


    @Override
    public void showFindCenterInfos(List<FindCenterInfo> findCenterInfos) {

        if (PAGE == 1) {
            findCenterAdapter.setNewData(findCenterInfos);
        } else {
            findCenterAdapter.addData(findCenterInfos);
        }
        if (findCenterInfos.size() == LIMIT) {
            PAGE++;
            findCenterAdapter.loadMoreComplete();
        } else {
            findCenterAdapter.loadMoreEnd();
        }

    }


    private void getData() {
        mPresenter.getFindcenterInfos(PAGE, LIMIT);
    }

    @Override
    public void hide() {
        stateView.hide();
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(llContainer, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PAGE = 1;
                getData();
            }
        });
    }

    @Override
    public void showLoading() {
        stateView.showLoading(llContainer);
    }

    @Override
    public void showNoData() {
        stateView.showNoData(llContainer);
    }


}
