package com.enation.app.shop.goods.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.goods.model.mapper.SpecValueMapper;
import com.enation.app.shop.goods.model.po.Category;
import com.enation.app.shop.goods.model.po.SpecValue;
import com.enation.app.shop.goods.model.po.Specification;
import com.enation.app.shop.goods.model.vo.SelectVo;
import com.enation.app.shop.goods.model.vo.SpecValueVo;
import com.enation.app.shop.goods.model.vo.SpecificationVo;
import com.enation.app.shop.goods.service.ICategoryManager;
import com.enation.app.shop.goods.service.ISpecManager;
import com.enation.app.shop.goods.service.ISpecValueManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.annotation.Log;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.log.LogType;
import com.enation.framework.util.StringUtil;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.UnProccessableServiceException;

/**
 * 
 * 规格的操作和读取
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年9月28日 下午1:55:45
 */
@Service
public class SpecManager implements ISpecManager {

	@Autowired
	private IDaoSupport daoSupport;
	@Autowired
	private ISpecValueManager specValueManager;
	@Autowired
	private ISpecManager specManager;
	@Autowired
	private ICategoryManager categoryManager;
	@Autowired
	private ISellerManager sellerMangager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.javashop.manager.ISpecManager#add(com.enation.javashop.entity.
	 * Specification, java.util.List)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Specification add(Specification spec) {
		spec.setDisabled(1);
		this.daoSupport.insert("es_specification", spec);
		Integer spec_id = this.daoSupport.getLastId("es_specification");
		spec.setSpec_id(spec_id);
		return spec;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.javashop.manager.ISpecManager#edit(com.enation.javashop.entity.
	 * Specification, java.util.List)
	 */
	@Override
	public void edit(Specification spec) {
		this.daoSupport.update("es_specification", spec, "spec_id=" + spec.getSpec_id());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.javashop.manager.ISpecManager#delete(java.lang.Integer[])
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@Log(type = LogType.GOODS, detail = "删除了一个规格")
	public void delete(Integer[] idArray) throws Exception {
		boolean res = specManager.checkUsed(idArray);
		if (res) {
			throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR, "存在被使用的规格,不可删除");
		}
		List<Object> term = new ArrayList<>();
		String[] id = new String[idArray.length];
		for (int i = 0; i < idArray.length; i++) {
			id[i] = "?";
			term.add(idArray[i]);
		}
		String idStr = StringUtil.arrayToString(id, ",");
		String sql = "delete from es_specification where spec_id in (" + idStr + ")";
		this.daoSupport.execute(sql, term.toArray());

		sql = "delete from es_spec_values where spec_id in (" + idStr + ")";
		this.daoSupport.execute(sql, term.toArray());

		sql = "delete from es_goods_spec where spec_id in (" + idStr + ")";
		this.daoSupport.execute(sql, term.toArray());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.javashop.manager.ISpecManager#getSpecList(java.lang.Integer,
	 * java.lang.Integer)
	 */
	@Override
	public Page getSpecList(Integer page_no, Integer page_size) {
		String sql = "select * from es_specification order by spec_id desc";
		Page page = this.daoSupport.queryForPage(sql, page_no, page_size);
		return page;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.javashop.manager.ISpecManager#get(java.lang.Integer)
	 */
	@Override
	public Specification get(Integer spec_id) throws Exception {
		String sql = "select * from es_specification where spec_id = ?";
		Specification vo = this.daoSupport.queryForObject(sql, Specification.class, spec_id);
		return vo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.javashop.manager.ISpecManager#checkUsed(java.lang.Integer[])
	 */
	@Override
	public boolean checkUsed(Integer[] spec_id) {
		if (spec_id == null) {
			return false;
		}

		String idStr = StringUtil.arrayToString(spec_id, ",");
		String sql = "select count(0)  from  es_goods_spec where spec_id in (-1," + idStr + ")";

		int count = this.daoSupport.queryForInt(sql);

		return count > 0;
	}

	@Override
	public List<SpecificationVo> getSpecByCategory(Integer category_id, Integer goods_id) {
		String sql = "select s.spec_id,s.spec_name,s.spec_type "
				+ "from es_specification s inner join es_category_spec cs on s.spec_id=cs.spec_id "
				+ "where cs.category_id = ?";
		List<SpecificationVo> list = this.daoSupport.queryForList(sql, SpecificationVo.class, category_id);
		if (list != null && list.size() > 0) {
			String spec_ids = "";
			for (SpecificationVo spec : list) {
				spec_ids += "," + spec.getSpec_id();
			}
			List<SpecValueVo> valueList = this.specValueManager.querySpecsValues(spec_ids.substring(1));
			List<Map> selectList = null;
			Map<Integer, List> selectmap = new HashMap<>();
			if (goods_id != null) {
				sql = "select spec_value_id,spec_id from es_goods_spec where goods_id = ?";
				selectList = this.daoSupport.queryForList(sql, goods_id);
				if (selectList != null) {
					for (Map map : selectList) {
						if (selectmap.get(map.get("spec_id")) == null) {
							List<Integer> select = new ArrayList<>();
							select.add((Integer) map.get("spec_value_id"));
							selectmap.put((Integer) map.get("spec_id"), select);
						} else {
							List<Integer> select = selectmap.get((Integer) map.get("spec_id"));
							select.add((Integer) map.get("spec_value_id"));
							selectmap.put((Integer) map.get("spec_id"), select);
						}
					}
				}
			}
			Map<Integer, List<SpecValueVo>> map = new HashMap<>();
			for (SpecValueVo value : valueList) {
				if (map.get(value.getSpec_id()) == null) {
					List<SpecValueVo> values = new ArrayList<>();
					values.add(value);
					map.put(value.getSpec_id(), values);
				} else {
					map.get(value.getSpec_id()).add(value);
				}
			}
			for (SpecificationVo spec : list) {
				spec.setSpecValues(map.get(spec.getSpec_id()));
				spec.setCheck_ids(selectmap.get(spec.getSpec_id()));// 如果有商品id，查询选中的规格
			}
		}

		return list;
	}

	@Override
	public List<SelectVo> getCatSpec(Integer category_id) {
		String sql = "select s.spec_id id,s.spec_name text, "
				+ "case category_id when ? then true else false end selected "
				+ "from es_specification s left join (select * from es_category_spec where category_id = ?) cs "
				+ "on s.spec_id=cs.spec_id";
		return this.daoSupport.queryForList(sql, SelectVo.class, category_id, category_id);
	}

	@Override
	public boolean checkUsed(Integer spec_value_id) {
		String sql = "select count(0) from es_goods_spec where spec_value_id=?";

		return this.daoSupport.queryForInt(sql, spec_value_id) > 0;
	}

	/**
	 * 根据规格，和规格值查询并组装
	 * 
	 * @param spec_ids
	 * @param spec_vids
	 * @return
	 */
	public List<SpecificationVo> queryBySpecAndValue(String spec_ids, String spec_vids) {
		spec_ids = spec_ids.replaceAll("、", ",");
		spec_vids = spec_vids.replaceAll("、", ",");
		String sql = "select * from es_specification where spec_id in (-1" + spec_ids + ")";
		List<Specification> list = this.daoSupport.queryForList(sql, Specification.class);
		sql = "select * from es_spec_values where spec_value_id in (-1" + spec_vids + ")";
		List<SpecValue> valueList = this.daoSupport.queryForList(sql, SpecValue.class);
		List<SpecificationVo> voList = new ArrayList<>();
		for (Specification spec : list) {// 暂时先这么写
			SpecificationVo vo = new SpecificationVo();
			vo.setSpec_id(spec.getSpec_id());
			vo.setSpec_name(spec.getSpec_name());
			vo.setSpec_type(spec.getSpec_type());
			List<SpecValueVo> specValues = new ArrayList<>();
			for (SpecValue value : valueList) {
				if (spec.getSpec_id().intValue() == value.getSpec_id().intValue()) {
					SpecValueVo valuevo = new SpecValueVo();
					valuevo.setSpec_id(spec.getSpec_id());
					valuevo.setSpec_value_id(value.getSpec_value_id());
					valuevo.setSpec_value(value.getSpec_value());
					valuevo.setSpec_image(value.getSpec_image());
					specValues.add(valuevo);
				}
			}
			vo.setSpecValues(specValues);
			voList.add(vo);
		}
		return voList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.shop.goods.service.ISpecManager#listByCatid(java.lang.
	 * Integer)
	 */
	@Override
	public List<Specification> querySellerSpec(Integer catid, Integer seller_id) {
		String sql = "select s.spec_id,s.spec_name,s.spec_type "
				+ "from es_specification s inner join es_category_spec cs on s.spec_id=cs.spec_id "
				+ "where cs.category_id = ? and (s.seller_id=0 or s.seller_id=?)";
		List<Specification> specList = this.daoSupport.queryForList(sql, Specification.class, catid, seller_id);
		Map<Integer, List<SpecValue>> map = new HashMap<>();
		for (Specification spec : specList) {
			String sqlValue = "select * from es_spec_values where spec_id = ? and (seller_id=0 or seller_id=?)";
			List<SpecValue> valueList = this.daoSupport.queryForList(sqlValue, SpecValue.class, spec.getSpec_id(),
					seller_id);
			map.put(spec.getSpec_id(), valueList);
		}
		for (Specification specValue : specList) {
			specValue.setValueList(map.get(specValue.getSpec_id()));
		}
		return specList;
	}

	@Override
	public List list() {
		String sql = "select * from es_specification order by spec_id desc";
		return this.daoSupport.queryForList(sql);
	}

	@Override
	public List<Specification> listSpecAndValueByType(int goodsTypeId, int goodsId) {
		String sql = "select s.* from es_specification s inner join es_type_spec ts on s.spec_id=ts.spec_id where ts.type_id=?";
		List<Specification> specList = this.daoSupport.queryForList(sql, Specification.class, goodsTypeId);
		List valueList = null;
		if (goodsId == 0) {
			sql = "select * from es_spec_values where inherent_or_add=0 and spec_id in (select spec_id from es_type_spec where type_id=?) order by spec_value_id";
			valueList = this.daoSupport.queryForList(sql, new SpecValueMapper(), goodsTypeId);
		} else {
			sql = "select * from es_spec_values where spec_value_id in (select spec_value_id from es_goods_spec where goods_id =?) OR inherent_or_add=0 and spec_id in (select spec_id from es_type_spec where type_id=?) order by spec_value_id";
			valueList = this.daoSupport.queryForList(sql, new SpecValueMapper(), goodsId, goodsTypeId);
		}

		for (Specification spec : specList) {
			List<SpecValue> newList = new ArrayList<SpecValue>();
			for (SpecValue value : (List<SpecValue>) valueList) {
				if (value.getSpec_id().intValue() == spec.getSpec_id().intValue()) {
					newList.add(value);
				}
			}
			spec.setValueList(newList);
		}
		return specList;
	}

	@Override
	public List<SpecValueVo> getSpecValueList(Integer spec_id) {
		List<SpecValueVo> valueList = specValueManager.querySpecValues(spec_id);
		return valueList;
	}

	@Override
	public void saveSpecValue(Integer spec_id, List<SpecValue> valueList) {
		// 先删除规格再添加
		String sql = "delete from es_spec_values where spec_id=? and seller_id=0";
		this.daoSupport.execute(sql, spec_id);
		specValueManager.add(spec_id, valueList);

	}

	/**
	 * 查询某分类关联的规格
	 */
	@Override
	public List<SpecificationVo> getCategorySpec(Integer category_id, Integer goods_id) {
		Category category = categoryManager.get(category_id);
		if (category == null) {
			throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR, "该分类不存在");
		}
		List<SpecificationVo> list = specManager.getSpecByCategory(category_id, goods_id);

		return list;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Specification addSellerSpec(SpecificationVo specificationVo) {
		Seller seller = sellerMangager.getSeller();
		Specification specification = new Specification();
		specification.setSeller_id(seller.getStore_id());
		if (specificationVo.getSpec_name() == null) {
			throw new UnProccessableServiceException(ErrorCode.INVALID_REQUEST_PARAMETER, "规格名称不能为空");
		}
		specification.setSpec_name(specificationVo.getSpec_name());
		if (specificationVo.getSpec_type() == null) {
			specificationVo.setSpec_type(0);
		}
		specification.setSpec_type(specificationVo.getSpec_type());
		specification = this.add(specification);
		categoryManager.saveSellerSpec(specificationVo.getCategory_id(), specification.getSpec_id());
		return specification;
	}
}
