package com.enation.app.cms.floor.controller;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.UnProccessableServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enation.app.cms.floor.model.floor.FloorDesign;
import com.enation.app.cms.floor.model.floor.PanelPo;
import com.enation.app.cms.floor.model.po.PanelTpl;
import com.enation.app.cms.floor.model.vo.Floor;
import com.enation.app.cms.floor.model.vo.Panel;
import com.enation.app.cms.floor.service.IFloorContentManager;
import com.enation.app.cms.floor.service.IFloorManager;
import com.enation.app.cms.floor.service.IPanelTplManager;
import com.enation.framework.action.GridController;

import io.swagger.annotations.Api;

/**
 * 
 * 楼层装修相关api
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月17日 下午5:18:44
 */
@RestController
@RequestMapping("/cms/admin")
@Api(description = "楼层装修相关api")
@Validated
public class FloorDesignController extends GridController {
	@Autowired
	private IFloorManager floorManager;
	@Autowired
	private IFloorContentManager floorContentManager;
	@Autowired
	private IPanelTplManager panelTplManager;

	/**
	 * PC端装修数据获取
	 * 
	 * @param floor_id
	 *            维护楼层的id
	 * @return Floor 对象
	 */
	@GetMapping(value = "/floor/pc/{floor_id}/design")
	public Floor getDesignInfo(@PathVariable Integer floor_id) {
		return this.floorManager.getFloorDesignInfo(floor_id);
	}

	/**
	 * Mobile端装修数据获取
	 * 
	 * @return Floor 对象
	 */
	@GetMapping(value = "/floor/mobile/design")
	public Floor getMobileDesignInfo() {
		Integer floor_id = this.floorManager.getMobileFloorId();
		return this.floorManager.getFloorDesignInfo(floor_id);
	}

	/**
	 * 楼层装修数据保存(PC、Mobile共用）
	 * 
	 * @param FloorDesign
	 *            模型
	 * @return FloorDesign 模型
	 */
	@PostMapping("/floor/design")
	public FloorDesign saveDesign(@RequestBody FloorDesign floor) {
		//判断是否为演示站点
		AdminUser user  = UserConext.getCurrentAdminUser();
		if(EopSetting.IS_DEMO_SITE && user!= null && user.getFounder() != 1){
			throw new UnProccessableServiceException(ErrorCode.DEMO_SITE_TIP, EopSetting.DEMO_SITE_TIP);
		}
		this.floorContentManager.design(floor);

		return floor;
	}

	/**
	 * 为楼层新增面板（PC、Mobile共用）
	 * 
	 * @param PanelPo
	 *            模型
	 * @return PanelPo 模型
	 */
	@PostMapping("/floor/panel")
	public PanelPo savePanel(PanelPo panel) {
		//判断是否为演示站点
		AdminUser user  = UserConext.getCurrentAdminUser();
		if(EopSetting.IS_DEMO_SITE && user!= null && user.getFounder() != 1){
			throw new UnProccessableServiceException(ErrorCode.DEMO_SITE_TIP, EopSetting.DEMO_SITE_TIP);
		}
		panel = this.floorContentManager.addPanel(panel);

		return panel;
	}

	/**
	 * 修改楼层面板（PC、Mobile共用）
	 * 
	 * @param id
	 *            板块id
	 * @param panel
	 *            对象
	 * @return panel 对象
	 */
	@PostMapping("/floor/panel/{id}")
	public Panel updatePanel(@NotNull(message = "必须输入板块id") @PathVariable Integer id, Panel panel) {
		//判断是否为演示站点
		AdminUser user  = UserConext.getCurrentAdminUser();
		if(EopSetting.IS_DEMO_SITE && user!= null && user.getFounder() != 1){
			throw new UnProccessableServiceException(ErrorCode.DEMO_SITE_TIP, EopSetting.DEMO_SITE_TIP);
		}
		panel.setId(id);
		this.floorContentManager.updatePanelName(panel);

		return panel;
	}

	/**
	 * 删除楼层面板（PC、Mobile共用）
	 * 
	 * @param id 板块id
	 */
	@DeleteMapping("/floor/panel/{id}")
	public void deletePanel(@NotNull(message = "必须输入板块id") @PathVariable Integer id) {
		//判断是否为演示站点
		AdminUser user  = UserConext.getCurrentAdminUser();
		if(EopSetting.IS_DEMO_SITE && user!= null && user.getFounder() != 1){
			throw new UnProccessableServiceException(ErrorCode.DEMO_SITE_TIP, EopSetting.DEMO_SITE_TIP);
		}
		this.floorContentManager.deleteFromById(id);

	}
	
	/**
	 * 板块排序
	 * @param id
	 * @param sort
	 * @return
	 */
	@PostMapping("/floor/panel/{id}/sort")
	public Panel panelSort(@NotNull(message = "必须输入板块id") @PathVariable Integer id, String sort){
		//判断是否为演示站点
		AdminUser user  = UserConext.getCurrentAdminUser();
		if(EopSetting.IS_DEMO_SITE && user!= null && user.getFounder() != 1){
			throw new UnProccessableServiceException(ErrorCode.DEMO_SITE_TIP, EopSetting.DEMO_SITE_TIP);
		}
		this.floorContentManager.updatePanelSort(id,sort);
		
		return null;
	}

	/**
	 * PC楼层面板列表获取
	 * 
	 * @return PanelTpl集合
	 */
	@GetMapping(value = "/panel-tpl/pc/normal")
	public List<PanelTpl> getPcNormalPanel() {
		return panelTplManager.getNormal();
	}

	/**
	 * Mobile楼层面板列表获取
	 * 
	 * @return PanelTpl 集合
	 */
	@GetMapping(value = "/panel-tpl/mobile")
	public List<PanelTpl> getMobilePanel() {
		String client_type = "mobile";
		return panelTplManager.getMain(client_type);
	}
}
