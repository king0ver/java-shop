package com.enation.app.shop.goods;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.enation.app.shop.goods.service.IGoodsManager;
import com.enation.framework.test.SpringTestSupport;
/**
 * 
 * 商品还原测试类 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年9月7日 上午11:06:09
 */
public class GoodsRevertGoodsTest extends SpringTestSupport{
	@Autowired 
	private IGoodsManager goodsManager;
	@Test
	@Rollback(false)
	public void testAdd() throws Exception {
		Integer  goodsid[] = {10};
		goodsManager.revert(goodsid);
	}
}
