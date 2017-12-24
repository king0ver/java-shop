package com.enation.app.nanshan.vo;

import java.io.Serializable;

/**
 * Created by yulong on 17/12/23.
 */
public class RechargeVo implements Serializable{

    private int recharge_id;

    /*游戏点数*/
    private String game_account;

    private int points;

    private String recharge_sn;

    private Double price;

    private int member_id;

    private String member_name;

    private String order_status;

    private String pay_status;

    private String payment_type;

    private long create_time;

    private String client_type;

    private String pay_order_no;

    public int getRecharge_id() {
        return recharge_id;
    }

    public void setRecharge_id(int recharge_id) {
        this.recharge_id = recharge_id;
    }

    public String getRecharge_sn() {
        return recharge_sn;
    }

    public void setRecharge_sn(String recharge_sn) {
        this.recharge_sn = recharge_sn;
    }

    public String getGame_account() {
        return game_account;
    }

    public void setGame_account(String game_account) {
        this.game_account = game_account;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }


    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getPay_status() {
        return pay_status;
    }

    public void setPay_status(String pay_status) {
        this.pay_status = pay_status;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getClient_type() {
        return client_type;
    }

    public void setClient_type(String client_type) {
        this.client_type = client_type;
    }

    public String getPay_order_no() {
        return pay_order_no;
    }

    public void setPay_order_no(String pay_order_no) {
        this.pay_order_no = pay_order_no;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
