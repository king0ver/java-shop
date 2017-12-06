package com.enation.app.shop.connect.service.impl;

import weibo4j.Account;
import weibo4j.Oauth;
import weibo4j.Users;
import weibo4j.http.AccessToken;
import weibo4j.model.User;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;
import javax.servlet.ServletRequest;
import com.enation.app.shop.connect.model.ConnectType;
import com.enation.app.shop.connect.model.ConnectUser;
import com.enation.app.shop.connect.service.ConnectLogin;


/**
 * Author: Dawei
 * Datetime: 2016-03-04 16:35
 */
public class WeiboConnectLogin extends ConnectLogin {

    public WeiboConnectLogin(ServletRequest request) {
        super(request);
    }

    @Override
    public String getLoginUrl() {
        try {
            return new Oauth().authorize("code");
        } catch (WeiboException ex) {
            ex.printStackTrace();
            return "";
        }
    }

    @Override
    public ConnectUser loginCallback() {
        String accesstoken = null;
        String code = request.getParameter("code");
        if (code != null) {
            try {
                Oauth oauth = new Oauth();
                AccessToken accessToken = oauth.getAccessTokenByCode(code);
                accesstoken = accessToken.getAccessToken();
                if (accessToken != null) {
                    Users users = new Users(accesstoken);

                    Account account = new Account(accesstoken);
                    JSONObject uidJson = account.getUid();
                    String uid = uidJson.getString("uid");
                    User weiboUser = users.showUserById(uid);

                    ConnectUser connectUser = new ConnectUser();
                    connectUser.setOpenId(weiboUser.getId());
                    connectUser.setNickName(weiboUser.getName());
                    connectUser.setFaceUrl(weiboUser.getAvatarLarge());
                    connectUser.setGender(0);
                    connectUser.setType(ConnectType.WEIBO);
                    return connectUser;
                }
            } catch (WeiboException ex) {
                ex.printStackTrace();
                return null;
            } catch (JSONException ex) {
                ex.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
