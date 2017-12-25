package com.enation.app.nanshan.core.action;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;




import com.enation.app.nanshan.constant.DefaultArticleIdEnum;
import com.enation.app.nanshan.core.model.Spec;
import com.enation.app.nanshan.core.model.SpecVal;
import com.enation.app.nanshan.core.service.IArticleManager;
import com.enation.app.nanshan.core.service.ISpecManager;
import com.enation.app.nanshan.model.ArtSpecRel;
import com.enation.app.nanshan.model.ArticleQueryParam;
import com.enation.app.nanshan.model.NanShanArticleVo;
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
@RequestMapping("/admin/article")
public class ArticleController extends GridController {
	String ctx=ThreadContextHolder.getHttpRequest().getContextPath();
	@Autowired
	IArticleManager  articleManager;
	@Autowired
	ISpecManager specManager;
	

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
				long create_time = DateUtil.getDateline(createTime, "yyyy-MM-dd");
				nanShanArticleVo.setCreate_time(create_time);
			}
			this.articleManager.addArticle(nanShanArticleVo);
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
	
	@RequestMapping(value="/list")
	public ModelAndView list(long catId) {
		ModelAndView view=new ModelAndView();
		view.setViewName("/nanshan/admin/news/list");
		view.addObject("ctx",ctx);
		view.addObject("catId",catId);
		return view;
	}
	/**
	 * 商品列表页json
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/list-json")
	public GridJsonResult listJson(ArticleQueryParam p) {		
		Page page = this.articleManager.queryArticleList(p, this.getPage(), this.getPageSize());
		return JsonResultUtil.getGridJson(page);
	}
	



	/** 
	* @param 
	* @Description: 添加页面
	* @author luyanfen  
	* @date 2017年12月19日 上午10:35:10
	*  
	*/ 
	@RequestMapping(value = "/add")
	public ModelAndView  add(int catId) {
		ModelAndView view=new ModelAndView();
		view.setViewName("/nanshan/admin/news/add");
		view.addObject("ctx",ctx);
		view.addObject("cats",this.articleManager.getCats(catId));
		view.addObject("catId",catId);
		return view;
	}
	
	/** 
	* @Description: 编辑页面
	* @author luyanfen  
	* @date 2017年12月19日 上午10:35:10
	*  
	*/ 
	@RequestMapping(value = "/edit")
	public ModelAndView  edit(int id,int catId) {
		ModelAndView view=new ModelAndView();
		try {
			view.setViewName("/nanshan/admin/news/edit");
			view.addObject("ctx",ctx);
			view.addObject("data",this.articleManager.queryArticleById(id));
			view.addObject("cats",this.articleManager.getCats(catId));
			view.addObject("catId",catId);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
		
	}
	
	
	@ResponseBody
	@RequestMapping(value="/save-edit")
	public JsonResult saveEdit(NanShanArticleVo nanShanArticleVo,String createTime ){
		try {
			if(!StringUtil.isEmpty(createTime)){
				long create_time = DateUtil.getDateline(createTime, "yyyy-MM-dd");
				nanShanArticleVo.setCreate_time(create_time);
			}
            this.articleManager.updateArticle(nanShanArticleVo);
		    return JsonResultUtil.getSuccessJson("修改成功");
		} catch (RuntimeException e) {
			return JsonResultUtil.getErrorJson("修改失败");
		}
	}
	
	@ResponseBody
	@RequestMapping(value="/del")
	public JsonResult del(int id ){
		try {
            this.articleManager.delArticle(id);
		    return JsonResultUtil.getSuccessJson("修改成功");
		} catch (RuntimeException e) {
			return JsonResultUtil.getErrorJson("修改失败");
		}
	}
	
	/** 
	* @param @param 
	* @Description: 添加活动页面
	* @author luyanfen  
	* @date 2017年12月19日 上午10:35:10
	*  
	*/ 
	@RequestMapping(value = "/act/add")
	public ModelAndView  actList(int catId) {
		ModelAndView view=new ModelAndView();
		view.setViewName("/nanshan/admin/act/add");
		view.addObject("ctx",ctx);
		view.addObject("catId",catId);
		Map<String, Object> map = new HashMap<String, Object>();
		List<Spec> specList = specManager.list(map);
		List<Integer> specIdList = new ArrayList<Integer>();
		for (Spec spec : specList) {
			specIdList.add(spec.getSpec_id());
		}
		map.put("specIdList", specIdList);
		List<SpecVal> specValList = specManager.querySpecValList(map);
		view.addObject("specList",specList);
		view.addObject("specValList", specValList);
		return view;
	}
	
	/** 
	* @param @param 
	* @Description: 添加活动页面
	* @author luyanfen  
	* @date 2017年12月19日 上午10:35:10
	*  
	*/ 
	@RequestMapping(value = "/act/list")
	public ModelAndView  addAct(int catId) {
		ModelAndView view=new ModelAndView();
		view.setViewName("/nanshan/admin/act/list");
		view.addObject("ctx",ctx);		
		view.addObject("catId",catId);		
		return view;
	}
	
	
	/** 
	* @Description: 编辑页面
	* @author luyanfen  
	* @date 2017年12月19日 上午10:35:10
	*  
	*/ 
	@RequestMapping(value = "/act/edit")
	public ModelAndView  actEdit(int id,int catId) {
		ModelAndView view=new ModelAndView();
		try {
			view.setViewName("/nanshan/admin/act/edit");
			view.addObject("ctx",ctx);
			view.addObject("data",this.articleManager.queryArticleById(id));
			Map<String, Object> map = new HashMap<String, Object>();
			List<Spec> specList = specManager.list(map);
			List<Integer> specIdList = new ArrayList<Integer>();
			for (Spec spec : specList) {
				specIdList.add(spec.getSpec_id());
			}
			map.put("specIdList", specIdList);
			List<SpecVal> specValList = specManager.querySpecValList(map);
			view.addObject("specList",specList);
			view.addObject("specValList", specValList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
		
	}
	
	
	/** 
	* @param @param 
	* @Description: 添加活动页面
	* @author luyanfen  
	* @date 2017年12月19日 上午10:35:10
	*  
	*/ 
	@RequestMapping(value = "/imgPanel")
	public ModelAndView  imgPanel(String panelId) {
		ModelAndView view=new ModelAndView();
		view.setViewName("/nanshan/common/img");
		view.addObject("ctx",ctx);		
		view.addObject("panelId",panelId);		
		return view;
	}
	
	
	
	
	@RequestMapping(value = "gamerank")
	public ModelAndView gameRank(int catId) {
		int id=2;
		NanShanArticleVo    vo=this.articleManager.queryArticleById(id);
		ModelAndView view=new ModelAndView();
		view.setViewName("/nanshan/admin/game/addrank");
		view.addObject("ctx",ctx);	
		view.addObject("vo",vo);	
		view.addObject("catId",catId);	
		return view;
	}
	
	
	/** 
	* @Description: 
	* @author luyanfen  
	* @date 2017年12月14日 下午5:21:52
	*  
	*/ 
	@ResponseBody
	@RequestMapping("/add_edit_rank")
	public JsonResult addEditRank(NanShanArticleVo nanShanArticleVo){
		try {
			nanShanArticleVo.setId(2);
			NanShanArticleVo vo= this.articleManager.queryArticleById(nanShanArticleVo.getId());
			if(vo==null){
				this.articleManager.addArticle(nanShanArticleVo);
			}
			this.articleManager.updateArticle(nanShanArticleVo);
			return JsonResultUtil.getSuccessJson("添加更改游戏排行成功");		
		} catch (RuntimeException e) {
			e.printStackTrace();			
			return JsonResultUtil.getErrorJson("添加更改游戏排行失败");
		}
	}
	
	
	@RequestMapping(value = "cinema-time")
	public ModelAndView movie(int catId) {
		int id=DefaultArticleIdEnum.MOVIETIME.getId();
		NanShanArticleVo   vo=this.articleManager.queryArticleById(id);
		ModelAndView view=new ModelAndView();
		view.setViewName("/nanshan/admin/cinema/add");
		view.addObject("ctx",ctx);	
		view.addObject("vo",vo);
		view.addObject("catId",catId);	
		return view;
	}
	
	@ResponseBody
	@RequestMapping(value = "cinema-edit")
	public JsonResult cinemaEdit(NanShanArticleVo nanShanArticleVo) {
		try {
			/*nanShanArticleVo.setId(DefaultArticleIdEnum.CINEMA4D.getId());
			System.out.println(DefaultArticleIdEnum.CINEMA4D.getId());*/
			NanShanArticleVo vo= this.articleManager.queryArticleById(nanShanArticleVo.getId());
			if(vo==null){
				this.articleManager.addArticle(nanShanArticleVo);
			}
			this.articleManager.updateArticle(nanShanArticleVo);
			return JsonResultUtil.getSuccessJson("操作成功");		
		} catch (RuntimeException e) {
			e.printStackTrace();			
			return JsonResultUtil.getErrorJson("操作失败");
		}
	}
	
	@RequestMapping(value = "cinema4d")
	public ModelAndView fourDcinema(int catId) {
		ModelAndView view=new ModelAndView();
		try {
			int id=DefaultArticleIdEnum.CINEMA4D.getId();
			NanShanArticleVo   vo=this.articleManager.queryArticleById(id);
			
			view.setViewName("/nanshan/admin/cinema/cinema4d");
			view.addObject("ctx",ctx);	
			view.addObject("vo",vo);
			view.addObject("catId",catId);	
			return view;
		} catch (Exception e) {
			e.printStackTrace();
			return view;
		}
		
	}
	
	
	
	
	
	
	
	

}
