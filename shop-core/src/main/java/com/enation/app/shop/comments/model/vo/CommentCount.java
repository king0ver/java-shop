package com.enation.app.shop.comments.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * (这里用一句话描述这个类的作用) 
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年11月29日 上午11:45:58
 */
@ApiModel(description = "")
public class CommentCount {
	
	@ApiModelProperty(value = "总评分"  )
	private Integer all;
	
	@ApiModelProperty(value = "中评" )
	private Integer general;
	
	@ApiModelProperty(value = "好评" )
	private Integer good;
	
	@ApiModelProperty(value = "评论图片" )
	private Integer image;
	
	@ApiModelProperty(value = "差评" )
	private Integer poor;
	public Integer getAll() {
		return all;
	}
	public void setAll(Integer all) {
		this.all = all;
	}
	public Integer getGeneral() {
		return general;
	}
	public void setGeneral(Integer general) {
		this.general = general;
	}
	public Integer getGood() {
		return good;
	}
	public void setGood(Integer good) {
		this.good = good;
	}
	public Integer getImage() {
		return image;
	}
	public void setImage(Integer image) {
		this.image = image;
	}
	public Integer getPoor() {
		return poor;
	}
	public void setPoor(Integer poor) {
		this.poor = poor;
	}
	
	
	
	
}
