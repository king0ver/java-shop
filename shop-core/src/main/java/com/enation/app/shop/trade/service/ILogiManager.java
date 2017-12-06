package com.enation.app.shop.trade.service;

import java.util.List;

import com.enation.app.shop.trade.model.po.Logi;
import com.enation.framework.database.Page;
/**
 * 物流公司管理类
 * @author fenlongli
 *
 */
public interface ILogiManager {
	/**
	 * 添加物流公司
	 * @param logi 物流公司实体
	 */
	public void saveAdd(Logi logi);
	
	/**
	 * 编辑物流公司
	 * @param logi 物流公司实体
	 */
	public void saveEdit(Logi logi);
	
	/**
	 * 分页读取物流公司
	 * @param order 订单实体
	 * @param page 当前页数
	 * @param pageSize 总页数
	 * @return  物流公司
	 */
	public Page pageLogi(String order ,Integer page,Integer pageSize);
	
	/**
	 * 读取所有物流公司列表
	 * @return 所有物流公司列表
	 */
	public List list();
	
	
	/**
	 * 删除物流公司
	 * @param logi_id  物流公司数组id
	 */
	public void delete(Integer[] logi_id);
	
	/**
	 * 通过id获取物流公司
	 * @param id  物流公司id
	 * @return 物流公司
	 */
	public Logi getLogiById(Integer id);

	/**
	 * 通过code获取物流公司
	 * @param code 物流公司code
	 * @return 物流公司
	 */
	public Logi getLogiByCode(String code);
	/**
	 * 通过快递鸟物流code获取物流公司
	 * @param code 物流公司code
	 * @return 物流公司
	 */
	public Logi getLogiBykdCode(String kdcode);
	
	/**
	 * 根据物流名称查询物流信息
	 * @param name 物流名称
	 * @return 物流公司
	 */
	public Logi getLogiByName(String name);
	
}
