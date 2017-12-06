package com.enation.app.shop.connect.service.impl;

import com.enation.app.shop.connect.model.ConnectType;
import com.enation.app.shop.connect.model.ConnectUser;
import com.enation.app.shop.connect.service.ConnectLogin;
import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.oauth.Oauth;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletRequest;

/**
 * Author: Dawei
 * Datetime: 2016-03-04 16:35
 */
public class QQConnectLogin extends ConnectLogin {

    public QQConnectLogin(ServletRequest request){
        super(request);
    }

    @Override
    public String getLoginUrl() {
        try {
            return new Oauth().getAuthorizeURL(request);
        } catch (QQConnectException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ConnectUser loginCallback() {
        try {
            AccessToken accessTokenObj = (new Oauth()).getAccessTokenByRequest(request);
            if (accessTokenObj.getAccessToken().equals("")) {
                return null;
            }

            String accessToken = accessTokenObj.getAccessToken();
            String openID = null;

            // 利用获取到的accessToken 去获取当前用的openid
            OpenID openIDObj = new OpenID(accessToken);
            openID = openIDObj.getUserOpenID();

            ConnectUser connectUser = new ConnectUser();
            connectUser.setOpenId(openID);
            connectUser.setType(ConnectType.QQ);

            UserInfo qzoneUserInfo = new UserInfo(accessToken, openID);
            UserInfoBean userInfoBean = qzoneUserInfo.getUserInfo();
            if (userInfoBean.getRet() == 0) {
                connectUser.setNickName(userInfoBean.getNickname());
                connectUser.setFaceUrl(userInfoBean.getAvatar().getAvatarURL100());
                connectUser.setGender(0);
                if(!StringUtils.isEmpty(userInfoBean.getGender())){
                    if(userInfoBean.getGender().equals("男")){
                        connectUser.setGender(1);
                    }else if(userInfoBean.getGender().equals("女")){
                        connectUser.setGender(2);
                    }
                }
            }
            return connectUser;
        } catch (QQConnectException e) {
            e.printStackTrace();
            return null;
        }
    }
}
