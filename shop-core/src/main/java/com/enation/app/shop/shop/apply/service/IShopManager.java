package com.enation.app.shop.shop.apply.service;

import java.util.List;
import java.util.Map;

import org.junit.validator.PublicClassValidator;

import com.enation.app.shop.shop.apply.model.po.Shop;
import com.enation.app.shop.shop.apply.model.po.ShopDetail;
import com.enation.app.shop.shop.apply.model.vo.ApplyStep1;
import com.enation.app.shop.shop.apply.model.vo.ApplyStep2;
import com.enation.app.shop.shop.apply.model.vo.ApplyStep3;
import com.enation.app.shop.shop.apply.model.vo.ApplyStep4;
import com.enation.app.shop.shop.apply.model.vo.ShopSetting;
import com.enation.app.shop.shop.apply.model.vo.ShopVo;
import com.enation.framework.database.Page;


/**
 * 店铺管理类
 * @author LiFenLong
 *2014-9-11  
 */
public interface IShopManager {
	
	public static final String CURRENT_STORE_KEY="curr_store";
	
	/**
	 * 初始化店铺信息
	 */
	public void init() ;
	
	/**
	 * 申请开店第一步
	 * @param applyStep1 店铺信息
	 */
	public void step1(ApplyStep1 applyStep1);
	
	/**
	 * 申请开店第二步
	 * @param applyStep2 店铺信息
	 */
	public void step2(ApplyStep2 applyStep2);
	
	/**
	 * 申请开店第三步
	 * @param applyStep3 店铺信息
	 */
	public void step3(ApplyStep3 applyStep3);
	
	/**
	 * 申请开店第四步
	 * @param applyStep4 店铺信息
	 */
	public void step4(ApplyStep4 applyStep4);
	/**
	 * 店铺列表
	 * @param other
	 * @param disabled 店铺状态
	 * @param pageNo 页数
	 * @param pageSize 每页数量
	 * @return
	 */
	public Page shop_list(Map other,String disabled,int pageNo,int pageSize);
	/**
	 * 禁用店铺
	 * @param storeId 店铺id
	 */
	public void disShop(Integer shopId);
	/**
	 * 恢复店铺使用
	 * @param storeId 店铺id
	 */
	public void useShop(Integer shopId);
	/**
	 * 获取一个店铺信息
	 * @param storeId 店铺id
	 * @return StoreVo 店铺信息
	 */
	public ShopVo getShopVo(Integer shopId);
	/**
	 * 店铺审核
	 * @param member_id 会员ID
	 * @param storeId 店铺ID
	 * @param pass	是否通过：1，通过 0未通过
	 */
	public void audit_pass(Integer member_id,Integer shopId,Integer pass);
	/**
	 * 获取一个店铺
	 * @param storeId
	 * @return Store
	 */
	public Shop getShop(Integer shopId);
	/**
	 * 检查店铺名称是否重复
	 * @param shopName
	 * @return boolean
	 */
	public boolean checkShopName(String shopName);
	/**
	 * 后台注册店铺
	 * @param storeVo 店铺详细
	 */
	public void registStore(ShopVo shopVo);
	/**
	 * 修改店铺信息-后台使用
	 * @param storeVo 店铺详细信息
	 */
	public void editShopInfo(ShopVo shopVo);
	/**
	 * 修改店铺信息-商家中心使用
	 * @param storeVo 店铺详细信息
	 */
	public void editShopSetting(ShopSetting shopSetting);
	/**
	 * 修改店铺中的某一个值
	 * @param key
	 * @param value
	 */
	public void editShopOnekey(String key,String value);
	/**
	 * 根据会员Id获取店铺
	 * @param memberId
	 * @return
	 */
	public Shop getShopByMember(Integer memberId);
	/**
	 * 获取店铺详细信息
	 * @param storeId  店铺id
	 * @return StoreDetail 店铺详细信息
	 */
	public ShopDetail getShopDetail(Integer shopId);
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 非分页店铺列表
	 * @return
	 */
	public List getShopList();
	
	
	/**
	 * 获取当前登录店铺信息
	 * @author Kanon
	 * @return store
	 */ 
	public Shop getShop();
	
	
	/**
	 * 修改店铺详细信息
	 * @param storeDetail 店铺详情
	 */
	public void editShop(ShopDetail shopDetail);
	
	/**
	 * 修改店铺信息
	 * @param store
	 */
	public void editShop(Map store);
	/**
	 *	检查会员是否已经申请了店铺
	 * @return
	 */
	public boolean checkShop();
	
	
	
	/***
	 * 检查身份证信息
	 * @author LiFenLong
	 * @param idNumber
	 */
	public Integer checkIdNumber(String idNumber);
	
	
	/**
	 * 增加收藏数量
	 * @param storeid
	 */
	public void addcollectNum(Integer storeid);
	
	/**
	 * 减少收藏数量
	 * @param storeid
	 */
	public void reduceCollectNum(Integer storeid);
	
	

	
	/**
	 * 根据会员Id获取店铺
	 * @param memberId 会员ID
	 * @return
	 */
	public ShopDetail getShopDetailByMember(Integer memberId);
	
	/**
	 * 获取已开启店铺列表
	 * @return List<Shop>
	 */
	public List<Shop> listAll();
	
	/**
	 * 获取商品所有父
	 * @param shop_id 商品id
	 * @return
	 */
	public List getShopManagementGoodsType(Integer shop_id);
	/**
	 * 店铺等级审核信息列表
	 * @param other
	 * @param disabled
	 * @param pageNo
	 * @param pageSize
	 * @return 店铺待审核信息列表
	 */
	public Page levelAuditList(int pageNo,int pageSize);
	/**
	 * 通过会员id获取店铺信息
	 * @param memberId
	 * @return
	 */
	public ShopVo getShopVoByMemberId(Integer memberId) ;
	/**
	 * 填充店铺信息，包括商品标签及店铺幻灯片
	 * @param shop_id
	 */
	public  void fillShopInformation(Integer shop_id) ;
		
	
}
