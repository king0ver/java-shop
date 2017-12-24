package com.enation.app.nanshan.core.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.nanshan.core.service.IArticleManager;
import com.enation.app.nanshan.core.service.ICatManager;
import com.enation.app.nanshan.model.ArticleCat;
import com.enation.app.nanshan.model.NanShanArticleVo;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.action.JsonResult;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonResultUtil;

/**
 * 线上体验管理
 * @author jianjianming
 * @version $Id: MovieController.java,v 0.1 2017年12月21日 下午9:07:49$
 */
@Controller 
@Scope("prototype")
@RequestMapping("/core/admin/experience")
public class ExperienceController extends GridController{
	
	@Autowired
	private IArticleManager  articleManager;
	
	@Autowired
	private ICatManager catManager;
	
	
	/**
	 * 跳转到管理列表
	 * @return
	 */
	@RequestMapping(value="/list")
	public String list(){
		return "/nanshan/admin/experience/list";
	}
	
	
	/**
	 * (分类为:仿真体验馆)获取列表JSON
	 * @return
	 */
	@ResponseBody
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/list-json")
	public GridJsonResult listJson(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("catParentIds","22");//父分类为(仿真体验馆)下的数据
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
		//查询(仿真体验馆)下的全部三级分类
		List<ArticleCat> catList = catManager.queryCatChildrenInfoByCatIds("22");
		view.addObject("catList", catList);
		view.setViewName("/nanshan/admin/experience/add");
		return view;
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
		//查询(仿真体验馆)下的全部三级分类
		List<ArticleCat> catList = catManager.queryCatChildrenInfoByCatIds("22");
		view.addObject("catList", catList);
		view.setViewName("/nanshan/admin/experience/edit");
		view.addObject("data",this.articleManager.queryArticleById(id));
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
	
	
}
