package com.enation.app.shop.promotion.fulldiscount.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.promotion.fulldiscount.model.po.FullDiscountGift;
import com.enation.app.shop.promotion.fulldiscount.service.IFullDiscountGiftManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.eop.SystemSetting;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.StringUtil;

/**
 * 
 * 满优惠促销赠品控制器 
 * @author zjp
 * @version v1.0
 * @since v6.4.0
 * 2017年9月5日 下午3:41:31
 */
@Controller
@Scope("prototype")
@RequestMapping("/api/shop/promotion/filldiscountgift")
public class FullDiscountGiftApiController {
protected final Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private IFullDiscountGiftManager fullDiscountGiftManager;
	
	@Autowired
	private ISellerManager sellerManager;
	
	/**
	 * 保存新增赠品信息
	 * @param storeActivityGift 新增赠品信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/save-add", produces=MediaType.APPLICATION_JSON_VALUE)
	public JsonResult saveAdd(FullDiscountGift fullDiscountGift){
		try {
			//赠品名称不能为空
			if (StringUtil.isEmpty(fullDiscountGift.getGift_name())) {
				return JsonResultUtil.getErrorJson("请填写赠品名称");
			}
			
			//赠品价格不能为空也不能为0
			if (fullDiscountGift.getGift_price() == null || fullDiscountGift.getGift_price() == 0) {
				return JsonResultUtil.getErrorJson("赠品价格不能为空也不能为0");
			}
			
			//赠品价格不能为空也不能为0
			if (fullDiscountGift.getActual_store() == null || fullDiscountGift.getActual_store() == 0) {
				return JsonResultUtil.getErrorJson("赠品库存不能为空也不能为0");
			}
			
			//判断是否上传了赠品图片
			if (StringUtil.isEmpty(fullDiscountGift.getGift_img())) {
				return JsonResultUtil.getErrorJson("请上传赠品图片");
			}
			
			//设置赠品可用库存(添加时可用库存=实际库存)
			fullDiscountGift.setEnable_store(fullDiscountGift.getActual_store());
			
			//添加赠品创建时间
			fullDiscountGift.setCreate_time(DateUtil.getDateline());
			
			//设置赠品类型(1.0版本赠品类型默认为0：普通赠品)
			fullDiscountGift.setGift_type(0);
			
			//默认不禁用
			fullDiscountGift.setDisabled(0);
			
			//设置赠品所属店铺id
			fullDiscountGift.setShop_id(sellerManager.getSeller().getStore_id());
			
			this.fullDiscountGiftManager.add(fullDiscountGift);
			return JsonResultUtil.getSuccessJson("添加成功！");
		} catch (Exception e) {
			this.logger.error("添加失败：", e);
			return JsonResultUtil.getErrorJson("添加失败！");
		}
		
	}
	
	/**
	 * 保存修改赠品信息
	 * @param storeActivityGift 修改后的赠品信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/save-edit", produces=MediaType.APPLICATION_JSON_VALUE)
	public JsonResult saveEdit(FullDiscountGift fullDiscountGift){
		try {
			FullDiscountGift gift = this.fullDiscountGiftManager.get(fullDiscountGift.getGift_id());
			//增加权限
			Seller member = sellerManager.getSeller();
			if(gift == null || !gift.getShop_id().equals(member.getStore_id())){
				return JsonResultUtil.getErrorJson("您没有权限");
			}
			
			//赠品名称不能为空
			if (StringUtil.isEmpty(fullDiscountGift.getGift_name())) {
				return JsonResultUtil.getErrorJson("请填写赠品名称");
			}
			
			//赠品价格不能为空也不能为0
			if (fullDiscountGift.getGift_price() == null || fullDiscountGift.getGift_price() == 0) {
				return JsonResultUtil.getErrorJson("赠品价格不能为空也不能为0");
			}
			
			//赠品价格不能为空也不能为0
			if (fullDiscountGift.getActual_store() == null || fullDiscountGift.getActual_store() == 0) {
				return JsonResultUtil.getErrorJson("赠品库存不能为空也不能为0");
			}
			
			//判断是否上传了赠品图片
			if (StringUtil.isEmpty(fullDiscountGift.getGift_img())) {
				return JsonResultUtil.getErrorJson("请上传赠品图片");
			}
			
			//获取赠品原实际库存和原可用库存的差
			int differ = gift.getActual_store() - gift.getEnable_store();
			
			//设置赠品修改后的可用库存
			fullDiscountGift.setEnable_store(fullDiscountGift.getActual_store() - differ);
			
			this.fullDiscountGiftManager.edit(fullDiscountGift);
			return JsonResultUtil.getSuccessJson("修改成功！");
		} catch (Exception e) {
			this.logger.error("修改失败：", e);
			return JsonResultUtil.getErrorJson("修改失败！");
		}
		
	}
	
	/**
	 * 删除赠品信息
	 * @param gift_id 赠品id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/delete", produces=MediaType.APPLICATION_JSON_VALUE)
	public JsonResult delete(Integer gift_id){
		try {
			//增加权限
			FullDiscountGift gift = this.fullDiscountGiftManager.get(gift_id);
			Seller member = sellerManager.getSeller();
			if(gift == null || !gift.getShop_id().equals(member.getStore_id())){
				return JsonResultUtil.getErrorJson("您没有权限");
			}
			
			int result = this.fullDiscountGiftManager.checkGiftInAct(gift_id);
			
			//如果此赠品没有参与促销活动，可以删除，如过参与了，不可删除
			if (result == 0) {
				this.fullDiscountGiftManager.delete(gift_id);
				return JsonResultUtil.getSuccessJson("赠品删除成功！");
			} else {
				return JsonResultUtil.getErrorJson("此赠品已经参与了促销活动，不可删除！");
			}
			
		} catch (Exception e) {
			this.logger.error("删除失败：", e);
			e.printStackTrace();
			return JsonResultUtil.getErrorJson("赠品删除失败！");
		}
		
	}
	
	/**
	 * 页面中传递过来的图片地址为:http://<staticserver>/<image path>
	 * 如:http://static.enationsoft.com/attachment/goods/1.jpg
	 * 存在库中的为fs:/attachment/goods/1.jpg 生成fs式路径
	 * @param path 原地址路径
	 * @return
	 */
	private String transformPath(String path) {
		String static_server_domain= SystemSetting.getStatic_server_domain();

		String regx =static_server_domain;
		path = path.replace(regx, EopSetting.FILE_STORE_PREFIX);
		return path;
	}
	
	
}
