
package com.enation.app.shop.promotion.tool.model.po;


/**
 * 活动商品对照实体
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年8月20日 下午12:34:06
 */
public class PromotionGoods implements java.io.Serializable{

	/** 商品对照ID */
	private Integer pg_id;
	/** 商品id */
	private Integer goods_id;
	/** 货品id */
	private Integer product_id;
	/** 活动开始时间 */
	private Long start_time;
	/** 活动结束时间 */
	private Long end_time;
	/** 活动id */
	private Integer activity_id;
	/** 促销工具类型 */
	private String promotion_type;
	/** 活动标题 */
	private String title;
	
	
	
	public Integer getPg_id() {
		return pg_id;
	}
	public void setPg_id(Integer pg_id) {
		this.pg_id = pg_id;
	}
	public Integer getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(Integer goods_id) {
		this.goods_id = goods_id;
	}
	public Integer getProduct_id() {
		return product_id;
	}
	public void setProduct_id(Integer product_id) {
		this.product_id = product_id;
	}
	public Long getStart_time() {
		return start_time;
	}
	public void setStart_time(Long start_time) {
		this.start_time = start_time;
	}
	public Long getEnd_time() {
		return end_time;
	}
	public void setEnd_time(Long end_time) {
		this.end_time = end_time;
	}
	public Integer getActivity_id() {
		return activity_id;
	}
	public void setActivity_id(Integer activity_id) {
		this.activity_id = activity_id;
	}
	public String getPromotion_type() {
		return promotion_type;
	}
	public void setPromotion_type(String promotion_type) {
		this.promotion_type = promotion_type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	
	
	
	
	
}
