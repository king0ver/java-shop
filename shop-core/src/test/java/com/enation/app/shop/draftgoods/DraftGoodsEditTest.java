package com.enation.app.shop.draftgoods;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.enation.app.shop.goods.model.po.GoodsGallery;
import com.enation.app.shop.goods.model.po.GoodsParams;
import com.enation.app.shop.goods.model.po.SpecValue;
import com.enation.app.shop.goods.model.vo.GoodsSkuVo;
import com.enation.app.shop.goods.model.vo.GoodsVo;
import com.enation.app.shop.goods.service.IDraftGoodsManager;
import com.enation.app.shop.member.service.IMemberManager;
import com.enation.app.shop.promotion.exchange.model.po.Exchange;
import com.enation.framework.test.SpringTestSupport;
/**
 * 
 * 草稿箱商品修改 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年11月1日 下午3:17:13
 */
public class DraftGoodsEditTest extends SpringTestSupport {
	@Autowired
	private IDraftGoodsManager draftGoodsManager;
	@Autowired
	private IMemberManager memberManager;

	@Test
	@Rollback(false)
	public void testEdit() throws Exception {
		memberManager.login("food");
		GoodsVo goodsVo =new GoodsVo();
		goodsVo.setGoods_id(2);
		goodsVo.setHas_changed(0);
		goodsVo.setGoods_name("tcl手机");
//		List<GoodsParams> goodsParamsList =new ArrayList<GoodsParams>();
//		GoodsParams goodsParams =new GoodsParams();
//		goodsParams.setGoods_id(1);
//		goodsParams.setParam_id(1);
//		goodsParams.setParam_name("aaa大小");
//		goodsParams.setParam_value("aaa128g");
//		goodsParamsList.add(goodsParams);
//		goodsVo.setGoodsParamsList(goodsParamsList);
//		List<GoodsGallery> goodsGalleryList = new ArrayList<GoodsGallery>();
//		GoodsGallery goodsGallery =new GoodsGallery();
//		goodsGallery.setOriginal("beijing.aliyuncs.com/demo/0AC730D954D646B180741F3E90B279CF.jpg");
//		goodsGalleryList.add(goodsGallery);
//		List<GoodsSkuVo> skuList =new ArrayList<GoodsSkuVo>();
//		GoodsSkuVo goodsSku =new GoodsSkuVo();
//		goodsSku.setCategory_id(1);
//		goodsSku.setGoods_name("bbb苹果手机");
//		goodsSku.setSn("000001");
//		goodsSku.setPrice(100.00);
//		goodsSku.setQuantity(2);
//		goodsSku.setEnable_quantity(2);
//		goodsSku.setSeller_name("aaa手机店");
//		goodsSku.setWeight(100.00);
//		skuList.add(goodsSku);
//		goodsVo.setSkuList(skuList);
//		goodsVo.setGoodsGalleryList(goodsGalleryList);
		Exchange exchange =new Exchange();
		exchange.setEnable_exchange(0);
		exchange.setExchange_money(1.00);
		exchange.setExchange_point(1);
		exchange.setCategory_id(1);
		goodsVo.setExchange(exchange);
		draftGoodsManager.edit(goodsVo);
	}
}
