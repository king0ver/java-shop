package com.enation.app.shop.comments.model.po;


import java.io.Serializable;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.comments.model.vo.MemberCommentVo;
import com.enation.app.shop.goods.model.po.Goods;
import com.enation.app.shop.trade.model.vo.OrderDetail;
import com.enation.app.shop.trade.model.vo.Product;
import com.enation.framework.util.DateUtil;
import com.google.gson.Gson;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 评论咨询表的实体
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年11月28日 下午1:35:29
 */
@ApiModel
public class MemberComment implements Serializable{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键id")
	private Integer comment_id;
	
	@ApiModelProperty(value = "商品id")
	private Integer goods_id;
	
	@ApiModelProperty(value = "商品名称")
	private String goods_name;
	
	@ApiModelProperty(value = "会员头像")
	private String member_face;
	
	@ApiModelProperty(value = "会员名称")
	private String member_name;
	
	@ApiModelProperty(value = "会员id")
	private Integer member_id;
	
	@ApiModelProperty(value = "评论内容")
	private String content;
	
	@ApiModelProperty(value = "评论时间")
	private long create_time;
	
	@ApiModelProperty(value = "ip地址")
	private String ip;
	
	@ApiModelProperty(value = "是否有图片")
	private Integer have_image;
	
	@ApiModelProperty(value = "回复内容")
	private String reply;
	
	@ApiModelProperty(value = "回复时间")
	private long replytime;
	
	@ApiModelProperty(value = "删除状态")
	private Integer status;
	
	@ApiModelProperty(value = "类型")
	private Integer type;
	
	@ApiModelProperty(value = "回复状态")
	private Integer replystatus;
	
	@ApiModelProperty(value = "好中差评")
	private Integer grade;
	
	@ApiModelProperty(value = "是否置顶")
	private Integer is_top;
	
	@ApiModelProperty(value = "规格")
	private String specs;
	
	@ApiModelProperty(value = "货品ID")
	private Integer sku_id;
	
	@ApiModelProperty(value = "店铺id")
	private Integer shop_id;
	
	@ApiModelProperty(value = "订单编号")
	private String order_sn;
	
	public MemberComment(Seller member, Goods goods,String content) {
		super();
		this.content = content;
		this.create_time = DateUtil.getDateline();
		this.goods_id = goods.getGoods_id();
		this.member_id = member.getMember_id();
		this.member_name = member.getName();
		this.shop_id = goods.getSeller_id();
		this.type = 2;
		this.status = 1;
	}

	
	public MemberComment(MemberCommentVo memberCommentVo,Product product,OrderDetail orderDetail) {
		super();
		this.content = memberCommentVo.getContent();
		this.create_time = DateUtil.getDateline();
		this.goods_id = product.getGoods_id();
		this.goods_name = product.getName();
		this.grade = memberCommentVo.getGrade();
		this.member_id = orderDetail.getMember_id();
		this.member_name = orderDetail.getMember_name();
		this.order_sn = orderDetail.getSn();
		this.shop_id = product.getSeller_id();
		this.sku_id = memberCommentVo.getSku_id();
		this.type = 1;
		this.status = 1;
		
		Gson gson = new Gson();
		this.specs = gson.toJson(product.getSpecList());
	}


	public Integer getComment_id() {
		return comment_id;
	}


	public void setComment_id(Integer comment_id) {
		this.comment_id = comment_id;
	}


	public Integer getGoods_id() {
		return goods_id;
	}


	public void setGoods_id(Integer goods_id) {
		this.goods_id = goods_id;
	}


	public String getGoods_name() {
		return goods_name;
	}


	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}


	public String getMember_face() {
		return member_face;
	}


	public void setMember_face(String member_face) {
		this.member_face = member_face;
	}


	public String getMember_name() {
		return member_name;
	}


	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}


	public Integer getMember_id() {
		return member_id;
	}


	public void setMember_id(Integer member_id) {
		this.member_id = member_id;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public long getCreate_time() {
		return create_time;
	}


	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}


	public String getIp() {
		return ip;
	}


	public void setIp(String ip) {
		this.ip = ip;
	}


	public Integer getHave_image() {
		return have_image;
	}


	public void setHave_image(Integer have_image) {
		this.have_image = have_image;
	}


	public String getReply() {
		return reply;
	}


	public void setReply(String reply) {
		this.reply = reply;
	}


	public long getReplytime() {
		return replytime;
	}


	public void setReplytime(long replytime) {
		this.replytime = replytime;
	}


	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}


	public Integer getType() {
		return type;
	}


	public void setType(Integer type) {
		this.type = type;
	}


	public Integer getReplystatus() {
		return replystatus;
	}


	public void setReplystatus(Integer replystatus) {
		this.replystatus = replystatus;
	}


	public Integer getGrade() {
		return grade;
	}


	public void setGrade(Integer grade) {
		this.grade = grade;
	}


	public Integer getIs_top() {
		return is_top;
	}


	public void setIs_top(Integer is_top) {
		this.is_top = is_top;
	}


	public String getSpecs() {
		return specs;
	}


	public void setSpecs(String specs) {
		this.specs = specs;
	}


	public Integer getSku_id() {
		return sku_id;
	}


	public void setSku_id(Integer sku_id) {
		this.sku_id = sku_id;
	}


	public Integer getShop_id() {
		return shop_id;
	}


	public void setShop_id(Integer shop_id) {
		this.shop_id = shop_id;
	}


	public String getOrder_sn() {
		return order_sn;
	}


	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}

	
	
}
