package com.enation.framework.validator;

import org.springframework.http.HttpStatus;

/**
 * 
 * 未能处理的服务异常类,各业务无法正常处理时抛出,需要规定异常码,并把异常码信息放入error_code中
 * @author jianghongyan
 * @version v1.0.0
 * @since v1.0.0
 * 2017年3月7日 上午11:09:29
 */
public class UnProccessableServiceException extends ServiceException{

 
	/**
	 * 
	 */
	private static final long serialVersionUID = 2723546781584452130L;


	public UnProccessableServiceException(String error_code, String message) {
		super(error_code, message);
		this.status_code=HttpStatus.UNPROCESSABLE_ENTITY;
	}


	public UnProccessableServiceException(String message) {
		super(ErrorCode.INVALID_REQUEST_PARAMETER, message);
		this.status_code=HttpStatus.UNPROCESSABLE_ENTITY;
	}
}
