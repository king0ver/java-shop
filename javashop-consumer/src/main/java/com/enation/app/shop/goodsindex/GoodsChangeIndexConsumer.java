package com.enation.app.shop.goodsindex;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.core.event.IGoodsChangeEvent;
import com.enation.app.shop.goods.service.IGoodsQueryManager;
import com.enation.app.shop.goodssearch.service.IGoodsIndexManager;
import com.enation.app.shop.goodssearch.service.IndexTypeFactory;
import com.enation.framework.jms.support.goods.GoodsChangeMsg;

/**
 * 消费者
 * @author zh
 * @version v1.0
 * @since v1.0
 * 2017年4月12日 下午4:33:14
 */
@Service
public class GoodsChangeIndexConsumer implements IGoodsChangeEvent{
	
	@Autowired
	private IGoodsQueryManager goodsQueryManager;
	
	@Override
	public void goodsChange(GoodsChangeMsg goodsChangeMsg) {
		
		Integer[] goods_ids = goodsChangeMsg.getGoods_ids();
		int operation_type = goodsChangeMsg.getOperation_type();
		List<Map<String, Object>> list = goodsQueryManager.getGoodsAndParams(goods_ids);
		
		IGoodsIndexManager goodsIndexManager = IndexTypeFactory.getIndexType();
		
		//添加
		if(GoodsChangeMsg.ADD_OPERATION==operation_type){
			
			if(list!=null &&list.size()>0){
				goodsIndexManager.addIndex(list.get(0));
			}
			
		}else if(GoodsChangeMsg.UPDATE_OPERATION==operation_type
				||GoodsChangeMsg.INRECYCLE_OPERATION==operation_type
				||GoodsChangeMsg.UNDER_OPERATION==operation_type
				||GoodsChangeMsg.REVERT_OPERATION==operation_type){//修改(修改，还原，下架，放入购物车)
			
			if(list!=null){
				for(Map<String, Object> map : list){
					goodsIndexManager.updateIndex(map);
				}
			}
			
		}else if(GoodsChangeMsg.DEL_OPERATION==operation_type){//删除
			
			if(list!=null){
				for(Map<String, Object> map : list){
					goodsIndexManager.deleteIndex(map);
				}
			}
			
		}
	}
}
