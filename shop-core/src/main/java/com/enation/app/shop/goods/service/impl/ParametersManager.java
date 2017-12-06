package com.enation.app.shop.goods.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.goods.model.po.Parameters;
import com.enation.app.shop.goods.service.IParametersManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.UnProccessableServiceException;


/**
 * 参数
 * @author fk
 * @version v1.0
 * 2017年4月10日 下午4:45:34
 */
@Service
public class ParametersManager implements IParametersManager {
	
	@Autowired
	private IDaoSupport daoSupport;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Parameters add(Parameters param) {
		
		String sql = "select * from es_parameters where group_id = ? order by sort desc limit 0,1";
		Parameters paramtmp = this.daoSupport.queryForObject(sql, Parameters.class, param.getGroup_id());
		if(paramtmp==null){
			param.setSort(1);
		}else{
			param.setSort(paramtmp.getSort()+1);
		}
		
		this.daoSupport.insert("es_parameters",param);
		int id = this.daoSupport.getLastId("es_parameter_group");
		param.setParam_id(id);
		
		return param;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Integer param_id) {
		
		String sql = "delete from es_parameters where param_id = ?";
		this.daoSupport.execute(sql, param_id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Parameters update(Parameters param) {
		
		this.daoSupport.update("es_parameters", param, "param_id = "+param.getParam_id());
		
		return param;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteByGroup(Integer group_id) {
		
		String sql = "delete from es_parameters where group_id = ?";
		this.daoSupport.execute(sql, group_id);
	}

	@Override
	public void paramSort(Integer param_id, String sort_type) {
		
		String sql = "select * from es_parameters where param_id = ?";
		
		Parameters curParam = this.daoSupport.queryForObject(sql, Parameters.class, param_id);//要操作的参数组
		
		if("up".equals(sort_type)){
			sql = "select * from es_parameters where sort<? and group_id=? order by sort desc limit 0,1";
		}else if("down".equals(sort_type)){
			sql = "select * from es_parameters where sort>? and group_id=? order by sort asc limit 0,1";
		}else{
			throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR, "排序参数错误");
		}
		Parameters changeParam = this.daoSupport.queryForObject(sql, Parameters.class,curParam.getSort(),curParam.getGroup_id());
		if(changeParam != null ){
			sql = "update es_parameters set sort = ? where param_id = ?";
			this.daoSupport.execute(sql, changeParam.getSort(),curParam.getParam_id());
			this.daoSupport.execute(sql, curParam.getSort(),changeParam.getParam_id());
		}
		
	}

	
}
