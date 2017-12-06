package com.enation.app.shop.goods.service;

import java.util.List;
import java.util.Map;

import com.enation.app.shop.goods.model.po.DraftGoods;
import com.enation.app.shop.goods.model.vo.GoodsSkuVo;
import com.enation.app.shop.goods.model.vo.GoodsVo;
import com.enation.framework.database.Page;

/**
 * 
 * 商品草稿箱操作接口
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月27日 下午1:09:59
 */
public interface IDraftGoodsManager {
	/**
	 * 商品加入草稿箱
	 * 
	 * @param goodsVo
	 * @return
	 */
	public DraftGoods add(GoodsVo goodsVo);

	/**
	 * 草稿箱商品上架
	 * 
	 * @param goodsVo
	 * @return
	 */
	public GoodsVo addMarcket(GoodsVo goodsVo) throws Exception;

	/**
	 * 删除草稿箱的商品
	 * 
	 * @param goodsid
	 * @return
	 */
	public String delete(Integer[] goodsid);

	/**
	 * 修改草稿箱的商品
	 * 
	 * @param goodsVo
	 * @return
	 */
	public DraftGoods edit(GoodsVo goodsVo);

	/**
	 * 根据id得到草稿箱的商品
	 * 
	 * @param goodsid
	 * @return
	 */
	public DraftGoods get(Integer goodsid);

	/**
	 * 获取店铺草稿箱商品列表
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param map
	 * @return
	 */
	public Page draftGoodsList(Integer pageNo, Integer pageSize, Map map);

	/**
	 * 获取草稿箱商品的sku信息
	 * 
	 * @param goods_id
	 * @return
	 */
	public List<GoodsSkuVo> draftGoodsSkuList(Integer goods_id);

}
