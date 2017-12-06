package com.enation.app.shop.shop.apply.controller.front;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.shop.apply.model.po.Shop;
import com.enation.app.shop.shop.apply.model.po.ShopDetail;
import com.enation.app.shop.shop.apply.model.vo.ApplyStep1;
import com.enation.app.shop.shop.apply.model.vo.ApplyStep2;
import com.enation.app.shop.shop.apply.model.vo.ApplyStep3;
import com.enation.app.shop.shop.apply.model.vo.ApplyStep4;
import com.enation.app.shop.shop.apply.model.vo.ShopSetting;
import com.enation.app.shop.shop.apply.service.IShopManager;
import com.enation.app.shop.shop.rdesign.service.IShopWapThemesManager;
import com.enation.app.shop.shop.rdesign.service.IStoreThemesManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.action.JsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonResultUtil;

/**
 * 店铺管理API
 * @author LiFenLong
 *
 */
@Controller
@RequestMapping("/api/b2b2c/shop-api")
@Validated
public class ShopApiController {

	protected Logger logger = Logger.getLogger(getClass());

	@Autowired
	private IShopManager shopManager;
	@Autowired
	private ISellerManager sellerManager;
	@Autowired
	private IShopWapThemesManager shopWapThemesManager;
	
	@Autowired 
	private IStoreThemesManager storeThemesManager;
	
	/**
	 * 初始化店铺信息
	 */
	@ResponseBody
	@RequestMapping(value="/init",produces= MediaType.APPLICATION_JSON_VALUE)
	public JsonResult init(){
		this.shopManager.init();
		return JsonResultUtil.getSuccessJson("操作成功");
		

	}
	/**
	 * 申请开店第一步
	 * @param applyStep1 店铺信息
	 */
	@ResponseBody
	@RequestMapping(value="/step1",produces= MediaType.APPLICATION_JSON_VALUE)
	public JsonResult step1(ApplyStep1 applyStep1) {
		Member member = UserConext.getCurrentMember();
		Shop shop = this.shopManager.getShopByMember(member.getMember_id());
		/**判断会员是否登陆*/
		if(member==null) {
			return JsonResultUtil.getErrorJson("请先登录再进行操作");
		}
		/**判断是否拥有店铺*/
		if(shop==null) {
			return JsonResultUtil.getErrorJson("抱歉,您还没有店铺!");
		}
		shopManager.step1(applyStep1);
		return JsonResultUtil.getSuccessJson("操作成功");
	}
	/**
	 * 申请开店第二步
	 * @param applyStep2 店铺信息
	 */
	@ResponseBody
	@RequestMapping(value="/step2",produces= MediaType.APPLICATION_JSON_VALUE)
	public JsonResult step2(ApplyStep2 applyStep2) {
		Member member = UserConext.getCurrentMember();
		Shop shop = this.shopManager.getShopByMember(member.getMember_id());
		/**判断会员是否登陆*/
		if(member==null) {
			return JsonResultUtil.getErrorJson("您没有登录不能申请开店");
		}
		if(shop==null) {
			return JsonResultUtil.getErrorJson("抱歉,您还没有店铺!");
		}
		HttpServletRequest request = ThreadContextHolder.getHttpRequest(); 
		/**将日期转换为秒数*/ 
		Long establishdate = DateUtil.getDateline(request.getParameter("establishdate").toString()); 
		if(establishdate>DateUtil.getDateline()) {
			return JsonResultUtil.getErrorJson("成立时间不能大于当前时间！");
		}
		
		Long licencestart = DateUtil.getDateline(request.getParameter("licencestart").toString()); 
		Long licenceend = DateUtil.getDateline(request.getParameter("licenceend").toString()); 
		if(licencestart>licenceend) {
			return JsonResultUtil.getErrorJson("营业执照有效期开始时间不能大于结束时间！");
		}
		applyStep2.setEstablish_date(establishdate);
		applyStep2.setLicence_start(licencestart);
		applyStep2.setLicence_end(licenceend);
		shopManager.step2(applyStep2);
		return JsonResultUtil.getSuccessJson("操作成功");
	}
	/**
	 * 申请开店第三步
	 * @param applyStep3 店铺信息
	 */
	@ResponseBody
	@RequestMapping(value="/step3",produces= MediaType.APPLICATION_JSON_VALUE)
	public JsonResult step3(ApplyStep3 applyStep3) {
		Member member = UserConext.getCurrentMember();
		Shop shop = this.shopManager.getShopByMember(member.getMember_id());
		/**判断会员是否登陆*/
		if(member==null) {
			return JsonResultUtil.getErrorJson("您没有登录不能申请开店");
		}
		/**判断店铺信息*/
		if(shop==null) {
			return JsonResultUtil.getErrorJson("抱歉,您还没有店铺!");
		}
		shopManager.step3(applyStep3);
		return JsonResultUtil.getSuccessJson("操作成功");
		
	}
	/**
	 * 申请开店第四步
	 * @param applyStep4 店铺信息
	 */
	@ResponseBody
	@RequestMapping(value="/step4",produces= MediaType.APPLICATION_JSON_VALUE)
	public JsonResult step4(ApplyStep4 applyStep4) {
		Member member = UserConext.getCurrentMember();
		Shop shop = this.shopManager.getShopByMember(member.getMember_id());
		/**判断会员是否登陆*/
		if(member==null) {
			return JsonResultUtil.getErrorJson("您没有登录不能申请开店");
		}
		/**判断店铺信息*/
		if(shop==null) {
			return JsonResultUtil.getErrorJson("抱歉,您还没有店铺!");
		}
		if(shopManager.checkShopName(applyStep4.getShop_name())) {
			return JsonResultUtil.getErrorJson("店铺名称重复！");
		}
		shopManager.step4(applyStep4);
		return JsonResultUtil.getSuccessJson("店铺申请成功");
	}
	/**
	 * 查看用户名是否重复
	 * @param storeName 店铺名称,String
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="/check-store-name",produces= MediaType.APPLICATION_JSON_VALUE)
	public JsonResult checkStoreName(String shopName){
		if(this.shopManager.checkShopName(shopName)){
			return JsonResultUtil.getErrorJson("店铺名称重复");
		}else{
			return JsonResultUtil.getSuccessJson("店铺名称可以使用");
		}
	}
	/**
	 * 修改店铺设置
	 * @param shopSetting 店铺信息
	 * @return JsonResult
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="/edit",produces= MediaType.APPLICATION_JSON_VALUE)
	public JsonResult edit(ShopSetting shopSetting){
		try {
			this.shopManager.editShopSetting(shopSetting);
			return JsonResultUtil.getSuccessJson("修改店铺信息成功");
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("修改店铺信息失败:"+e);
			return JsonResultUtil.getErrorJson("修改店铺信息失败");
		}
	}
	
	/**
	 * 检测身份证
	 * @param id_number 身份证号
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="/check-id-number",produces= MediaType.APPLICATION_JSON_VALUE)
	public JsonResult checkIdNumber(String id_number){
		int result=shopManager.checkIdNumber(id_number);
		if(result==0){
			return JsonResultUtil.getSuccessJson("身份证可以使用！");
		}else{
			return JsonResultUtil.getErrorJson("身份证已经存在！");
		}
	}
	/**
	 * 修改店铺Logo
	 * @param logo Logo,String
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="/edit-shop-logo",produces= MediaType.APPLICATION_JSON_VALUE)
	public JsonResult editShopLogo( String logo){
		try {
			shopManager.editShopOnekey("shop_logo",logo);
			return JsonResultUtil.getSuccessJson("店铺Logo修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("修改店铺Logo失败:"+e);
			return JsonResultUtil.getErrorJson("店铺Logo修改失败");
		}
	}
	
	/**
	 * 更换店铺模板
	 * @param themes_id 店铺模板ID
	 * @return 更换状态
	 */
	@ResponseBody
	@RequestMapping(value="/change-shop-themes",produces= MediaType.APPLICATION_JSON_VALUE)
	public JsonResult changeShopThemes(Integer themes_id){
		try {
			storeThemesManager.changeStoreThemes(themes_id);
			return JsonResultUtil.getSuccessJson("更换成功");
		} catch (Exception e) {
			this.logger.error(e);
			return JsonResultUtil.getErrorJson("更换失败");
		}
	}

	/**
	 * 修改货品预警数设置
	 * @param store 店铺信息,Store
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="/warning_count_edit",produces= MediaType.APPLICATION_JSON_VALUE)
	public JsonResult warning_count_edit(String goods_warning_count){
		try {
			this.shopManager.editShopOnekey("goods_warning_count",goods_warning_count);
			return JsonResultUtil.getSuccessJson("修改店铺信息成功");
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("修改店铺信息失败:"+e);
			return JsonResultUtil.getErrorJson("修改店铺信息失败");
		}
	}
	/**
	 * 提升店铺等级申请
	 * @param list 店铺等级信息集合
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/shop-levle-apply")
	public Object storeLevelApply(Integer shop_id){
		try {
			//判断店铺id
			if(shop_id==null){
				Seller Seller = sellerManager.getSeller();
				shop_id = Seller.getStore_id();
			}
			//编辑店铺信息
			ShopDetail shopDetail = new ShopDetail();
			//将店铺等级申请状态设置为待审核
			shopDetail.setShop_level_apply(1);
			shopManager.editShop(shopDetail);
			return JsonResultUtil.getSuccessJson("申请成功,请耐心等待");
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResultUtil.getErrorJson("申请失败");
		}
	}
	
	/**
	 * 更换店铺模板
	 * @param themes_id 店铺模板ID
	 * @return 更换状态
	 */
	@ResponseBody
	@RequestMapping(value="/change-wap-shop-themes",produces= MediaType.APPLICATION_JSON_VALUE)
	public JsonResult changeShopWapThemes(Integer themes_id){
		try {
			shopWapThemesManager.changeStoreThemes(themes_id);
			return JsonResultUtil.getSuccessJson("更换成功");
		} catch (Exception e) {
			this.logger.error(e);
			return JsonResultUtil.getErrorJson("更换失败");
		}
	}
}
