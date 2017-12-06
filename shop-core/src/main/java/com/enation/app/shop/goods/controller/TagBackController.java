package com.enation.app.shop.goods.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.shop.goods.model.vo.Tag;
import com.enation.app.shop.goods.service.ITagManager;
import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.JsonResultUtil;

/**
 * 标签维护
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年10月12日 下午5:06:51
 */
@Controller
@RequestMapping("/shop/admin/tag")
public class TagBackController extends GridController{

	@Autowired
	private ITagManager tagManager;
	/**
	 * 商品标签列表
	 * @return
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(){
		
		ModelAndView view = getGridModelAndView();
		view.setViewName("/shop/admin/tag/tag_list");
		return view;
	}
	
	
	
	@ResponseBody
	@RequestMapping(value="/list-json")
	public GridJsonResult listJson(){
		return JsonResultUtil.getGridJson(tagManager.list(this.getPage(), this.getPageSize()));
	}
	
	/**
	 * 检测标签是否有相关联的商品
	 * @param tag_id 标签Id数组,Integer[]
	 * @return json
	 * result 1.有.0.没有
	 */
	@ResponseBody
	@RequestMapping(value="/check-join-goods")
	public JsonResult checkJoinGoods(Integer[] tag_id){
		if(this.tagManager.checkJoinGoods(tag_id)){
			return JsonResultUtil.getSuccessJson("");
		}else{
			return JsonResultUtil.getErrorJson("");
		}
	}
	
	/**
	 * 检测标签名是否重名
	 * @param tag 标签,Tag
	 * @return json
	 * result 1.有.0.没有
	 */
	@ResponseBody
	@RequestMapping(value="/check-name")
	public JsonResult checkname( Tag tag){
		if( this.tagManager.checkname(tag.getTag_name(), tag.getTag_id()) ){
			return JsonResultUtil.getSuccessJson("");
		}else{
			return JsonResultUtil.getErrorJson("");
		}
	}
	
	/**
	 * 跳转至添加标签页面
	 * @return 添加标签页面
	 */
	@RequestMapping(value="/add")
	public String add(){
		return "/shop/admin/tag/add";
	}
	
	/**
	 * 跳转至修改标签页面
	 * @param tagId 标签Id,Integer
	 * @return 修改标签页面
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit(Integer tagId){
		
		ModelAndView view=new ModelAndView();
		view.addObject("tag", this.tagManager.getById(tagId));
		view.setViewName("/shop/admin/tag/edit");
		return view;
	}
	
	/**
	 * 添加标签
	 * @param tag 标签,Tag
	 * @return json
	 * result 1.操作成功.0.操作失败
	 */
	@ResponseBody
	@RequestMapping(value="save-add")
	public JsonResult saveAdd( Tag tag){
		try {
			this.tagManager.add(tag);
			return JsonResultUtil.getSuccessJson("添加标签成功");
		} catch (Exception e) {
			logger.error("添加标签失败", e);
			return JsonResultUtil.getErrorJson("添加标签失败");
		}
	}
	
	
	/**
	 * 保存修改
	 * @param tag 标签,Tag
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="save-edit")
	public JsonResult saveEdit( Tag tag){
		
		AdminUser user  = UserConext.getCurrentAdminUser();
		if(EopSetting.IS_DEMO_SITE && user!= null && user.getFounder() != 1){
			if(tag.getTag_id()<=3){
				return JsonResultUtil.getErrorJson("抱歉，当前为演示站点，以不能修改这些示例数据，请下载安装包在本地体验这些功能！");
			}
		}
		
		this.tagManager.update(tag);
		return JsonResultUtil.getSuccessJson("商品标签修改成功");
	}
	
	/**
	 * 删除标签
	 * @param tag_id 标签Id数组,Integer[]
	 * @return result
	 * result 1.操作成功.0.操作失败
	 */
	@ResponseBody
	@RequestMapping(value="delete")
	public JsonResult delete(Integer[] tag_id){
		AdminUser user  = UserConext.getCurrentAdminUser();
		if(EopSetting.IS_DEMO_SITE && user!= null && user.getFounder() != 1){
			for(Integer tid : tag_id){
				if(tid<=3){
					return JsonResultUtil.getErrorJson("抱歉，当前为演示站点，以不能修改这些示例数据，请下载安装包在本地体验这些功能！");
				}
			}
		}
		
	 	try {
			this.tagManager.delete(tag_id);
			return JsonResultUtil.getSuccessJson("标签删除成功");
		} catch (Exception e) {
			logger.error("标签删除失败", e);
			return JsonResultUtil.getErrorJson("标签删除失败");
		}
	}
	
}
