package com.enation.app.shop.shop.rdesign.model;

/**
 * 
 * (wap店铺模板) 
 * @author zjp
 * @version v1.0
 * @since v6.4.0
 * 2017年8月25日 下午1:14:23
 */
public class WapShopThemes {
	/**id*/
	private Integer id;		
	/**名称*/
	private String name;	
	/**目录*/
	private String path;	
	/**是否为默认*/
	private Integer is_default;	
	
	//get set
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Integer getIs_default() {
		return is_default;
	}
	public void setIs_default(Integer is_default) {
		this.is_default = is_default;
	}
}
