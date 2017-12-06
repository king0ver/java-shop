package com.enation.framework.validator;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * 
 * Eop 参数校验处理类
 * @author jianghongyan
 * @version v1.0
 * @since v6.2
 * 2016年12月9日 上午12:00:53
 */
@ControllerAdvice
public class JavashopExceptionHandler {
	/**
	 * 处理单个参数校验
	 * @param e
	 * @return
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorMessage handleValidationException(ConstraintViolationException e){
		for(ConstraintViolation<?> s:e.getConstraintViolations()){
			return new ErrorMessage("Invalid_Request_Parameter",s.getMessage());
		}
		return new ErrorMessage("Invalid_Request_Parameter","未知参数错误");
	}
	/**
	 * 处理参数异常	
	 * @param e
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorMessage handleValidationBodyException(MethodArgumentNotValidException e){
		for(ObjectError s:e.getBindingResult().getAllErrors()){
			return new ErrorMessage("Invalid_Request_Parameter",s.getObjectName()+": "+s.getDefaultMessage());
		}
		return new ErrorMessage("Invalid_Request_Parameter","未知参数错误");
	}
	/**
	 * 处理实体类校验
	 * @param e
	 * @return
	 */
	@ExceptionHandler(BindException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorMessage handleValidationBeanException(BindException e){
		for(ObjectError s:e.getAllErrors()){
			
			return new ErrorMessage("Invalid_Request_Parameter",s.getObjectName()+": "+s.getDefaultMessage());
		}
		return new ErrorMessage("Invalid_Request_Parameter","未知参数错误");
	}
	
	/**
	 * 处理POST/PUTCH/PATCH等请求的不可操作业务异常
	 * @param e
	 * @return
	 */
	@ExceptionHandler(ServiceException.class)
	@ResponseBody
	public ErrorMessage handleUnProccessableServiceException(ServiceException e,HttpServletResponse response) {
		response.setStatus(e.getStatus_code().value());
		return new ErrorMessage(e.getError_code(),e.getMessage());
	}
	
	/**
	 * 处理参数传递异常
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseBody
	public ErrorMessage handleUnProccessableServiceException(IllegalArgumentException e,HttpServletResponse response) {
		
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ErrorMessage("IllegalArgumentException",e.getMessage());
	}
	
	
	
}
