package com.enation.app.shop.draftgoods;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.enation.app.shop.goods.service.IDraftGoodsManager;
import com.enation.framework.test.SpringTestSupport;

/**
 * 
 * 获取草稿箱商品sku信息
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年11月1日 下午2:37:58
 */
public class DraftGetGoodsSkuTest extends SpringTestSupport {
	@Autowired
	private IDraftGoodsManager draftGoodsManager;

	@Test
	@Rollback(false)
	public void testGet() throws Exception {
		Integer goodsid = 2;
		draftGoodsManager.draftGoodsSkuList(goodsid);
	}
}
