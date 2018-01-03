package com.enation.app.nanshan.core.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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
import com.enation.app.nanshan.model.ArticleQueryParam;
import com.enation.app.nanshan.model.NanShanArticleVo;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.action.JsonResult;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonResultUtil;

/**
 * 游戏管理
 * @author jianjianming
 * @version $Id: GameController.java,v 0.1 2017年12月21日 下午9:09:54$
 */
@Controller 
@Scope("prototype")
@RequestMapping("/core/admin/game")
public class GameController extends GridController{
	
	@Autowired
	private IArticleManager  articleManager;
	
	@Autowired
	private ICatManager catManager;
	
	@Autowired
	private ISpecManager specManager;
	
	/**
	 * 跳转到管理列表
	 * @return
	 */
	@RequestMapping(value="/list")
	public String list(){
		return "/nanshan/admin/game/list";
	}
	
	/**
	 * (分类为:游戏)获取列表JSON
	 * @return
	 */
	@ResponseBody
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/list-json")
	public GridJsonResult listJson(ArticleQueryParam param){
		Map<String, Object> params = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(param.getArticleName())) params.put("title", param.getArticleName());
		if(StringUtils.isNotBlank(param.getArticleId())) params.put("id", param.getArticleId());
		if(StringUtils.isNotBlank(param.getIsDel())) params.put("is_del", param.getIsDel());
		if(StringUtils.isNotBlank(param.getStartDate())) params.put("startDate", param.getStartDate());
		if(StringUtils.isNotBlank(param.getEndDate())) params.put("endDate", param.getEndDate());
		params.put("catParentIds","21");//父分类为(游戏)下的数据
		Page page = this.articleManager.queryArticleListByConiditon(params, this.getPage(), this.getPageSize());
		return JsonResultUtil.getGridJson(page);
	}
	
	/**
	 * 跳转到增加页面
	 * @return
	 */
	@RequestMapping(value="/add")
	public ModelAndView add(){
		ModelAndView view = new ModelAndView();
		String specIds = getCatIsContainSpecIds("24,26");
		if(StringUtils.isNotBlank(specIds)){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("specId", specIds);
			List<Spec> specList = specManager.list(map);
			List<Integer> specIdList = new ArrayList<Integer>();
			for (Spec spec : specList) {
				specIdList.add(spec.getSpec_id());
			}
			map.put("specIdList", specIdList);
			map.put("is_valid", "0");
			List<SpecVal> specValList = specManager.querySpecValList(map);
			if(specValList!=null && specValList.size()>0){
				view.addObject("specList",specList);
				view.addObject("specValList", specValList);
			}
		}
		//查询(游戏)分类
		List<ArticleCat> catList = catManager.queryCatInfoByCatIds("24,26");
		view.addObject("catList", catList);
		view.setViewName("/nanshan/admin/game/add");
		return view;
	}
	
	/**
	 * 查询分类是否包含基础属性
	 * @param catIds
	 * @return
	 */
	private String getCatIsContainSpecIds(String catIds) {
		String specIds = "";
		List<ArticleCat> catList = catManager.queryCatInfoByCatIds(catIds);
		if(catList!=null && catList.size()>0){
			for (ArticleCat articleCat : catList) {
				specIds += articleCat.getSpec_id()+",";
			}
			if(StringUtils.isNotEmpty(specIds.toString())){
				specIds = specIds.substring(0,specIds.length()-1);
				specIds = specIds.replaceAll(";",",");
			}
		}
		return specIds;
	}

	/**
	 * 增加展示展览
	 * @param nanShanArticleVo
	 * @param createTime
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/add_save")
	public JsonResult add(NanShanArticleVo nanShanArticleVo){
		try {
			nanShanArticleVo.setCreate_time(DateUtil.getDateline());
			this.articleManager.addArticle(nanShanArticleVo);			
			return JsonResultUtil.getSuccessJson("添加成功.");
		} catch (RuntimeException e) {
			e.printStackTrace();
			return JsonResultUtil.getErrorJson("添加失败.");
		}
	}
	
	/**
	 * 跳转到修改页面
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/edit")
	public ModelAndView edit(int id) {
		ModelAndView view=new ModelAndView();
		String specIds = getCatIsContainSpecIds("24,26");
		if(StringUtils.isNotBlank(specIds)){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("specId", specIds);
			List<Spec> specList = specManager.list(map);
			List<Integer> specIdList = new ArrayList<Integer>();
			for (Spec spec : specList) {
				specIdList.add(spec.getSpec_id());
			}
			map.put("specIdList", specIdList);
			map.put("is_valid", "0");
			List<SpecVal> specValList = specManager.querySpecValList(map);
			if(specValList!=null && specValList.size()>0){
				view.addObject("specList",specList);
				view.addObject("specValList", specValList);
			}
		}
		//查询(游戏)分类
		List<ArticleCat> catList = catManager.queryCatInfoByCatIds("24,26");
		view.addObject("catList", catList);
		view.addObject("data",this.articleManager.queryArticleById(id));
		view.setViewName("/nanshan/admin/game/edit");
		return view;
	}
	
	
	/**
	 * 保存修改
	 * @param nanShanArticleVo
	 * @param createTime
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/save-edit")
	public JsonResult saveEdit(NanShanArticleVo nanShanArticleVo){
		try {
            this.articleManager.updateArticle(nanShanArticleVo);
		    return JsonResultUtil.getSuccessJson("修改成功");
		} catch (RuntimeException e) {
			return JsonResultUtil.getErrorJson("修改失败");
		}
	}
	
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/delete")
	public JsonResult del(int id ){
		try {
            this.articleManager.delArticle(id);
		    return JsonResultUtil.getSuccessJson("删除成功");
		} catch (RuntimeException e) {
			return JsonResultUtil.getErrorJson("删除失败");
		}
	}
	
	
	/**
	 * 跳转到热门游戏管理
	 * @return
	 */
	@RequestMapping(value="/hotGame")
	public String hotGame(){
		return "/nanshan/admin/game/hotGame";
	}
}
