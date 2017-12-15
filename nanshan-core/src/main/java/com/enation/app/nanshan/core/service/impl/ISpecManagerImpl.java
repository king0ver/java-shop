package com.enation.app.nanshan.core.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.nanshan.core.model.Spec;
import com.enation.app.nanshan.core.service.ISpecManager;
import com.enation.framework.database.IDaoSupport;
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
	public void add(Spec spec) {
	}

	@Override
	public List<Spec> list(Map<String, Object> params) {
		String sql = "select * from es_nanshan_spec spec";
		if(params != null){
			String keyword = (String) params.get("keyword");
			if(!StringUtil.isEmpty(keyword)){
				sql+=" where spec spec_name '%"+keyword+"%'";
			}
		}
		return daoSupport.queryForList(sql, Spec.class);
	}

	@Override
	public void edit(Spec spec) {
	}

	@Override
	public void delete(int id) {
	}

}
