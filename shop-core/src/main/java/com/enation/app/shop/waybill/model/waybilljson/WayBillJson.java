package com.enation.app.shop.waybill.model.waybilljson;
/**
 * 	电子面单发送json请求的应用级参数封装的实体
 * (这里用一句话描述这个类的作用) 
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年8月12日 下午5:38:11
 */

import java.util.List;

/**
 * 	快递鸟电子面板封装参数实体
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年8月14日 上午10:51:10
 */
public class WayBillJson {

	/** 订单编号 */
	private String OrderCode; 
	/** 快递公司编码 */
	private String ShipperCode; 
	/** 邮费支付方式:1-现付，2-到付，3-月结，4-第三方支付 */
	private Integer PayType; 
	/** 快递类型：1-标准快件 */
	private String ExpType;  
	/** 是否通知快递员上门揽件：0-通知；1-不通知；不填则默认为1 */
	private Integer IsNotice; 
	/** 电子面单客户账号（与快递网点申请或通过快递鸟官网申请或通过申请电子面单客户号申请） */
	private String CustomerName; 
	/** 电子面单密码 */
	private String CustomerPwd;
	/** 寄件费（运费） */
	private Double Cost; 
	/** 其他费用 */
	private Double OtherCost;
	/** 发件人的信息 */
	private Information Sender; 
	/** 收件人的信息 */
	private Information Receiver; 
	/** 寄送商品信息 */
	private List<Commodity> Commodity; 
	/** 物品总重量kg */
	private Double Weight; 
	/** 件数/包裹数 */
	private Integer Quantity; 
	/** 物品总体积m3 */
	private Double Volume; 
	/** 备注 */
	private String Remark;
	/** 返回电子面单模板：0-不需要；1-需要 */
	private String IsReturnPrintTemplate; 
	
	
	/**
	 * 快递鸟电子面单的参数需要添加可在下面添加
	 */
	
	
	
	
	public String getOrderCode() {
		return OrderCode;
	}
	public void setOrderCode(String orderCode) {
		OrderCode = orderCode;
	}
	public String getShipperCode() {
		return ShipperCode;
	}
	public void setShipperCode(String shipperCode) {
		ShipperCode = shipperCode;
	}
	public Integer getPayType() {
		return PayType;
	}
	public void setPayType(Integer payType) {
		PayType = payType;
	}
	public String getExpType() {
		return ExpType;
	}
	public void setExpType(String expType) {
		ExpType = expType;
	}
	public Integer getIsNotice() {
		return IsNotice;
	}
	public void setIsNotice(Integer isNotice) {
		IsNotice = isNotice;
	}
	public String getCustomerName() {
		return CustomerName;
	}
	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}
	public String getCustomerPwd() {
		return CustomerPwd;
	}
	public void setCustomerPwd(String customerPwd) {
		CustomerPwd = customerPwd;
	}
	public Double getCost() {
		return Cost;
	}
	public void setCost(Double cost) {
		Cost = cost;
	}
	public Double getOtherCost() {
		return OtherCost;
	}
	public void setOtherCost(Double otherCost) {
		OtherCost = otherCost;
	}
	
	
	public Information getSender() {
		return Sender;
	}
	public void setSender(Information sender) {
		Sender = sender;
	}
	public Information getReceiver() {
		return Receiver;
	}
	public void setReceiver(Information receiver) {
		Receiver = receiver;
	}
	public List<Commodity> getCommodity() {
		return Commodity;
	}
	public void setCommodity(List<Commodity> commodity) {
		Commodity = commodity;
	}
	public Double getWeight() {
		return Weight;
	}
	public void setWeight(Double weight) {
		Weight = weight;
	}
	public Integer getQuantity() {
		return Quantity;
	}
	public void setQuantity(Integer quantity) {
		Quantity = quantity;
	}
	public Double getVolume() {
		return Volume;
	}
	public void setVolume(Double volume) {
		Volume = volume;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}
	public String getIsReturnPrintTemplate() {
		return IsReturnPrintTemplate;
	}
	public void setIsReturnPrintTemplate(String isReturnPrintTemplate) {
		IsReturnPrintTemplate = isReturnPrintTemplate;
	}

	

	
	

}
