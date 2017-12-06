package com.enation.app.shop.goods.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.goods.model.vo.BrandVo;
import com.enation.app.shop.goods.service.IBrandManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 
 * 品牌列表标签 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年9月13日 下午8:28:53
 */
@Component
@Scope("prototype")
public class BrandListTag extends BaseFreeMarkerTag {
	@Autowired
	private IBrandManager brandManager;

	/**
	 * @param 不需要输出参数，
	 */
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		String category_id = (String) params.get("category_id");
		if (category_id != null) {
			return brandManager.getBrandsByCategory(Integer.valueOf(category_id));
		}
		List<BrandVo> brandList = new ArrayList<BrandVo>();
		return brandList;
	}
}
