package com.yc.sleepmm.setting.contract;

import com.yc.sleepmm.base.view.IDialog;
import com.yc.sleepmm.base.view.IPresenter;
import com.yc.sleepmm.base.view.IView;
import com.yc.sleepmm.setting.bean.UploadInfo;

import java.io.File;

/**
 * Created by wanglin  on 2018/2/5 14:11.
 */

public interface SettingContract {
    interface View extends IView, IDialog {
        void showUploadFile(UploadInfo data);
    }

    interface Presenter extends IPresenter {
        void getPayInfos();

        void uploadFile(File file, String fileName);
    }

}
