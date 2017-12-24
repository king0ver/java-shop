package com.enation.app.shop.goods;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.enation.app.shop.goods.service.IGoodsManager;
import com.enation.framework.test.SpringTestSupport;
/**
 * 
 * 将商品放入回收站 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年9月7日 上午11:07:22
 */
public class GoodsInRecycleGoodsTest extends SpringTestSupport{
	@Autowired 
	private IGoodsManager goodsManager;
	@Test
	public void testClean() throws Exception {
		Integer  goodsid[] = {1};
		goodsManager.inRecycle(goodsid);
	}
}
