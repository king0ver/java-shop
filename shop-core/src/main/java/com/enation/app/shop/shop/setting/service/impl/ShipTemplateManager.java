package com.enation.app.shop.shop.setting.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.shop.shop.setting.model.po.ShipTemplate;
import com.enation.app.shop.shop.setting.service.IShipTemplateManager;
import com.enation.framework.cache.ICache;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.util.StringUtil;

@Service
public class ShipTemplateManager implements IShipTemplateManager {

	private static final String PREFIX = "ship_template_";

	private static final String SELECT_PREFIX = "ship_select_template_";

	@Autowired
	private IDaoSupport daoSupport;

	@Autowired
	private ICache cache;

	@Override
	public ShipTemplate save(ShipTemplate template) {
		this.daoSupport.insert("es_ship_template", template);
		template.setTemplate_id(this.daoSupport.getLastId("es_ship_template"));
		this.reputCache(template.getTemplate_id(), template);
		this.reputCacheSeller(template.getSeller_id(), null);
		return template;
	}

	@Override
	public ShipTemplate edit(ShipTemplate template) {
		this.daoSupport.update("es_ship_template", template, "template_id=" + template.getTemplate_id());
		this.reputCache(template.getTemplate_id(), template);
		this.reputCacheSeller(template.getSeller_id(), null);
		return template;
	}

	@Override
	public List<ShipTemplate> getStoreTemplate(Integer seller_id) {
		List<ShipTemplate> cache = this.sellerCache(seller_id);
		if (cache != null) {
			return cache;
		} else {
			return this.reputCacheSeller(seller_id, null);
		}
	}

	private List<ShipTemplate> getStoreTemplateDB(Integer seller_id) {
		return this.daoSupport.queryForList("select * from es_ship_template where seller_id = ? ", ShipTemplate.class,
				seller_id);
	}

	@Override
	public void delete(Integer template_id) {
		this.daoSupport.execute("delete from es_ship_template where template_id=?", template_id);
		Integer seller_id = this.getOne(template_id).getSeller_id();
		this.cache.remove(ShipTemplateManager.SELECT_PREFIX + template_id);
		this.reputCacheSeller(seller_id, null);
	}

	@Override
	public ShipTemplate getOne(Integer template_id) {
		ShipTemplate tpl = (ShipTemplate) this.cache.get(ShipTemplateManager.SELECT_PREFIX + template_id);
		if (tpl == null) {
			return this.reputCache(template_id, null);
		}
		return tpl;
	}

	private ShipTemplate getOneDB(Integer template_id) {
		return this.daoSupport.queryForObject("select * from es_ship_template where template_id = ?",
				ShipTemplate.class, template_id);
	}

	@Override
	public List<ShipTemplate> getStoreTemplate(Integer seller_id, Integer area_id) {
		List<ShipTemplate> templates = this.getStoreTemplate(seller_id);
		List<ShipTemplate> list = new ArrayList<ShipTemplate>();
		for (ShipTemplate shipTemplate : templates) {
			if (StringUtil.isEmpty(shipTemplate.getArea_id())) {
				list.add(shipTemplate);
			} else if (shipTemplate.getArea_id().indexOf("," + area_id + ",") > -1) {
				list.add(shipTemplate);
				// 如果没有设置地区，则默认所有地区都可以使用
			}

		}
		return list;
	}

	/**
	 * 获取店铺
	 * 
	 * @param seller_id
	 * @return
	 */
	private List<ShipTemplate> sellerCache(Integer seller_id) {
		return (List<ShipTemplate>) cache.get(ShipTemplateManager.PREFIX + seller_id);
	}

	/**
	 * 更新 商家全部物流 缓存
	 * 
	 * @param seller_id
	 *            商家id
	 * @param list
	 *            数据集合，传递空则会再次查询
	 */
	private List<ShipTemplate> reputCacheSeller(Integer seller_id, List list) {
		if (list == null) {
			list = this.getStoreTemplateDB(seller_id);
		}
		cache.put(ShipTemplateManager.PREFIX + seller_id, list);
		return list;
	}

	/**
	 * 更新缓存
	 * 
	 * @param seller_id
	 *            商家id
	 * @param list
	 *            数据集合，传递空则会再次查询
	 */
	private ShipTemplate reputCache(Integer tpl_id, ShipTemplate tpl) {
		if (tpl == null) {
			tpl = this.getOneDB(tpl_id);
		}
		cache.put(ShipTemplateManager.SELECT_PREFIX + tpl_id, tpl);
		return tpl;
	}

}
