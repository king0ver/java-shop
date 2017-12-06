package com.enation.app.shop.trade.plugin.setting;

import com.enation.app.base.core.plugin.setting.IOnSettingInputShow;
import com.enation.framework.plugin.AutoRegister;
import com.enation.framework.plugin.AutoRegisterPlugin;

/**
 * 系统设置，订单设置插件
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月28日 下午6:48:24
 */
@AutoRegister
public class OrderSettingPlugin extends AutoRegisterPlugin implements IOnSettingInputShow{

	@Override
	public String onShow() {

		return "order-setting";
	}

	@Override
	public String getSettingGroupName() {

		return "order";
	}

	@Override
	public String getTabName() {
		return "订单设置";
	}

	@Override
	public int getOrder() {

		return 6;
	}

}
