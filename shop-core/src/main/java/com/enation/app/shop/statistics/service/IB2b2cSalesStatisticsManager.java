package com.enation.app.shop.statistics.service;

import java.util.List;
import java.util.Map;

import com.enation.app.shop.statistics.model.StatisticsQueryParam;
import com.enation.framework.database.Page;

public interface IB2b2cSalesStatisticsManager {

	/**
	 * 订单统计   下单量 按月的统计
	 * @author LSJ
	 * @param order_status 订单状态
	 * @param year 年
	 * @param month 月
	 * @param store_id 店铺ID
	 * @return 下单量list
	 * 2016年12月6日下午15:16
	 */
	List<Map> statisticsMonth_Amount(String order_status, int year, int month, String store_id);

	/**
	 * 订单统计   下单量 按年的统计
	 * @author LSJ
	 * @param order_status 订单状态
	 * @param year 年
	 * @param store_id 店铺ID
	 * @return 下单量 list
	 * 2016年12月6日下午15:16
	 */
	List<Map> statisticsYear_Amount(String order_status, int year, String store_id);

	/**
	 * 销售收入金额统计
	 * @author LSJ
	 * @param statisticsQueryParam 统计查询参数
	 * @return 收款金额
	 * 2016年12月6日下午15:16
	 */
	Double getReceivables(StatisticsQueryParam statisticsQueryParam);

	/**
	 * 销售收入退款金额总计
	 * @author LSJ
	 * @param statisticsQueryParam 统计查询参数
	 * @return 退款金额
	 * 2016年12月6日下午15:16
	 */
	Double getRefund(StatisticsQueryParam statisticsQueryParam);
	
	/**
	 * 销售实收统计
	 * @param statisticsQueryParam 统计查询参数
	 * @return
	 */
	Double getPaid(StatisticsQueryParam statisticsQueryParam);

	/**
	 * 销售收入统计json数据
	 * @author LSJ
	 * @param year 年
	 * @param month 月
	 * @param page 当前页数
	 * @param pageSize 分页大小
	 * @param map
	 * @param store_id 店铺ID
	 * @return 销售收入统计json数据分页
	 * 2016年12月6日下午15:16
	 */
	Page getSalesIncome(int year, int month, int page, int pageSize, Map map, String store_id);

	/**
	 * 热卖商品排行—下单金额
	 * @author LSJ
	 * @return 热卖商品排行—下单金额list
	 * 2016年12月6日下午15:16
	 */
	List<Map> hotGoodsTop_Money();

	
	List<Map> hotGoodsTop_Num();

	/**
	 * @author LSJ
	 * @param orderMap
	 * @param page 当前页数
	 * @param pageSize 分页大小
	 * @param sort 排序
	 * @param order
	 * @return
	 * 2016年12月6日下午15:16
	 */
	Page listOrder(Map orderMap, int page, int pageSize, String sort, String order);

}
