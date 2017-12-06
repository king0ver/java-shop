package com.enation.app.shop.goods.tag;


import com.enation.app.shop.goods.model.po.Specification;
import com.enation.app.shop.goods.model.vo.GoodsSkuVo;
import com.enation.app.shop.goods.model.vo.GoodsVo;
import com.enation.app.shop.goods.service.IGoodsManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import freemarker.template.TemplateModelException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
/**
 * 
 * (预警商品规格标签) 
 * @author zjp
 * @version v1.0
 * @since v6.2
 * 2016年12月26日 下午6:51:24
 */
@Component
@Scope("prototype")
public class WarningGoodsTag extends BaseFreeMarkerTag {
	
	
	@Autowired
	private IGoodsManager goodsManager;
	
	/**
	 * 商品规格标签
	 * @param  无
	 * @return 商品规格，类型Map。根据have_spec(是否有规格 0无  1有) 的值不同，返回不同的值
	 * have_spec：0 时，返回值Map的key有： productid(商品的货品id)、productList(商品的货品列表){@link Product}
	 * have_spec：1 时，返回值Map的key有：specList(商品规格数据列表){@link Specification}、productList(商品的货品列表)
	 */
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		Integer goods_id=Integer.parseInt(params.get("goods_id").toString());
		GoodsVo goods = this.goodsManager.getFromCache(goods_id);
		Integer store_id = (Integer) goods.getSeller_id();
		List<GoodsSkuVo> productList  = this.goodsManager.warningGoodsList(goods_id,store_id);
		return productList;
	}


}
