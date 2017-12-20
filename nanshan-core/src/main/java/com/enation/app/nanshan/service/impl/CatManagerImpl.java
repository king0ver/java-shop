package com.enation.app.nanshan.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.base.core.service.dbsolution.DBSolutionFactory;
import com.enation.app.nanshan.constant.NanShanCommonConstant;
import com.enation.app.nanshan.model.NanShanActReserve;
import com.enation.app.nanshan.model.ArticleCat;
import com.enation.app.nanshan.model.NanShanArticleCatVo;
import com.enation.app.nanshan.service.ICatManager;
import com.enation.app.nanshan.util.NCatTreeUtil;
import com.enation.app.nanshan.vo.NCatVo;
import com.enation.framework.annotation.Log;
import com.enation.framework.cache.ICache;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.data.IDataOperation;
import com.enation.framework.log.LogType;

@Service("catManager")
public class CatManagerImpl implements ICatManager  {


	@Autowired
	private IDataOperation dataOperation;	

	@Autowired
	private IDaoSupport  daoSupport;
	
	@Autowired
	private ICache cache; 
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	@Log(type=LogType.SETTING,detail="初始化南山分类数据")
	public void reset() {
		Connection conn = DBSolutionFactory.getConnection(null);
		try {
			Statement state = conn.createStatement();
			state.execute("truncate table es_nanshan_article_category");	//	先清空表中数据
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String xmlFile = "file:com/enation/app/nanshan/nanshan_cat_data.xml";		
		dataOperation.imported(xmlFile);
	
	}

	
	private List<NCatVo> getCats() {	
        List<ArticleCat> list=this.getCategoryList();
        List<NCatVo> newCatlist=null;
        NCatVo nCatVo;
        if(list!=null&&list.size()>0){ 
        	
        	newCatlist=new ArrayList<NCatVo>();
        	for (ArticleCat articleCat : list) {
        		nCatVo=new NCatVo();
        		nCatVo.setId(articleCat.getCat_id());
        		nCatVo.setName(articleCat.getCat_name());
        		nCatVo.setParentId(articleCat.getParent_id());
        		nCatVo.setPcUrl(articleCat.getPc_url());
        		nCatVo.setWapUrl(articleCat.getWap_url());
        		newCatlist.add(nCatVo);
			}
        }
		return newCatlist;
	}
	
	
	/**
	 * 查询所有分类列表
	 */
	private List<ArticleCat> getCategoryList() {
		String sql = "select * from es_nanshan_article_category";
		List<ArticleCat> list = this.daoSupport.queryForList(sql, ArticleCat.class);
		return list;
	}


	@Override
	public void reserve(NanShanActReserve NanShanActReserve) {
		this.daoSupport.insert("nanshan_act_reserve", NanShanActReserve);		
	}


	@Override
	public List<NCatVo> getCatList() {
		List<NCatVo> listTree= (List<NCatVo>) this.cache.get(NanShanCommonConstant.NANSHANCATCACHENAME);
	    if(listTree==null||listTree.size()<1){
	    	listTree= new NCatTreeUtil(this.getCats()).buildTree();
		    this.cache.put(NanShanCommonConstant.NANSHANCATCACHENAME,listTree);
	    }
		return listTree;
	}

}
