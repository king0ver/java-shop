package com.enation.app.shop.connect.service;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.connect.model.ConnectUser;
/**
 * Author: Dawei
 * Datetime: 2016-03-14 16:44
 */
public interface IConnectMemberManager {

    /**
     * 绑定会员的openid
     * @param member
     * @param connectUser
     * @return
     */
    public boolean bind(Member member, ConnectUser connectUser);

    /**
     * 通过openId登录
     * @param connectUser
     * @return
     */
    public int loginWithOpenId(ConnectUser connectUser);

}
