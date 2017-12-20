package com.enation.app.nanshan.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;








import com.enation.app.nanshan.model.NanShanArticleVo;
import com.enation.app.nanshan.service.INanShanArticleService;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.action.JsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.StringUtil;


/**  
* 
* @author luyanfen
* @date 2017年12月14日 下午4:49:15
*  
*/ 
@Controller
@Scope("prototype")
@RequestMapping("/article")
public class NanShanArticleController extends GridController {
	
	@Autowired
	INanShanArticleService  nanShanArticleService;
	String ctx=ThreadContextHolder.getHttpRequest().getContextPath();
	

	/** 
	* @Description: 
	* @author luyanfen  
	* @date 2017年12月14日 下午5:21:52
	*  
	*/ 
	@ResponseBody
	@RequestMapping("/add_save")
	public JsonResult add(NanShanArticleVo nanShanArticleVo,String createTime){
		try {
			if(!StringUtil.isEmpty(createTime)){
				long create_time = DateUtil.getDateline(createTime, "yyyy-MM-dd HH:mm:ss");
				nanShanArticleVo.setCreate_time(create_time);
			}
			this.nanShanArticleService.addArticle(nanShanArticleVo);			
			return JsonResultUtil.getSuccessJson("添加文章成功");		
		} catch (RuntimeException e) {
			e.printStackTrace();			
			return JsonResultUtil.getErrorJson("添加文章失败");
		}
	}
	/**
	 * 返回所有子分类
	 * @param parentid
	 * @return
	 */
	@ApiOperation(value = "查询南山分类列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "parentid", value = "父id，顶级为0", required = true, dataType = "int", paramType = "query"), })
	@RequestMapping(value = "/cat/{parentid}/children", method = RequestMethod.GET)
	public List list(@PathVariable Integer parentid,String format) {
			//List list = this.categoryManager.list(parentid,format);
		List list = null;
		return list;
	}
	
	@RequestMapping(value="/newsbulletin/list")
	public ModelAndView list() {
		ModelAndView view=new ModelAndView();
		view.setViewName("/nanshan/admin/news/list");
		view.addObject("ctx",ctx);
		return view;
	}
	/**
	 * 商品列表页json
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/newsbulletin/list-json")
	public GridJsonResult listJson() {	
		Page page = this.nanShanArticleService.queryArticleList(null, this.getPage(), this.getPageSize());
		return JsonResultUtil.getGridJson(page);
	}
	



	/** 
	* @param @param a
	* @Description: 添加页面
	* @author luyanfen  
	* @date 2017年12月19日 上午10:35:10
	*  
	*/ 
	@RequestMapping(value = "/newsbulletin/add")
	public ModelAndView  add() {
		ModelAndView view=new ModelAndView();
		view.setViewName("/nanshan/admin/news/add");
		view.addObject("ctx",ctx);
		return view;
	}
	
	/** 
	* @Description: 编辑页面
	* @author luyanfen  
	* @date 2017年12月19日 上午10:35:10
	*  
	*/ 
	@RequestMapping(value = "/newsbulletin/edit")
	public ModelAndView  edit(int id) {
		ModelAndView view=new ModelAndView();
		try {
			view.setViewName("/nanshan/admin/news/edit");
			view.addObject("ctx",ctx);
			view.addObject("data",this.nanShanArticleService.queryArticleById(id));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
		
	}
	
	
	@ResponseBody
	@RequestMapping(value="/newsbulletin/save-edit")
	public JsonResult saveEdit(NanShanArticleVo nanShanArticleVo,String createTime ){
		try {
			if(!StringUtil.isEmpty(createTime)){
				long create_time = DateUtil.getDateline(createTime, "yyyy-MM-dd HH:mm:ss");
				nanShanArticleVo.setCreate_time(create_time);
			}
            this.nanShanArticleService.updateArticle(nanShanArticleVo);
		    return JsonResultUtil.getSuccessJson("修改成功");
		} catch (RuntimeException e) {
			return JsonResultUtil.getErrorJson("修改失败");
		}
	}
	
	@ResponseBody
	@RequestMapping(value="/newsbulletin/del")
	public JsonResult del(int id ){
		try {
            this.nanShanArticleService.delArticle(id);
		    return JsonResultUtil.getSuccessJson("修改成功");
		} catch (RuntimeException e) {
			return JsonResultUtil.getErrorJson("修改失败");
		}
	}
	
	

}
