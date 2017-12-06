package com.enation.app.shop.goods;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.enation.app.shop.goods.model.vo.GoodsQuantityVo;
import com.enation.app.shop.goods.service.IGoodsQuantityManager;
import com.enation.framework.test.SpringTestSupport;

public class GoodsQuantityTest extends SpringTestSupport {
	@Autowired
	private IGoodsQuantityManager goodsQuantityManager;

	@Test
	public void testClean() throws Exception {
		List<GoodsQuantityVo> list = new ArrayList<GoodsQuantityVo>();
		GoodsQuantityVo goodsQuantity = new GoodsQuantityVo();
		goodsQuantity.setEnable_quantity(30);
		goodsQuantity.setGoods_id(157);
		goodsQuantity.setQuantity(30);
		goodsQuantity.setSku_id(159);
		list.add(goodsQuantity);
		goodsQuantityManager.updateGoodsQuantity(list);
	}
}
