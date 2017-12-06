package com.enation.app.shop.promotion;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.promotion.tool.service.IPromotionBackupManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.test.SpringTestSupport;

/**
 * 促销备份
 * 
 * @author Chopper
 * @version v1.0
 * @since v6.4 2017年9月5日 下午4:36:27
 *
 */
@Rollback(false)
public class PromotionGoodsBackendTest extends SpringTestSupport {

	@Autowired
	private IPromotionBackupManager promotionBackendManager;

	
//	@Autowired
//	private IDaoSupport daoSupport;

	@Test
	public void testBackend() { 
		try {
//			daoSupport.execute("insert into es_promotion_goods_backend values(?,?,?,?,?,?,?,?)", 4,4,4,4 ,4,4,4,5);
			promotionBackendManager.backup();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	@Test
	public void testRollback() { 
		try {
//			daoSupport.execute("insert into es_promotion_goods_backend values(?,?,?,?,?,?,?,?)", 4,4,4,4 ,4,4,4,5);
			rollback();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	@Transactional
	private void rollback() {
		promotionBackendManager.backup();
		System.out.println(1/0);
	}
	 

}
