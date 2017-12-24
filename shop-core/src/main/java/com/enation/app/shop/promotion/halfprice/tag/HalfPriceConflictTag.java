package com.enation.app.shop.promotion.halfprice.tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.enation.app.shop.promotion.tool.model.po.PromotionGoods;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
/**
 * 获取添加第二件半件促销活动发生冲突的活动详细tag
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年8月22日 下午1:38:51
 */
@Component
public class HalfPriceConflictTag extends BaseFreeMarkerTag {

	/**
	 * 获取添加时促销活动发生冲突的活动tag
	 * params 传入活动id
	 * Object 返回页面封装的list进行页面的展示(后期会写一个vo实体,后期做修改)
	 */
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		int pageSize = 5;
		Integer pageNo = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page").toString());
		List<PromotionGoods> actList = (List) ThreadContextHolder.getSession().getAttribute("actList");
		Integer size = actList.size();
		Integer fromIndex = pageSize*(pageNo-1);
		Integer toIndex = size<5+pageSize*(pageNo-1)?size:5+pageSize*(pageNo-1);
		List subList = actList.subList(fromIndex, toIndex);
		
		Map result = new HashMap();
		
		result.put("goodsList",subList);
		result.put("totalCount", size);
		result.put("page", pageNo);
		result.put("pageSize", pageSize);
		return result;
	}

}
