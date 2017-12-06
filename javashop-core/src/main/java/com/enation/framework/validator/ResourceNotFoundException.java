package com.enation.framework.validator;

import org.aspectj.lang.reflect.DeclareErrorOrWarning;
import org.springframework.http.HttpStatus;

/**
 * 
 * 资源找不到异常 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * 2017年8月15日 下午1:02:50
 */
public class ResourceNotFoundException  extends ServiceException{


	private static final long serialVersionUID = -6945068834935110333L;

	public ResourceNotFoundException(String error_code, String message) {
		super(error_code, message);
		this.status_code=HttpStatus.NOT_FOUND;
	}

	public ResourceNotFoundException(String message) {

		super(ErrorCode.RESOURCE_NOT_FOUND,message);
		this.status_code=HttpStatus.NOT_FOUND;

	}
}
