package com.enation.framework.validator;

import java.io.Serializable;

public class ErrorMessage implements Serializable{

	/**
	 * v1.0.0 serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private String error_code;
	private String error_message;
	public String getError_code() {
		return error_code;
	}
	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
	public String getError_message() {
		return error_message;
	}
	public void setError_message(String error_message) {
		this.error_message = error_message;
	}
	public ErrorMessage(String error_code, String error_message) {
		super();
		this.error_code = error_code;
		this.error_message = error_message;
	}
	public ErrorMessage() {
		super();
	}
	
	

}
