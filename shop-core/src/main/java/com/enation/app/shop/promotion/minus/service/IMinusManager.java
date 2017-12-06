package com.enation.app.shop.promotion.minus.service;

import java.util.List;

import com.enation.app.shop.promotion.minus.model.vo.MinusVo;
import com.enation.framework.database.Page;

/**
 * 单品立减接口
 * 
 * @author mengyuanming
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月18日下午9:20:46
 *
 */
public interface IMinusManager {

	/**
	 * 获取单品立减商品列表
	 * 1. 编写sql语句，根据当前登录店铺查询es_minus里属于本店铺的所有单品立减活动
	 * 2. 查询数据库
	 * 
	 * @return List
	 */
	public List list();

	/**
	 * 分页获取单品立减商品列表
	 * 
	 * @param keyword
	 *            按title搜索商品
	 * @param shop_id
	 *            当前店铺id
	 * @param pageNO
	 *            起始页页码
	 * @param pageSize
	 *            每页显示数量
	 * 
	 * 1.验证keyword是否为空，不为空则重新赋值
	 * 2.拼接sql语句，按minus_id降序排列
	 * 3.查询数据库
	 * 
	 * @return Page
	 */
	public Page listJson(String keyword, Integer shop_id, Integer pageNO, Integer pageSize);

	/**
	 * 修改单品立减活动
	 * 
	 * @param minusVo
	 *            单品立减Vo
	 * 
	 * 1.获取店铺id，以及minusVo中的各项参数
	 * 2.判断是否为全部商品参与活动，并填充商品列表
	 * 3.填充minus实例，并填入数据库中es_minus表
	 * 4.将商品列表传入promotionGoodsManager,填充es_promotion_goods表
	 * 5.获取redis的key并将minusVo填入redis中
	 * 
	 */
	public void edit(MinusVo minusVo);

	/**
	 * 根据id获取单品立减商品
	 * 
	 * @param MinusId
	 *            单品立减活动Id
	 * @param shop_id
	 *            活动所属店铺Id
	 *            
	 * 1.获取店铺id及单品立减活动id
	 * 2.根据id获取reids的key
	 * 3.根据key获取minusVo实例对象
	 * 4.如果Redis中没有相关数据，在数据库中搜索数据
	 *            
	 * @return MinusVo
	 */
	public MinusVo get(Integer minusId);

	/**
	 * 根据id删除单品立减商品
	 * 
	 * @param minus_id
	 *            单品立减活动对象id
	 *            
	 * 1.根据活动id删除es_minus中的数据
	 * 2.调用promotionGoodsManager中的删除方法，删除es_promotion_goods表中的数据
	 * 3.删除Redis中的活动实例对象
	 *            
	 */
	public void delete(Integer minus_id);

	/**
	 * 增加单品立减商品
	 * 
	 * @param minusVo
	 *            单品立减活动对象
	 * 
	 * 1.判断是否为全部商品参与活动，并填充商品列表
	 * 2.获取minusVo中的参数，并填充minus实例对象
	 * 3.通过minus实例对象，将数据填充入数据库es_minus表中
	 * 4.获取数据库中新增的minus_id，填充入商品列表中
	 * 5.将商品列表传入promotionGoodsManager中，将数据填充入es_promotion_goods表中
	 * 6.获取redis的key，将新数据压入缓存
	 * 
	 */
	public void add(MinusVo minusVo);

	/**
	 * 活动是否过期
	 * 
	 * @param minusId
	 *            活动id
	 * 
	 * 1.获取系统时间和店铺id
	 * 2.根据活动id与店铺id获取redis的key，在redis中获取MinusVo实例
	 * 3.在minusVo中获取活动时间
	 * 4.将活动时间与系统时间进行对比，未过期则返回true，过期则返回false
	 * 
	 * @return boolean
	 */
	public boolean isOutOfDate(Integer minusId);

	/**
	 * 判断一件商品是否同时参与两个单品立减活动
	 * 
	 * @param minusVo
	 *            单品立减vo
	 * 
	 * 1.获取商品列表，如果为全部商品，则查询本店铺所有商品
	 * 2.获取商品的id的Integer类型数组
	 * 3.按商品id获取es_promotion_goods与es_minus中相关数据
	 * 4.判断是否有时间冲突，如果有，将图片、商品名、时间、活动名称加入返回值
	 * 
	 * @return List
	 */
	public List isRepetition(MinusVo minusVo,Long start_time,Long end_time);

	/**
	 * 判断修改后的时间是否缩短
	 * 
	 * @param minusVo
	 * 
	 * 1.查询此活动id从前的时间
	 * 2.查询页面传入的时间
	 * 3.判断页面传入的开始时间和结束时间是否在从前的时间段内
	 * 4.如果是，返回true，如果否，返回false
	 * 
	 * @return boolean
	 */
	public boolean checkTime(MinusVo minusVo);

	/**
	 * 判断活动是否已失效
	 * 
	 * @param minus_id
	 * 
	 * 1.获取系统时间
	 * 2.获取此活动过去的结束时间
	 * 3.将结束时间与系统时间对比
	 * 4.如果结束时间小于系统时间，返回true，否则返回false
	 * 
	 * @return boolean
	 */
	public boolean isAfterTime(Integer minus_id);

	/**
	 * 根据id获取单品立减商品
	 * 
	 * @param MinusId
	 *            单品立减活动Id
	
	 */
	public MinusVo getFromDB(Integer minusId);
}
