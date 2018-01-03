package com.enation.app.nanshan.core.action;

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
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
	@Autowired
	ICatManager catManager;
	

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
				long create_time = DateUtil.getDateline(createTime, "yyyy-MM-dd hh:mm:ss");
				nanShanArticleVo.setCreate_time(create_time);
			}
			this.articleManager.addArticle(nanShanArticleVo);
			return JsonResultUtil.getSuccessJson("添加文章成功");		
		} catch (RuntimeException e) {
			e.printStackTrace();			
			return JsonResultUtil.getErrorJson("添加文章失败");
		}
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
				long create_time = DateUtil.getDateline(createTime, "yyyy-MM-dd hh:mm:ss");
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
		map.put("specId", getCatIsContainSpecIds("38"));
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

			NanShanArticleVo nanShanArticleVo = this.articleManager.queryArticleById(id);

			view.setViewName("/nanshan/admin/act/edit");
			view.addObject("ctx",ctx);
			view.addObject("data", nanShanArticleVo);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("specId", getCatIsContainSpecIds("38"));

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
	
	
	
	
	/** 
	* @param @param catId
	* @param @return
	* @Description: 游戏排行
	* @author luyanfen  
	* @date 2017年12月27日 上午10:10:15
	*  
	*/ 
	@RequestMapping(value = "gamerank")
	public ModelAndView gameRank(int catId) {
		ModelAndView view=new ModelAndView();
		try {
			NanShanArticleVo   vo=this.articleManager.queryArtByCatId(catId);
			view.setViewName("/nanshan/admin/game/addrank");
			view.addObject("ctx",ctx);	
			view.addObject("vo",vo);	
			view.addObject("catId",catId);	
			return view;
		} catch (Exception e) {
			e.printStackTrace();
			return view;
		}
		
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
			NanShanArticleVo vo= this.articleManager.queryArtByCatId(nanShanArticleVo.getCat_id());
			if(vo==null){
				this.articleManager.addArticle(nanShanArticleVo);
			}else{
				this.articleManager.updateArticle(nanShanArticleVo);	
			}
			return JsonResultUtil.getSuccessJson("添加更改游戏排行成功");		
		} catch (RuntimeException e) {
			e.printStackTrace();			
			return JsonResultUtil.getErrorJson("添加更改游戏排行失败");
		}
	}
	
	
	/** 
	* @param @param catId 
	* @param @return
	* @Description: 影片拍片
	* @author luyanfen  
	* @date 2017年12月27日 上午10:09:35
	*  
	*/ 
	@RequestMapping(value = "cinema-time")
	public ModelAndView movie(int catId) {
		NanShanArticleVo   vo=this.articleManager.queryArtByCatId(catId);
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
			NanShanArticleVo vo= this.articleManager.queryArtByCatId(nanShanArticleVo.getCat_id());
			if(vo==null){
				this.articleManager.addArticle(nanShanArticleVo);
			}else{
				this.articleManager.updateArticle(nanShanArticleVo);
			}
			return JsonResultUtil.getSuccessJson("操作成功");		
		} catch (RuntimeException e) {
			e.printStackTrace();			
			return JsonResultUtil.getErrorJson("操作失败");
		}
	}
	
	/** 
	* @param @param catId
	* @param @return
	* @Description: 4D影院
	* @author luyanfen  
	* @date 2017年12月27日 上午10:09:00
	*  
	*/ 
	@RequestMapping(value = "cinema4d")
	public ModelAndView fourDcinema(int catId) {
		ModelAndView view=new ModelAndView();
		try {
			NanShanArticleVo   vo=this.articleManager.queryArtByCatId(catId);
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
	
	
	 
	/** 
	* @param @param catId
	* @param @return
	* @Description:推荐路线 
	* @author luyanfen  
	* @date 2017年12月27日 上午10:11:45
	*  
	*/ 
	@RequestMapping(value="route")
	public ModelAndView recroute(int catId,NanShanArticleVo nanShanArticleVo) {
		ModelAndView view=new ModelAndView();
		try {
			
			NanShanArticleVo   vo=new NanShanArticleVo();
			if(nanShanArticleVo.getCat_id()!=0){
				vo=this.articleManager.queryArtByCatId(Integer.valueOf(nanShanArticleVo.getCat_id()));
			}
			view.setViewName("/nanshan/admin/guide/navroute");
			view.addObject("ctx",ctx);	
			if(vo==null){
				vo=new NanShanArticleVo();
				vo.setCat_id(nanShanArticleVo.getCat_id());
			}
			view.addObject("vo",vo);	
			view.addObject("cats",this.articleManager.getCats(catId));
			return view;
		} catch (Exception e) {
			e.printStackTrace();
			return view;
		}
	}
	
	/** 
	* @param @param catId
	* @param @return
	* @Description: 人才招聘页面
	* @author luyanfen  
	* @date 2017年12月27日 上午10:09:00
	*  
	*/ 
	@RequestMapping(value = "employ")
	public ModelAndView employ(int catId) {
		ModelAndView view=new ModelAndView();
		view.setViewName("/nanshan/admin/employ/list");
		view.addObject("ctx",ctx);
		view.addObject("catId",catId);
		return view;
	}
	

	/** 
	* @param @param catId
	* @param @return
	* @Description: 
	* @author luyanfen  
	* @date 2018年1月2日 下午2:55:33
	*  
	*/ 
	@RequestMapping(value = "/employ/add")
	public ModelAndView addEmploy(int catId) {
		ModelAndView view=new ModelAndView();
		view.setViewName("/nanshan/admin/employ/add");
		view.addObject("ctx",ctx);
		view.addObject("catId",catId);
		return view;
	}
	

	/** 
	* @param @param id
	* @param @param catId
	* @param @return
	* @Description: 
	* @author luyanfen  
	* @date 2018年1月2日 下午2:57:13
	*  
	*/ 
	@RequestMapping(value = "/employ/edit")
	public ModelAndView editEmploy(int id,int catId) {
		ModelAndView view=new ModelAndView();
		try {
			view.setViewName("/nanshan/admin/employ/edit");
			view.addObject("ctx",ctx);
			view.addObject("data",this.articleManager.queryArticleById(id));
		} catch (Exception e) {
			e.printStackTrace();
		}
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

}
