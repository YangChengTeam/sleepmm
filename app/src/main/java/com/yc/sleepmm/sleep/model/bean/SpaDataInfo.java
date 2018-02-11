package com.yc.sleepmm.sleep.model.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yc.sleepmm.sleep.adapter.SpaListAdapter;

/**
 * Created by admin on 2018/1/25.
 */

public class SpaDataInfo extends AbstractExpandableItem<SpaItemInfo> implements MultiItemEntity {

    public String title;

    public SpaDataInfo(String title) {
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
        return SpaListAdapter.TYPE_LEVEL_0;
    }

    @Override
    public int getLevel() {
        return SpaListAdapter.TYPE_LEVEL_0;
    }
}
