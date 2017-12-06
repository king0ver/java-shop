package com.enation.app.shop.shop.rdesign.controller;
import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.shop.apply.model.po.ShopDetail;
import com.enation.app.shop.shop.apply.service.IShopManager;
import com.enation.app.shop.shop.rdesign.model.StorePhotos;
import com.enation.app.shop.shop.rdesign.service.IStorePhotosManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.app.shop.shop.setting.model.po.StoreLevel;
import com.enation.app.shop.shop.setting.service.IStoreLevelManager;
import com.enation.eop.SystemSetting;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.framework.action.JsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.CurrencyUtil;
import com.enation.framework.util.JsonResultUtil;
/**
 * 
 * (店铺相册api) 
 * @author zjp
 * @version v1.0
 * @since v6.2
 * 2017年4月10日 上午11:01:37
 */
@Controller
@RequestMapping("/api/b2b2c/store-photos")
public class StorePhotosApiController {
	@Autowired
	private IStorePhotosManager storePhotosManager;
	
	@Autowired
	private ISellerManager sellerManager;
	
	@Autowired
	private IShopManager storeManager;
	
	@Autowired
	private IStoreLevelManager storeLevelManager;
	/**
	 * 添加店铺图片
	 * @param photo_id 图片Id,Integer[]
	 * @param store_fs 图片地址,String[]
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="/add-store-photos",produces= MediaType.APPLICATION_JSON_VALUE)
	public JsonResult addStorePhotos(){
		try {
			Seller member=sellerManager.getSeller();
			//获取店铺id
			Integer store_id = member.getStore_id();
			//静态服务器路径
			String static_server_path = null;
			//静态服务器域名 
			String static_server_domain = SystemSetting.getStatic_server_domain();
			//获取店铺信息
			///Store store = storeManager.getStore(store_id);
			ShopDetail storeDetail = storeManager.getShopDetail(store_id);
			//获取店铺等级信息
			StoreLevel storeLevel = storeLevelManager.getStoreLevel(storeDetail.getShop_level());
			//获取店铺存储空间
			Integer space_capacity = storeLevel.getSpace_capacity();
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			//获取图片路径
			String[] picnames_storephotos = request.getParameterValues("picnames_storephotos");
			if(picnames_storephotos==null){
				return JsonResultUtil.getErrorJson("请选择图片");
			}
			for (String img : picnames_storephotos) {
				//查询图片的大小
				String url = img.replace(static_server_domain,static_server_path);
				File file = new File(url);
				long size = file.length();
				//将图片转换为M为单位
				double div = CurrencyUtil.div(CurrencyUtil.div(size, 1024, 2), 1024, 2);
				//比对前获取最新的存储量
				storeDetail = storeManager.getShopDetail(store_id);
				Double store_space_capacity = storeDetail.getStore_space_capacity();
				//判断存储空间是否已满
				if(store_space_capacity+div>space_capacity){
					return JsonResultUtil.getErrorJson("存储空间已满，请提升店铺等级扩充容量");
				}
				img = this.transformPath(img);
//				将图片信息存入数据库
				storeDetail.setStore_space_capacity(store_space_capacity+div);
				this.storeManager.editShop(storeDetail);
				this.storePhotosManager.add(store_id, img);
			}
			return JsonResultUtil.getSuccessJson("添加成功");
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson("添加失败");
		}
	}
	/**
	 * 删除图片
	 * @param photo_id 图片id
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	@ResponseBody
	@RequestMapping(value="/delete-store-photos",produces= MediaType.APPLICATION_JSON_VALUE)
	public JsonResult deletePhoto(Integer photo_id){
		try {
			//静态服务器路径
			String static_server_path = null;
			//获取会员信息
			Seller Seller = this.sellerManager.getSeller();
			Integer store_id = Seller.getStore_id();
			//获取店铺信息
			ShopDetail storeDetail = storeManager.getShopDetail(store_id);
			StorePhotos storePhotos = storePhotosManager.getStorePhotos(store_id, photo_id);
			String img = storePhotos.getImg();
			//查询图片的大小
			String url = img.replace(EopSetting.FILE_STORE_PREFIX,static_server_path);
			File file = new File(url);
			//long size = FileUtils.sizeOfDirectory(file);
			long size = file.length();
			//将图片转换为M为单位
			double div = CurrencyUtil.div(CurrencyUtil.div(size, 1024, 2), 1024, 2);
			//更新存储信息
			storeDetail.setStore_space_capacity(storeDetail.getStore_space_capacity()-div);
			this.storePhotosManager.delete(store_id, photo_id);
			this.storeManager.editShop(storeDetail);
			return JsonResultUtil.getSuccessJson("删除成功");
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson("删除失败");
		}
	}
	/**
	 * 页面中传递过来的图片地址为:http://<staticserver>/<image path>如:
	 * http://static.enationsoft.com/attachment/goods/1.jpg
	 * 存在库中的为fs:/attachment/goods/1.jpg 生成fs式路径
	 * 
	 * @param path
	 * @return
	 */
	private String transformPath(String path) {
		String static_server_domain= SystemSetting.getStatic_server_domain();

		String regx =static_server_domain;
		path = path.replace(regx, EopSetting.FILE_STORE_PREFIX);
		return path;
	}
}
