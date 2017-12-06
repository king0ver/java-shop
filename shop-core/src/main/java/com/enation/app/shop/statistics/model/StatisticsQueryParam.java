package com.enation.app.shop.statistics.model;

/**
 * 统计查询参数封装
 * @author zh
 * @version v1.0
 * @since v6.4.0
 * 2017年9月29日 下午7:48:23
 */
public class StatisticsQueryParam {
    /** 年 */
    private Integer year;
    /** 月 */
    private Integer month;
    /** 卖家id */
    private Integer seller_id;


    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(Integer seller_id) {
        this.seller_id = seller_id;
    }
}
