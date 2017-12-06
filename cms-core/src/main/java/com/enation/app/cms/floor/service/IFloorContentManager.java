package com.enation.app.cms.floor.service;

import com.enation.app.cms.floor.model.floor.FloorDesign;
import com.enation.app.cms.floor.model.floor.PanelPo;
import com.enation.app.cms.floor.model.vo.Panel;

/**
 * 
 * 楼层内容管理接口
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月12日 下午1:13:48
 */
public interface IFloorContentManager {

	/**
	 * 根据楼层id删除楼层内容
	 * 
	 * @param id
	 */
	void deleteByFloorId(Integer id);

	/**
	 * 楼层内容设计
	 * @param floorDesign 楼层设计模型
	 * @return FloorDesign对象
	 */
	FloorDesign design(FloorDesign floorDesign);

	/**
	 * 删除板块
	 * 
	 * @param id 模版主键
	 */
	void deleteFromById(Integer id);

	/**
	 * 修改板块
	 * 
	 * @param panel 模版实体
	 */
	void updatePanelName(Panel panel);

	/**
	 * 添加楼层板块
	 * 
	 * @param panel 模版实体
	 * @return PanelPo 模版实体
	 */
	PanelPo addPanel(PanelPo panel);

	/**
	 * 板块排序
	 * @param id
	 * @param sort
	 */
	PanelPo updatePanelSort(Integer id, String sort);

}
