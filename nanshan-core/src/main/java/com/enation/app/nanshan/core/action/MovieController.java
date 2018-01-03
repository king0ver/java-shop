package com.enation.app.nanshan.core.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.base.upload.UploadFactory;
import com.enation.app.base.upload.plugin.IUploader;
import com.enation.app.nanshan.core.service.IArticleManager;
import com.enation.app.nanshan.core.service.ICatManager;
import com.enation.app.nanshan.model.ArticleCat;
import com.enation.app.nanshan.model.ArticleQueryParam;
import com.enation.app.nanshan.model.NanShanArticleVo;
import com.enation.app.nanshan.service.IArticleService;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.action.JsonResult;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.FileUtil;
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
	
	@Autowired
	private IArticleService articleService;
	
	@Autowired
	private UploadFactory uploadFactory;

	/**
	 * 上传附件
	 * @param file 附件
	 * @param fileFileName 附件名称
	 * @param subFolder 附件存放文件夹
	 * @param path 上传后的图片路径
	 * @param ajax 是否为异步提交
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/upload")
	public JSONObject uploadFile(MultipartFile file){
		JSONObject json = new JSONObject();
		String path = null;
		if (file != null && file.getOriginalFilename() != null) {
			try{
				if(!FileUtil.isAllowUpImg(file.getOriginalFilename())){
					throw new IllegalArgumentException("不允许上传的文件格式，请上传gif,jpg,bmp格式文件。");
				}
				IUploader uploader= uploadFactory.getUploader();
				path = uploader.upload(file);
			}catch(IllegalArgumentException e){
				throw new IllegalArgumentException(e.toString());
			}
			json.put("url", path);
		}else{
			throw new IllegalArgumentException("没有文件");
		}
		return json;
	}
	
	/**
	 * 跳转到管理列表
	 * @return
	 */
	@RequestMapping(value="/list")
	public String list(){
		articleService.queryArticleInfoById(97);
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
	public GridJsonResult listJson(ArticleQueryParam param){
		Map<String, Object> params = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(param.getArticleName())) params.put("title", param.getArticleName());
		if(StringUtils.isNotBlank(param.getArticleId())) params.put("id", param.getArticleId());
		if(StringUtils.isNotBlank(param.getIsDel())) params.put("is_del", param.getIsDel());
		if(StringUtils.isNotBlank(param.getStartDate())) params.put("startDate", param.getStartDate());
		if(StringUtils.isNotBlank(param.getEndDate())) params.put("endDate", param.getEndDate());
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
	 * 增加
	 * @param nanShanArticleVo
	 * @param request
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
	
	

	/**
	 * 保存修改
	 * @param nanShanArticleVo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/save-edit")
	public JsonResult saveEdit(NanShanArticleVo nanShanArticleVo,HttpServletRequest request){
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
