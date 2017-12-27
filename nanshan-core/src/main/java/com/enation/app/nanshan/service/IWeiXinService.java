package com.enation.app.nanshan.service;

import java.util.Map;

import com.enation.app.nanshan.vo.BaseResultVo;

/**
 * 微信服务
 * @author jianjianming
 * @version $Id: IWeiXinService.java,v 0.1 2017年12月26日 下午4:53:29$
 */
public interface IWeiXinService {
	/**
	 * 微信发送消息
	 * @param tempId 模版ID
	 * @param params 模版对应的数据
	 * @return 调用结果
	 */
	public BaseResultVo sendWeiXinMsg(String tempId,Map<String, Object> params);
	
	/**
	 * 得到token
	 * @param type
	 * @return
	 * @throws Exception 
	 */
	public String getAccessToken(String type) throws Exception;
	
}
