package com.enation.app.shop.shop.rdesign.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.shop.shop.rdesign.model.WapShopThemes;
import com.enation.app.shop.shop.rdesign.service.IShopWapThemesManager;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.action.JsonResult;
import com.enation.framework.database.Page;
import com.enation.framework.util.JsonResultUtil;

/**
 * 
 * (wap店铺模板Action) 
 * @author zjp
 * @version v1.0
 * @since v6.4.0
 * 2017年8月25日 下午1:07:46
 */
@Controller
@RequestMapping("/b2b2c/admin/wap/store-themes")
public class ShopWapThemesController extends GridController{

	@Autowired
	private IShopWapThemesManager storeWapThemesManager;

	/***
	 * 跳转至店铺模板列表页面
	 * @return
	 */
	@RequestMapping("/list")
	public ModelAndView  list(){

		ModelAndView view=this.getGridModelAndView();
		view.setViewName("/b2b2c/admin/store/wapthemes/wap_store_themes_list");
		return view;
	}

	/**
	 * 店铺模板列表JSON
	 * @param list 店铺模板列表
	 * @return 店铺模板列表JSON
	 */
	@ResponseBody
	@RequestMapping("/list-json")
	public GridJsonResult listJson(){
		this.webpage=storeWapThemesManager.list(this.getPage(), this.getPageSize());
		return JsonResultUtil.getGridJson(this.webpage);
	}

	/**
	 * 跳转至店铺模板添加页面
	 * @return
	 */
	@RequestMapping("/add")
	public ModelAndView add(){
		ModelAndView view=new ModelAndView();
		view.setViewName("/b2b2c/admin/store/wapthemes/wap_store_themes_add");
		return view;
	}

	/**
	 * 添加店铺模板
	 * @param name 店铺模板名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/save-add")
	public JsonResult saveAdd(WapShopThemes wapStoreThemes){
		try {
			storeWapThemesManager.add(wapStoreThemes);
			return JsonResultUtil.getSuccessJson("添加成功");
		} catch (Exception e) {
			this.logger.error(e);
			return JsonResultUtil.getErrorJson("添加失败");
		}

	}

	/**
	 * 获取店铺模板
	 * @param id 店铺模板Id
	 * @param financeLevel 店铺模板
	 * @return
	 */
	@RequestMapping("/edit")
	public ModelAndView edit(Integer id){
		ModelAndView view=new ModelAndView();
		view.addObject("storeThemes", this.storeWapThemesManager.getStorethThemes(id));
		view.setViewName("/b2b2c/admin/store/wapthemes/wap_store_themes_edit");
		return view;
	}

	/**
	 * 修改保存
	 * @param id 店铺模板Id
	 * @param name 店铺模板名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/save-edit")
	public JsonResult saveEdit(WapShopThemes wapStoreThemes){
		try {
			// 如果当前模版为默认模版 则无法修改默认从是变为否
			WapShopThemes oldStoreThremes=(WapShopThemes) this.storeWapThemesManager.getStorethThemes(wapStoreThemes.getId());
			if(oldStoreThremes.getIs_default().equals(1) && wapStoreThemes.getIs_default().equals(0)){
				return JsonResultUtil.getErrorJson("必须保留一个默认模版");
			}
			storeWapThemesManager.edit(wapStoreThemes);
			return JsonResultUtil.getSuccessJson("修改成功");
		} catch (Exception e) {
			this.logger.error(e);
			return JsonResultUtil.getErrorJson("修改失败");
		}
	}

	/**
	 * 删除店铺模板
	 * @param id 店铺模板Id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public JsonResult delete(Integer id){
		try {
			Page page =storeWapThemesManager.list(1, 2);
			if(page.getTotalCount()<=1){
				return JsonResultUtil.getErrorJson("店铺模板必须保留一个，删除失败!!!");

			}else if(storeWapThemesManager.getStorethThemes(id).getIs_default()==1){
				return JsonResultUtil.getErrorJson("默认店铺模板不能删除，删除失败!!!");

			}else{
				storeWapThemesManager.delete(id);
				return JsonResultUtil.getSuccessJson("删除成功");
			}
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson("删除失败");
		}

	}
}
