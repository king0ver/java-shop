package com.enation.app.nanshan.core.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

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
 * 影片管理
 * @author jianjianming
 * @version $Id: MovieController.java,v 0.1 2017年12月21日 下午9:07:49$
 */
@Controller 
@Scope("prototype")
@RequestMapping("/core/admin/movie")
public class MovieController extends GridController{
	
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
		return "/nanshan/admin/movie/list";
	}
	
	/**
	 * 获取列表JSON
	 * @param keyword 关键字
	 * @return
	 */
	@ResponseBody
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/list-json")
	public GridJsonResult listJson(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("catIds","13");
		Page page = this.articleManager.queryArticleListByConiditon(params, this.getPage(), this.getPageSize());
		return JsonResultUtil.getGridJson(page);
	}
	
	/**
	 * 跳转到（影片）的增加页面
	 * @return
	 */
	@RequestMapping(value="/add")
	public ModelAndView add(){
		ModelAndView view = new ModelAndView();
		//查询(影片预告)三级分类
		List<ArticleCat> catList = catManager.queryCatInfoByCatIds("13");
		view.addObject("catList", catList);
		view.setViewName("/nanshan/admin/movie/add");
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
	public JsonResult add(NanShanArticleVo nanShanArticleVo,HttpServletRequest request){
		try {
			String movieTimeLength = request.getParameter("movieTimeLength");//电影时长
			String movieTime = request.getParameter("movieTime");//放映时间
			String movieAddr = request.getParameter("movieAddr");//放映地点
			String movieIntroduce = request.getParameter("movieIntroduce");//影片介绍
			String filmTidbits = request.getParameter("filmTidbits");//电影详情花絮
			nanShanArticleVo.setCreate_time(DateUtil.getDateline());
			JSONObject movieInfo = new JSONObject();
			movieInfo.put("movieTimeLength", movieTimeLength);
			movieInfo.put("movieTime", movieTime);
			movieInfo.put("movieAddr",movieAddr);
			movieInfo.put("movieIntroduce",movieIntroduce);
			movieInfo.put("filmTidbits",filmTidbits);
			nanShanArticleVo.setContent(movieInfo.toString());
			this.articleManager.addArticle(nanShanArticleVo);			
			return JsonResultUtil.getSuccessJson("电影添加成功.");		
		} catch (RuntimeException e) {
			e.printStackTrace();
			return JsonResultUtil.getErrorJson("电影添加失败.");
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
		//查询(影片预告)三级分类
		List<ArticleCat> catList = catManager.queryCatInfoByCatIds("13");
		view.addObject("catList", catList);
		view.setViewName("/nanshan/admin/movie/edit");
		view.addObject("data",this.articleManager.queryArticleById(id));
		return view;
	}
	
}
