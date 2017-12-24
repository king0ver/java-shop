package com.enation.app.shop.order.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.goods.model.vo.GoodsQuantityVo;
import com.enation.app.shop.goods.service.IGoodsQuantityManager;
import com.enation.app.shop.order.manager.IProductIntoDBManager;
import com.enation.app.shop.trade.model.enums.OrderStatus;
import com.enation.app.shop.trade.model.po.OrderItem;
import com.enation.app.shop.trade.model.vo.Order;
import com.enation.app.shop.trade.model.vo.Product;
import com.enation.framework.database.IDaoSupport;

/**
 * 产品入库业务类
 * @author xulipeng
 * @since v6.4
 * @version v1.0 
 * 2017年09月28日11:46:15
 */
@Service
public class ProductIntoDBManager implements IProductIntoDBManager {

	protected final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private IDaoSupport daoSupport;
	
	@Autowired
	private IGoodsQuantityManager goodsQuantityManager;
	
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.order.manager.IProductIntoDBManager#productIntoDB(com.enation.app.shop.trade.model.vo.Order, java.util.List)
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public void productIntoDB(Order order, List<Product> productList) {
		
		//保证插入成功
		for (Product product : productList) {
			
			OrderItem item = new OrderItem(product);
			item.setOrder_sn(order.getSn());
			item.setTrade_sn(order.getTrade_sn());
			this.daoSupport.insert("es_order_items", item);
		}
		
		try {
			//判断库存扣减的失败
			boolean flag = true;
			/**
			 * 此List记录已经成功扣减库存的产品
			 * 当如果productList中的产品有一个扣减库存失败，那么要将库存已经已经扣减成功的产品, 回滚库存。
			 * 因redis不支持事务，不能回滚。
			 */
			List<Product> productIntoDBList = new ArrayList<Product>();
			
			//减库存
			for (Product product : productList) {
				//下单库存扣减（可用库存）
				GoodsQuantityVo goodsQuantity = new GoodsQuantityVo();
				goodsQuantity.setEnable_quantity(product.getNum());
				goodsQuantity.setGoods_id(product.getGoods_id());
				goodsQuantity.setQuantity(0);
				goodsQuantity.setSku_id(product.getProduct_id());
				
				if(this.goodsQuantityManager.reduceGoodsQuantity(goodsQuantity)){
					productIntoDBList.add(product);
				}else{
					flag = false;
				}
			}
			
			//如果扣减库存出现失败，则回滚库存
			if(!flag && !productIntoDBList.isEmpty()){
				
				StringBuffer products = new StringBuffer();
				for(Product product : productIntoDBList){
					//库存回滚（可用库存）
					GoodsQuantityVo goodsQuantity = new GoodsQuantityVo();
					goodsQuantity.setEnable_quantity(product.getNum());
					goodsQuantity.setGoods_id(product.getGoods_id());
					goodsQuantity.setQuantity(0);
					goodsQuantity.setSku_id(product.getProduct_id());
					this.goodsQuantityManager.addGoodsQuantity(goodsQuantity);
					products.append(product.getProduct_id()+",");
					
				}
				
				String productIds = products.substring(0, products.length()-1).toString();

				//修改为非上面产品id的出库状态
				this.daoSupport.execute("update es_order_items set state=1 where order_sn=? and product_id not in("+productIds+")", order.getSn());
				order.setOrder_status(OrderStatus.INTODB_ERROR.value());
			}
			
		} catch (Exception e) {
			order.setOrder_status(OrderStatus.INTODB_ERROR.value());
			logger.error("产品出库失败",e);
		}
		
	}


}
