package com.enation.app.cms.floor.tag;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.cms.floor.service.IFloorManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 
 * 楼层名及锚点Json输出
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月9日 下午3:53:08
 */
@Component
@Scope("prototype")
public class FloorAnchorTag extends BaseFreeMarkerTag {
	@Autowired
	private IFloorManager floorManager;

	@Override
	protected Object exec(Map params) throws TemplateModelException {
		String client_type = (String) params.get("client_type");
		return this.floorManager.getFloorAnchor(client_type);
	}

}
