package com.enation.app.nanshan.core.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.nanshan.constant.NanShanCommonConstant;
import com.enation.app.nanshan.core.service.IArticleManager;
import com.enation.app.nanshan.core.service.ICatManager;
import com.enation.app.nanshan.model.ArticleCat;
import com.enation.app.nanshan.model.NanShanArticleVo;
import com.enation.framework.action.GridController;
import com.enation.framework.action.JsonResult;
import com.enation.framework.cache.ICache;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonResultUtil;

/**
 * 服务管理
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
	
	@Autowired
	private ICache cache; 

	
	
	/**
	 * 跳转到修改页面
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit(Integer catId){
		ModelAndView view = new ModelAndView();
		List<ArticleCat> catList = catManager.queryCatChildrenInfoByCatIds("46");
		List<NanShanArticleVo> list = new ArrayList<NanShanArticleVo>();
		if(null == catId){
			if(catList != null && catList.size()>0){
				Long articleCatId = catList.get(0).getCat_id();
				list = articleManager.queryArticleByCatId(articleCatId.intValue());
			}
		}else{
			list = articleManager.queryArticleByCatId(catId);
		}
		if(list!=null && list.size()>0){
			view.addObject("data",list.get(0));
		}else{
			NanShanArticleVo articleVo = new NanShanArticleVo();
			if(catId != null){
				articleVo.setCat_id(catId);
			}
			view.addObject("data", articleVo);
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
	public JsonResult saveEdit(NanShanArticleVo articleVo,HttpServletRequest request){
		//保存修改
		try{
			String address = request.getParameter("address");
			JSONObject json = new JSONObject();
			json.put("address",address);
			json.put("content", articleVo.getContent());
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
	
	
	/**
	 * 添加分类
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/add-cat")
	public JsonResult saveCat(ArticleCat articleCat,HttpServletRequest request){
		try{
			articleCat.setParent_id(46);
			if(articleCat.getCat_id()<1){
				catManager.addCat(articleCat);	
			}
			else{
				catManager.updateCat(articleCat);
			}
			this.cache.remove(NanShanCommonConstant.NANSHANCATCACHENAME);
			return JsonResultUtil.getSuccessJson("操作成功");
		}catch(Exception e){
			return JsonResultUtil.getErrorJson("操作失败:"+e.getMessage());
		}		
	}
	
	
	/**
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/del-cat")
	public JsonResult saveCat(int catId,HttpServletRequest request){
		try{
			catManager.delCat(catId);
			this.cache.remove(NanShanCommonConstant.NANSHANCATCACHENAME);
			return JsonResultUtil.getSuccessJson("操作成功");
		}catch(Exception e){
			return JsonResultUtil.getErrorJson("操作失败:"+e.getMessage());
		}		
	}
	
	/**
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/edit-cat")
	public JsonResult editCat(int catId,HttpServletRequest request){
		try{
			ArticleCat cat=catManager.queryCatById(catId);
			return JsonResultUtil.getObjectJson(cat);
		}catch(Exception e){
			return JsonResultUtil.getErrorJson("操作失败:"+e.getMessage());
		}		
	}
	
}
