package com.enation.app.shop.waybill.plugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.ConfigItem;
import com.enation.app.base.core.service.IRegionsManager;
import com.enation.app.shop.shop.apply.model.po.Shop;
import com.enation.app.shop.shop.apply.model.po.ShopDetail;
import com.enation.app.shop.shop.apply.service.impl.ShopManager;
import com.enation.app.shop.trade.model.po.Logi;
import com.enation.app.shop.trade.model.vo.OrderDetail;
import com.enation.app.shop.trade.service.IOrderQueryManager;
import com.enation.app.shop.trade.service.impl.LogiManager;
import com.enation.app.shop.waybill.model.WayBill;
import com.enation.app.shop.waybill.model.waybilljson.Commodity;
import com.enation.app.shop.waybill.model.waybilljson.Information;
import com.enation.app.shop.waybill.model.waybilljson.WayBillJson;
import com.enation.app.shop.waybill.service.impl.WayBillManager;
import com.enation.framework.util.JsonUtil;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import net.sf.json.JSONObject;



		
/**
 * 快递鸟电子面板插件
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年8月14日 上午10:39:03
 */
@SuppressWarnings("unchecked")
@Component
public class KdniaoPlugin implements WayBillEvent {

	
	
	@Autowired
	private LogiManager loginManager;
	@Autowired
	private IRegionsManager regionManager;
	@Autowired
	private WayBillManager wayBillManager;
	@Autowired
	private ShopManager shopManager;
	@Autowired
	private IOrderQueryManager orderQueryManager;
	
	
	@Override
	public List<ConfigItem> definitionConfigItem() {
		List<ConfigItem> list = new ArrayList<>();
		ConfigItem seller_mchidItem = new ConfigItem();
		seller_mchidItem.setName("EBusinessID");
		seller_mchidItem.setText("电商ID");
		ConfigItem seller_appidItem = new ConfigItem();
		seller_appidItem.setName("AppKey");
		seller_appidItem.setText("密钥");
		ConfigItem seller_keyItem = new ConfigItem();
		seller_keyItem.setName("ReqURL");
		seller_keyItem.setText("请求url");
		list.add(seller_mchidItem);
		list.add(seller_appidItem);
		list.add(seller_keyItem);
		return list;
	}

	@Override
	public String getPluginId() {
		return "kdniaoPlugin";
	}
	
	
	@Override
	public String createPrintData(String ordersn,Integer logi_id) throws Exception{
		//订单
		OrderDetail order = orderQueryManager.getOneBySn(ordersn);
		//获取店铺信息
		Shop store = shopManager.getShop(order.getSeller_id());
		ShopDetail shopDetail = shopManager.getShopDetail(order.getSeller_id());
		//收件人地址
		String Ship_province =  regionManager.get(order.getShip_provinceid()).getLocal_name();
		String Ship_city =  regionManager.get(order.getShip_cityid()).getLocal_name();
		String Ship_region =  regionManager.get(order.getShip_regionid()).getLocal_name();
		
		//获取物流公司信息
		Logi logi = loginManager.getLogiById(logi_id);
		//获取电子面单参数
		WayBill wayBill = this.wayBillManager.getOpen();
		String config = wayBillManager.getConfig(wayBill.getBill_bean());
		String EBusinessID = "";
		String AppKey = "";
		String ReqURL = "";
		if(!config.equals("")) {
			JSONObject jsonObject =  JSONObject.fromObject(config); 
		    EBusinessID = jsonObject.getString("EBusinessID");
			AppKey = jsonObject.getString("AppKey");
			ReqURL = jsonObject.getString("ReqURL");
		}
		//支付方式的对接,获取订单的支付方式 /** 邮费支付方式:1-现付，2-到付，3-月结，4-第三方支付 */
		Integer payType = 3;//TODO
		WayBillJson wayBillJson = new WayBillJson();
		//发送者赋值
		Information senders = new Information();
		senders.setName(store.getMember_name());
		senders.setMobile(shopDetail.getLink_phone());
		senders.setProvinceName(shopDetail.getShop_province());
		senders.setCityName(shopDetail.getShop_city());
		senders.setExpAreaName(shopDetail.getShop_region());
		senders.setAddress(shopDetail.getShop_add());
		//接收者赋值
		Information receivers = new Information();
		receivers.setName(order.getShip_name());
		receivers.setMobile(order.getShip_mobile());
		receivers.setProvinceName(Ship_province);
		receivers.setCityName(Ship_city);
		receivers.setExpAreaName(Ship_region);
		receivers.setAddress(order.getShip_addr());
		
		//商品赋值
		List<Commodity> commoditys = new ArrayList<>();
		Commodity commodity = new Commodity();
		commodity.setGoodsName("12345");
		commodity.setGoodsquantity(order.getGoods_num());
		commodity.setGoodsWeight(order.getWeight());
		commoditys.add(commodity);
		
		wayBillJson.setOrderCode(order.getSn());
		wayBillJson.setShipperCode(logi.getKdcode());
		wayBillJson.setPayType(payType);
		wayBillJson.setExpType("1");
		wayBillJson.setCustomerName(logi.getCustomer_name());
		wayBillJson.setCustomerPwd(logi.getCustomer_pwd());
		wayBillJson.setCost(order.getShipping_price());
		wayBillJson.setOtherCost(1.0);
		wayBillJson.setWeight(order.getWeight());
		wayBillJson.setQuantity(order.getGoods_num());
		wayBillJson.setVolume(0.0);
		wayBillJson.setRemark(order.getRemark());
		wayBillJson.setRemark("小心轻放");
		wayBillJson.setIsReturnPrintTemplate("1");
		wayBillJson.setSender(senders);
		wayBillJson.setReceiver(receivers);
		wayBillJson.setCommodity(commoditys);
		
		String requestData =JsonUtil.ObjectToJson(wayBillJson);
		Map<String, String> params = new HashMap<String, String>();
		params.put("RequestData", urlEncoder(requestData, "UTF-8"));
		params.put("EBusinessID", EBusinessID);
		params.put("RequestType", "1007");
	
		String dataSign=this.encrypt(requestData, AppKey, "UTF-8");
		params.put("DataSign", urlEncoder(dataSign, "UTF-8"));
		params.put("DataType", "2");
		
		String result=sendPost(ReqURL, params);	
		return result;
				
	}


	/**
     * MD5加密
     * @param str 内容       
     * @param charset 编码方式
	 * @throws Exception 
     */
	@SuppressWarnings("unused")
	private String MD5(String str, String charset) throws Exception {
	    MessageDigest md = MessageDigest.getInstance("MD5");
	    md.update(str.getBytes(charset));
	    byte[] result = md.digest();
	    StringBuffer sb = new StringBuffer(32);
	    for (int i = 0; i < result.length; i++) {
	        int val = result[i] & 0xff;
	        if (val <= 0xf) {
	            sb.append("0");
	        }
	        sb.append(Integer.toHexString(val));
	    }
	    return sb.toString().toLowerCase();
	}
	
	/**
     * base64编码
     * @param str 内容       
     * @param charset 编码方式
	 * @throws UnsupportedEncodingException 
     */
	private String base64(String str, String charset) throws UnsupportedEncodingException{
		String encoded = Base64.encode(str.getBytes(charset));
		return encoded;    
	}	
	
	@SuppressWarnings("unused")
	private String urlEncoder(String str, String charset) throws UnsupportedEncodingException{
		String result = URLEncoder.encode(str, charset);
		return result;
	}
	
	/**
     * 电商Sign签名生成
     * @param content 内容   
     * @param keyValue Appkey  
     * @param charset 编码方式
	 * @throws UnsupportedEncodingException ,Exception
	 * @return DataSign签名
     */
	@SuppressWarnings("unused")
	private String encrypt (String content, String keyValue, String charset) throws UnsupportedEncodingException, Exception
	{
		if (keyValue != null)
		{
			return base64(MD5(content + keyValue, charset), charset);
		}
		return base64(MD5(content, charset), charset);
	}
	
	 /**
     * 向指定 URL 发送POST方法的请求     
     * @param url 发送请求的 URL    
     * @param params 请求的参数集合     
     * @return 远程资源的响应结果
     */
	@SuppressWarnings("unused")
	private String sendPost(String url, Map<String, String> params) {
		OutputStreamWriter out = null;
        BufferedReader in = null;        
        StringBuilder result = new StringBuilder(); 
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn =(HttpURLConnection) realUrl.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // POST方法
            conn.setRequestMethod("POST");
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.connect();
            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            // 发送请求参数            
            if (params != null) {
		          StringBuilder param = new StringBuilder(); 
		          for (Map.Entry<String, String> entry : params.entrySet()) {
		        	  if(param.length()>0){
		        		  param.append("&");
		        	  }	        	  
		        	  param.append(entry.getKey());
		        	  param.append("=");
		        	  param.append(entry.getValue());		        	  
		          }
		          out.write(param.toString());
            }
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {            
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result.toString();
    }
}

