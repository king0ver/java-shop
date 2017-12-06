package com.enation.app.shop.member.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enation.app.shop.member.service.IMemberAddressManager;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.util.JsonResultUtil;

import io.swagger.annotations.ApiOperation;
/**
 * 
 * 会员地址控制器 
 * @author zh
 * @version v1.0
 * @since v1.0
 * 2017年10月19日 下午4:38:43
 */
@RestController
@RequestMapping("/shop/admin/address")
@Scope("prototype")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MemberAddressController  extends GridController{
	@Autowired
	private IMemberAddressManager memberAddressManager;

	@ApiOperation(value="获取会员地址" )
	@PostMapping(value="/get-member-address", produces=MediaType.APPLICATION_JSON_VALUE)
	public GridJsonResult getMemberPoing(Integer member_id) {
		webpage = memberAddressManager.getMemberAddress(this.getPage(), this.getPageSize(), member_id);
		return JsonResultUtil.getGridJson(webpage);
	}
}
