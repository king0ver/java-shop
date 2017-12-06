package com.enation.app.shop.connect.service.impl;

import com.enation.app.shop.connect.model.ConnectType;
import com.enation.app.shop.connect.model.ConnectUser;
import com.enation.app.shop.connect.service.ConnectLogin;
import com.enation.app.shop.connect.utils.HttpUtils;
import com.qq.connect.utils.json.JSONObject;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletRequest;
import java.net.URLEncoder;

/**
 * 微信服务号登录
 * Author: Dawei
 * Datetime: 2016-03-04 16:36
 */
public class WechatMpConnectLogin extends ConnectLogin {

    public WechatMpConnectLogin(ServletRequest request){
        super(request);
    }

    @Override
    public String getLoginUrl() {
        try {
            return "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + connectSetting.getWechatMp_AppId() +
                    "&redirect_uri=" + URLEncoder.encode(connectSetting.getWechatMp_RedirectUri(), "utf-8")+
                    "&response_type=code&scope=snsapi_userinfo&state=wechatmp_login";
        }catch(Exception ex){
            ex.printStackTrace();
            return "";
        }
    }

    @Override
    public ConnectUser loginCallback() {
        String code = request.getParameter("code");
        if(StringUtils.isEmpty(code)){
            return null;
        }
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + connectSetting.getWechatMp_AppId() +
                "&secret=" + connectSetting.getWechatMp_AppSecret() + "&code=" + code + "&grant_type=authorization_code";
        String content = HttpUtils.get(url);
        try {
            //获取openid
            JSONObject json = new JSONObject(content);
            String openid = json.getString("openid");
            String accessToken = json.getString("access_token");
            if(StringUtils.isEmpty(openid) || StringUtils.isEmpty(accessToken)){
                return null;
            }

            ConnectUser connectUser = new ConnectUser();
            connectUser.setOpenId(openid);
            connectUser.setType(ConnectType.WECHAT);

            //通过openid获取userinfo
            content = HttpUtils.get("https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openid);
            if(StringUtils.isEmpty(content)){
                return connectUser;
            }
            json = new JSONObject(content);
            if(json == null){
                return connectUser;
            }
            connectUser.setNickName(json.getString("nickname"));
            connectUser.setFaceUrl(json.getString("headimgurl"));
            connectUser.setGender(json.getInt("sex"));
            return connectUser;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
}
