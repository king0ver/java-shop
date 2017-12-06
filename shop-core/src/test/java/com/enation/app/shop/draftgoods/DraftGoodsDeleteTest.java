package com.enation.app.shop.draftgoods;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.enation.app.shop.goods.service.IDraftGoodsManager;
import com.enation.framework.test.SpringTestSupport;

/**
 * 
 * 删除草稿箱商品
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年11月1日 下午2:24:18
 */
public class DraftGoodsDeleteTest extends SpringTestSupport {
	@Autowired
	private IDraftGoodsManager draftGoodsManager;

	@Test
	@Rollback(false)
	public void testClean() throws Exception {
		Integer goodsid[] = { 1 };
		draftGoodsManager.delete(goodsid);

	}
}
