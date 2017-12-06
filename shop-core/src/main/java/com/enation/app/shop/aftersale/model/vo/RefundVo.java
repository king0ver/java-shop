package com.enation.app.shop.aftersale.model.vo;

import com.enation.app.shop.aftersale.model.enums.RefundStatus;
import com.enation.app.shop.aftersale.model.enums.RefuseType;
import com.enation.app.shop.aftersale.model.po.Refund;
import com.enation.framework.util.DateUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 退货（款）单列表行
 * 
 * @author kingapex
 * @version 1.0
 * @since pangu1.0 2017年4月25日下午3:26:36
 */
@ApiModel(description = "退货(款)单")
public class RefundVo extends Refund {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4323810525178408877L;

	@ApiModelProperty(value = "退货(款)单状态")
	public String getRefund_status_text() {
		return RefundStatus.valueOf(this.getRefund_status()).description();
	}

	@ApiModelProperty(value = "申请时间")
	public String create_time_text() {
		return DateUtil.toString(this.getCreate_time(), "yyyy-MM-dd HH:mm");

	}

	@ApiModelProperty(value = "售后操作允许情况")
	public OperateAllowable getOperateAllowable() {
		OperateAllowable allowable = new OperateAllowable(RefuseType.valueOf(this.getRefuse_type()),
				RefundStatus.valueOf(this.getRefund_status()));
		return allowable;
	}

	@ApiModelProperty(value = "退款方式文字")
	public String getAccount_type_text() {

		if ("alipayDirectPlugin".equals(this.getAccount_type())) {
			
			return "支付宝";
		} else if ("weixinPayPlugin".equals(this.getAccount_type())) {
			
			return "微信";
		} else if ("银行转账".equals(this.getAccount_type())) {
			
			return "银行转账";
		}
		return "";

	}

}
