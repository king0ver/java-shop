package com.enation.app.shop.goods.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.base.upload.UploadFactory;
import com.enation.app.base.upload.plugin.IUploader;
import com.enation.app.shop.goods.model.po.Category;
import com.enation.app.shop.goods.model.vo.GoodsParamsList;
import com.enation.app.shop.goods.model.vo.SelectVo;
import com.enation.app.shop.goods.service.IBrandManager;
import com.enation.app.shop.goods.service.ICategoryManager;
import com.enation.app.shop.goods.service.ICategoryParamsManager;
import com.enation.app.shop.goods.service.ISpecManager;
import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.util.FileUtil;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.UnProccessableServiceException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * 商品分类后台controller
 * 
 * @author fk yanlin
 * @version v1.0 v2.0(微服务迁移过来的)
 * @since v6.4.0
 * @date 2017年3月31日 上午10:12:41,2017年8月17日 下午4:39:29
 */
@Api(description = "管理员对商品分类维护接口，读操作")
@RestController
@RequestMapping("/goods-info")
public class CategoryBackController {

	@Autowired
	private ICategoryManager categoryManager;
	@Autowired
	private ISpecManager specManager;
	@Autowired
	private IBrandManager brandManager;
	@Autowired
	private ICategoryParamsManager categoryParamsManager;
	@Autowired
	private UploadFactory uploadFactory;

	// 进入品牌页面
	@RequestMapping(value = "/admin/brand-page")
	public ModelAndView brand() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/shop/admin/cat/category_brand");
		return view;
	}

	// 进入规格页面
	@RequestMapping(value = "/admin/spec-page")
	public ModelAndView spec() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/shop/admin/cat/category_spec");
		return view;
	}

	/**
	 * 返回所有子分类
	 * 
	 * @param parentid
	 * @return
	 */
	@ApiOperation(value = "查询分类列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "parentid", value = "父id，顶级为0", required = true, dataType = "int", paramType = "query"), })
	@RequestMapping(value = "/category/{parentid}/children", method = RequestMethod.GET)
	public List list(@PathVariable Integer parentid,String format) {
			List list = this.categoryManager.list(parentid,format);
			return list;
		
	}

	@ApiOperation(value = "查询某一个商品分类")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "分类id", required = true, dataType = "int", paramType = "path"), })
	@RequestMapping(value = "/admin/category/{id}", method = RequestMethod.GET)
	public Category getCategory(@PathVariable Integer id) {

		Category category = this.categoryManager.get(id);

		return category;
	}


	@ApiOperation(value = "查询分类规格", notes = "查询所有规格，包括分类绑定的规格，selected为true")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "category_id", value = "分类id", required = true, dataType = "int", paramType = "path"), })
	@RequestMapping(value = "/admin/category/{category_id}/spec", method = RequestMethod.GET)
	public List<SelectVo> getCatSpecs(@PathVariable Integer category_id) {

		List<SelectVo> brands = specManager.getCatSpec(category_id);

		return brands;
	}

	@ApiOperation(value = "查询分类品牌", notes = "查询所有品牌，包括分类绑定的品牌，select为true")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "category_id", value = "分类id", required = true, dataType = "int", paramType = "path"), })
	@RequestMapping(value = "/admin/category/{category_id}/brand", method = RequestMethod.GET)
	public List<SelectVo> getCatBrands(@PathVariable Integer category_id) {

		List<SelectVo> brands = brandManager.getCatBrand(category_id);

		return brands;
	}

	@ApiOperation(value = "查询分类参数", notes = "查询分类绑定的参数")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "category_id", value = "分类id", required = true, dataType = "int", paramType = "path"), })
	@RequestMapping(value = "/admin/category/{category_id}/param", method = RequestMethod.GET)
	public List<GoodsParamsList> getCatParam(@PathVariable Integer category_id) {
		return categoryParamsManager.getCategoryParams(category_id, null);
	}


	/**
	 * 后台保存新增商品分类
	 */
	@ResponseBody
	@RequestMapping(value = "/admin/category/save-add", method = RequestMethod.POST)
	public Category saveAdd(Integer cattype, Category category,
			@RequestParam(value = "file", required = false) MultipartFile file) {
		AdminUser user  = UserConext.getCurrentAdminUser();
		if(EopSetting.IS_DEMO_SITE && user!= null && user.getFounder() != 1){
			throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR,
					"抱歉，当前为演示站点，以不能添加这些示例数据，请下载安装包在本地体验这些功能！");
		}
		if (file != null) {
			if (!FileUtil.isAllowUpImg(file.getOriginalFilename())) {
				throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR, "不允许上传的文件格式，请上传gif,jpg,bmp格式文件。");
			} else {
				// 上传分类图片
				IUploader uploader = uploadFactory.getUploader();
				category.setImage(uploader.upload(file));
			}
		}
		if (cattype == 1) {
			category.setParent_id(0);
		}
		category.setList_show(1);
		category.setGoods_count(0);
		categoryManager.saveAdd(category);
		return category;
	}

	/**
	 * 保存修改商品分类
	 * 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/admin/category/save-edit", method = RequestMethod.POST)
	public Category saveEdit(Category cat, Integer cattype,
			@RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
		AdminUser user  = UserConext.getCurrentAdminUser();
		if(EopSetting.IS_DEMO_SITE && user!= null && user.getFounder() != 1){
			throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR,
					"抱歉，当前为演示站点，以不能添加这些示例数据，请下载安装包在本地体验这些功能！");
		}
		if (file != null) {
			if (!FileUtil.isAllowUpImg(file.getOriginalFilename())) {
				throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR, "不允许上传的文件格式，请上传gif,jpg,bmp格式文件。");
			} else {
				// 上传分类图片
				IUploader uploader = uploadFactory.getUploader();
				cat.setImage(uploader.upload(file));
			}
		}
		// 如果选择是顶级分类那么设置父类id为0
		if (cattype == 1) {
			cat.setParent_id(0);
		}
		if (cat.getParent_id().intValue() == 0) {
			this.categoryManager.update(cat);
			return cat;
		}
		Category targetCat = this.categoryManager.get(cat.getParent_id());// 将要修改为父分类的对象
		// 如果是子分类，且是显示状态
		if (targetCat.getList_show().equals("1") && targetCat.getList_show().equals("0")) {

			throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR, "保存失败：上级分类为未显示状态");
		}
		if (cat.getParent_id().intValue() == cat.getCategory_id().intValue()
				|| targetCat.getParent_id().intValue() == cat.getCategory_id().intValue()) {

			throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR, "保存失败：上级分类不能选择当前分类或其子分类");
		}

		this.categoryManager.update(cat);
		return cat;
	}

	/**
	 * 删除商品分类
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/category/delete/{cat_id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable Integer cat_id) throws Exception {
		AdminUser user  = UserConext.getCurrentAdminUser();
		if(EopSetting.IS_DEMO_SITE && cat_id < 93 && user!= null && user.getFounder() != 1){
			throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR,
					"抱歉，当前为演示站点，以不能添加这些示例数据，请下载安装包在本地体验这些功能！");
		}
			this.categoryManager.delete(cat_id);
	}
	@ApiOperation(value = "初始化分类缓存",notes = "管理员初始化分类缓存")
	@GetMapping(value = "/init-cache")
	public void initCategory(){
		categoryManager.initCategory();
	}
	
	/**
	 * 管理员操作分类绑定规格
	 * @param category_id 分类id
	 * @param choose_specs 规格id数组
	 */
	@ApiOperation(value = "管理员操作分类绑定规格", notes = "管理员操作分类绑定规格使用")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "category_id", value = "分类id", required = true, paramType = "path", dataType = "int"),
			@ApiImplicitParam(name = "choose_specs", value = "规格id数组", required = true, paramType = "query", dataType = "int", allowMultiple = true), })
	@RequestMapping(value = "/admin/category/{category_id}/spec", method = RequestMethod.POST)
	public void saveSpec(@PathVariable Integer category_id, Integer[] choose_specs) {

		this.categoryManager.saveSpec(category_id, choose_specs);
	}
	/**
	 * 管理员操作分类绑定品牌
	 * @param category_id 分类id
	 * @param choose_brands 品牌id数组
	 */
	@ApiOperation(value = "管理员操作分类绑定品牌", notes = "管理员操作分类绑定品牌使用")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "category_id", value = "分类id", required = true, paramType = "path", dataType = "int"),
			@ApiImplicitParam(name = "choose_brands", value = "品牌id数组", required = true, paramType = "query", dataType = "int", allowMultiple = true), })
	@RequestMapping(value = "/admin/category/{category_id}/brand", method = RequestMethod.POST)
	public void saveBrand(@PathVariable Integer category_id, Integer[] choose_brands) {

		this.categoryManager.saveBrand(category_id, choose_brands);
	}
	/**
	 * 管理员保存分类排序
	 * @param category_ids 分类id数组
	 * @param category_sorts 排序数值数组
	 * @return
	 */
	@ApiOperation(value = "管理员保存分类排序", notes = "管理员保存分类排序使用")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "category_ids", value = "分类id数组", required = true, paramType = "query", dataType = "int", allowMultiple = true),
			@ApiImplicitParam(name = "category_sorts", value = "排序数值数组", required = true, paramType = "query", dataType = "int", allowMultiple = true), })
	@RequestMapping(value = "/admin/category/sort", method = RequestMethod.POST)
	public boolean saveSort(Integer[] category_ids, Integer[] category_sorts) {

		this.categoryManager.saveSort(category_ids, category_sorts);

		return true;
	}
}
