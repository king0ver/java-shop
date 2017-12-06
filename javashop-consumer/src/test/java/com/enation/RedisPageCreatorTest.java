package com.enation;


import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.enation.app.cms.floor.model.enumeration.BlockType;
import com.enation.app.cms.floor.model.vo.Block;
import com.enation.app.cms.floor.model.vo.Floor;
import com.enation.app.cms.floor.model.vo.Layout;
import com.enation.app.cms.floor.model.vo.ManualGoodsBlock;
import com.enation.app.cms.floor.model.vo.Panel;
import com.enation.app.cms.floor.service.IFloorManager;
import com.enation.app.cms.floor.service.impl.FloorManager;
import com.enation.app.cms.pagecreate.service.IPageCreator;
import com.enation.app.shop.payment.model.enums.ClientType;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisPageCreatorTest {

	@Autowired
	private IPageCreator redisPageCreator;

	@Autowired
	private IFloorManager floorManager;
	
	
	@Test
	public void testGetFloor() {
		
		Floor floor = floorManager.getFloorDesignInfo(1);//读楼层id为1的
		System.out.println(floor.getName() +"的商品有:");
		
		List<Panel> panelList  = floor.getPanelList();
		for (Panel panel : panelList) {
			List<Layout> layoutList  = panel.getLayoutList();
			for (Layout layout : layoutList) {
				List<Block> blockList =  layout.getBlock_list();
				for (Block block : blockList) {
					
					//只判断商品的区块
					if ( block.getBlock_type().equals(BlockType.MANUAL_GOODS) ) {
						ManualGoodsBlock mBlock = (ManualGoodsBlock)block;
						int goodsid  =  mBlock.getGoods_id();
						System.out.println(goodsid);
					}
				}
			}
		}
		
	}
	
	
	
	/**
	 * 生成商品页面测试
	 */
	@Test
	public void createGoods(){
		redisPageCreator.createGoods();
	}
	/**
	 * 生成首页测试
	 */
	@Test
	public void createIndex(){
		redisPageCreator.createIndex();
	}
	/**
	 * 生成帮助中心页面测试
	 */
	@Test
	public void createHelp(){
		redisPageCreator.createHelp();
	}
	/**
	 * 生成所有页面测试
	 */
	@Test
	public void createALl(){
		redisPageCreator.createAll();
	}
	/**
	 * 生成单个页面测试
	 */
	@Test
	public void createOne(){
		redisPageCreator.createOne("/goods-100.html",ClientType.PC.name());
		redisPageCreator.createOne("/goods-100.html",ClientType.WAP.name());
	}
}
