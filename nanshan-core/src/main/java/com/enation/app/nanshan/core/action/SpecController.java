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
import com.enation.app.nanshan.core.service.ISpecManager;
import com.enation.eop.resource.IThemeUriManager;
import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonResultUtil;
import com.qq.connect.utils.json.JSONObject;

/**
 * 基础数据属性规格信息管理
 * @author jianjianming
 * @version $Id: ThemeUriController.java,v 0.1 2017年12月13日 下午4:25:58$
 */
@Controller 
@Scope("prototype")
@RequestMapping("/core/admin/spec")
public class SpecController extends GridController{
	
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
		return "/nanshan/admin/spec/list";
	}
	
	/**
	 * 获取列表JSON
	 * @param keyword 关键字
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/list-json")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public GridJsonResult listJson(String keyword){
		Map map = new HashMap();
		map.put("keyword", keyword);
		List specList  = specManager.list(map);
		return JsonResultUtil.getGridJson(specList);
	}
	
	/**
	 * 跳转到属性规格管理
	 * @return 添加页面
	 */
	@RequestMapping(value="/add")
	public String add(){
		return "/nanshan/admin/spec/add";
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
					specVal.setIs_valid(0);
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
		map.put("specId",String.valueOf(spec.getSpec_id()));
		map.put("is_valid", "0");
		List<SpecVal> specValList = specManager.querySpecValList(map);
		view.addObject("spec",spec);
		view.addObject("specValList", specValList);
		view.setViewName("/nanshan/admin/spec/edit");
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
	public JsonResult saveEdit(Spec spec,String[] specValName,Integer[] specValId){
		AdminUser user  = UserConext.getCurrentAdminUser();
		//保存修改
		try{
			if(spec.getSpec_id() == null){
				return JsonResultUtil.getErrorJson("修改失败:修改参数ID为空.");
			}
			spec.setOperator(user.getUsername());
			List<SpecVal> specValList = new ArrayList<SpecVal>();
			for (int i = 0; i < specValName.length; i++) {
				int specValIdLength = specValId.length;
				SpecVal specVal = new SpecVal();
				if( i < specValIdLength){
					specVal.setSpecval_id(specValId[i]);
					specVal.setSpecval_name(specValName[i]);
					specVal.setUpdate_time(DateUtil.getDateline());
					specValList.add(specVal);
					continue;
				}
				specVal.setSpec_id(spec.getSpec_id());
				specVal.setSpecval_name(specValName[i]);
				specVal.setIs_valid(0);
				specVal.setCreation_time(DateUtil.getDateline());
				specValList.add(specVal);
			}
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("spec", spec);
			params.put("specValList", specValList);
			specManager.updateSpecInfo(params);
			return JsonResultUtil.getSuccessJson("修改成功");
		}catch(RuntimeException e){
			return JsonResultUtil.getErrorJson("修改失败:"+e.getMessage());
		}		
	}
	
}
