package com.enation.app.cms.focuspic.tag;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.cms.focuspic.model.vo.CmsFrontFocusPicture;
import com.enation.app.cms.focuspic.service.IFocusPictureManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
import io.swagger.annotations.Api;

/**
 * 
 * 前台获取焦点图标签
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年9月11日 上午10:26:52
 */

@Component
@Scope("prototype")
@Api(description = "前台获取焦点图数据接口")
public class FocusPictureTag extends BaseFreeMarkerTag {

	@Autowired
	private IFocusPictureManager focusPictureManager;

	@Override
	protected List<CmsFrontFocusPicture> exec(Map params) throws TemplateModelException {
		String client_type = (String) params.get("client_type");
		return this.focusPictureManager.getCmsFrontFocusPicture(client_type);
	}

}
