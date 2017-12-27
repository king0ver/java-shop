package com.enation.app.nanshan.vo;

import java.io.Serializable;

/**
 *  结果返回对象 ： 父类
 * @author jianjianming
 * @version $Id: BaseResult.java,v 0.1 2017年12月13日 下午5:08:03$
 */
public class BaseResultVo implements Serializable{

	private static final long serialVersionUID = 770948408001090195L;

	/**
	 * 返回标识  (成功:true | 失败:false )
	 */
	private boolean isSuccess;
	
	/**
	 * 返回编码 (0:成功 | -1：失败 | -2：参数为空)
	 */
	private String resultCode;
	
	/**
	 * 返回信息
	 */
	private String message;
	
	public boolean isSuccess() {
		return this.isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getResultCode() {
		return this.resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
