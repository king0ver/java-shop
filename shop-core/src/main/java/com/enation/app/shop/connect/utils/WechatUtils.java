package com.enation.app.shop.connect.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Author: Dawei
 * Datetime: 2016-04-22 15:18
 */
public class WechatUtils {

    /**
     * 进行sha1签名
     *
     * @param params
     * @return
     */
    public static String sha1Sign(Map params) {

        String url = createLinkString(params);
        String sign = Sha1.encode(url);

        return sign;
    }

    public static String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if ("sign".equals(key)) {
                continue;
            }

            if ("".equals(prestr)) {// 拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + "&" + key + "=" + value;
            }
        }
        return prestr;
    }


}
