package com.enation.app.shop.core;

import com.enation.app.shop.payment.model.enums.ClientType;
import com.enation.eop.SystemSetting;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.StringUtil;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * 域名获取util
 * Created by kingapex on 2017/10/21.
 *
 * @author kingapex
 * @version 1.0
 * @since 6.4.0
 * 2017/10/21
 */
public class DomainUtil {

    /**
     * 获取商品域名
     * @param clientType 客户端类型
     * @return
     */
    public static String getGoodsDomain(ClientType clientType){

        String domain="";
        if(ClientType.PC.equals(clientType)){
            domain= getGoodsPcDomain();
        }

        return  domain;
    }

    /**
     * 获取商品pc的域名
     * @return
     */
    private static String getGoodsPcDomain( ){

        String domain = SystemSetting.getPrimary_domain();

        if(StringUtil.isEmpty(domain)){
            domain = getDomainFromReq();
        }

        //填补可能不存在的http://
        if(!domain.startsWith("http://")){
            domain="http://"+domain;
        }

        return  domain;
    }



    /**
     * 由requst中读取域名
     * @return 如果request获取为空返回空串
     */
    private static String getDomainFromReq(){
        HttpServletRequest request = ThreadContextHolder.getHttpRequest();
        if (request==null){
            return "";
        }
        String contextPath = request.getContextPath();
        String serverName =request.getServerName();
        int port = request.getServerPort();
        String portstr = "";
        if(port!=80){
            portstr = ":"+port;
        }

        String   domain = serverName+portstr+contextPath;
        return domain;
    }

}
