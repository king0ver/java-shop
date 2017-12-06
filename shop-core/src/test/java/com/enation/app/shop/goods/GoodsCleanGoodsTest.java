package com.enation.app.shop.goods;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.enation.app.shop.goods.service.IGoodsManager;
import com.enation.framework.test.SpringTestSupport;
/**
 * 
 * 彻底删除商品测试类 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年9月7日 上午11:26:49
 */
public class GoodsCleanGoodsTest extends SpringTestSupport{
	@Autowired 
	private IGoodsManager goodsManager;
	@Test
	@Rollback(false)
	public void testClean() throws Exception {
		Integer  goodsid[] = {2};
		goodsManager.delete(goodsid);
		
		
	}
}
