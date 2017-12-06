package com.enation.app.shop.member.controller.front;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enation.app.shop.member.service.IPointHistoryManager;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.JsonResultUtil;

import io.swagger.annotations.ApiOperation;

/**
 * 
 * 积分查询API
 * @author zh
 * @version v1.0
 * @since v1.0
 * 2017年10月19日 下午3:54:14
 */
@RestController
@RequestMapping("/api/shop/point")
@Scope("prototype")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MemberPointApiContrller  extends GridController{

	@Autowired
	private IPointHistoryManager pointHistoryManager;

	@ApiOperation(value="获取会员积分" )
	@PostMapping(value="/get-point", produces=MediaType.APPLICATION_JSON_VALUE)
	public GridJsonResult getMemberPoing(Integer member_id , Integer type) {
		webpage = pointHistoryManager.getMemberPointHistoty(this.getPage(), this.getPageSize(), member_id, type);
		return JsonResultUtil.getGridJson(webpage);
	}
}
