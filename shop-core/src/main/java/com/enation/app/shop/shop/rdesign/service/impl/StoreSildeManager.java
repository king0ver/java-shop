package com.enation.app.shop.shop.rdesign.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.shop.apply.model.po.Shop;
import com.enation.app.shop.shop.apply.service.IShopManager;
import com.enation.app.shop.shop.rdesign.model.StoreSilde;
import com.enation.app.shop.shop.rdesign.service.IStoreSildeManager;
import com.enation.eop.sdk.utils.StaticResourcesUtil;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.util.StringUtil;
@Service("storeSildeManager")
public class StoreSildeManager implements IStoreSildeManager {
	@Autowired
	private IDaoSupport daoSupport;
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreSildeManager#list(java.lang.Integer)
	 */
	@Override
	public List<StoreSilde> list(Integer store_id) {
		//获取幻灯片列表
		String sql="select * from es_store_silde where store_id=?";
		List<StoreSilde> list=this.daoSupport.queryForList(sql, StoreSilde.class, store_id);
		//更替图片路径
		//this.editImg(list);
		return list;
	}
	/**
	 * 修改店铺轮播图
	 * @param list
	 */
	private void editImg(List<StoreSilde> list){
		for (StoreSilde storeSilde : list) {
			storeSilde.setSildeImg( StaticResourcesUtil.convertToUrl(storeSilde.getImg()));
		}
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreSildeManager#edit(java.lang.Integer[], java.lang.String[], java.lang.String[])
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void edit(Integer[] silde_id,String[] fsImg,String[] silde_url) {
		Shop store=(Shop)ThreadContextHolder.getSession().getAttribute(IShopManager.CURRENT_STORE_KEY);
		/** 如果幻灯片就一个且url未填写，重新赋值 */
		if(silde_url.length <= 0 ) {
			silde_url = new String[1];
			silde_url[0] = "";
		}
		for (int i = 0; i < silde_id.length; i++) {
			StoreSilde storeSilde=new StoreSilde();
			storeSilde.setImg(fsImg[i]);
			storeSilde.setSilde_url(silde_url[i]);
			/** 如果前端传入id不为0 则为修改 否则为新增 */
			if(StringUtil.toInt(silde_id[i],false) == 0) {
				storeSilde.setStore_id(store.getShop_id());
				this.addSilde(storeSilde);

			}else {
				storeSilde.setSilde_id(silde_id[i]);
				this.editSilde(storeSilde);
			}
		}

	}
	/*
	 * 修改店铺轮播图
	 */
	private void editSilde(StoreSilde storeSilde){
		this.daoSupport.update("es_store_silde", storeSilde, "silde_id="+storeSilde.getSilde_id());
	}
	/**
	 * 新增店铺轮播图
	 * @param storeSilde	轮播图模型
	 */
	private void addSilde(StoreSilde storeSilde) {
		this.daoSupport.insert("es_store_silde", storeSilde);
	}
	
	@Override
	public void delete(Integer silde_id) {
		this.daoSupport.execute("delete from es_store_silde where silde_id = ?", silde_id);;
		
	}

}
