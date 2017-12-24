package com.enation.app.shop.draftgoods;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.enation.app.shop.goods.model.vo.GoodsVo;
import com.enation.app.shop.goods.service.IDraftGoodsManager;
import com.enation.app.shop.member.service.IMemberManager;
import com.enation.app.shop.promotion.exchange.model.po.Exchange;
import com.enation.framework.test.SpringTestSupport;

/**
 * 
 * 添加草稿箱商品
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月31日 下午3:25:07
 */
public class DraftGoodsAddTest extends SpringTestSupport {
	@Autowired
	private IMemberManager memberManager;
	@Autowired
	private IDraftGoodsManager draftGoodsManager;

	@Test
	@Rollback(false)
	public void testAdd() throws Exception {
		memberManager.login("food");
		GoodsVo goodsVo = new GoodsVo();
		//goodsVo.setGoods_name("苹果手机");
		goodsVo.setCategory_id(1);
		goodsVo.setShop_cat_id(1);
		//goodsVo.setBrand_id(1);
		//goodsVo.setSn("0003658474");
		//goodsVo.setPrice(100d);
		//goodsVo.setCost(105.00);
		//goodsVo.setMktprice(150.00);
		//goodsVo.setWeight(100.00);
		goodsVo.setGoods_transfee_charge(1);
		//goodsVo.setIntro("手机");
		//goodsVo.setHave_spec(1);
		//goodsVo.setUnit("千克");
		Exchange exchange =new Exchange();
		exchange.setEnable_exchange(1);
		exchange.setExchange_money(1.00);
		exchange.setExchange_point(1);
		exchange.setCategory_id(1);
//		List<GoodsGallery> goodsGalleryList = new ArrayList<GoodsGallery>();
//		GoodsGallery goodsGallery =new GoodsGallery();
//		goodsGallery.setOriginal("beijing.aliyuncs.com/demo/0AC730D954D646B180741F3E90B279CF.jpg");
//		goodsGalleryList.add(goodsGallery);
//		List<GoodsParams> goodsParamsList = new ArrayList<GoodsParams>();
//		GoodsParams goodsParams = new GoodsParams();
//		goodsParams.setGoods_id(1);
//		goodsParams.setParam_id(1);
//		goodsParams.setParam_name("大小");
//		goodsParams.setParam_value("128g");
//		goodsParamsList.add(goodsParams);
//		goodsVo.setGoodsParamsList(goodsParamsList);
//		List<GoodsSkuVo> skuList = new ArrayList<GoodsSkuVo>();
//		GoodsSkuVo goodsSku = new GoodsSkuVo();
//		goodsSku.setCategory_id(1);
//		goodsSku.setGoods_id(1);
//		goodsSku.setGoods_name("苹果手机");
//		goodsSku.setSn("0000265845");
//		goodsSku.setPrice(100.00);
//		goodsSku.setQuantity(2);
//		goodsSku.setEnable_quantity(2);
//		goodsSku.setSeller_id(1);
//		goodsSku.setSeller_name("手机店");
//		goodsSku.setWeight(100.00);
//		List<SpecValue> specValues = new ArrayList<SpecValue>();
//		SpecValue specValue = new SpecValue();
//		specValue.setSpec_id(1);
//		specValue.setSpec_value("重量");
//		specValue.setSpec_type(0);
//		specValues.add(specValue);
//		goodsSku.setSpecList(specValues);
//		skuList.add(goodsSku);
		//goodsVo.setSkuList(skuList);
		//goodsVo.setGoodsGalleryList(goodsGalleryList);
		goodsVo.setExchange(exchange);
		draftGoodsManager.add(goodsVo);
	}
}
