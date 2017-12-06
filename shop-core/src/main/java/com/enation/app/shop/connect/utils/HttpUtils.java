package com.enation.app.shop.connect.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * Created with IntelliJ IDEA.
 * User: Dawei
 * Date: 14-7-16
 * Time: 上午11:11
 * To change this template use File | Settings | File Templates.
 */
public class HttpUtils {

    /**
     * 获取指定网址的源码
     *
     * @param url
     * @return
     */
    public static String get(String url) {
        return invoke(HttpClients.createDefault(), new HttpGet(url));
    }

    /**
     * 获取图片
     * @param url
     * @param path  图片保存路径
     * @return
     */
    public static boolean getImage(String url, String path){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            CloseableHttpResponse response = httpClient.execute(new HttpGet(url));
            try {
                HttpEntity entity = response.getEntity();

                BufferedInputStream bis = new BufferedInputStream(entity.getContent());
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(path)));
                int inByte;
                while((inByte = bis.read()) != -1) bos.write(inByte);
                bis.close();
                bos.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            } finally {
                if (response != null) {
                    try {
                        response.close();
                    } catch (Exception ex) {
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return true;
    }

    /**
     * Post提交数据
     *
     * @param url
     * @param dataMap
     * @param charset
     * @return
     */
    public static String post(String url, Map<String, String> dataMap, String charset) {
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        for (String key : dataMap.keySet()) {
            postParams.add(new BasicNameValuePair(key, dataMap.get(key)));
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(postParams, Charset.forName(charset)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return invoke(HttpClients.createDefault(), httpPost);
    }

    private static String invoke(CloseableHttpClient httpClient, HttpRequestBase httpRequest) {
        String content = "";
        try {
            CloseableHttpResponse response = httpClient.execute(httpRequest);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    byte[] bytes = EntityUtils.toByteArray(entity);
                    if (bytes != null) {
                        String charset = "utf-8";
                        content = new String(bytes, Charset.forName(charset));
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return "";
            } finally {
                if (response != null) {
                    try {
                        response.close();
                    } catch (Exception ex) {
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return content;
    }

    public static String postWithGzip(String url, Map<String, String> dataMap, String postCharset) {
        DefaultHttpClient httpClient = new DefaultHttpClient();      //实例化一个HttpClient

        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        for (String key : dataMap.keySet()) {
            postParams.add(new BasicNameValuePair(key, dataMap.get(key)));
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(postParams, Charset.forName(postCharset)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        httpPost.addHeader("Accept-Encoding", "gzip");

        HttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
        } catch (Exception ex) {
            response = null;
        }
        if (response == null)
            return "";

        HttpEntity entity = response.getEntity();
        Header contentHeader = entity.getContentEncoding();
        if (contentHeader != null) {
            for (HeaderElement element : contentHeader.getElements()) {
                if (element.getName().equalsIgnoreCase("gzip")) {
                    response.setEntity(new GzipDecompressingEntity(response.getEntity()));
                }
            }
        }

        //返回服务器响应
        String content = "";
        if(response.getEntity() == null)
            return content;

        try {
            byte[] bytes = EntityUtils.toByteArray(response.getEntity());
            if(bytes == null)
                return content;
            String charset = "utf-8";
            content = new String(bytes, Charset.forName(charset));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (entity != null) {
                try {
                    entity.consumeContent();
                } catch (Exception ex) {
                }
            }
        }
        return content;
    }

    public static String getWithGzip(String url) {
        DefaultHttpClient httpClient = new DefaultHttpClient();      //实例化一个HttpClient
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Accept-Encoding", "gzip");
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
        } catch (Exception ex) {
            response = null;
        }
        if (response == null)
            return "";

        HttpEntity entity = response.getEntity();
        Header contentHeader = entity.getContentEncoding();
        if (contentHeader != null) {
            for (HeaderElement element : contentHeader.getElements()) {
                if (element.getName().equalsIgnoreCase("gzip")) {
                    response.setEntity(new GzipDecompressingEntity(response.getEntity()));
                }
            }
        }

        //返回服务器响应
        String content = "";
        if(response.getEntity() == null)
            return content;
        try {
            byte[] bytes = EntityUtils.toByteArray(response.getEntity());
            if(bytes == null)
                return content;
            String charset = "utf-8";
            content = new String(bytes, Charset.forName(charset));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (entity != null) {
                try {
                    entity.consumeContent();
                } catch (Exception ex) {
                }
            }
        }
        return content;
    }

}
