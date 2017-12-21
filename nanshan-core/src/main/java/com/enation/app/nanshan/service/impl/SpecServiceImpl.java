package com.enation.app.nanshan.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.nanshan.core.model.Spec;
import com.enation.app.nanshan.core.model.SpecVal;
import com.enation.app.nanshan.model.ArticleCat;
import com.enation.app.nanshan.service.ISpecService;
import com.enation.app.nanshan.vo.SpecValVo;
import com.enation.app.nanshan.vo.SpecVo;
import com.enation.framework.database.IDaoSupport;

/**
 * 基础属性服务实现
 * @author jianjianming
 * @version $Id: SpecService.java,v 0.1 2017年12月21日 下午3:00:46$
 */
@Service("specService")
public class SpecServiceImpl implements ISpecService {

	
	private Logger logger = Logger.getLogger(SpecServiceImpl.class);
	
	@Autowired
	private IDaoSupport daoSupport;
	
	@Override
	public List<SpecVo> querySpecInfoByCatId(Integer catId) {
		List<SpecVo> specList = new ArrayList<SpecVo>();
		try {
			if(null == catId){
				logger.info("通过分类ID查询基础属性信息：catId为空.");
				return null;
			}
			logger.info("通过分类ID查询基础属性信息：catId,"+catId);
			String catSql = "select * from es_nanshan_article_category cat where cat.cat_id="+catId;
			ArticleCat cat = daoSupport.queryForObject(catSql, ArticleCat.class);
			if(null == cat){
				logger.info("通过分类ID查询分类信息信息为空,catId="+catId);
				return null;
			}
			if(StringUtils.isBlank(cat.getSpec_id())){
				logger.info("通过分类ID查询基础属性信息没有发现关联基础属性,catId="+catId);
				return null;
			}
			String specSql = "select * from es_nanshan_spec where spec_id in ("+cat.getSpec_id()+")";
			List<Spec> specInfoList  = daoSupport.queryForList(specSql, Spec.class);
			if(specInfoList != null && specInfoList.size() > 0){
				String specIdStr = "";
				for (Spec spec : specInfoList) {
					specIdStr = specIdStr+String.valueOf(spec.getSpec_id())+","; 
				}
				if(StringUtils.isNotEmpty(specIdStr)){
					specIdStr = specIdStr.substring(0,specIdStr.length()-1);
				}
				String specValSql = "select * from es_nanshan_specval where spec_id in ("+specIdStr+")";
				List<SpecVal> specValList = daoSupport.queryForList(specValSql, SpecVal.class);
				if(specValList!=null){
					for (Spec spec : specInfoList) {
						SpecVo specVo = new SpecVo();
						Integer specId = spec.getSpec_id();
						List<SpecValVo> specValVos = new ArrayList<SpecValVo>();
						for (SpecVal specVal : specValList) {
							if(specId == specVal.getSpec_id()){
								SpecValVo specValVo = new SpecValVo();
								specValVo.setId(specVal.getSpecval_id());
								specValVo.setName(specVal.getSpecval_name());
								specValVos.add(specValVo);
							}
						}
						specVo.setId(specId);
						specVo.setName(spec.getSpec_name());
						specVo.setSpecValVos(specValVos);
						specList.add(specVo);
					}
				}
			}else{
				logger.info("通过分类关联的基础属性未查询到基础属性,spec_id="+cat.getSpec_id());
				return null;
			}
		} catch (Exception e) {
			logger.info("catId:"+catId+",通过分类ID查询基础属性信息出现异常:",e);
		}
		return specList;
	}

}
