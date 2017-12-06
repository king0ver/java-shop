package com.enation.app.shop.goods.tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.goods.model.vo.CategoryVo;
import com.enation.app.shop.goods.service.ICategoryManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;
import io.swagger.annotations.Api;

@Component
@Scope("prototype")
@Api(description = "商家中心获取商品分类路径标签")
public class GoodsCatPathTag extends BaseFreeMarkerTag{
@Autowired 
 private ICategoryManager categoryManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		Map result=new HashMap();
		int catid = StringUtil.toInt(params.get("catid").toString(),true);
		
		List<CategoryVo> parentList=this.categoryManager.getParents(catid);
		//找到当前的父，以便确定商品类型id
		CategoryVo currentCat = parentList.get(parentList.size()-1);
		result.put("catid", catid);
		result.put("parentList", parentList);
		result.put("cat", categoryManager.get(catid));
		return result;
	}

}
