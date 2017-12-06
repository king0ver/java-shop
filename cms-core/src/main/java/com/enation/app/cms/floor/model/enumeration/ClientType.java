package com.enation.app.cms.floor.model.enumeration;
/**
 * 
 * 区分模版中客户端类型 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月13日 下午8:48:29
 */
public enum ClientType {
	pc("pc"),
	mobile("mobile");
	private String clientType;

	ClientType(String _clientType) {
		this.clientType = _clientType;

	}

	public String clientType(){
		return this.clientType;
	}
	
	public String value(){
		return this.name();
	}

}
