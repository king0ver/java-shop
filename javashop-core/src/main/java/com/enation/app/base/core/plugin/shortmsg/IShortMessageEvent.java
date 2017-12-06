package com.enation.app.base.core.plugin.shortmsg;

import java.util.List;

import com.enation.app.base.core.model.ShortMsg;
import com.enation.app.base.core.plugin.job.JobExecutePluginsBundle;
import com.enation.framework.plugin.Bundle;

/**
 * 后台短消息获取事件
 * @author kingapex
 *
 */
@Bundle(ShortMsgPluginBundle.class)
public interface IShortMessageEvent  {
 
	public List<ShortMsg> getMessage();
 
}
