package com.enation.app.shop.goods.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.enation.app.shop.core.DomainUtil;
import com.enation.app.shop.payment.model.enums.ClientType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.goods.model.vo.BrandNav;
import com.enation.app.shop.goods.model.vo.BrandVo;
import com.enation.app.shop.goods.model.vo.CategoryVo;
import com.enation.app.shop.goods.service.IBrandManager;
import com.enation.app.shop.goods.service.ICategoryManager;
import com.enation.app.shop.goodssearch.util.BrandUrlUtils;
import com.enation.app.shop.goodssearch.util.CatUrlUtils;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 
 * 商品分类标签
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年9月14日 下午8:53:04
 */
@Component
@Scope("prototype")
public class GoodsCategoryTag extends BaseFreeMarkerTag {
	@Autowired
	private ICategoryManager categoryManager;
	@Autowired
	private IBrandManager brandManager;

	/**
	 * 商品分类数据,包括品牌
	 *
	 * @return 分类数据信息，Map型，其中的key结构为: showimg:String型,是否显示分类图片 cat_tree:List型,分类列表数据
	 */
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Integer parentid = (Integer) params.get("parentid");
		if (parentid == null) {
			parentid = 0;
		}

		List<CategoryVo> cat_tree = categoryManager.listAllChildren(parentid);
		String catimage = (String) params.get("catimage");
		boolean showimage = catimage != null && catimage.equals("on") ? true : false;
		String domain = DomainUtil.getGoodsDomain(ClientType.PC);
		for (CategoryVo cat : cat_tree) {
			// 一级分类
			cat.setUrl(domain + "/goods_list.html?" + CatUrlUtils.createCatUrl(cat, true));
			// 二级分类
			for (CategoryVo c2 : cat.getChildren()) {// 二级
				c2.setUrl(domain + "/goods_list.html?" + CatUrlUtils.createCatUrl(c2, true));
				// 三级分类
				for (CategoryVo c3 : c2.getChildren()) {// 二级
					c3.setUrl(domain + "/goods_list.html?" + CatUrlUtils.createCatUrl(c3, true));
				}
			}
			if (cat.getParent_id().compareTo(parentid) == 0) {
				List<BrandVo> brandList = null;
				try {
					brandList = brandManager.getBrandsByCategory(cat.getCategory_id());
				} catch (Exception e) {
					e.printStackTrace();
				}
				List<BrandNav> brandNavList = new ArrayList<>();
				for (BrandVo brand : brandList) {
					BrandNav brandNav = new BrandNav();
					brandNav.setLogo(brand.getLogo());
					brandNav.setName(brand.getName());
					brandNav.setUrl(domain + "/goods_list.html?"
							+ BrandUrlUtils.createBrandUrl(brand.getBrand_id().toString()));
					brandNavList.add(brandNav);
				}
				cat.setBrands(brandNavList);
			}
		}
		Map<String, Object> data = new HashMap();
		data.put("showimg", showimage);// 是否显示分类图片
		data.put("cat_tree", cat_tree);// 分类列表数据
		return data;
	}

}
