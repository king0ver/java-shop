package com.enation.app.shop.promotion.exchange.controller.backend;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.base.upload.UploadFactory;
import com.enation.app.base.upload.plugin.IUploader;
import com.enation.app.shop.promotion.exchange.model.po.ExchangeGoodsCategory;
import com.enation.app.shop.promotion.exchange.service.IExchangeCategoryManager;
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
 * 积分商品分类操作
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月24日 下午4:39:02
 */
@Api(description = "积分商品分类操作")
@RestController
@RequestMapping("/shop/admin/exchange-cat")
public class ExchangeCategoryController {
	@Autowired
	private IExchangeCategoryManager exchangeCatManger;
	@Autowired
	private UploadFactory uploadFactory;

	/**
	 * 添加积分商品分类页面
	 * 
	 * @return
	 */
	@RequestMapping("/add-cat")
	public ModelAndView addCat() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/shop/admin/exchange/cat_add");
		return view;
	}

	/**
	 * 跳转积分商城分类列表页面
	 * 
	 * @return
	 */
	@RequestMapping("/list-cat")
	public ModelAndView listCat() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/shop/admin/exchange/cat_list");
		return view;
	}

	/**
	 * 编辑积分商品分类页面 跳转至分类修改页面 并获取数据返回
	 * 
	 * @return
	 */
	@GetMapping("/edit-cat")
	public ModelAndView editCat(Integer cat_id) {
		ModelAndView view = new ModelAndView();
		ExchangeGoodsCategory exchangeGoodsCat = this.exchangeCatManger.getById(cat_id);
		// 添加商品分类列表修改的上级分类和商品类型的数据展示
		view.addObject("catList", this.exchangeCatManger.listAllChildren(0));
		view.addObject("cat", exchangeGoodsCat);
		view.setViewName("/shop/admin/exchange/cat_edit");
		return view;
	}

	@ApiOperation(value = "获取单个积分商品分类")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "cat_id", value = "分类id", required = true, dataType = "int", paramType = "path"), })
	@GetMapping("/get")
	public ExchangeGoodsCategory get(@PathVariable Integer cat_id) {
		ExchangeGoodsCategory exchangeGoodsCat = this.exchangeCatManger.getById(cat_id);
		return exchangeGoodsCat;
	}

	/**
	 * 删除分类操作
	 * 
	 * @return
	 */
	@ApiOperation(value = "删除分类操作")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "cat_id", value = "分类id", required = true, dataType = "int", paramType = "path"), })
	@DeleteMapping(value = "/delete-cat/{cat_id}")
	public void deleteCat(@PathVariable Integer cat_id) throws Exception {
		AdminUser user = UserConext.getCurrentAdminUser();
		if (EopSetting.IS_DEMO_SITE && cat_id < 93 && user != null && user.getFounder() != 1) {
			throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR,
					"抱歉，当前为演示站点，以不能添加这些示例数据，请下载安装包在本地体验这些功能！");
		}
		this.exchangeCatManger.delete(cat_id);
	}

	/**
	 * 保存添加分类
	 * 
	 * @return
	 */
	@ApiOperation(value = "保存添加分类")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "cattype", value = "如果选择是顶级分类那么传1", required = true, dataType = "int", paramType = "query"), })
	@PostMapping(value = "/save-add-cat")
	public ExchangeGoodsCategory saveAdd(Integer cattype, ExchangeGoodsCategory category,
			@RequestParam(value = "file", required = false) MultipartFile file) {
		AdminUser user = UserConext.getCurrentAdminUser();
		if (EopSetting.IS_DEMO_SITE && user != null && user.getFounder() != 1) {
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
		exchangeCatManger.saveAdd(category);
		return category;
	}

	/**
	 * 保存修改商品分类
	 * 
	 * @throws Exception
	 */
	@ApiOperation(value = "保存修改商品分类")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "cattype", value = "如果选择是顶级分类那么传1", required = true, dataType = "int", paramType = "query"), })
	@ResponseBody
	@RequestMapping(value = "/save-edit-cat", method = RequestMethod.POST)
	public ExchangeGoodsCategory saveEdit(ExchangeGoodsCategory cat, Integer cattype,
			@RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
		AdminUser user = UserConext.getCurrentAdminUser();
		if (EopSetting.IS_DEMO_SITE && user != null && user.getFounder() != 1) {
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
			this.exchangeCatManger.update(cat);
			return cat;
		}
		ExchangeGoodsCategory targetCat = this.exchangeCatManger.getById(cat.getParent_id());// 将要修改为父分类的对象
		// 如果是子分类，且是显示状态
		if (targetCat.getList_show().equals("1") && targetCat.getList_show().equals("0")) {

			throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR, "保存失败：上级分类为未显示状态");
		}
		if (cat.getParent_id().intValue() == cat.getCategory_id().intValue()
				|| targetCat.getParent_id().intValue() == cat.getCategory_id().intValue()) {

			throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR, "保存失败：上级分类不能选择当前分类或其子分类");
		}

		// this.categoryManager.update(cat);
		return cat;
	}

	/**
	 * 返回所有子分类
	 * 
	 * @param parentid
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "查询分类列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "parentid", value = "父id，顶级为0", required = true, dataType = "int", paramType = "query"), })
	@RequestMapping(value = "/{parentid}/children", method = RequestMethod.GET)
	public List list(@PathVariable Integer parentid) {
		List catList = exchangeCatManger.getListChildren(parentid);
		return catList;

	}
}
