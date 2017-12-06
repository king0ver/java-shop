package com.enation.app.shop.goods.service;


import java.util.Map;
import java.util.List;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.goods.model.po.Goods;
import com.enation.app.shop.goods.model.vo.GoodsQueryParam;
import com.enation.app.shop.goods.model.vo.GoodsSkuVo;
import com.enation.app.shop.goods.model.vo.GoodsVo;
import com.enation.framework.database.Page;

/**
 * 关于商品的接口
 *
 * @author yanlin
 * @version v1.0
 * @date 2017年9月28日 下午1:32:35
 * @since v6.4.0
 */
public interface IGoodsManager {

    /**
     * 添加商品
     *
     * @param goodsVo
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void add(GoodsVo goodsVo) throws Exception;

    /**
     * 修改商品
     *
     * @param goodsVo
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void edit(GoodsVo goodsVo) throws Exception;

    /**
     * 删除商品
     *
     * @param goods_id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Integer[] goods_id) throws Exception;

    /**
     * 商品下架
     *商家和管理员共用
     * @param goods_id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void under(Integer[] goods_id,String message);

    /**
     * 商品上架
     *
     * @param goods_id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void up(Integer[] goods_id);

    /**
     * 商品还原
     *
     * @param goods_ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void revert(Integer[] goods_ids);

    /**
     * 将商品放入回收站
     *
     * @param goods_ids
     */
    public void inRecycle(Integer[] goods_ids);

    /**
     * 管理员审核商品
     *
     * @param goods_id
     * @param pass
     * @param message
     * @return
     */
    public GoodsVo authStoreGoods(Integer goods_id, Integer pass, String message);

    /**
     * 获得商品vo
     *
     * @param goods_id
     * @return
     */
    public GoodsVo getFromCache(Integer goods_id);

    /**
     * 获得商品
     *
     * @param goods_id
     * @return
     */
    public Goods getFromDB(Integer goods_id);

    /**
     * 查询该范围下的该卖家下的商品个数
     *
     * @param goodsId
     * @param seller_id
     * @return
     */
    public Integer getCountByGoodsIds(Integer[] goodsId, Integer seller_id);

    /**
     * 后台查看商品列表
     *
     * @param goodsQueryParam
     * @return
     */
    public Page list(GoodsQueryParam goodsQueryParam);

    /**
     * 商品浏览数累加功能实现
     *
     * @param goods_id 商品id
     */
    public void visitedGoodsNum(Integer goods_id);

    /**
     * 店铺设置插件使用，系统设置把所有商品置为可审核或不需要审核时调用
     */
    public void editAllGoodsAuthAndMarketenable(Integer selfOrstore);

    /**
     * 更新商品的评分
     *
     * @param goods_id 商品id
     */
    public void editStoreGoodsGrade(Integer goods_id);
    public void addStoreGoodsComment(Integer goods_id);
    /**
	 * 获取店铺商品列表
	 * @param pageNo
	 * @param pageSize
	 * @param map
	 * @return
	 */
	public Page shopGoodsList(Integer pageNo,Integer pageSize,Map map);
	/**
	 * 店铺商品列表
	 * @param page
	 * @param pageSize
	 * @param map
	 * @return
	 */
	public Page shopSearchGoodsList(Integer page,Integer pageSize,Map map);


    /**
     * 商品列表
     * @param catid 分类Id
     * @param tagid 标签Id
     * @param goodsnum 数量
     * @return List
     */
    public List listGoods(String catid,String tagid,String goodsnum);
    
	/**
	 * 获取店铺预警商品列表
	 * @param pageNo
	 * @param pageSize
	 * @param map
	 * @return
	 */
	public Page storeWarningGoodsList(Integer pageNo,Integer pageSize,Map map);
	/**
	 * 查询某个商品的预警货品
	 * 
	 * @param goods_id 商品id
	 * @param store_id 店铺id
	 * @return
	 */
	
	public List<GoodsSkuVo> warningGoodsList(Integer goods_id,Integer shop_id);
	
}
