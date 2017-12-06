package com.enation.framework.validator;

import org.springframework.http.HttpStatus;

/**
 * 
 * 服务异常类,各业务异常需要继承此异常
 * @author jianghongyan
 * @version v1.0.0
 * @since v1.0.0
 * 2017年3月7日 上午11:09:29
 */
public abstract class ServiceException extends RuntimeException{
 
	protected HttpStatus status_code=HttpStatus.INTERNAL_SERVER_ERROR;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4498703243345917888L;
	
	
	private String error_code;

	

	public HttpStatus getStatus_code() {
		return status_code;
	}

	public void setStatus_code(HttpStatus status_code) {
		this.status_code = status_code;
	}

	public ServiceException(String error_code,String message) {
		super(message);
		this.error_code = error_code;
	}

	public String getError_code() {
		return error_code;
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
	
	
	
}
