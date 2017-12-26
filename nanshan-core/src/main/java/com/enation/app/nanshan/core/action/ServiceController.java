package com.enation.app.nanshan.core.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.nanshan.core.model.Spec;
import com.enation.app.nanshan.core.model.SpecVal;
import com.enation.app.nanshan.core.service.IArticleManager;
import com.enation.app.nanshan.core.service.ICatManager;
import com.enation.app.nanshan.core.service.ISpecManager;
import com.enation.app.nanshan.model.ArticleCat;
import com.enation.app.nanshan.model.NanShanArticleVo;
import com.enation.eop.resource.IThemeUriManager;
import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonResultUtil;
import com.qq.connect.utils.json.JSONObject;

/**
 * 服务管理实现
 * @author LiJM
 *
 */
@Controller 
@Scope("prototype")
@RequestMapping("/core/admin/service")
public class ServiceController extends GridController{
	
	@Autowired
	private IArticleManager  articleManager;
	
	@Autowired
	private ICatManager catManager;
	
	/**
	 * 跳转到修改页面
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit(Integer catId){
		ModelAndView view = new ModelAndView();
		List<ArticleCat> catList = catManager.queryCatChildrenInfoByCatIds("1");
		if(null == catId){
			if(catList != null && catList.size()>0){
				Long articleCatId = catList.get(0).getCat_id();
				List<NanShanArticleVo> list = articleManager.queryArticleByCatId(articleCatId.intValue());
				if(list!=null && list.size()>0){
					view.addObject("data",list.get(0));
				}else{
					view.addObject("data", new NanShanArticleVo());
				}
			}
		}
		view.addObject("catList", catList);
		view.setViewName("/nanshan/admin/service/page");
		return view;
	}
	
	/**
	 * 修改
	 * @param spec
	 * @param specValName
	 * @param spec
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/save-edit")
	public JsonResult saveEdit(NanShanArticleVo articleVo){
		//保存修改
		try{
	        this.articleManager.updateArticle(articleVo);
			return JsonResultUtil.getSuccessJson("修改成功");
		}catch(Exception e){
			return JsonResultUtil.getErrorJson("修改失败:"+e.getMessage());
		}		
	}
	
}
