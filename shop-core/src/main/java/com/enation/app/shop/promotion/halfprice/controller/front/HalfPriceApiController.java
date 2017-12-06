package com.enation.app.shop.promotion.halfprice.controller.front;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.goods.service.IGoodsQueryManager;
import com.enation.app.shop.goods.service.impl.GoodsManager;
import com.enation.app.shop.promotion.halfprice.model.vo.HalfPriceVo;
import com.enation.app.shop.promotion.halfprice.service.IHalfPriceManager;
import com.enation.app.shop.promotion.tool.model.enums.PromotionTypeEnum;
import com.enation.app.shop.promotion.tool.model.vo.PromotionGoodsVo;
import com.enation.app.shop.promotion.tool.support.PromotionServiceConstant;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.app.shop.trade.model.vo.Cart;
import com.enation.framework.action.GridController;
import com.enation.framework.action.JsonResult;
import com.enation.framework.cache.ICache;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.StringUtil;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;


/**
 * 第二件半价商家中心控制器
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年8月22日 上午10:59:33
 */
@RestController
@Scope("prototype")
@RequestMapping("/api/shop/promotion")
public class HalfPriceApiController extends GridController{
	
	protected final Logger logger = Logger.getLogger(getClass());


	@Autowired
	private ISellerManager storeMemberManager;
	@Autowired
	private IHalfPriceManager halfPriceManager;
	@Autowired
	private IGoodsQueryManager goodsQueryManager;
	@Autowired
	private GoodsManager goodsManager;
	@Autowired
	private ICache cache;
	
	
	/**
	 * 保存添加第二件半价促销活动
	 * @param halfPriceVo 第二件半价vo实体传值
	 * @return 保存结果json
	 */
	@ApiOperation(value="添加第二件半价活动促销" ,response=HalfPriceVo.class)
	@ApiImplicitParams({
         @ApiImplicitParam(name = "title", value = "活动标题", required = true, dataType = "String" ,paramType="query"),
         @ApiImplicitParam(name = "start_time_str", value = "活动开始时间", required = true, dataType = "String" ,paramType="query"),
         @ApiImplicitParam(name = "end_time_str", value = "活动结束时间", required = true, dataType = "String" ,paramType="query"),
         @ApiImplicitParam(name = "range_type", value = "是否全部商品添加", required = true, dataType = "int" ,paramType="query")
	 })
	@PostMapping(value="/half-add", produces=MediaType.APPLICATION_JSON_VALUE)
	public JsonResult add(@RequestBody HalfPriceVo halfPriceVo) {
		try {
			
			/** 获取当前登陆的店铺ID */
			Seller storeMember = storeMemberManager.getSeller();
			
			/** 增加权限 */
			if(storeMember.getStore_id() == null){
				return JsonResultUtil.getErrorJson("您没有权限,请登陆！");
			}
			
			/**校验活动信息*/
			JsonResult checkInfo = checkInfo(halfPriceVo);
			if(checkInfo!=null) {
				return checkInfo;
			}
			
			HalfPriceVo addInfo = addInfo(halfPriceVo);
			if(addInfo != null) {
				//TODO 商品选择器添加规格以后需要在验证这做修改
				/** 获取发生冲突的活动列表和商品列表 */
				List<PromotionGoodsVo> actList = this.halfPriceManager.HalfMap(addInfo);
				if(actList.size() > 0){
					ThreadContextHolder.getSession().setAttribute("actList", actList);
					JsonResult jsonRetult=new JsonResult();
					jsonRetult.setData(actList);
					jsonRetult.setResult(0);
					return jsonRetult;
				}
			}
			this.halfPriceManager.add(addInfo);
			return JsonResultUtil.getSuccessJson("新增促销活动成功");
		} catch (Exception e) {
			this.logger.error("新增促销活动失败:", e);
			e.printStackTrace();
			return JsonResultUtil.getErrorJson("新增促销活动失败");
		}
	}
	
	

	/**
	 * 保存修改第二件半价促销活动
	 * @param halfPriceVo 第二件半价vo实体传值
	 * @return 修改结果json
	 */
	@ApiOperation(value="修改第二件半价活动促销" ,response=Cart.class)
	@ApiImplicitParams({
         @ApiImplicitParam(name = "title", value = "活动标题", required = true, dataType = "String" ,paramType="query"),
         @ApiImplicitParam(name = "start_time_str", value = "活动开始时间", required = true, dataType = "String" ,paramType="query"),
         @ApiImplicitParam(name = "end_time_str", value = "活动结束时间", required = true, dataType = "String" ,paramType="query"),
         @ApiImplicitParam(name = "range_type", value = "是否全部商品添加", required = true, dataType = "int" )
	 })
	@PostMapping(value="/half-edit", produces=MediaType.APPLICATION_JSON_VALUE)
	public JsonResult saveSecondHalfEdit(@RequestBody HalfPriceVo halfPriceVo){
		try {
			
			/**如果活动已过期则不进行修改操作*/
			String plugin = PromotionServiceConstant.getHalfPriceKey(halfPriceVo.getHp_id() );
			HalfPriceVo halfPrice = (HalfPriceVo) cache.get(plugin);
			if(halfPrice.getEnd_time()<DateUtil.getDateline()) {
				return JsonResultUtil.getErrorJson("活动已截止");
			}
			
			/** 获取当前登陆的店铺ID */
			Seller storeMember = storeMemberManager.getSeller();
			
			/** 增加权限 */
			if(halfPriceVo.getShop_id() == null || !halfPriceVo.getShop_id().equals(storeMember.getStore_id())){
				return JsonResultUtil.getErrorJson("您没有权限");
			}
			/**校验活动信息*/
			JsonResult checkInfo = checkInfo(halfPriceVo);
			if(checkInfo!=null) {
				return checkInfo;
			}
			
			/** 如果活动正在进行中不能做修改 */
			if(halfPriceManager.getCurrentStatus(halfPriceVo.getStart_time(),halfPriceVo .getEnd_time()).equals("进行中")) {
				return JsonResultUtil.getErrorJson("活动已开始，不能修改！");
			}
			HalfPriceVo addInfo = addInfo(halfPriceVo);
			/** 判断促销商品在同一时间段是否参加了其他的重复的促销活动,返回没有参加活动的商品ID和活动ID */
			List<PromotionGoodsVo> actList = this.halfPriceManager.HalfMap(addInfo);
			if(actList.size() > 0){
				ThreadContextHolder.getSession().setAttribute("actList", actList);
				JsonResult jsonRetult=new JsonResult();
				jsonRetult.setData(actList);
				jsonRetult.setResult(0);
				return jsonRetult;
			}
			this.halfPriceManager.edit(addInfo);
			return JsonResultUtil.getSuccessJson("修改促销活动成功！");
		} catch (Exception e) {
			this.logger.error("修改促销活动失败:", e);
			return JsonResultUtil.getErrorJson("修改促销活动失败！");
		}
		
	}
	
	
	
	/**
	 * 删除促销活动
	 * @param activity_id 促销活动id
	 * @return 返回删除的结果
	 */
	@DeleteMapping(value="/delete", produces=MediaType.APPLICATION_JSON_VALUE)
	public JsonResult delete(Integer activity_id){
		try {
			/** 获取当前登陆的店铺ID */
			Seller storeMember = storeMemberManager.getSeller();
			HalfPriceVo halfPriceVo = halfPriceManager.get(activity_id);
			/** 如果活动正在进行中不能做删除 */
			if(halfPriceManager.getCurrentStatus(halfPriceVo.getStart_time(),halfPriceVo.getEnd_time()).equals("进行中")) {
				return JsonResultUtil.getErrorJson("活动已开始，不能删除！！");
			}
			
			if(halfPriceVo != null) {
				/** 增加权限 */
				if(halfPriceVo.getShop_id() == null || !halfPriceVo.getShop_id().equals(storeMember.getStore_id())){
					return JsonResultUtil.getErrorJson("您没有权限");
				}
			}
			/** 如果促销活动id等于空 */
			if (activity_id == null) {
				return JsonResultUtil.getErrorJson("请选择要删除的数据");
			}
			
			this.halfPriceManager.delete(activity_id);
			return JsonResultUtil.getSuccessJson("删除成功！");
		} catch (Exception e) {
			this.logger.error("删除失败：", e);
			return JsonResultUtil.getErrorJson("删除失败！");
		}
	}
	
	
	/**
	 * 填充活动商品信息
	 * @param halfPriceVo 第二件半价vo实体传值
	 * @return
	 */
	private HalfPriceVo  addInfo(HalfPriceVo halfPriceVo) {
		List<PromotionGoodsVo> promotionGoodsVoList = new ArrayList<PromotionGoodsVo>();
		/**判断是全部商品参数还是部分商品参与*/
		if(halfPriceVo.getRange_type()==1) {
			/**所有商品参与需要查询店铺的所有有效商品*/
			List<Map> storeGoodsList = this.goodsQueryManager.getGoodsList(halfPriceVo.getShop_id());
			for (Map map : storeGoodsList) {
				PromotionGoodsVo promotionGoodsVo = new PromotionGoodsVo();
				promotionGoodsVo.setPromotion_type(PromotionTypeEnum.HALFPRICE.getType());
				promotionGoodsVo.setEnd_time(halfPriceVo.getEnd_time());
				promotionGoodsVo.setStart_time(halfPriceVo.getStart_time());
				promotionGoodsVo.setTitle(halfPriceVo.getTitle());
				promotionGoodsVo.setGoods_id(Integer.parseInt(StringUtil.toString(map.get("goods_id"))));
				promotionGoodsVo.setProduct_id(Integer.parseInt(StringUtil.toString(map.get("sku_id"))));
				promotionGoodsVo.setThumbnail(StringUtil.toString(map.get("thumbnail")));
				promotionGoodsVo.setName(StringUtil.toString(map.get("goods_name")));
				promotionGoodsVoList.add(promotionGoodsVo);
				halfPriceVo.setGoodsList(promotionGoodsVoList);
			}
		}else {
			/**部分商品填充商品信息*/
			promotionGoodsVoList = halfPriceVo.getGoodsList();
			for(PromotionGoodsVo promotionGoodsVo : promotionGoodsVoList) {
				promotionGoodsVo.setPromotion_type(PromotionTypeEnum.HALFPRICE.getType());
				promotionGoodsVo.setEnd_time(halfPriceVo.getEnd_time());
				promotionGoodsVo.setStart_time(halfPriceVo.getStart_time());
				promotionGoodsVo.setTitle(halfPriceVo.getTitle());
			}
		}
		
		return halfPriceVo;
	}

	
	/**
	 * 校验活动信息
	 * @param halfPriceVo 活动vo
	 * @return
	 */
	private JsonResult checkInfo(HalfPriceVo halfPriceVo) {
		
		Seller seller = this.storeMemberManager.getSeller();
		
		Long start_time = DateUtil.getDateline(halfPriceVo.getStart_time_str(), "yyyy-MM-dd HH:mm:ss");
	    Long end_time = DateUtil.getDateline(halfPriceVo.getEnd_time_str(), "yyyy-MM-dd HH:mm:ss");
		
		/** 向表填充数据用于保存活动表 */
	    halfPriceVo.setStart_time(start_time);
	    halfPriceVo.setEnd_time(end_time);
	    halfPriceVo.setDisabled(0);
	    halfPriceVo.setShop_id(seller.getStore_id());
	  //促销活动名称不能为空
		if (StringUtil.isEmpty(halfPriceVo.getTitle())) {
			return JsonResultUtil.getErrorJson("请填写促销活动名称");	
		}
		
		//促销活动的生效日期不能为空
		if (StringUtil.isEmpty(halfPriceVo.getStart_time_str()) || StringUtil.isEmpty(halfPriceVo.getEnd_time_str())) {
			return JsonResultUtil.getErrorJson("请填写促销活动生效时间");
		}
		if (end_time < start_time) {
			return JsonResultUtil.getErrorJson("开始时间不能大于结束时间");
		}
		/** 如果促销活动选择的是部分商品参加活动 */
		if (halfPriceVo.getRange_type() == 2) {
		  	List<PromotionGoodsVo> list = halfPriceVo.getGoodsList();
			/** 商品id组不能为空 */
			if (list.size() == 0) {
				return JsonResultUtil.getErrorJson("请选择要参与活动的商品");
			}
		}
		return null;
	}
	
}
