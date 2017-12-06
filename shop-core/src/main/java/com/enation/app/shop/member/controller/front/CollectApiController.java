package com.enation.app.shop.member.controller.front;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.member.model.Favorite;
import com.enation.app.shop.member.service.IFavoriteManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.JsonResultUtil;

/**
 * 详细页收藏
 * @author lina
 * @version 2.0,2016-02-18 wangxin v60版本改造
 *
 */
@Scope("prototype")
@Controller
@RequestMapping("/api/shop/collect")
public class CollectApiController {
	
	@Autowired
	private IFavoriteManager favoriteManager;
	
	
	
	/**
	 * 收藏一个商品，必须登录才能调用此api
	 * @param goods_id ：要收藏的商品id,int型
	 * @return
	 * @return 返回json串
	 * result  为1表示调用成功0表示失败 ，int型
	 * message 为提示信息
	 */
	@ResponseBody
	@RequestMapping(value="/add-collect",produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult addCollect(Integer goods_id){
		Member memberLogin = UserConext.getCurrentMember();
		if(memberLogin!=null){
			int count = favoriteManager.getCount(goods_id,memberLogin.getMember_id());
			if (count == 0){
				favoriteManager.add(goods_id);
				return JsonResultUtil.getSuccessJson("添加收藏成功");
			}else{
				return JsonResultUtil.getErrorJson("已收藏该商品");				
			}
		}else{
			return JsonResultUtil.getErrorJson("对不起，请您登录后再收藏该商品！");				
		}
	}
	
	
	/**
	 * 取消一个商品的收藏，必须登录才能调用此api
	 * @param favorite_id ：收藏id，即Favorite.favorite_id
	 * @return
	 * @return 返回json串
	 * result  为1表示调用成功0表示失败 ，int型
	 * message 为提示信息
	 * 
	 * {@link Favorite}
	 */
	@ResponseBody
	@RequestMapping(value="/cancel-collect",produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult cancelCollect(Integer favorite_id){
		favoriteManager.delete(favorite_id);
		return JsonResultUtil.getSuccessJson("取消成功");
	}
	
	/**
	 * 根据 商品id和 店铺id取消商品收藏
	 * @param goods_id
	 * @param member_id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/cancel-favorite",produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult cancelCollectByGoodsId(Integer goods_id){
		Member currentMember = UserConext.getCurrentMember();
		if(StringUtils.isEmpty(goods_id)) {
			return JsonResultUtil.getErrorJson("参数错误");	
		}
		if(currentMember==null) {
			return JsonResultUtil.getErrorJson("请先登录再进行此操作");	
		}
		favoriteManager.delete(goods_id,currentMember.getMember_id());
		return JsonResultUtil.getSuccessJson("取消成功");
	}
	
	

	
}
