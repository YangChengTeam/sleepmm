package com.yc.sleepmm.setting.bean;

/**
 * Created by wanglin  on 2018/1/25 18:23.
 */

public class FindCenterInfo {
    private String img;
    private int imgId;
    private String name;
    private String desc;
    private String url;

    public FindCenterInfo() {
    }

    public FindCenterInfo(int imgId, String name, String desc) {
        this.imgId = imgId;
        this.name = name;
        this.desc = desc;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
