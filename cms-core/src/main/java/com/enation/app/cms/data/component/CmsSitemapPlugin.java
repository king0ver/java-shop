package com.enation.app.cms.data.component;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.SiteMapUrl;
import com.enation.app.base.core.plugin.IRecreateMapEvent;
import com.enation.app.base.core.service.ISitemapManager;
import com.enation.app.cms.data.event.IDataDeleteEvent;
import com.enation.app.cms.data.event.IDataSaveEvent;
import com.enation.app.cms.data.model.DataCat;
import com.enation.app.cms.data.model.DataModel;
import com.enation.app.cms.data.service.IDataCatManager;
import com.enation.app.cms.data.service.IDataManager;
import com.enation.framework.plugin.AutoRegisterPlugin;

/**
 * CMS的sitemap生成插件
 * @author kingapex
 *
 */
@Component
public class CmsSitemapPlugin extends AutoRegisterPlugin implements	IDataSaveEvent, IDataDeleteEvent, IRecreateMapEvent {
	
	private ISitemapManager sitemapManager;
	private IDataCatManager dataCatManager;
	private IDataManager dataManager;

	public void register() {

	}

	public void onSave(Map<String, Object> data, DataModel dataModel, int dataSaveType) {
		DataCat cat = this.dataCatManager.get(Integer.valueOf(data.get("cat_id").toString()));
		if (cat.getTositemap() == 1) { // 要加入到Sitemap
			SiteMapUrl url = new SiteMapUrl();
			url.setLoc("/" + cat.getDetail_url() + "-" + cat.getCat_id() + "-" + data.get("id") + ".html");
			url.setLastmod(System.currentTimeMillis());
			this.sitemapManager.addUrl(url);
		}
	}

	public void onDelete(Integer catid, Integer articleid) {
		DataCat cat = this.dataCatManager.get(catid);
		
		//ISitemapManager这个接口暂时没有实现类，会抛出空指针异常，所以暂时注释掉
		//this.sitemapManager.delete("/" + cat.getDetail_url() + "-" + cat.getCat_id() + "-" + articleid + ".html");
	}
	
	public void onRecreateMap() {
		List<DataCat> listCat = this.dataCatManager.listAllChildren(0);
		for (DataCat cat : listCat) {
			if (cat.getTositemap() == 1) {
				List<Map<String, Object>> list = this.dataManager.list(cat.getCat_id());
				for (Map map : list) {
					SiteMapUrl url = new SiteMapUrl();
					url.setLoc("/" + cat.getDetail_url() + "-" + cat.getCat_id() + "-" + map.get("id") + ".html");
					url.setLastmod(System.currentTimeMillis());
					this.sitemapManager.addUrl(url);
				}
			}
		}
	}

	
	public IDataCatManager getDataCatManager() {
		return dataCatManager;
	}

	public void setDataCatManager(IDataCatManager dataCatManager) {
		this.dataCatManager = dataCatManager;
	}

	public IDataManager getDataManager() {
		return dataManager;
	}

	public void setDataManager(IDataManager dataManager) {
		this.dataManager = dataManager;
	}

	public ISitemapManager getSitemapManager() {
		return sitemapManager;
	}

	public void setSitemapManager(ISitemapManager sitemapManager) {
		this.sitemapManager = sitemapManager;
	}

}
