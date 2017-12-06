package com.enation.app.shop.comments.model.vo;

import java.io.Serializable;

/**
 * 商品评论消息
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年10月18日 下午9:50:37
 */
public class GoodsCommentMsg implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8978542323509463579L;

	private Integer member_id;
	
	private Integer goods_id;
	
	private Integer sku_id;
	
	private Integer comment_id;

	public Integer getMember_id() {
		return member_id;
	}

	public void setMember_id(Integer member_id) {
		this.member_id = member_id;
	}

	public Integer getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(Integer goods_id) {
		this.goods_id = goods_id;
	}

	public Integer getSku_id() {
		return sku_id;
	}

	public void setSku_id(Integer sku_id) {
		this.sku_id = sku_id;
	}

	public Integer getComment_id() {
		return comment_id;
	}

	public void setComment_id(Integer comment_id) {
		this.comment_id = comment_id;
	}
	
	
}
