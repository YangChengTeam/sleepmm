package com.yc.sleepmm.setting.bean;

/**
 * Created by wanglin  on 2018/1/26 13:40.
 */

public class SkinInfo
{
    private int imgId;
    private boolean idUsed;

    public SkinInfo() {
    }

    public SkinInfo(int imgId, boolean idUsed) {
        this.imgId = imgId;
        this.idUsed = idUsed;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public boolean isIdUsed() {
        return idUsed;
    }

    public void setIdUsed(boolean idUsed) {
        this.idUsed = idUsed;
    }
}
