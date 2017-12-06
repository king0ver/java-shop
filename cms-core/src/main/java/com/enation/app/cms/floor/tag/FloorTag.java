package com.enation.app.cms.floor.tag;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.cms.floor.model.floor.FrontFloor;
import com.enation.app.cms.floor.service.IFloorManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
import io.swagger.annotations.Api;
/**
 * 
 * 前台获取楼层数据接口
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月15日 下午12:40:07
 */
@Component
@Scope("prototype")
@Api(description = "前台获取楼层数据接口")
public class FloorTag extends BaseFreeMarkerTag {

	@Autowired
	private IFloorManager floorManager;

	@Override
	protected List<FrontFloor> exec(Map params) throws TemplateModelException {
		String client_type =(String) params.get("client_type");
		return this.floorManager.getAllCmsFrontFloor(client_type);
	}
}
