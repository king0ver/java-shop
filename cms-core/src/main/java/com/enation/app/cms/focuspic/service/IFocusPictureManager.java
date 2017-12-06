package com.enation.app.cms.focuspic.service;

import java.util.List;

import com.enation.app.cms.focuspic.model.po.CmsFocusPicture;
import com.enation.app.cms.focuspic.model.vo.CmsFrontFocusPicture;
/**
 * 
 * 焦点图管理接口 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月14日 上午11:07:37
 */
public interface IFocusPictureManager {

	/**
	 * 添加焦点图
	 * @param cmsFocusPicture 实体
	 * @return CmsFocusPicture实体
	 */
	CmsFocusPicture addFocus(CmsFocusPicture cmsFocusPicture);

	/**
	 * 删除焦点图
	 * @param id 主键
	 */
	void deleteFocus(Integer id);

	/**
	 * 更新焦点图
	 * @param cmsFocusPicture 实体
	 * @return CmsFocusPicture 实体
	 */
	CmsFocusPicture updateFocus(CmsFocusPicture cmsFocusPicture);

	/**
	 * 获取焦点图
	 * @param id 主键
	 * @return CmsFocusPicture 模型
	 */
	CmsFocusPicture getFocus(Integer id);

	/**
	 * 后台获取焦点图列表
	 * @param client_type 客户端类型
	 * @return CmsFocusPicture 集合
	 */
	List<CmsFocusPicture> getAllFocusList(String client_type);

	/**
	 * 前台获取焦点图
	 * @return  CmsFrontFocusPicture 集合
	 */ 
	List<CmsFrontFocusPicture> getCmsFrontFocusPicture(String client_type);



}
