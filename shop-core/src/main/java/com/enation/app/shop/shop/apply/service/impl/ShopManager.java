package com.enation.app.shop.shop.apply.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.log.B2b2cLogType;
import com.enation.app.shop.shop.apply.model.enums.ShopDisableStatus;
import com.enation.app.shop.shop.apply.model.po.Shop;
import com.enation.app.shop.shop.apply.model.po.ShopDetail;
import com.enation.app.shop.shop.apply.model.vo.ApplyStep1;
import com.enation.app.shop.shop.apply.model.vo.ApplyStep2;
import com.enation.app.shop.shop.apply.model.vo.ApplyStep3;
import com.enation.app.shop.shop.apply.model.vo.ApplyStep4;
import com.enation.app.shop.shop.apply.model.vo.ShopSetting;
import com.enation.app.shop.shop.apply.model.vo.ShopVo;
import com.enation.app.shop.shop.apply.service.IShopManager;
import com.enation.app.shop.shop.goods.model.StoreTag;
import com.enation.app.shop.shop.goods.service.IStoreGoodsTagManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.annotation.Log;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

/**
 * 
 * 店铺管理类
 * 
 * @author Kanon 增加方法获取当前已经登录的店铺,修改店铺信息刷新存储session中的店铺信息 yanlin
 * @version v1.1 v2.0
 * @since v3.1 v6.4.0
 * @date 	   2017年8月17日 下午4:24:50
 */
@Service("shopManager")
public class ShopManager implements IShopManager {
	@Autowired
	private IDaoSupport daoSupport;

	@Autowired
	private ISellerManager sellerManager;
 
	@Autowired
	private IStoreGoodsTagManager storeGoodsTagManager;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void init() {
		/**获取会员系信息*/
		Member member = UserConext.getCurrentMember();
		
		/**查看会员时候拥有店铺*/
		if(member!=null&&this.getShopByMember(member.getMember_id())==null) {
			Shop shop = new Shop();
			/**初始化店铺信息*/
			shop.setShop_disable(ShopDisableStatus.applying.toString());
			/**设置会员信息*/
			shop.setMember_id(member.getMember_id());
			shop.setMember_name(member.getName());
			this.daoSupport.insert("es_shop", shop);
			
			/**初始化店铺详细信息*/
			int lastId = this.daoSupport.getLastId("es_shop");
			ShopDetail shopDetail = new ShopDetail();
			/**设置店铺等级*/
			shopDetail.setShop_level(1);
			/**设置店铺佣金*/
			shopDetail.setShop_commission(0.0);
			/**设置库存预警值*/
			shopDetail.setGoods_warning_count(5);
			/**设置店铺id*/
			shopDetail.setShop_id(lastId);
			this.daoSupport.insert("es_shop_detail", shopDetail);
		}
	}

	@Override
	public void step1(ApplyStep1 applyStep1) {
		Member member = UserConext.getCurrentMember();
		Shop shop = this.getShopByMember(member.getMember_id());
		if(member!=null) {
			this.daoSupport.update("es_shop_detail", applyStep1, "shop_id = "+shop.getShop_id());
		}
		
	}

	@Override
	public void step2(ApplyStep2 applyStep2) {
		Member member = UserConext.getCurrentMember();
		Shop shop = this.getShopByMember(member.getMember_id());
		if(member!=null) {
			this.daoSupport.update("es_shop_detail", applyStep2, "shop_id = "+shop.getShop_id());
		}
		
	}

	@Override
	public void step3(ApplyStep3 applyStep3) {
		Member member = UserConext.getCurrentMember();
		Shop shop = this.getShopByMember(member.getMember_id());
		if(member!=null) {
			this.daoSupport.update("es_shop_detail", applyStep3, "shop_id = "+shop.getShop_id());
		}
	}

	@Override
	public void step4(ApplyStep4 applyStep4) {
		Member member = UserConext.getCurrentMember();
		Shop shop = this.getShopByMember(member.getMember_id());
		if(member!=null) {
			/**更新店铺基本信息*/
			String sql = "update es_shop set shop_name = ? , shop_disable = ?  where member_id = ? ";
			this.daoSupport.execute(sql, applyStep4.getShop_name(),ShopDisableStatus.apply.toString(),member.getMember_id());
			/**获取店铺详细信息*/
			ShopDetail shopDetail = new ShopDetail();
			shopDetail.setShop_province(applyStep4.getShop_province());
			shopDetail.setShop_province_id(applyStep4.getShop_province_id());
			shopDetail.setShop_city(applyStep4.getShop_city());
			shopDetail.setShop_city_id(applyStep4.getShop_city_id());
			shopDetail.setShop_region(applyStep4.getShop_region());
			shopDetail.setShop_region_id(applyStep4.getShop_region_id());
			shopDetail.setShop_town(applyStep4.getShop_town());
			shopDetail.setShop_town_id(applyStep4.getShop_town_id());
			shopDetail.setGoods_management_category(applyStep4.getGoods_management_category());
			String shop_add = applyStep4.getShop_province()+applyStep4.getShop_city()+applyStep4.getShop_region();
			if(applyStep4.getShop_town()!=null) {
				shop_add = shop_add+applyStep4.getShop_town();
			}
			shopDetail.setShop_add(shop_add);
			this.daoSupport.update("es_shop_detail", shopDetail, "shop_id = "+shop.getShop_id());
			/**更新会员信息*/
//			 sql = "update es_member set is_store=1,store_id=? where member_id=?";
//			daoSupport.execute(sql, shop.getShop_id(), member.getMember_id());
			/**更新商家信息*/
			//ThreadContextHolder.getSession().setAttribute(ISellerManager.CURRENT_STORE_MEMBER_KEY, sellerManager.getSeller(member.getMember_id()));
		}
	}
	
	@Override
	public boolean checkShopName(String shopName) {
		String sql = "select  count(shop_id) from es_shop where shop_name= ? and shop_disable= '"+ShopDisableStatus.open +"'";
		Integer count = this.daoSupport.queryForInt(sql, shopName);
		return count != 0 ? true : false;
	}
	
	@Override
	public Page shop_list(Map other, String disabled, int pageNo, int pageSize) {
		StringBuffer sql = new StringBuffer("");
		disabled = disabled == null ? "open" : disabled;
		
		String shop_name = other.get("shop_name") == null ? "" : other.get("shop_name").toString();
		String member_name = other.get("member_name") == null ? "" : other.get("member_name").toString();
		String start_time = other.get("start_time") == null ? "" : other.get("start_time").toString();
		String end_time = other.get("end_time") == null ? "" : other.get("end_time").toString();
		String keyword = other.get("keyword") == null ? "" : other.get("keyword").toString();
		// 店铺状态
		if (disabled.equals("all")) {
			sql.append("select  s.member_id,s.member_name,s.shop_name,s.shop_disable,s.shop_createtime,s.shop_endtime,sd.* from es_shop s  left join es_shop_detail sd on s.shop_id = sd.shop_id  where  shop_disable != 'applying' ");
		} else {
			sql.append("select  s.member_id,s.member_name,s.shop_name,s.shop_disable,s.shop_createtime,s.shop_endtime,sd.* from es_shop s  left join es_shop_detail sd on s.shop_id = sd.shop_id   where  shop_disable = '" + disabled+"'");
		}
		if (!StringUtil.isEmpty(keyword)) {
			sql.append("  and s.shop_name like '%" + keyword + "%'");
		}
		if (!StringUtil.isEmpty(shop_name)) {
			sql.append("  and s.shop_name like '%" + shop_name + "%'");
		}
		if (!StringUtil.isEmpty(member_name)) {
			sql.append("  and s.member_name like '%" + member_name + "%'");
		}
		if (start_time != null && !StringUtil.isEmpty(start_time)) {
			long stime = DateUtil.getDateline(start_time + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
			sql.append(" and s.shop_createtime>" + stime);
		}
		if (end_time != null && !StringUtil.isEmpty(end_time)) {
			long etime = DateUtil.getDateline(end_time + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
			sql.append(" and s.shop_endtime<" + etime);
		}
		sql.append(" order by shop_id" + " desc");
		return this.daoSupport.queryForPage(sql.toString(), pageNo, pageSize);
	}
	@Override
	public ShopVo getShopVo(Integer shopId) {
		if (shopId == null) {
			return null;
		}
		String sql = "select s.member_id,s.member_name,s.shop_name,s.shop_disable,s.shop_createtime,s.shop_endtime,d.* from es_shop s left join es_shop_detail d on  s.shop_id = d.shop_id where s.shop_id = ? ";
		ShopVo storeVo = this.daoSupport.queryForObject(sql, ShopVo.class, shopId);
		return storeVo;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@Log(type = B2b2cLogType.STORE, detail = "审核店铺ID为${storeId}的店铺，开店会员的ID为${member_id}")
	public void audit_pass(Integer member_id, Integer shopId, Integer pass) {
		if (pass == 1) {
			this.editShopdis(ShopDisableStatus.open.toString(), shopId);
		} else {
			// 审核未通过
			this.editShopdis(ShopDisableStatus.refused.toString(), shopId);
		}
		
	}
	
	/**
	 * 更改店铺状态
	 * 
	 * @param disabled 店铺状态
	 * @param store_id 店铺id
	 */
	private void editShopdis(String disabled, Integer shop_id) {
		
		this.daoSupport.execute("update es_shop set shop_disable = ? where shop_id=? ",disabled,shop_id);
	}
	
	@Override
	public Shop getShop(Integer shopId) {
		if (shopId == null) {
			return null;
		}
		String sql = "select * from es_shop where shop_id=" + shopId;
		List<Shop> list = this.daoSupport.queryForList(sql, Shop.class);
		if (list == null || list.size() == 0) {
			return null;
		}
		Shop store = (Shop) list.get(0);
		return store;
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@Log(type = B2b2cLogType.STORE, detail = "禁用店铺ID为${storeId}的店铺")
	public void disShop(Integer shopId) {
		// 关闭店铺
		this.daoSupport.execute("update es_shop set  shop_endtime=? ,shop_disable = ? where shop_id=?", DateUtil.getDateline(),ShopDisableStatus.closed.toString(), shopId);
		// 修改会员店铺状态
		this.daoSupport.execute("update es_member set is_store=? where member_id=?", 3,
				this.getShop(shopId).getMember_id());
		// 更高店铺商品状态
		this.daoSupport.execute("update es_goods set disabled=? where seller_id=?", 1, shopId);
	}

	@Override
	@Log(type = B2b2cLogType.STORE, detail = "恢复店铺ID为${storeId}的店铺为使用状态")
	public void useShop(Integer shopId) {
		this.editShopdis(ShopDisableStatus.open.toString(), shopId);
		this.daoSupport.execute(
				"update es_member set is_store=" + 1 + " where member_id= ? " , this.getShop(shopId).getMember_id());
		// 更高店铺商品状态
		this.daoSupport.execute("update es_goods set disabled=? where seller_id=?", 0, shopId);
	}
	
	
	@Override
	@Log(type = B2b2cLogType.STORE, detail = "后台注册一个店铺名为${store.store_name}的店铺")
	@Transactional(propagation = Propagation.REQUIRED)
	public void registStore(ShopVo shopVo) {
		
			/**保存店铺信息*/
			shopVo.setMember_id(this.sellerManager.getSeller(shopVo.getMember_name()).getMember_id());
			/**获取店铺店铺信息实体 */
			Shop shop = new Shop();
			ShopDetail shopDetail = new ShopDetail();
			BeanUtils.copyProperties(shopVo,shopDetail);
			BeanUtils.copyProperties(shopVo,shop);
			this.daoSupport.insert("es_shop", shop);
			int lastId = this.daoSupport.getLastId("es_shop");
			shopDetail.setShop_id(lastId);
			this.daoSupport.insert("es_shop_detail", shopDetail);
			shop.setShop_id(this.daoSupport.getLastId("es_shop"));
			/**修改会员信息*/
			String sql = "update es_member set is_store=1,store_id=? where member_id=?";
			daoSupport.execute(sql, shopDetail.getShop_id(), shopVo.getMember_id());
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@Log(type = B2b2cLogType.STORE, detail = "修改店铺名为${store.store_name}店铺信息【后台使用】")
	public void editShopInfo(ShopVo shopVo) {
		
		/**获取店铺店铺信息实体 */
		Shop shop = new Shop();
		ShopDetail shopDetail = new ShopDetail();
		BeanUtils.copyProperties(shopVo,shopDetail);
		BeanUtils.copyProperties(shopVo,shop);
		this.daoSupport.update("es_shop", shop, " shop_id=" + shop.getShop_id());
		this.daoSupport.update("es_shop_detail", shopDetail, " shop_id=" + shop.getShop_id());
	}
	
	@Override
	public void editShopSetting(ShopSetting shopSetting) {
		this.daoSupport.update("es_shop_detail", shopSetting, " shop_id=" + shopSetting.getShop_id());
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void editShopOnekey(String key, String value) {
		Seller member = sellerManager.getSeller();
		Map map = new HashMap();
		map.put(key, value);
		this.daoSupport.update("es_shop_detail", map, "shop_id=" + member.getStore_id());
	}
	
	@Override
	public Shop getShopByMember(Integer memberId) {
		return  this.daoSupport.queryForObject("select * from es_shop where member_id=?", Shop.class,
				memberId);
	}
	@Override
	public ShopDetail getShopDetail(Integer shopId) {
		if(shopId==null){
			return null;
		}
		String sql = "select * from es_shop_detail where shop_id=" + shopId;
		List<ShopDetail> list = this.daoSupport.queryForList(sql, ShopDetail.class);
		if(list==null || list.size()==0){
			return null;
		}
		ShopDetail shopDetail = (ShopDetail) list.get(0);
		return shopDetail;
	}
	
	@Override
	public Shop getShop() {
		Shop shop = (Shop) ThreadContextHolder.getSession().getAttribute(this.CURRENT_STORE_KEY);
		return shop;
	}

	@Override
	public void editShop(Map store) {
		this.daoSupport.update("es_shop_detail", store, " shop_id=" + store.get("shop_id"));
	}

	@Override
	public boolean checkShop() {
		Seller member = sellerManager.getSeller();
		String sql = "select count(store_id) from es_shop where member_id=?";
		int isHas = this.daoSupport.queryForInt(sql, member.getMember_id());
		if (isHas > 0)
			return true;
		else
			return false;
	}

	
	@Override
	public Integer checkIdNumber(String idNumber) {
		String sql = "select member_id from es_shop where id_number=?";
		List result = this.daoSupport.queryForList(sql, idNumber);
		return result.size() > 0 ? 1 : 0;
	}

	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@Log(type = B2b2cLogType.STORE, detail = "增加店铺Id为${storeid}的店铺收藏数量")
	public void addcollectNum(Integer shopId) {
		String sql = "update es_shop_detail set shop_collect = shop_collect+1 where shop_id=?";
		this.daoSupport.execute(sql, shopId);
	}

	
	@Override
	@Log(type = B2b2cLogType.STORE, detail = "减少店铺Id为${storeid}的店铺收藏数量")
	public void reduceCollectNum(Integer shopId) {
		String sql = "update es_shop_detail set shop_collect = shop_collect-1 where shop_id=?";
		this.daoSupport.execute(sql, shopId);
	}

	
	@Override
	public ShopDetail getShopDetailByMember(Integer memberId) {
		Shop store = this.getShopByMember(memberId);
		return (ShopDetail) this.daoSupport.queryForObject("select * from es_shop_detail where shop_id=?", ShopDetail.class,
				store.getShop_id());
	}
	
	

	@Override
	public List<Shop> listAll() {
		String sql = "select * from es_shop where shop_disable = '" + ShopDisableStatus.open + "'";
		List<Shop> storeList = this.daoSupport.queryForList(sql, Shop.class);
		return storeList;
	}

	/**
	 * 获取商品所有父
	 */
	@Override
	public List getShopManagementGoodsType(Integer shop_id) {
		ArrayList list = new ArrayList<String>();
		if (shop_id == null) {
			return null;
		}
		String sql = "select goods_management_category from es_shop_detail where shop_id=" + shop_id;

		// 获取到经营类目
		String goods_management_category = this.daoSupport.queryForString(sql);

		// 经营类目是否存在，不存在返回null
		try {
			if (goods_management_category.length() > 0) {
				String[] managementType = goods_management_category.split(",");
				for (int i = 0; i < managementType.length; i++) {
					sql = "select name from es_goods_cat where cat_id=" + managementType[i];
					String goodsCategory = this.daoSupport.queryForString(sql);
					list.add(goodsCategory);
				}
				return list;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.getMessage();
			return null;
		}
	}

	
	@Override
	public Page levelAuditList(int pageNo, int pageSize) {
		StringBuffer sql = new StringBuffer("select * from es_shop s left join es_shop_detail sd on s.shop_id = sd.shop_id  where  shop_disable = 'open'  ");
		sql.append(" and  shop_level_apply=1 ");
		sql.append(" order by s.shop_id ");
		return this.daoSupport.queryForPage(sql.toString(), pageNo, pageSize);
	}

	
	@Override
	public List getShopList() {
		return this.daoSupport.queryForList("select * from es_shop", Shop.class);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@Log(type=B2b2cLogType.STORE,detail="修改店铺名为${store.store_name}的店铺信息")
	public void editShop(ShopDetail shopDetail) {
		Seller member = sellerManager.getSeller();
		this.daoSupport.update("es_shop_detail", shopDetail, "shop_id=" + member.getStore_id());
		
		ThreadContextHolder.getSession().setAttribute(IShopManager.CURRENT_STORE_KEY, shopDetail);
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.shop.service.IShopManager#getShopVoByMemberId(java.lang.Integer)
	 */
	@Override
	public ShopVo getShopVoByMemberId(Integer memberId) {
		if (memberId == null) {
			return null;
		}
		String sql = "select s.member_id,s.member_name,s.shop_name,s.shop_disable,s.shop_createtime,s.shop_endtime,d.* from es_shop s left join es_shop_detail d on  s.shop_id = d.shop_id where s.member_id = ? ";
		ShopVo storeVo = this.daoSupport.queryForObject(sql, ShopVo.class, memberId);
		return storeVo;
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.shop.apply.service.IShopManager#fillShopInformation(java.lang.Integer)
	 */
	@Override
	public void fillShopInformation(Integer shop_id) {
		/**添加商品标签*/
		StoreTag storeTag = new StoreTag();
		storeTag.setStore_id(shop_id);
		// 热卖排行
		storeTag.setTag_name("热卖排行");
		storeTag.setMark("hot");
		storeGoodsTagManager.add(storeTag);
		// 新品推荐
		storeTag.setTag_name("新品推荐");
		storeTag.setMark("new");
		storeGoodsTagManager.add(storeTag);
		// 推荐商品
		storeTag.setTag_name("推荐商品");
		storeTag.setMark("recommend");
		storeGoodsTagManager.add(storeTag);
		
		/**添加店铺幻灯片*/
		Map map = new HashMap();
		for (int i = 0; i < 5; i++) {
			map.put("store_id", shop_id);
			map.put("img", "fs:/images/s_side.jpg");
			this.daoSupport.insert("es_store_silde", map);
		}
		
	}
	

}