package com.enation.app.cms.floor.service;

import java.util.List;

import com.enation.app.cms.floor.model.floor.FloorPo;
import com.enation.app.cms.floor.model.floor.FrontFloor;
import com.enation.app.cms.floor.model.vo.Floor;
import com.enation.app.cms.floor.model.vo.FloorAnchor;
import com.enation.app.cms.floor.model.vo.MobilePanel;
import com.enation.app.cms.floor.model.vo.Panel;
import com.enation.framework.database.Page;

/**
 * 
 * cms系统首页楼层管理接口
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0 2017年8月12日 下午1:14:18
 */
public interface IFloorManager {

	/**
	 * 添加楼层
	 * 
	 * @param floorPo
	 *            楼层模型
	 */
	void addFloor(FloorPo floorPo);

	/**
	 * 根据id删除楼层
	 * 
	 * @param id
	 *            楼层主键
	 */
	void delete(Integer id);

	/**
	 * 更新楼层
	 * 
	 * @param cmsFloor
	 *            楼层模型
	 */
	void updateFloor(FloorPo cmsFloor);

	/**
	 * 获取楼层
	 * 
	 * @param id
	 *            楼层主键
	 * @return FloorPo 楼层模型
	 */
	FloorPo getCmsFloor(Integer id);

	/**
	 * 获取楼层
	 * 
	 * @return FloorPo 楼层模型
	 */
	List<FloorPo> getCmsFloorAll();

	/**
	 * 分页获取楼层
	 * 
	 * @param page_no
	 *            当前页数
	 * @param page_size
	 *            分页大小
	 * @param order_by
	 *            排序依据
	 * @param order
	 *            正序反序
	 * @return
	 */
	Page getCmsFloorList(Integer page_no, Integer page_size, String order_by, String order, String client_type);

	/**
	 * 获取所有前台楼层
	 * 
	 * @param client_type
	 *            客户端类型
	 * @return FrontFloor集合
	 */
	List<FrontFloor> getAllCmsFrontFloor(String client_type);

	/**
	 * 根据楼层id获取内容信息
	 * 
	 * @param floor_id
	 *            楼层id
	 * 
	 * @return Floor 实体
	 */
	Floor getFloorDesignInfo(Integer floor_id);

	/**
	 * 获取移动端楼层id
	 * 
	 * @return 移动端楼层id
	 */
	Integer getMobileFloorId();

	/**
	 * 楼层名及锚点Json输出
	 * 
	 * @return
	 */
	List<FloorAnchor> getFloorAnchor(String client_type);

	/**
	 * app需要的楼层格式
	 * @param is_refresh 是否刷新缓存  true 刷新  false  不刷新
	 * @return
	 */
	List<MobilePanel> getAllCmsFrontMobilePanel(boolean is_refresh);
	
}
