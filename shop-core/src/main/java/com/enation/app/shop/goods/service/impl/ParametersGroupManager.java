package com.enation.app.shop.goods.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.goods.model.po.ParametersGroup;
import com.enation.app.shop.goods.service.IParametersGroupManager;
import com.enation.app.shop.goods.service.IParametersManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.UnProccessableServiceException;


/**
 * 参数组
 * @author fk
 * @version v1.0
 * 2017年4月10日 下午3:43:19
 */
@Service
public class ParametersGroupManager implements IParametersGroupManager {

	@Autowired
	private IDaoSupport daoSupport;
	@Autowired
	private IParametersManager parametersManager;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ParametersGroup add(ParametersGroup categoryParamsGroup) {
		
		String sql = "select * from es_parameter_group where category_id = ? order by sort desc limit 0,1";
		ParametersGroup grouptmp = this.daoSupport.queryForObject(sql, ParametersGroup.class, categoryParamsGroup.getCategory_id());
		if(grouptmp==null){
			categoryParamsGroup.setSort(1);
		}else{
			categoryParamsGroup.setSort(grouptmp.getSort()+1);
		}
		
		this.daoSupport.insert("es_parameter_group", categoryParamsGroup);
		int id = this.daoSupport.getLastId("es_parameter_group");
		categoryParamsGroup.setGroup_id(id);
		
		return categoryParamsGroup;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteGroup(Integer group_id) {
		
		String sql = "delete from es_parameter_group where group_id = ?";
		
		this.daoSupport.execute(sql, group_id);
		//删除参数组，需要将参数下的参数同时删除
		parametersManager.deleteByGroup(group_id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ParametersGroup update(ParametersGroup categoryParamsGroup) {
		
		this.daoSupport.update("es_parameter_group", categoryParamsGroup, "group_id = " +categoryParamsGroup.getGroup_id());
		
		return categoryParamsGroup;
	}

	@Override
	public void groupSort(Integer group_id, String sort_type) {
		String sql = "select * from es_parameter_group where group_id = ?";
		ParametersGroup curGroup = this.daoSupport.queryForObject(sql, ParametersGroup.class, group_id);//要操作的参数组
		
		if("up".equals(sort_type)){
			sql = "select * from es_parameter_group where sort<? and category_id=? order by sort desc limit 0,1";
		}else if("down".equals(sort_type)){
			sql = "select * from es_parameter_group where sort>? and category_id=? order by sort asc limit 0,1";
		}else{
			throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR, "排序参数错误");
		}
		ParametersGroup changeGroup = this.daoSupport.queryForObject(sql, ParametersGroup.class,curGroup.getSort(),curGroup.getCategory_id());
		if(changeGroup != null ){
			sql = "update es_parameter_group set sort = ? where group_id = ?";
			this.daoSupport.execute(sql, changeGroup.getSort(),curGroup.getGroup_id());
			this.daoSupport.execute(sql, curGroup.getSort(),changeGroup.getGroup_id());
		}
	}

}
