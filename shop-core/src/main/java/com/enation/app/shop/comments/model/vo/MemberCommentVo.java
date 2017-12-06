package com.enation.app.shop.comments.model.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 会员评论vo
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年11月28日 下午1:59:48
 */
@ApiModel(description = "会员评论vo")
public class MemberCommentVo {

	@ApiModelProperty(value = "会员评论内容"  )
	private String content;
	
	@ApiModelProperty(value = "好中差评"  )
	private Integer grade;
	
	@ApiModelProperty(value = "会员评论的图片"  )
	private List<String> images;
	
	@ApiModelProperty(value = "会员评论商品规格id"  )
	private Integer sku_id;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getGrade() {
		return grade;
	}
	public void setGrade(Integer grade) {
		this.grade = grade;
	}
	public List<String> getImages() {
		return images;
	}
	public void setImages(List<String> images) {
		this.images = images;
	}
	public Integer getSku_id() {
		return sku_id;
	}
	public void setSku_id(Integer sku_id) {
		this.sku_id = sku_id;
	}
	
	
}
