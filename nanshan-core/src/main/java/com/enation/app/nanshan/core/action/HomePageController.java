package com.enation.app.nanshan.core.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.nanshan.core.service.IArticleManager;
import com.enation.app.nanshan.core.service.ICatManager;
import com.enation.app.nanshan.model.NanShanArticleVo;
import com.enation.framework.action.GridController;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonResultUtil;
import com.google.gson.JsonObject;

/**
 * 首页服务
 * @author jianjianming
 * @version $Id: HomePageController.java,v 0.1 2017年12月28日 上午10:30:20$
 */
@Controller 
@Scope("prototype")
@RequestMapping("/core/admin/home")
public class HomePageController extends GridController{
	
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
		List<NanShanArticleVo> list = articleManager.queryArticleByCatId(catId);
		if(list != null && list.size()>0){
			view.addObject("data",list.get(0));
		}else{
			list.add(new NanShanArticleVo());
			view.addObject("data",list.get(0));
		}
		view.setViewName("/nanshan/admin/home/page");
		return view;
	}

	/**
	 * 修改
	 * @param articleVo
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/save-edit")
	public JsonResult saveEdit(NanShanArticleVo articleVo,HttpServletRequest request){
		//保存修改
		try{
			JSONArray jsonArray = new JSONArray();
			String[] imgUrl = request.getParameterValues("imgUrl");
			String[] pageUrl = request.getParameterValues("pageUrl");
			if(pageUrl!=null && pageUrl.length>0){
				for (int i = 0; i < pageUrl.length; i++) {
					JSONObject json = new JSONObject();
					json.put("index", i);
					json.put("imgUrl", imgUrl[i]);
					json.put("pageUrl",pageUrl[i]);
					jsonArray.add(json);
				}
			}
			JSONObject json = new JSONObject();
			String routineImgUrl = request.getParameter("routineImgUrl");
			String temporaryImgUrl = request.getParameter("temporaryImgUrl");
			String scienceImgUrl = request.getParameter("scienceImgUrl");
			json.put("routineImgUrl", routineImgUrl);
			json.put("temporaryImgUrl", temporaryImgUrl);
			json.put("scienceImgUrl", scienceImgUrl);
			json.put("imgs", jsonArray);
			articleVo.setContent(json.toString());
			if(articleVo.getId()>0){
				articleManager.updateArticle(articleVo);
			}else{
				articleVo.setCreate_time(DateUtil.getDateline());
				articleManager.addArticle(articleVo);
			}
			return JsonResultUtil.getSuccessJson("操作成功");
		}catch(Exception e){
			return JsonResultUtil.getErrorJson("操作失败:"+e.getMessage());
		}		
	}
	
}
