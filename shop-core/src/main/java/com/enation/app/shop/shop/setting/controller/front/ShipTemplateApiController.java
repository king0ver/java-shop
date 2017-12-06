package com.enation.app.shop.shop.setting.controller.front;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.app.shop.shop.setting.model.po.ShipTemplate;
import com.enation.app.shop.shop.setting.service.IShipTemplateManager;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.StringUtil;
import com.enation.framework.validator.UnProccessableServiceException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * 运费模版控制器
 * 
 * @author Chopper
 * @version v1.0
 * @since pangu1.0 2017年5月11日 下午2:04:04
 *
 */
@RestController
@RequestMapping("/shop")
@Api(description = "运费模版接口")
public class ShipTemplateApiController {

	@Autowired
	private IShipTemplateManager shipTemplateManager;

	@Autowired
	private ISellerManager sellerManager;

	/**
	 * 添加店铺
	 * 
	 * @param shipTemplate
	 * @return
	 */
	@ApiOperation(value = "添加运费模版")
	@RequestMapping(value = "/seller/ship-template", method = RequestMethod.POST)
	public JsonResult save(ShipTemplate shipTemplate,String area) {
		Seller seller = sellerManager.getSeller(); 
		if (seller == null) {
			throw new UnProccessableServiceException("no_login", "未登录，请稍后重试");
		}
		shipTemplate.setSeller_id(seller.getStore_id());
		if(!StringUtil.isEmpty(area)) {
			shipTemplate.putArea(area);
			shipTemplate.setArea_json(area);
		}
		shipTemplateManager.save(shipTemplate);
		return JsonResultUtil.getSuccessJson("成功");
	}

	/**
	 * 模版编辑
	 * 
	 * @param shipTemplate
	 * @param template_id
	 * @return
	 */
	@ApiOperation(value = "编辑运费模版")
	@ApiImplicitParam(name = "template_id", value = "模版id", required = true, paramType = "path", dataType = "int")
	@RequestMapping(value = "/seller/ship-template/edit", method = RequestMethod.POST)
	public JsonResult edit(ShipTemplate shipTemplate, int template_id,String area) {
		ShipTemplate tpl = shipTemplateManager.getOne(template_id);
		if (sellerManager.verification(shipTemplateManager.getOne(template_id).getSeller_id())) {
			shipTemplate.setTemplate_id(template_id);
			if(!StringUtil.isEmpty(area)) {
				shipTemplate.putArea(area);
				shipTemplate.setArea_json(area);
			}
			shipTemplateManager.edit(shipTemplate);
			return JsonResultUtil.getSuccessJson("成功");
		} else {
			throw new UnProccessableServiceException("illegal_request", "非法请求或是非法参数");
		}
	}

	/**
	 * 删除运费模版
	 * 
	 * @param template_id
	 * @return
	 */
	@ApiOperation(value = "删除运费模版")
	@ApiImplicitParam(name = "template_id", value = "模版id", required = true, paramType = "query", dataType = "int")
	@RequestMapping(value = "/seller/ship-template", method = RequestMethod.DELETE)
	public JsonResult delete(Integer template_id) {
		if (sellerManager.verification(shipTemplateManager.getOne(template_id).getSeller_id())) {
			shipTemplateManager.delete(template_id);
			return JsonResultUtil.getSuccessJson("成功");
		} else {
			throw new UnProccessableServiceException("illegal_request", "非法请求或是非法参数");
		}
	}

	/**
	 * 获取店铺所有运费模版
	 * 
	 * @return
	 */
	@ApiOperation(value = "获取运费模版")
	@RequestMapping(value = "/seller/ship-template", method = RequestMethod.GET)
	public List<ShipTemplate> getAll() {
		Seller seller = sellerManager.getSeller(); 
		if (seller == null) {
			throw new UnProccessableServiceException("no_login", "未登录，请稍后重试");
		} 
		return shipTemplateManager.getStoreTemplate(seller.getStore_id());
	}

	/**
	 * 获取店铺指定运费模版
	 * 
	 * @param template_id
	 *            模版id
	 * @return
	 */
	@ApiOperation(value = "获取指定运费模版")
	@ApiImplicitParam(name = "template_id", value = "模版id", required = false, paramType = "path", dataType = "int")
	@RequestMapping(value = "/seller/ship-template/{template_id}", method = RequestMethod.GET)
	public List<ShipTemplate> get(@PathVariable("template_id") Integer template_id) {
		Seller seller = sellerManager.getSeller(); 
		if (seller == null) {
			throw new UnProccessableServiceException("no_login", "未登录，请稍后重试");
		} 
		return shipTemplateManager.getStoreTemplate(template_id);
	}

}
