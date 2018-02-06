package com.yc.sleepmm.vip.bean;

/**
 * Created by wanglin  on 2018/2/4 09;//31.
 */

public class GoodsInfo {

    public int id;// 商品ID
    public String name;// 商品名称
    public int type_id;// 分类ID
    public int type_relate_val;// 关联的id，0为所有
    public int app_id;// 应用ID
    public String img;// 商品图像
    public String desp;// 商品描述
    public String price;// 市场价
    public String m_price;// 优惠价
    public String vip_price;// 会员价
    public String unit;// 单位
    public int use_time_limit;// 有效时长
    public int sort;// 排序
    public int status;// 状态
}
