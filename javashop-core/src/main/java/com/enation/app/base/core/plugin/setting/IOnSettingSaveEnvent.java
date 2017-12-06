package com.enation.app.base.core.plugin.setting;

import com.enation.framework.plugin.Bundle;

/**
 * 设置保存事件
 * @author kingapex
 *
 */
@Bundle(SettingPluginBundle.class)
public interface IOnSettingSaveEnvent {
	
	
	public void onSave();
	
}
