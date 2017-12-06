package com.enation.framework.validator;

import org.springframework.http.HttpStatus;

/**
 * 
 * 无权限异常，比如试图更新一个别人的账号的密码 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * 2017年8月15日 下午1:07:30
 */
public class NoPermissionException extends ServiceException{
 
	private static final long serialVersionUID = 8207742972948289957L;

	public NoPermissionException(String message){
		super(ErrorCode.NO_PERMISSION,message);
		this.status_code=HttpStatus.FORBIDDEN;
	}

	public NoPermissionException(String error_code, String message) {
		super(error_code, message);
		this.status_code=HttpStatus.FORBIDDEN;
	}




}
