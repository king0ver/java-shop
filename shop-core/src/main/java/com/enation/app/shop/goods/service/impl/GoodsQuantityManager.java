package com.enation.app.shop.goods.service.impl;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.goods.model.enums.GoodsCacheKey;
import com.enation.app.shop.goods.model.vo.GoodsQuantityVo;
import com.enation.app.shop.goods.model.vo.GoodsSkuVo;
import com.enation.app.shop.goods.model.vo.GoodsVo;
import com.enation.app.shop.goods.service.IGoodsManager;
import com.enation.app.shop.goods.service.IGoodsQuantityManager;
import com.enation.app.shop.goods.service.IGoodsSkuManager;
import com.enation.eop.sdk.config.redis.transactional.RedisTransactional;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.UnProccessableServiceException;

/**
 * 商品库存
 * 
 * @author fk
 * @version v1.0 2017年4月1日 下午12:03:08
 */
@Service
public class GoodsQuantityManager implements IGoodsQuantityManager {

	protected final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private IDaoSupport daoSupport;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private IGoodsSkuManager goodsSkuManager;

	@Autowired
	private IGoodsManager goodsManager;

	@Override
	@RedisTransactional
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean addGoodsQuantity(GoodsQuantityVo goodsQuantity) {

		Integer enable_quantity = goodsQuantity.getEnable_quantity();
		Integer quantity = goodsQuantity.getQuantity();
		Integer goods_id = goodsQuantity.getGoods_id();
		Integer sku_id = goodsQuantity.getSku_id();

		GoodsVo goods = goodsManager.getFromCache(goods_id);
		GoodsSkuVo skuVo = goodsSkuManager.getSkuFromCache(sku_id);

		// 原库存
		Integer sku_old_quantity = skuVo.getQuantity();
		Integer goods_old_quantity = goods.getQuantity();

		// 原可用库存
		Integer sku_old_enable_quantity = skuVo.getEnable_quantity();
		Integer goods_old_enable_quantity = goods.getEnable_quantity();
		// 进行Sku库存增加计算
		sku_old_quantity = sku_old_quantity + quantity;
		sku_old_enable_quantity = sku_old_enable_quantity + enable_quantity;

		// 进行商品库存增加计算
		goods_old_quantity = goods_old_quantity + quantity;
		goods_old_enable_quantity = goods_old_enable_quantity + enable_quantity;

		skuVo.setQuantity(sku_old_quantity);
		skuVo.setEnable_quantity(sku_old_enable_quantity);

		goods.setQuantity(goods_old_quantity);
		goods.setEnable_quantity(goods_old_enable_quantity);

		//更新goods中的skulist，以便更新缓存
		this.updateSkuVoList(goods,sku_id,sku_old_quantity,sku_old_enable_quantity);


		// 更新缓存
		this.redisTemplate.opsForValue().set(GoodsCacheKey.SKU.name() + sku_id, skuVo);
		this.redisTemplate.opsForValue().set(GoodsCacheKey.GOODS.name() + goods_id, goods);

		// 更新数据库
		this.daoSupport.execute("update es_goods set quantity=?, enable_quantity=? where goods_id=? ",
				goods_old_quantity, goods_old_enable_quantity, goods_id);
		this.daoSupport.execute("update es_goods_sku set quantity = ? , enable_quantity = ? where sku_id=?",
				sku_old_quantity, sku_old_enable_quantity, sku_id);
		return true;
	}

	private boolean innerReduceGoodsQuantity(GoodsQuantityVo goodsQuantity) {
		// 要扣减的可用库存
		Integer enable_quantity = goodsQuantity.getEnable_quantity();
		Integer quantity = goodsQuantity.getQuantity();
		Integer goods_id = goodsQuantity.getGoods_id();
		Integer sku_id = goodsQuantity.getSku_id();

		// 为空归零
		enable_quantity = enable_quantity == null ? 0 : enable_quantity;
		quantity = quantity == null ? 0 : quantity;

		GoodsVo goods = goodsManager.getFromCache(goods_id);
		GoodsSkuVo skuVo = goodsSkuManager.getSkuFromCache(sku_id);

		// 原库存
		Integer sku_old_quantity = skuVo.getQuantity();
		Integer goods_old_quantity = goods.getQuantity();

		// 原可用库存
		Integer sku_old_enable_quantity = skuVo.getEnable_quantity();
		Integer goods_old_enable_quantity = goods.getEnable_quantity();

		goods_old_enable_quantity = goods_old_enable_quantity == null ? 0 : goods_old_enable_quantity;
		goods_old_quantity = goods_old_quantity == null ? 0 : goods_old_quantity;

		// 库存和可用库存都充足
		if (sku_old_quantity >= quantity && sku_old_enable_quantity >= enable_quantity) {

			// 进行Sku库存扣减计算
			sku_old_quantity = sku_old_quantity - quantity;
			sku_old_enable_quantity = sku_old_enable_quantity - enable_quantity;

			// 进行商品库存扣减计算
			goods_old_quantity = goods_old_quantity - quantity;
			goods_old_enable_quantity = goods_old_enable_quantity - enable_quantity;

			skuVo.setQuantity(sku_old_quantity);
			skuVo.setEnable_quantity(sku_old_enable_quantity);

			goods.setQuantity(goods_old_quantity);
			goods.setEnable_quantity(goods_old_enable_quantity);


			//更新goods中的skulist，以便更新缓存
			this.updateSkuVoList(goods,sku_id,sku_old_quantity,sku_old_enable_quantity);

			// 更新缓存
			this.redisTemplate.opsForValue().set(GoodsCacheKey.SKU.name() + sku_id, skuVo);
			this.redisTemplate.opsForValue().set(GoodsCacheKey.GOODS.name() + goods_id, goods);

			// 更新数据库
			this.daoSupport.execute("update es_goods set quantity=?, enable_quantity=? where goods_id=? ",
					goods_old_quantity, goods_old_enable_quantity, goods_id);
			this.daoSupport.execute("update es_goods_sku set quantity = ? , enable_quantity = ? where sku_id=?",
					sku_old_quantity, sku_old_enable_quantity, sku_id);
			return true;
		} else {
			logger.info(("商品[goodsid:" + goods.getGoods_id() + "  goodsname:" + goods.getGoods_name() + " skuid:"
					+ skuVo.getSku_id() + "]库存不足 old_quantity:" + sku_old_quantity + " quantity:" + quantity));
			return false;
		}
	}


	/**
	 * 更新goods中的skuList 的库存
	 * @param goods 商品vo
	 * @param skuid 要更新的skuid
	 * @param sku_quantity 库存
	 * @param sku_enable_quantity 可用库存
	 */
	private void updateSkuVoList(GoodsVo goods,int skuid,int sku_quantity,int sku_enable_quantity){

		//对商品的skulist进行更新（这会对缓存进行更新）
		List<GoodsSkuVo> skuVoList = goods.getSkuList();
		for (GoodsSkuVo sku:skuVoList){
			if( skuid == sku.getSku_id()){
				sku.setQuantity(sku_quantity);
				sku.setEnable_quantity(sku_enable_quantity);
				break;
			}
		}

	}


	@Override
	@RedisTransactional
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean reduceGoodsQuantity(GoodsQuantityVo goodsQuantity) {
		try {
			return this.innerReduceGoodsQuantity(goodsQuantity);

		} catch (Exception e) {
			logger.error("库存扣减出现异常", e);
			return false;

		}

	}

	@Override
	@RedisTransactional
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateGoodsQuantity(List<GoodsQuantityVo> goodsQuantity) {
		if(goodsQuantity==null||goodsQuantity.size()==0||goodsQuantity.isEmpty()) {
			throw new UnProccessableServiceException(ErrorCode.INVALID_REQUEST_PARAMETER, "参数错误");
		}
		Integer quantity = 0;
		Integer enableQuantity = 0;
		// 先把库存信息存到库中
		for (GoodsQuantityVo quantityVo : goodsQuantity) {
			// 更新货品库存
			this.daoSupport.execute("update es_goods_sku set quantity=?,enable_quantity=? where sku_id=? ",
					quantityVo.getQuantity(), quantityVo.getEnable_quantity(), quantityVo.getSku_id());
			quantity = quantityVo.getQuantity() + quantity;
			enableQuantity = quantityVo.getEnable_quantity() + enableQuantity;
			//更新sku缓存
			GoodsSkuVo skuVo = goodsSkuManager.getSkuFromCache(quantityVo.getSku_id());
			skuVo.setQuantity(quantityVo.getQuantity());
			skuVo.setEnable_quantity(quantityVo.getEnable_quantity());
			this.redisTemplate.opsForValue().set(GoodsCacheKey.SKU.name() + quantityVo.getSku_id(), skuVo);
		}
		//把累加的库存和可用库存保存到商品表,并更新商品缓存
		this.daoSupport.execute("update es_goods set quantity=?, enable_quantity=? where goods_id=? ", quantity,
				enableQuantity, goodsQuantity.get(0).getGoods_id());
		GoodsVo goods = goodsManager.getFromCache(goodsQuantity.get(0).getGoods_id());
		this.redisTemplate.delete(GoodsCacheKey.GOODS.name() + goodsQuantity.get(0).getGoods_id());
	}
}
