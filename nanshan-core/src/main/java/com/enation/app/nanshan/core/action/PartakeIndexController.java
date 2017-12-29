package com.enation.app.nanshan.core.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.nanshan.core.service.IArticleManager;
import com.enation.app.nanshan.core.service.ICatManager;
import com.enation.app.nanshan.model.NanShanArticleVo;
import com.enation.framework.action.GridController;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonResultUtil;

/**
 * 参与指数
 * @author jianjianming
 * @version $Id: PartakeIndexController.java,v 0.1 2017年12月29日 下午5:57:50$
 */
@Controller 
@Scope("prototype")
@RequestMapping("/core/admin/partake")
public class PartakeIndexController extends GridController{
	
	@Autowired
	private IArticleManager  articleManager;

	@Autowired
	private ICatManager catManager;
	
	/**
	 * 跳转到修改页面
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit(){
		ModelAndView view = new ModelAndView();
		List<NanShanArticleVo> list = articleManager.queryArticleByCatId(80);
		if(list != null && list.size()>0){
			view.addObject("data",list.get(0));
		}else{
			NanShanArticleVo articleVo = new NanShanArticleVo();
			articleVo.setCat_id(80);
			list.add(articleVo);
			view.addObject("data",list.get(0));
		}
		view.setViewName("/nanshan/admin/partake/page");
		return view;
	}

	/**
	 * 修改
	 * @param articleVo
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/save-edit")
	public JsonResult saveEdit(NanShanArticleVo articleVo,HttpServletRequest request){
		//保存修改
		try{
			JSONArray jsonArray = new JSONArray();
			String[] imgUrl = request.getParameterValues("imgUrl");
			if(imgUrl!=null && imgUrl.length>0){
				for (int i = 0; i < imgUrl.length; i++) {
					JSONObject json = new JSONObject();
					json.put("index", i);
					json.put("imgUrl", imgUrl[i]);
					jsonArray.add(json);
				}
			}
			articleVo.setContent(jsonArray.toString());
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
	
}
