package com.yc.sleepmm.sleep.model.bean;

import java.util.List;

/**
 * Created by wanglin  on 2018/2/11 08;//59.
 */

public class SpaDetailInfo {
    public String id;// SPAID
    public String title;// 标题
    public String type_id;// 分类ID
    public String img;// 图片
    public String author;// 作者ID
    public String desp;// 简介
    public String file;// 音频
    public String play_num;// 播放次数
    public String time;// 时长
    public String add_time;// 添加时间
    public String add_date;// 添加日期

    public int is_favorite;// 是否收藏
    public String author_title;// 作者名称

    // extend
    public String author_desp;// 作者简介
    public String author_img;// 作者图片

    public List<SpaDetailInfo> lists;
}
