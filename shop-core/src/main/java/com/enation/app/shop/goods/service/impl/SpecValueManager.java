package com.enation.app.shop.goods.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.goods.model.po.SpecValue;
import com.enation.app.shop.goods.model.vo.SpecValueVo;
import com.enation.app.shop.goods.service.ISpecValueManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.database.IDaoSupport;

/**
 * 规格值manager
 * 
 * @author fk
 * @version v1.0 2017年3月22日 下午2:07:32
 */
@Service
public class SpecValueManager implements ISpecValueManager {

	@Autowired
	private IDaoSupport daoSupport;
	@Autowired
	private ISellerManager sellerMangager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.javashop.manager.ISpecValueManager#querySpecValues(java.lang.
	 * Integer)
	 */
	@Override
	public List<SpecValueVo> querySpecValues(Integer spec_id) {
		String sql = "select * from es_spec_values where spec_id = ? and seller_id=0";
		List<SpecValueVo> list = this.daoSupport.queryForList(sql, SpecValueVo.class, spec_id);
		return list;
	}

	@Override
	public List<SpecValueVo> querySpecsValues(String spec_ids) {

		String sql = "select * from es_spec_values where spec_id in (" + spec_ids + ")";
		List<SpecValueVo> list = this.daoSupport.queryForList(sql, SpecValueVo.class);

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.javashop.manager.ISpecValueManager#add(com.enation.javashop.
	 * entity.SpecValue)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void add(Integer spec_id, List<SpecValue> value) {
		if (value != null && value.size() > 0) {
			for (SpecValue specvalue : value) {
				if (specvalue.getSpec_image() == null) {
					specvalue.setSpec_image("");
				}
				this.daoSupport.execute("insert into es_spec_values (spec_id,spec_value,spec_image)values(?,?,?)",
						spec_id, specvalue.getSpec_value(), specvalue.getSpec_image());

			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.javashop.manager.ISpecValueManager#update(com.enation.javashop.
	 * entity.SpecValue)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void update(SpecValue value) {
		this.daoSupport.update("es_spec_values", value, "spec_value_id=" + value.getSpec_value_id());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.shop.goods.service.ISpecValueManager#listBySpecid(java.lang.
	 * Integer)
	 */
	@Override
	public List<SpecValue> listBySpecid(Integer specid) {
		String sql = "select * from es_spec_values where spec_id = ?";
		List<SpecValue> list = this.daoSupport.queryForList(sql, SpecValue.class, specid);
		return list;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public SpecValue addSpecValue(SpecValue specValue) {
		if (specValue != null) {
			Seller seller = sellerMangager.getSeller();
			if (specValue.getSpec_image() == null) {
				specValue.setSpec_image("");
			}
			this.daoSupport.execute(
					"insert into es_spec_values (spec_id,spec_value,spec_image,seller_id) values(?,?,?,?)",
					specValue.getSpec_id(), specValue.getSpec_value(), specValue.getSpec_image(), seller.getStore_id());
			specValue.setSpec_value_id(this.daoSupport.getLastId("es_spec_values"));
		}
		return specValue;
	}

}
