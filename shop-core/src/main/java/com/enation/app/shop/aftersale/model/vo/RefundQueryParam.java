package com.enation.app.shop.aftersale.model.vo;

import com.enation.app.shop.aftersale.model.enums.RefundStatus;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 退货单查询参数
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年4月24日下午11:14:47
 */
@ApiModel( description = "退货(款)单查询参数")
public class RefundQueryParam {
	
	@ApiModelProperty(value = "页码"  )
	private Integer page_no;
	
	@ApiModelProperty(value = "分页大小"  )
	private Integer page_size;
	
	@ApiModelProperty(value = "退货(款)单状态"  )
	private String refund_status;
	
	
	@ApiModelProperty(value = "退货(款)单编号" )
	private String sn;
	
	@ApiModelProperty(hidden=true)
	private Integer member_id;

	@ApiModelProperty(hidden=true)
	private Integer seller_id;
	
	@ApiModelProperty(value = "卖家店铺名称" )
	private String seller_name;
	
	@ApiModelProperty(value = "订单编号" )
	private String order_sn;
	
	@ApiModelProperty(value = "类型",allowableValues="return_money,return_goods" ,hidden=true)
	private String refuse_type;
	
	@ApiModelProperty(value = "起始时间" )
	private String start_time;
	
	@ApiModelProperty(value = "结束时间" )
	private String end_time;

	private Integer ident = 0;

	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	
	public Integer getMember_id() {
		return member_id;
	}
	public void setMember_id(Integer member_id) {
		this.member_id = member_id;
	}
	public Integer getSeller_id() {
		return seller_id;
	}
	
	public void setSeller_id(Integer seller_id) {
		this.seller_id = seller_id;
	}
	public String getOrder_sn() {
		return order_sn;
	}
	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}
	public String getRefuse_type() {
		return refuse_type;
	}
	public void setRefuse_type(String refuse_type) {
		this.refuse_type = refuse_type;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	} 
	
	public Integer getPage_no() {
		if(page_no==null){
			page_no=1;
		}
		return page_no;
	}
	
	public void setPage_no(Integer page_no) {
		this.page_no = page_no;
	}
	
	public Integer getPage_size() {
		if(page_size==null){
			page_size=20;
		}
		return page_size;
	}
	public void setPage_size(Integer page_size) {
		this.page_size = page_size;
	}

	public String getRefund_status() {
		return refund_status;
	}
	public void setRefund_status(String refund_status) {
		this.refund_status = refund_status;
	}
	public String getSeller_name() {
		return seller_name;
	}
	public void setSeller_name(String seller_name) {
		this.seller_name = seller_name;
	}

	public Integer getIdent() {
		return ident;
	}

	public void setIdent(Integer ident) {
		this.ident = ident;
	}
}
