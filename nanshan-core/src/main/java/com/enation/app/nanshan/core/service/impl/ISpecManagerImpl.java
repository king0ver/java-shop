package com.enation.app.nanshan.core.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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
			String specId = (String) params.get("specId");
			if(!StringUtil.isEmpty(keyword)){
				sql+=" where spec.spec_name '%"+keyword+"%'";
			}
			if(specId!=null){
				sql+=" where spec.spec_id in ("+specId+")";
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
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("spec_name", spec.getSpec_name());
		params.put("operator", spec.getOperator());
		params.put("update_time", DateUtil.getDateline());
		daoSupport.update("es_nanshan_spec", params, "spec_id="+spec.getSpec_id());
	}

	@Override
	public void delete(int id) {
		
	}

	@Override
	public List<SpecVal> querySpecValList(Map<String, Object> params) {
		String sql = "select * from es_nanshan_specval spec where 1=1";
		if(params != null){
			String keyword =null;
			String specId = null;
			String isValid = null;
			if(params.containsKey("keyword")){
				keyword = (String) params.get("keyword");
			}
			if(params.containsKey("specId")){
				specId = (String) params.get("specId");
			}
			if(params.containsKey("is_valid")){
				isValid = (String) params.get("is_valid");
			}
			if(!StringUtil.isEmpty(isValid)){
				sql+=" and spec.is_valid ="+isValid;
			}
			if(!StringUtil.isEmpty(keyword)){
				sql+=" and spec.spec_name '%"+keyword+"%'";
			}
			if(!StringUtil.isEmpty(specId)){
				sql+=" and spec.spec_id in ("+specId+")";
			}
		}
		List<SpecVal> specValList = daoSupport.queryForList(sql, SpecVal.class);
		return specValList;
	}

	@Override
	public Spec querySpecById(int id) {
		return daoSupport.queryForObject("select * from es_nanshan_spec where spec_id=?", Spec.class, id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public void updateSpecInfo(Map<String, Object> params) {
		Spec spec = (Spec) params.get("spec");
		@SuppressWarnings("unchecked")
		List<SpecVal> specValList =  (List<SpecVal>) params.get("specValList");
		params.put("specId",String.valueOf(spec.getSpec_id()));
		List<SpecVal> specValInfoList = querySpecValList(params);
		List<SpecVal> existList = new ArrayList<SpecVal>();
		List<SpecVal> notExistList = new ArrayList<SpecVal>();
		List<SpecVal> delList = new ArrayList<SpecVal>();
		compareSpecInfo(specValList,specValInfoList,delList,existList,notExistList);
		//更新属性
		edit(spec);
		//更新属性值
		if(existList!=null && existList.size()>0){
			for (SpecVal specVal : existList) {
				updateSpecValInfo(specVal);
			}
		}
		if(delList!=null && delList.size()>0){
			for (SpecVal specVal : delList) {
				specVal.setIs_valid(1);
				updateSpecValInfo(specVal);
			}
		}
		if(notExistList!=null && notExistList.size()>0){
			for (SpecVal specVal : notExistList) {
				daoSupport.insert("es_nanshan_specval", specVal);
			}
		}
	}
	
	public void updateSpecValInfo(SpecVal specVal){
		Map<String, Object> params = new HashMap<String, Object>();
		if(StringUtils.isBlank(specVal.getSpecval_name())){
			params.put("specval_name", specVal.getSpecval_name());
		}
		if(StringUtils.isBlank(specVal.getOperator())){
			params.put("operator", specVal.getOperator());
		}
		if(null != specVal.getIs_valid()){
			params.put("is_valid", specVal.getIs_valid());
		}
		params.put("update_time", DateUtil.getDateline());
		daoSupport.update("es_nanshan_specval", params, "specval_id="+specVal.getSpecval_id());
	}

	/**
	 * 比较id
	 * @param specValList
	 * @param specValInfoList
	 * @param existMap
	 * @param notExistMap
	 */
	private void compareSpecInfo(List<SpecVal> specValList,List<SpecVal> specValInfoList, List<SpecVal> delList,List<SpecVal> existList,List<SpecVal> notExistList) {
		//查找存在的,与不存在的
		for (SpecVal specVal : specValList) {
			Integer specValId = specVal.getSpecval_id();
			if(specValId==null){
				if(!notExistList.contains(specVal)){
					notExistList.add(specVal);
					continue;
				}
			}
			for (SpecVal specValInfo : specValInfoList) {
				Integer specValInfoId = specValInfo.getSpecval_id();
				if(specValInfoId.intValue() == specValId.intValue()){
					existList.add(specVal);
					break;
				}
			}
		}
		//需要删除的
		for (SpecVal specValInfo : specValInfoList) {
			boolean flag = false;
			Integer specValInfoId = specValInfo.getSpecval_id();
			for (SpecVal specVal : existList) {
				if(specValInfoId.intValue()==specVal.getSpecval_id().intValue()){
					flag = true;
				}
			}
			if(!flag){
				delList.add(specValInfo);
			}
		}
	}

}
