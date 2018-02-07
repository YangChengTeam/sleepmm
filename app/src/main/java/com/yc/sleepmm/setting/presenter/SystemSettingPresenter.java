package com.yc.sleepmm.setting.presenter;

import android.content.Context;

import com.yc.sleepmm.base.APP;
import com.yc.sleepmm.base.model.BaseEngine;
import com.yc.sleepmm.base.presenter.BasePresenter;
import com.yc.sleepmm.setting.contract.SystemSettingContract;
import com.yc.sleepmm.setting.utils.GlideCacheHelper;

/**
 * Created by wanglin  on 2018/2/7 10:10.
 */

public class SystemSettingPresenter extends BasePresenter<BaseEngine, SystemSettingContract.View> implements SystemSettingContract.Presenter {
    public SystemSettingPresenter(Context mContext, SystemSettingContract.View view) {
        super(mContext, view);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;
        getCacheSize();

        mView.showLogout(APP.getInstance().getUserData() != null);
    }


    private void getCacheSize() {
        mView.showCacheSize(GlideCacheHelper.getInstance(mContext).getCacheSize());
    }

    public boolean clearCache() {
        return GlideCacheHelper.getInstance(mContext).clearCache();
    }
}
