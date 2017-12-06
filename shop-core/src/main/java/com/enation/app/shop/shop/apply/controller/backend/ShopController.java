package com.enation.app.shop.shop.apply.controller.backend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.goods.service.ICategoryManager;
import com.enation.app.shop.shop.apply.model.enums.ShopDisableStatus;
import com.enation.app.shop.shop.apply.model.po.Shop;
import com.enation.app.shop.shop.apply.model.po.ShopDetail;
import com.enation.app.shop.shop.apply.model.vo.ShopVo;
import com.enation.app.shop.shop.apply.service.IShopManager;
import com.enation.app.shop.shop.rdesign.model.StoreThemes;
import com.enation.app.shop.shop.rdesign.model.WapShopThemes;
import com.enation.app.shop.shop.rdesign.service.IShopWapThemesManager;
import com.enation.app.shop.shop.rdesign.service.IStoreThemesManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.app.shop.shop.setting.service.IStoreLevelManager;
import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.action.JsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.StringUtil;
/**
 * 店铺管理
 * @author LiFenLong
 *
 */
@Controller
@RequestMapping("/b2b2c/admin/shop")
public class ShopController extends GridController{


	@Autowired
	private IShopManager shopManager;

	@Autowired
	private ISellerManager storeMemberManager;

	
	@Autowired
	private IStoreThemesManager storeThemesManager;
	
	@Autowired
	private IShopWapThemesManager shopWapThemesManager;
	
	@Autowired
	private IStoreLevelManager storeLevelManager;
	
	@Autowired
	private ISellerManager sellerManager;
	
	
	@Autowired
	private ICategoryManager categoryManager;
	/**
	 * 店铺列表
	 * @return
	 */
	@RequestMapping(value="/shop-list")
	public ModelAndView storeList(){
		ModelAndView view=getGridModelAndView();
		view.setViewName("/b2b2c/admin/store/store_list");
		return view;
	}
	
	/**
	 * 店铺列表JSON
	 * @param disabled 店铺状态
	 * @param storeName 店铺名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/shop-list-json")
	public GridJsonResult storeListJson( String uname, @RequestParam(value = "shop_name", required = false) String shop_name,
			@RequestParam(value = "member_name", required = false) String member_name,
			@RequestParam(value = "start_time", required = false) String start_time,
			@RequestParam(value = "end_time", required = false) String end_time,
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "shop_disable", required = false) String shop_disable
			){
		Map other=new HashMap();
		other.put("shop_name", shop_name);
		other.put("member_name", member_name);
		other.put("start_time", start_time);
		other.put("end_time", end_time);
		other.put("keyword", keyword);
		return JsonResultUtil.getGridJson(shopManager.shop_list(other,shop_disable,this.getPage(),this.getPageSize()));
	}
	/**
	 * 禁用店铺
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="dis-shop")
	public JsonResult disShop(Integer shopId){
		AdminUser user  = UserConext.getCurrentAdminUser();
		if(EopSetting.IS_DEMO_SITE && user!= null && user.getFounder() != 1){
			return JsonResultUtil.getErrorJson(EopSetting.DEMO_SITE_TIP);
		}
		try {
			shopManager.disShop(shopId);
			return JsonResultUtil.getSuccessJson("店铺关闭成功");
		} catch (Exception e) {
			this.logger.error("店铺关闭失败:"+e);
			return JsonResultUtil.getErrorJson("店铺关闭失败");
		}
	}
	/**
	 * 店铺恢复使用
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/use-shop")
	public JsonResult useShop(Integer shopId){
		try {
			shopManager.useShop(shopId);
			return JsonResultUtil.getSuccessJson("店铺恢复使用成功");
		} catch (Exception e) {
			this.logger.error("店铺恢复使用失败"+e);
			return JsonResultUtil.getErrorJson("店铺恢复使用失败");
		}
	}
	
	/**
	 * 修改店铺
	 * @param storeId
	 * @return
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit(Integer shopId){
		ModelAndView view= new ModelAndView();
		view.addObject("shop", shopManager.getShopVo(shopId));
		view.addObject("goodsType", categoryManager.list(0, null));
		view.setViewName("/b2b2c/admin/store/store_edit");
		return view;
	}
	/**
	 * 修改店铺信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/save-edit")
	public JsonResult saveEdit(ShopVo shopVo,Integer pass){
		try {
			HttpServletRequest request = ThreadContextHolder.getHttpRequest(); 
			/**将日期转换为秒数*/ 
			Long establishdate = DateUtil.getDateline(request.getParameter("establishdate").toString()); 
			Long licencestart = DateUtil.getDateline(request.getParameter("licencestart").toString()); 
			Long licenceend = DateUtil.getDateline(request.getParameter("licenceend").toString()); 
			shopVo.setEstablish_date(establishdate);
			shopVo.setLicence_start(licencestart);
			shopVo.setLicence_end(licenceend);
			/**
			 * 判断审核还是修改状态
			 */
			if(pass!=null) {
				Shop shop = shopManager.getShop(shopVo.getShop_id());
				if(pass==1){
					StoreThemes st = storeThemesManager.getDefaultStoreThemes();
					WapShopThemes dst = shopWapThemesManager.getDefaultStoreThemes();
					/**设置模版信息*/
					shopVo.setShop_themeid(st.getId());
					shopVo.setShop_theme_path(st.getPath());
					shopVo.setWap_theme_path(dst.getPath());
					shopVo.setWap_themeid(dst.getId());
					/**更新店铺信息*/
					shopVo.setShop_disable(ShopDisableStatus.open.toString());
					shopVo.setShop_createtime(DateUtil.getDateline());
					/**更新会员信息*/
					Seller seller = sellerManager.getSeller(shop.getMember_id());
					seller.setStore_id(shopVo.getShop_id());
					seller.setIs_store(1);
					sellerManager.updateSeller(seller);
					shopManager.fillShopInformation(shopVo.getShop_id());
				}else {
					shopVo.setShop_disable(ShopDisableStatus.refused.toString());
				}
			}
			this.shopManager.editShopInfo(shopVo);
			return JsonResultUtil.getSuccessJson("修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return  JsonResultUtil.getErrorJson("修改失败，请稍后重试！");
		}
	}
	/**
	 * 开店申请
	 * @return
	 */
	@RequestMapping(value="/audit-list")
	public ModelAndView auditList(){
		ModelAndView view=getGridModelAndView();
		view.setViewName("/b2b2c/admin/store/audit_list");
		return view;
	}
	/**
	 * 跳转到店铺添加页面
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/add")
	public ModelAndView add(String uname){
		ModelAndView view=new ModelAndView();
		view.addObject("uname", uname);
		view.addObject("goodsType", categoryManager.list(0, null));
		view.setViewName("/b2b2c/admin/store/store_add");
		return view;
	}
	/**
	 * 后台添加店铺
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/save")
	public JsonResult save(ShopVo shopVo){
		try {
			HttpServletRequest request = ThreadContextHolder.getHttpRequest(); 
			StoreThemes st = storeThemesManager.getDefaultStoreThemes();
			WapShopThemes dst = shopWapThemesManager.getDefaultStoreThemes();
			if(st==null){ 
				return JsonResultUtil.getErrorJson("店铺模版异常，请处理店铺模版");
			}
			if(shopManager.checkShopName(shopVo.getShop_name())) {
				return JsonResultUtil.getErrorJson("店铺名称重复！");
			}
			/**将日期转换为秒数*/ 
			Long establishdate = DateUtil.getDateline(request.getParameter("establishdate").toString()); 
			Long licencestart = DateUtil.getDateline(request.getParameter("licencestart").toString()); 
			Long licenceend = DateUtil.getDateline(request.getParameter("licenceend").toString()); 
			/**设置店铺信息*/
			shopVo.setEstablish_date(establishdate);
			shopVo.setLicence_start(licencestart);
			shopVo.setLicence_end(licenceend);
			/**设置模版信息*/
			shopVo.setShop_themeid(st.getId());
			shopVo.setShop_theme_path(st.getPath());
			shopVo.setWap_theme_path(dst.getPath());
			shopVo.setWap_themeid(dst.getId());
			/**设置店铺等级*/
			shopVo.setShop_level(1);
			/**后台无需审核直接开店*/
			shopVo.setShop_disable(ShopDisableStatus.open.toString());
			/**设置开店时间*/
			shopVo.setShop_createtime(DateUtil.getDateline());
			this.shopManager.registStore(shopVo);
			return JsonResultUtil.getSuccessJson("保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResultUtil.getErrorJson("保存失败");
		}
	}
	


	/**
	 * 验证用户 
	 * @param uname 会员名称
	 * @param password 密码
	 * @param assign_password 是否验证密码 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/opt-member")
	public JsonResult optMember(String uname,String password,Integer assign_password){
		try {
			Seller storeMember= storeMemberManager.getSeller(uname);
			//检测是否为新添加的会员
			if(storeMember.getIs_store()==null){
				return JsonResultUtil.getSuccessJson(uname);
			}
			//判断用户是否已经拥有店铺
			if(storeMember.getIs_store()==1){
				return JsonResultUtil.getErrorJson("会员已拥有店铺");
			}
			//验证会员密码
			if(assign_password!=null&&assign_password==1){
				if(!storeMember.getPassword().equals(StringUtil.md5(password))){
					return JsonResultUtil.getErrorJson("密码不正确");
				}
			}
			if(storeMember.getIs_store()==-1){
				return JsonResultUtil.getSuccessJson(uname);
			}else{
				return JsonResultUtil.getSuccessJson("2");
			}
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson("没有此用户");
		}

	}

	/**
	 * 跳转至店铺等级设置页面
	 * @return
	 */
	@RequestMapping(value="/shop-levle-setting")
	public ModelAndView storeSetting(){
		ModelAndView view=new ModelAndView();
		List storeLevelList = storeLevelManager.storeLevelList();
		view.addObject("storeLevelList", storeLevelList);
		view.setViewName("/b2b2c/admin/store/store_levle_setting");
		return view;
	}
	/**
	 * 保存店铺等级信息
	 * @param list 店铺等级信息集合
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/save-store-levle")
	public Object saveStoreLevle(Integer level_id[],Integer space_capacity[]){
		try {
			//遍历数组
			for(int i=0;i<level_id.length;i++){
				//校验数据的正确性
				if(level_id[i]<1||space_capacity[i]<0||level_id.length!=space_capacity.length){
					return JsonResultUtil.getErrorJson("请输入正确的数据");
				}
				if(i>0){
					if(space_capacity[i]<space_capacity[i-1]){
						return JsonResultUtil.getErrorJson("上一级数值必须小于下一级");
					}
				}
				
				//更新数据库信息
				storeLevelManager.editStoreLevel(level_id[i],space_capacity[i]);
			}
			return JsonResultUtil.getSuccessJson("修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResultUtil.getErrorJson("修改失败");
		}
	}
	/**
	 * 跳转至店铺等级审核页面
	 * @return
	 */
	@RequestMapping(value="/shop-levle-audit")
	public ModelAndView storeLevelAudit(){
		ModelAndView view=getGridModelAndView();
		//view.setViewName("/b2b2c/admin/store/audit_list");
		view.setViewName("/b2b2c/admin/store/store_levle_audit_list");
		return view;
	}
	/**
	 * 店铺等级申请列表
	 * @param other
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/level-audit-list-json")
	public GridJsonResult levelAuditListJson(){
		return  JsonResultUtil.getGridJson(shopManager.levelAuditList(this.getPage(), this.getPageSize()));
	}
	/**
	 * 店铺等级审核
	 * @param other
	 * @param disabled
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/level-audit.do")
	public JsonResult levelAudit(Integer storeId,Integer store_level_apply_status){
		try {
			ShopDetail storeDetail = shopManager.getShopDetail(storeId);
			Map map = new HashMap();
			//判断是否通过审核
			if(store_level_apply_status==0){
				map.put("shop_id", storeId);
				map.put("shop_level_apply", 0);
				map.put("shop_level", storeDetail.getShop_level()+1);
			}
			if(store_level_apply_status==2){
				map.put("shop_id", storeId);
				map.put("shop_level_apply", 2);
			}
			//更新店铺信息
			shopManager.editShop(map);
			return JsonResultUtil.getSuccessJson("审核成功");
		} catch (Exception e) {
			return JsonResultUtil.getSuccessJson("审核失败");
		}
	}
}
