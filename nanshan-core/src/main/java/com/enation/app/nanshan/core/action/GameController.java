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
import com.enation.app.nanshan.core.service.ISpecManager;
import com.enation.app.nanshan.model.NanShanArticleVo;
import com.enation.eop.resource.IThemeUriManager;
import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.resource.model.ThemeUri;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.action.JsonResult;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.StringUtil;
import com.qq.connect.utils.json.JSONObject;

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
	private IThemeUriManager themeUriManager;
	
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
	 * 获取列表JSON
	 * @param keyword 关键字
	 * @return
	 */
	
	@ResponseBody
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/list-json")
	public GridJsonResult listJson(String keyword){
		Page page = this.articleManager.queryArticleList(null, this.getPage(), this.getPageSize());
		return JsonResultUtil.getGridJson(page);
	}
	
	/**
	 * 跳转到增加页面
	 * @return
	 */
	@RequestMapping(value="/add")
	public ModelAndView add(){
		ModelAndView view = new ModelAndView();
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
		view.setViewName("/nanshan/admin/game/add");
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
	public JsonResult add(NanShanArticleVo nanShanArticleVo,String createTime){
		try {
			if(!StringUtil.isEmpty(createTime)){
				long create_time = DateUtil.getDateline(createTime, "yyyy-MM-dd HH:mm:ss");
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
	 * 跳转到属性规格管理
	 * @return 添加页面
	 */
	@RequestMapping(value="/add1")
	public ModelAndView add1(){
		ModelAndView view = new ModelAndView();
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
		view.setViewName("/core/admin/spec/add_context");
		return view;
	}
	
	
	/**
	 *  新增属性规格
	 * @param sepc
	 * @param specvalName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/save-add")
	public JsonResult saveAdd(Spec spec,String[] specValName){
		try{
			List<SpecVal> specValList = new ArrayList<SpecVal>();
			AdminUser user  = UserConext.getCurrentAdminUser();
			String userName = user.getUsername();
			if(specValName!=null && specValName.length >0){
				for(int i=0, len=specValName.length;i<len;i++){
					SpecVal specVal  = new SpecVal();
					specVal.setSpecval_name(specValName[i]);
					specVal.setOperator(userName);
					specValList.add(specVal);
				}
			}
			spec.setOperator(userName);
			this.specManager.add(spec,specValList);
			return JsonResultUtil.getSuccessJson("添加成功");
		}catch(RuntimeException e){
			return JsonResultUtil.getErrorJson("失败:"+e.getMessage());
		}
	}
	
	
	@ResponseBody
	@RequestMapping(value="/getSpecInfo")
	public JsonResult getSpecInfo(Integer articlId,Integer catId){
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			List<Spec> specList = specManager.list(map);
			List<Integer> specIdList = new ArrayList<Integer>();
			for (Spec spec : specList) {
				specIdList.add(spec.getSpec_id());
			}
			map.put("specIdList", specIdList);
			List<SpecVal> specValList = specManager.querySpecValList(map);
			JSONObject jsonInfo = new JSONObject();
			JSONArray  specJsonArray = JSONArray.fromObject(specList);
			JSONArray  specValJsonArray = JSONArray.fromObject(specValList);
			jsonInfo.put("specList", specJsonArray);
			jsonInfo.put("specValList", specValJsonArray);
			return JsonResultUtil.getSuccessJson(jsonInfo.toString());
		}catch(Exception e){
			return JsonResultUtil.getErrorJson("失败:"+e.getMessage());
		}
	}
	
	/**
	 * 跳转到修改页面
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit(int id){
		Spec spec = specManager.querySpecById(id);
		Map<String, Object> map = new HashMap<String, Object>();
		ModelAndView view = new ModelAndView();
		map.put("specId",spec.getSpec_id());
		List<SpecVal> specValList = specManager.querySpecValList(map);
		view.addObject("spec",spec);
		view.addObject("specValList", specValList);
		view.setViewName("/core/admin/spec/edit");
		return view;
	}
	
	/**
	 * 
	 * @param spec
	 * @param specValName
	 * @param spec
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/save-edit")
	public JsonResult saveEdit(Spec spec,String[] specValName,Integer[] specValId){
		AdminUser user  = UserConext.getCurrentAdminUser();
		//保存修改
		try{
			if(spec.getSpec_id() == null){
				return JsonResultUtil.getErrorJson("修改失败:修改参数ID为空.");
			}
			specManager.edit(spec);
			return JsonResultUtil.getSuccessJson("修改成功");
		}catch(RuntimeException e){
			return JsonResultUtil.getErrorJson("修改失败:"+e.getMessage());
		}		
	}
	
	/**
	 * 批量修改
	 * @param uri uri映射数组
	 * @param ids uriId数组
	 * @param path  
	 * @param pagename 页面名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/batch-edit")
	public JsonResult batchEdit(String[] uri,int[] ids,String[] path,String[] pagename,int[] point,int[] httpcache){
		try{
			List<ThemeUri> uriList  = new ArrayList<ThemeUri>();
			if(uri!=null ){
				for(int i=0, len=uri.length;i<len;i++){
					ThemeUri themeUri  = new ThemeUri();
					themeUri.setUri( uri[i] );
					themeUri.setId(ids[i]);
					themeUri.setPath(path[i]);
					themeUri.setPagename(pagename[i]);
					if (point != null) {
						themeUri.setPoint(point[i]);
					}
					if (httpcache != null) {
						themeUri.setHttpcache(httpcache[i]);
					}
					uriList.add(themeUri);
				}
			}
			this.themeUriManager.edit(uriList);
			return JsonResultUtil.getSuccessJson("保存修改成功");
		}catch(RuntimeException e){
			e.printStackTrace();
			return JsonResultUtil.getErrorJson("失败:"+e.getMessage());
		}
	}
	
	@ResponseBody
	@RequestMapping(value="/delete")
	public JsonResult delete(int id){
		
		AdminUser user  = UserConext.getCurrentAdminUser();
		if(EopSetting.IS_DEMO_SITE && user!= null && user.getFounder() != 1){
			if(id<=6){
				return JsonResultUtil.getErrorJson("抱歉，当前为演示站点，以不能删除这些示例数据，请下载安装包在本地体验这些功能！");
			}
		}
		
		try{
			this.themeUriManager.delete(id);
			return JsonResultUtil.getSuccessJson("删除成功");

		}catch(RuntimeException e){
			return JsonResultUtil.getErrorJson("删除失败:"+e.getMessage());

		}
	}
	
	
	
}
