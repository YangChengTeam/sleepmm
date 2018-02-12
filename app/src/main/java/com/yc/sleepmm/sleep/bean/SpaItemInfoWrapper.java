package com.yc.sleepmm.sleep.bean;

import com.yc.sleepmm.sleep.model.bean.SpaItemInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/2/11.
 */

public class SpaItemInfoWrapper {

    private List<SpaItemInfo> list;

    public List<SpaItemInfo> getList() {
        return list;
    }

    public void setList(List<SpaItemInfo> list) {
        this.list = list;
    }

    public void setAddList(List<SpaItemInfo> aLists) {
        if (list != null) {
            list.addAll(aLists);
        } else {
            list = new ArrayList<>();
        }
    }

}
