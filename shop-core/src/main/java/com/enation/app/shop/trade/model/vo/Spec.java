package com.enation.app.shop.trade.model.vo;

import java.io.Serializable;

/**
 * @author kingapex
 * @version 1.0
 * @created 21-����-2017 19:33:41
 */
public class Spec  implements Serializable {

 
	private static final long serialVersionUID = -5455151998039013086L;
	 
	private String spec_name;
	private String spec_value;
	 

	public String getSpec_name() {
		return spec_name;
	}

	public void setSpec_name(String spec_name) {
		this.spec_name = spec_name;
	}

	public String getSpec_value() {
		return spec_value;
	}

	public void setSpec_value(String spec_value) {
		this.spec_value = spec_value;
	}

	public Spec(){

	}

	public void finalize() throws Throwable {

	}

}