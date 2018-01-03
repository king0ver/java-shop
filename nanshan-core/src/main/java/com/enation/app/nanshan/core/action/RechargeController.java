package com.enation.app.nanshan.core.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.nanshan.core.model.RechargeRecordVo;
import com.enation.app.nanshan.core.service.IRechargeManager;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.database.Page;
import com.enation.framework.util.JsonResultUtil;

/**
 * 充值服务
 * @author jianjianming
 * @version $Id: RechargeController.java,v 0.1 2018年1月3日 下午3:09:18$
 */
@Controller 
@Scope("prototype")
@RequestMapping("/core/admin/recharge")
public class RechargeController extends GridController{
	
	@Autowired
	private IRechargeManager rechargeManager;
	
	/**
	 * 跳转到管理列表
	 * @return
	 */
	@RequestMapping(value="/list")
	public String list(){
		return "/nanshan/admin/recharge/list";
	}
	
	/**
	 * 获取列表JSON
	 * @param keyword 关键字
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/list-json")
	public GridJsonResult listJson(){
		Page<RechargeRecordVo> page = rechargeManager.queryRechargeRecordList(null, this.getPage(), this.getPageSize());
		return JsonResultUtil.getGridJson(page);
	}
	
}
