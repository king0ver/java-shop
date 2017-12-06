package com.enation.app.shop.comments.model.po;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 评论图片po实体
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年11月28日 下午1:42:46
 */
@ApiModel
public class MemberCommentGallery implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "评论ID")
	private Integer comment_id;
	
	@ApiModelProperty(value = "主键ID")
	private Integer img_id;
	
	@ApiModelProperty(value = "原图")
	private String original;
	
	@ApiModelProperty(value = "排序")
	private Integer sort;
	
	
	
	
	public MemberCommentGallery(Integer comment_id, List<String> images) {
		super();
		this.comment_id = comment_id;
		this.original = StringUtils.join(images, ",");
	}
	public Integer getComment_id() {
		return comment_id;
	}
	public void setComment_id(Integer comment_id) {
		this.comment_id = comment_id;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Integer getImg_id() {
		return img_id;
	}
	public void setImg_id(Integer img_id) {
		this.img_id = img_id;
	}
	public String getOriginal() {
		return original;
	}
	public void setOriginal(String original) {
		this.original = original;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	
}
