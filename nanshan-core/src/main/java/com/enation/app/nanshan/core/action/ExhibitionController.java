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
 * 展示展览管理
 * @author jianjianming
 * @version $Id: ExhibitionController.java,v 0.1 2017年12月21日 下午9:23:52$
 */
@Controller 
@Scope("prototype")
@RequestMapping("/core/admin/exhibition")
public class ExhibitionController extends GridController{
	
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
		return "/nanshan/admin/exhibition/list";
	}
	
	/**
	 * 获取列表JSON
	 * @param keyword 关键字
	 * @return
	 */
	@ResponseBody
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/list-json")
	public GridJsonResult listJson(String keyword){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("catParentIds","9,10");
		Page page = this.articleManager.queryArticleListByConiditon(params, this.getPage(), this.getPageSize());
		return JsonResultUtil.getGridJson(page);
	}
	
	/**
	 * 跳转到（临时展览、常设展览）的增加页面
	 * @return
	 */
	@RequestMapping(value="/add")
	public ModelAndView add(){
		ModelAndView view = new ModelAndView();
		//查询(临时展览、常设展览)下的三级分类
		List<ArticleCat> catList = catManager.queryCatChildrenInfoByCatIds("9,10");
		view.addObject("catList", catList);
		view.setViewName("/nanshan/admin/exhibition/add");
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
		//查询(临时展览、常设展览)下的三级分类
		List<ArticleCat> catList = catManager.queryCatChildrenInfoByCatIds("9,10");
		view.addObject("catList", catList);
		view.setViewName("/nanshan/admin/exhibition/edit");
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
