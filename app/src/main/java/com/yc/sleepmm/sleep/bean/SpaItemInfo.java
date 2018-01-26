package com.yc.sleepmm.sleep.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yc.sleepmm.sleep.adapter.SpaListAdapter;

/**
 * Created by admin on 2018/1/25.
 */

public class SpaItemInfo implements MultiItemEntity {

    public String title;

    public int bgType;//1.上，2.正常，3下

    public SpaItemInfo(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int getItemType() {
        return SpaListAdapter.TYPE_LEVEL_1;
    }

    public int getBgType() {
        return bgType;
    }

    public void setBgType(int bgType) {
        this.bgType = bgType;
    }
}
