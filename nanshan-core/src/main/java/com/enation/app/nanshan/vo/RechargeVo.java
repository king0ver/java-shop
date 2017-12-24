package com.enation.app.nanshan.vo;

import java.io.Serializable;

/**
 * Created by yulong on 17/12/23.
 */
public class RechargeVo implements Serializable{

    private int rechange_id;

    /*游戏点数*/
    private String game_account;

    private int points;

    private String rechange_sn;

    private int member_id;

    private String member_name;

    private String order_status;

    private String pay_status;

    private String payment_type;

    private long create_time;

    private String client_type;

    private String pay_order_no;


    public int getRechange_id() {
        return rechange_id;
    }

    public void setRechange_id(int rechange_id) {
        this.rechange_id = rechange_id;
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

    public String getRechange_sn() {
        return rechange_sn;
    }

    public void setRechange_sn(String rechange_sn) {
        this.rechange_sn = rechange_sn;
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
}
