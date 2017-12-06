package com.enation.app.shop.goods.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.shop.goods.model.po.Category;
import com.enation.app.shop.goods.model.po.Parameters;
import com.enation.app.shop.goods.model.po.ParametersGroup;
import com.enation.app.shop.goods.model.vo.GoodsParamsList;
import com.enation.app.shop.goods.model.vo.GoodsParamsVo;
import com.enation.app.shop.goods.service.ICategoryManager;
import com.enation.app.shop.goods.service.ICategoryParamsManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.UnProccessableServiceException;

/**
 * 分类参数manager
 * 
 * @author fk
 * @version v1.0 2017年4月6日 下午6:02:55
 */
@Service
public class CategoryParamsManager implements ICategoryParamsManager {

	@Autowired
	private IDaoSupport daoSupport;
	@Autowired
	private ICategoryManager categoryManager;

	@Override
	public List<GoodsParamsList> getParamByCatAndGoods(Integer category_id, Integer goods_id) {
		String sql = "select * from es_parameter_group where category_id = ?";
		List<ParametersGroup> groupList = this.daoSupport.queryForList(sql, ParametersGroup.class, category_id);
		sql = "select p.param_id,p.param_name,p.param_type,p.`options`,p.unit,p.required,gp.param_value,p.group_id "
				+ "from es_parameters p "
				+ "left join es_goods_params gp on p.param_id=gp.param_id where p.category_id = ?"
				+ " and (gp.goods_id = ?  or gp.goods_id is null)";

		List<GoodsParamsVo> paramList = this.daoSupport.queryForList(sql, GoodsParamsVo.class, category_id, goods_id);

		List<GoodsParamsList> resList = this.convertParamList(groupList, paramList);

		return resList;
	}

	@Override
	public List<GoodsParamsList> getParamByCategory(Integer category_id) {
		String sql = "select * from es_parameter_group where category_id = ? order by sort asc";
		List<ParametersGroup> groupList = this.daoSupport.queryForList(sql, ParametersGroup.class, category_id);
		sql = "select p.param_id,p.param_name,p.param_type,p.`options`,p.unit,p.required,p.group_id,p.is_index "
				+ "from es_parameters p where p.category_id = ? order by sort asc";

		List<GoodsParamsVo> paramList = this.daoSupport.queryForList(sql, GoodsParamsVo.class, category_id);

		List<GoodsParamsList> resList = this.convertParamList(groupList, paramList);

		return resList;
	}

	/**
	 * 拼装返回值
	 * 
	 * @param paramList
	 * @return
	 */
	private List<GoodsParamsList> convertParamList(List<ParametersGroup> groupList, List<GoodsParamsVo> paramList) {
		Map<Integer, List<GoodsParamsVo>> map = new HashMap<>();
		for (GoodsParamsVo param : paramList) {
			if (map.get(param.getGroup_id()) != null) {
				map.get(param.getGroup_id()).add(param);
			} else {
				List<GoodsParamsVo> list = new ArrayList<>();
				list.add(param);
				map.put(param.getGroup_id(), list);
			}
		}
		List<GoodsParamsList> resList = new ArrayList<>();
		for (ParametersGroup group : groupList) {
			GoodsParamsList list = new GoodsParamsList();
			list.setGroup_name(group.getGroup_name());
			list.setGroup_id(group.getGroup_id());
			list.setParams(map.get(group.getGroup_id()));
			resList.add(list);
		}
		return resList;
	}

	@Override
	public List<Parameters> getIndexParams(Integer category_id) {
		String sql = "select * from es_parameters where category_id =? and is_index = 1 and param_type = 2 ";
		List<Parameters> list = this.daoSupport.queryForList(sql, Parameters.class, category_id);
		return list;
	}

	/**
	 * 查询分类关联的参数，包括参数组 分类列表的参数，查询每个分类的参数
	 */
	@Override
	public List<GoodsParamsList> getCategoryParams(Integer category_id, Integer goods_id) {
		Category category = categoryManager.get(category_id);
		if (category == null) {
			throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR, "该分类不存在");
		}
		List<GoodsParamsList> list = null;
		if (goods_id != null) {
			list = getParamByCatAndGoods(category_id, goods_id);
		} else {
			list = getParamByCategory(category_id);
		}
		return list;
	}
}
