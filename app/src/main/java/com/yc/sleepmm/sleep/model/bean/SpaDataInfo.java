package com.yc.sleepmm.sleep.model.bean;

/**
 * Created by admin on 2018/1/25.
 * 分类信息
 */

public class SpaDataInfo {

    private String id;
    private String title;
    private String img;

    private SpaItemInfo first;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public SpaItemInfo getFirst() {
        return first;
    }

    public void setFirst(SpaItemInfo first) {
        this.first = first;
    }
}
