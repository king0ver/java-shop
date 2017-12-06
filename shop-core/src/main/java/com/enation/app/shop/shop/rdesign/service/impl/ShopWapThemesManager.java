package com.enation.app.shop.shop.rdesign.service.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.shop.apply.model.po.Shop;
import com.enation.app.shop.shop.apply.service.IShopManager;
import com.enation.app.shop.shop.rdesign.model.WapShopThemes;
import com.enation.app.shop.shop.rdesign.service.IShopWapThemesManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.StringUtil;
/**
 * 
 * (wap店铺模版管理类) 
 * @author zjp
 * @version v1.0
 * @since v6.4.0
 * 2017年8月23日 上午9:33:43
 */
@Service("storeWapThemesManager")
public class ShopWapThemesManager implements IShopWapThemesManager {
	
	@Autowired
	private IDaoSupport daoSupport;
	
	@Autowired
	private ISellerManager storeMemberManager;
	
	@Autowired
	private IShopManager shopManager;
	
	@Override
	public Page list(Integer pageNo,Integer pageSize) {
		String sql="select * from es_wap_store_themes";
		return daoSupport.queryForPage(sql, pageNo, pageSize);
	}

	@Override
	public void add(WapShopThemes wapStoreThemes) {
		String sql="select count(id) from es_wap_store_themes";
		if(daoSupport.queryForInt(sql)==0){
			wapStoreThemes.setIs_default(1);
		}else{
			wapStoreThemes.setIs_default(0);
		}
		this.daoSupport.insert("es_wap_store_themes", wapStoreThemes);
	}

	@Override
	public void edit(WapShopThemes wapStoreThemes) {
		//只能有一个默认模板
		if(wapStoreThemes.getIs_default().equals(1)){
			this.daoSupport.execute("UPDATE es_wap_store_themes SET is_default=0");
		}
		this.daoSupport.update("es_wap_store_themes", wapStoreThemes, "id="+wapStoreThemes.getId());
	}
	
	@Override
	public void delete(Integer id) {
		//查询到当前默认模版
		String sql="select * from es_wap_store_themes where is_default = 1";
		WapShopThemes themes=this.daoSupport.queryForObject(sql, WapShopThemes.class);
		//将用到此模版的店铺修改成默认模版
		this.daoSupport.execute("update es_shop_detail set wap_themeid = ?,wap_theme_path = ? where wap_themeid = ?",themes.getId(),themes.getPath(),id);
		//删除模版
		this.daoSupport.execute("delete from es_wap_store_themes where id=?", id);
	}
	
	@Override
	public WapShopThemes getStorethThemes(Integer id) {
		return (WapShopThemes) this.daoSupport.queryForObject("select * from es_wap_store_themes where id=?", WapShopThemes.class, id);
	}
	
	@Override
	public String getStrorePath(Integer store_id) {
		String storePath=this.daoSupport.queryForString("select wap_theme_path from es_shop_detail where shop_id="+store_id);
		if(storePath!=null){
			return storePath;
		}else{
			return this.getDefaultStoreThemes().getPath();
		}
	}
	
	@Override
	public void changeStoreThemes(Integer themes_id) {
		Seller member=storeMemberManager.getSeller();
		WapShopThemes wapStoreThemes=this.getStorethThemes(themes_id);
		this.daoSupport.execute("update es_shop_detail set wap_themeid=?,wap_theme_path=? where shop_id=?", themes_id,wapStoreThemes.getPath(),member.getStore_id());
	}

	@Override
	public Shop getStoreByUrl(String url) {
		String storeId=paseStoreId(url);
		if(storeId == null){
			return null;
		}
		return shopManager.getShop(StringUtil.toInt(storeId, true));
	}
	
	@Override
	public Integer getStoreIdByUrl(String url) {
		String storeId=paseStoreId(url);
		
		if(StringUtil.toInt(storeId, 0)==0){
			return 0;
		}
		return StringUtil.toInt(storeId,true);
	}
	
	/**
	 * 通过正则获取url 中 的店铺Id
	 * @param url url请求
	 * @return 店铺Id
	 */
	private  static String  paseStoreId(String url){
		String pattern = "(/)(\\d+)(/)";
		String value = null;
		Pattern p = Pattern.compile(pattern, 2 | Pattern.DOTALL);
		Matcher m = p.matcher(url);
		if (m.find()) {
			value=m.group(2);
		}
		return value;
	}

	@Override
	public WapShopThemes getDefaultStoreThemes() {
		return (WapShopThemes) this.daoSupport.queryForObject("select * from es_wap_store_themes where is_default=1", WapShopThemes.class);
	}

}
