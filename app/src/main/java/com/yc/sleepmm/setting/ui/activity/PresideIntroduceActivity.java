package com.yc.sleepmm.setting.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.view.BaseActivity;
import com.yc.sleepmm.setting.bean.PresideInfo;
import com.yc.sleepmm.setting.ui.adapter.PresideIntroduceAdapter;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2018/3/1 16:28.
 */

public class PresideIntroduceActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_vip_protocol)
    TextView tvVipProtocol;
    @BindView(R.id.recyclerView_introduce)
    RecyclerView recyclerViewIntroduce;

    @Override
    public int getLayoutId() {
        return R.layout.activity_preside_introduce;
    }

    @Override
    public void init() {
        tvTitle.setText(getString(R.string.preside_setting));
        tvVipProtocol.setVisibility(View.GONE);
        RxView.clicks(ivBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });
        recyclerViewIntroduce.setLayoutManager(new LinearLayoutManager(this));
        PresideIntroduceAdapter presideIntroduceAdapter = new PresideIntroduceAdapter(PresideInfo.getPresideInfos());
        recyclerViewIntroduce.setAdapter(presideIntroduceAdapter);


    }


}
