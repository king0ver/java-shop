package com.enation.app.shop.snapshot.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.goods.model.po.Brand;
import com.enation.app.shop.goods.model.po.Category;
import com.enation.app.shop.goods.model.po.Goods;
import com.enation.app.shop.goods.model.po.GoodsGallery;
import com.enation.app.shop.goods.model.vo.GoodsParamsList;
import com.enation.app.shop.goods.service.IBrandManager;
import com.enation.app.shop.goods.service.ICategoryManager;
import com.enation.app.shop.goods.service.ICategoryParamsManager;
import com.enation.app.shop.goods.service.IGoodsGalleryManager;
import com.enation.app.shop.goods.service.IGoodsManager;
import com.enation.app.shop.snapshot.model.po.Snapshot;
import com.enation.app.shop.snapshot.service.ISnapshotManager;
import com.enation.app.shop.trade.model.po.OrderPo;
import com.enation.app.shop.trade.model.vo.Product;
import com.enation.app.shop.trade.service.IOrderItemManager;
import com.enation.app.shop.trade.service.IOrderOperateManager;
import com.enation.framework.database.IDaoSupport;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

@Service
public class SnapshotManager implements ISnapshotManager{
	@Autowired
	IDaoSupport daoSupport;
	
	@Autowired
	IGoodsManager goodsManager;
	
	@Autowired
	ICategoryParamsManager categoryParamsManager;
	
	@Autowired
	IGoodsGalleryManager goodsGalleryManager;
	
	@Autowired
	IOrderOperateManager orderOperateManager;
	
	@Autowired
	IBrandManager brandManager;
	
	@Autowired
	ICategoryManager categoryManager;
	
	@Autowired
	IOrderItemManager orderItemQueryManager;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void add(OrderPo orderPo) {
		//将json数据转换成对象
				String items_json = orderPo.getItems_json();
				Gson gson = new Gson();
				List<Product> list = gson.fromJson(items_json, new TypeToken<List<Product>>() {}.getType());
				Snapshot snapshot = new Snapshot();
				for (Product product : list) {
					//获取商品信息
					Integer goods_id = product.getGoods_id();
					Goods goods = goodsManager.getFromDB(goods_id);
					List<GoodsParamsList> params = categoryParamsManager.getParamByCatAndGoods(goods.getCategory_id(), goods_id);
					String paramsJson = gson.toJson(params);
					List<GoodsGallery> galleries = goodsGalleryManager.list(goods_id);
					String galleryJson = gson.toJson(galleries);
					if(goods.getBrand_id()!=null&&goods.getBrand_id()!=0) {
						Brand brand = brandManager.get(goods.getBrand_id());
						snapshot.setBrand_name(brand.getName());
					}
					
					if(goods.getHave_spec()!=null) {
						snapshot.setHave_spec(goods.getHave_spec());
					}
					
					Category category = categoryManager.get(goods.getCategory_id());
					
					//将商品信息存入快照表
					snapshot.setGoods_id(goods_id);
					snapshot.setName(goods.getGoods_name());
					snapshot.setSn(goods.getSn());
					snapshot.setCategory_name(category.getName());
					snapshot.setGoods_type(goods.getGoods_type());
					snapshot.setUnit(goods.getUnit());
					snapshot.setWeight(goods.getWeight());
					snapshot.setIntro(goods.getIntro());
					snapshot.setPrice(goods.getPrice());
					snapshot.setCost(goods.getCost());
					snapshot.setMktprice(goods.getMktprice());
					snapshot.setParams_json(paramsJson);
					snapshot.setImg_json(galleryJson);
					snapshot.setPoint(goods.getPoint());
					snapshot.setShop_id(goods.getSeller_id());
					
					//存入数据库
					this.daoSupport.insert("es_goods_snapshot", snapshot);
					
					//获取快照ID,填充信息
					Integer snapshot_id =  this.daoSupport.getLastId("es_goods_snapshot");
					product.setSnapshot_id(snapshot_id);
					
					//更新订单项信息
					orderItemQueryManager.updateItemSnapshotId(orderPo.getSn(), product.getProduct_id(), snapshot_id);
					
				}
				//更新订单状态
				orderOperateManager.updateItemJson(gson.toJson(list),orderPo.getSn());
	}


	@Override
	public Snapshot get(Integer snapshot_id) {
		String sql = "select * from es_goods_snapshot where snapshot_id = ? ";
		return this.daoSupport.queryForObject(sql, Snapshot.class, snapshot_id);
	}

}
