package com.enation.app.shop.promotion.groupbuy.model.vo;

import com.enation.app.shop.promotion.groupbuy.model.po.GroupBuyActive;
import com.enation.framework.database.NotDbField;
import com.enation.framework.util.DateUtil;

/**
 * 团购vo 
 * @author Chopper
 * @version v1.0
 * @since v6.4
 * 2017年9月7日 下午1:39:32 
 *
 */
public class GroupBuyActiveVo extends GroupBuyActive {

	@NotDbField
	public String getStatus() {
		Long now = DateUtil.getDateline();
		if(this.getStart_time()>now) {
			return GroupBuyEnum.NOTBEGIN.getStatus();
		}else if(this.getStart_time()<now&&this.getEnd_time()>now) {
			return GroupBuyEnum.CONDUCT.getStatus();
		}else {
			return GroupBuyEnum.OVERDUE.getStatus();
		}
	}
}
