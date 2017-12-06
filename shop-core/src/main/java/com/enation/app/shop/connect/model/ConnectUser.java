package com.enation.app.shop.connect.model;

import java.io.Serializable;

/**
 * Author: Dawei
 * Datetime: 2016-03-04 16:28
 */
public class ConnectUser implements Serializable {

    /**
     * 开放用户ID
     */
    private String openId;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 性别
     * 1男 2女 0保密
     */
    private int gender;

    /**
     * 头像图片url
     */
    private String faceUrl;

    /**
     * OpenId类型
     */
    private int type;

    public ConnectUser() {
    }

    public ConnectUser(String openId, String nickName) {
        this.openId = openId;
        this.nickName = nickName;
    }

    public ConnectUser(String openId, int type) {
		super();
		this.openId = openId;
		this.type = type;
	}

	public ConnectUser(String openId, String nickName, int gender, String faceUrl) {
        this.openId = openId;
        this.nickName = nickName;
        this.gender = gender;
        this.faceUrl = faceUrl;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
