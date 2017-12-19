package com.enation.app.nanshan.core.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.nanshan.core.model.Spec;
import com.enation.app.nanshan.core.model.SpecVal;
import com.enation.app.nanshan.core.service.ISpecManager;
import com.enation.framework.annotation.Log;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.log.LogType;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

/**
 * 规格属性服务实现
 * @author jianjianming
 * @version $Id: ISpecManagerImpl.java,v 0.1 2017年12月13日 下午5:16:25$
 */
@Service("iSpecManager")
public class ISpecManagerImpl implements ISpecManager {

	@Autowired
	private IDaoSupport daoSupport;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	@Log(type=LogType.SETTING,detail="批量修改uri映射信息")
	public void add(Spec spec,List<SpecVal> specValList) {
		spec.setCreation_time(DateUtil.getDateline());
		daoSupport.insert("es_nanshan_spec", spec);
		int specId = daoSupport.getLastId("es_nanshan_spec");
		if(specValList !=null && specValList.size()>0){
			for (SpecVal specVal : specValList) {
				specVal.setSpec_id(specId);
				specVal.setCreation_time(DateUtil.getDateline());
				daoSupport.insert("es_nanshan_specval", specVal);
			}
		}
	}

	@Override
	public List<Spec> list(Map<String, Object> params) {
		String sql = "select * from es_nanshan_spec spec";
		if(params != null){
			String keyword = (String) params.get("keyword");
			Integer specId = (Integer) params.get("specId");
			if(!StringUtil.isEmpty(keyword)){
				sql+=" where spec.spec_name '%"+keyword+"%'";
			}
			if(specId!=null){
				sql+=" where spec.spec_id="+specId;
			}
		}
		List<Spec> specList = daoSupport.queryForList(sql, Spec.class);
		for (Spec spec : specList) {
			spec.setCreationTime(DateUtil.toString(spec.getCreation_time(), null));
		}
		return specList;
		
	}

	@Override
	public void edit(Spec spec) {
	}

	@Override
	public void delete(int id) {
	}

	@Override
	public List<SpecVal> querySpecValList(Map<String, Object> params) {
		String sql = "select * from es_nanshan_specval spec";
		if(params != null){
			String keyword = (String) params.get("keyword");
			Integer specId = (Integer) params.get("specId");
			if(!StringUtil.isEmpty(keyword)){
				sql+=" where spec.spec_name '%"+keyword+"%'";
			}
			if(specId!=null){
				sql+=" where spec.spec_id="+specId;
			}
		}
		List<SpecVal> specValList = daoSupport.queryForList(sql, SpecVal.class);
		return specValList;
	}

}
