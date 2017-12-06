package com.enation.app.base.core.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.base.core.model.RegionVo;
import com.enation.app.base.core.model.Regions;
import com.enation.app.base.core.service.IRegionsManager;
import com.enation.app.base.core.service.dbsolution.DBSolutionFactory;
import com.enation.framework.annotation.Log;
import com.enation.framework.cache.ICache;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.IntegerMapper;
import com.enation.framework.database.data.IDataOperation;
import com.enation.framework.log.LogType;
import com.enation.framework.util.JsonUtil;

import net.sf.json.JSONArray;

/**
 * 地区管理
 * @author kingapex
 * 2010-7-18下午08:12:03
 * @version v2.0,2016年2月20日
 * @since v6.0
 */
@Service("regionsManager")
public class RegionsManager implements IRegionsManager {

	@Autowired
	private IDataOperation dataOperation;


	@Autowired
	private IDaoSupport  daoSupport;
	public final static String REGIONALL = "REGIONALL";
	public final static String REGIONLIDEPTH = "REGIONLIDEPTH";
	private List<RegionVo> returnList = new ArrayList<RegionVo>();

	@Autowired
	private ICache cache;
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.base.core.service.IRegionsManager#listCity(int)
	 */
	@Override
	public List listCity(int province_id) {
		List list = this.daoSupport.queryForList("select * from es_regions where region_grade = 2 and p_region_id = ?", Regions.class, province_id);
		list = list == null ? new ArrayList() : list;
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.base.core.service.IRegionsManager#listProvince()
	 */
	@Override
	public List listProvince() {
		List list = this.daoSupport.queryForList("select * from es_regions where region_grade = 1", Regions.class);
		list = list == null ? new ArrayList() : list;
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.base.core.service.IRegionsManager#listRegion(int)
	 */
	@Override
	public List listRegion(int city_id) {
		List list = this.daoSupport.queryForList("select * from es_regions where region_grade = 3 and p_region_id = ?", Regions.class, city_id);
		list = list == null ? new ArrayList() : list;
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.base.core.service.IRegionsManager#listChildrenAsyn(java.lang.Integer)
	 */
	@Override
	public List listChildrenAsyn(Integer regionid) {

		String sql = "select reg.*,tt.t_num as totle_num from es_regions reg left join "
				+ " (select r.p_region_id r_pid ,count(r.region_id) as t_num from es_regions r where r.p_region_id in "
				+ " (select re.region_id from es_regions re where re.p_region_id=? ) GROUP BY r.p_region_id) tt on reg.region_id = tt.r_pid "
				+ " where reg.p_region_id=? ";

		List<Map> regionlist = this.daoSupport.queryForList(sql, regionid,regionid);

		for(Map map :regionlist){
			if(map.get("totle_num")==null){		//判断某一个分类下的子分类数量 null赋值为0
				map.put("totle_num", 0);
			}
			int totle_num = Integer.parseInt(map.get("totle_num").toString());
			if(totle_num!=0){		//判断某一个分类下的子分类数量 不为0 则是(easyui)文件夹并且是闭合状态。
				map.put("state", "closed");
			}
		}
		return regionlist;

	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.base.core.service.IRegionsManager#listChildren(java.lang.Integer)
	 */
	@Override
	public List listChildren(Integer regionid) {
		StringBuffer sql  = new StringBuffer();
		sql.append("select * from  es_regions ");
		List<Regions> list = this.daoSupport.queryForList(sql.toString(),Regions.class);
		List<Regions> reglist = new ArrayList<Regions>();
		for(Regions map:list){
			if(map.getP_region_id().equals(regionid)){
				List<Regions> children =this.getChildren(list, map.getRegion_id());
				map.setChildren(children);
				int i = this.daoSupport.queryForInt("select count(0) from es_regions where p_region_id="+map.getRegion_id());
				if(i!=0){
					map.setState("closed");
				}
				reglist.add(map);
			}
		}		
		return reglist;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.base.core.service.IRegionsManager#listChildren(java.lang.String)
	 */
	@Override
	public List listChildren(String regionid) {

		if(regionid==null || regionid.equals("")) return new ArrayList();
		StringBuffer sql  = new StringBuffer();
		sql.append("select * from  ");
		sql.append(" es_regions ");
		sql.append(" c");
		sql.append("  where c. p_region_id in("+regionid+")  order by region_id");
		List list = this.daoSupport.queryForList(sql.toString(),new IntegerMapper());
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.base.core.service.IRegionsManager#listChildrenByid(java.lang.Integer)
	 */
	@Override
	public List listChildrenByid(Integer regionid) {
		StringBuffer sql  = new StringBuffer();
		sql.append("select c.region_id,c.local_name,c.region_grade,c.p_region_id,count(s.region_id) as childnum,c.zipcode,c.cod  from  ");

		sql.append(" es_regions ");
		sql.append(" c");

		sql.append(" left join ");
		sql.append(" es_regions ");
		sql.append(" s");

		sql.append(" on s.p_region_id = c.region_id  where c.p_region_id=? group by c.region_id,c.local_name,c.region_grade,c.p_region_id,c.zipcode,c.cod order by region_id");
		List list = this.daoSupport.queryForList(sql.toString(),regionid);
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.base.core.service.IRegionsManager#getChildrenJson(java.lang.Integer)
	 */
	@Override
	public String getChildrenJson(Integer regionid) {
		List list  = this.listChildren(regionid);
		JSONArray jsonArray = JSONArray.fromObject( list );  
		return jsonArray.toString();
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.base.core.service.IRegionsManager#add(com.enation.app.base.core.model.Regions)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Log(type=LogType.SETTING,detail="新增加了名为${regions.local_name}地区")
	public void add(Regions regions) {
		this.daoSupport.insert("es_regions", regions);
		String region_path = "";
		int region_id = this.daoSupport.getLastId("es_regions");
		if(regions.getP_region_id()!=null&&regions.getP_region_id() != 0){
			Regions p = get(regions.getP_region_id());
			region_path = p.getRegion_path() + region_id + ",";
		}else{
			region_path = "," + region_id + ",";
		}
		regions = get(region_id);
		regions.setRegion_path(region_path);
		update(regions);
		for (int i = 1; i <= 4; i++) {
			List<Regions> rgs = this.daoSupport.queryForList("select * from es_regions where region_grade = ?", i);
			this.cache.put(this.REGIONLIDEPTH+i, rgs);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.base.core.service.IRegionsManager#delete(int)
	 */
	@Log(type=LogType.SETTING,detail="删除ID为${regionId}地区信息")
	public void delete(int regionId) {
		this.daoSupport.execute("delete from es_regions where region_path like '%,"+regionId+",%'");
		for (int i = 1; i <= 4; i++) {
			List<Regions> regions = this.daoSupport.queryForList("select * from es_regions where region_grade = ?", i);
			this.cache.put(this.REGIONLIDEPTH+i, regions);
		}

	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.base.core.service.IRegionsManager#get(int)
	 */
	@Override
	public Regions get(int regionId) {
		return this.daoSupport.queryForObject("select * from es_regions where region_id = ?", Regions.class, regionId);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.base.core.service.IRegionsManager#getByName(java.lang.String)
	 */
	@Override
	public Regions getByName(String name) {
		try{
			List<Regions> list = this.daoSupport.queryForList("select * from es_regions where local_name = ?", Regions.class, name  );
			if(list== null || list.isEmpty()) {
				return null;
			}
			return list.get(0);
		}catch(RuntimeException e){
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.base.core.service.IRegionsManager#update(com.enation.app.base.core.model.Regions)
	 */
	@Override
	@Log(type=LogType.SETTING,detail="修改了名为${regions.local_name}地区信息")
	public void update(Regions regions) {
		this.daoSupport.update("es_regions", regions, "region_id="+regions.getRegion_id());
		this.daoSupport.execute("update es_regions set cod = ? where region_path like (?)",regions.getCod(),"%"+regions.getRegion_id()+"%");
		for (int i = 1; i <= 4; i++) {
			List<Regions> rgs = this.daoSupport.queryForList("select * from es_regions where region_grade = ?", i);
			this.cache.put(this.REGIONLIDEPTH+i, rgs);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.base.core.service.IRegionsManager#reset()
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	@Log(type=LogType.SETTING,detail="初始化/恢复地区数据")
	public void reset() {
		Connection conn = DBSolutionFactory.getConnection(null);
		try {
			Statement state = conn.createStatement();
			state.execute("truncate table es_regions");	//	先清空表中数据
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String xmlFile = "file:com/enation/app/base/city.xml";
		dataOperation.imported(xmlFile);
		/** 将组织好的树结构放到缓存中 key为id+深度*/
		this.cache.put(REGIONALL+3, this.getAll(3));
		this.cache.put(REGIONALL+4, this.getAll(4));
		/** 将四级分别地区放入缓存 */
		for (int i = 1; i <= 4; i++) {
			List<Regions> regions = this.daoSupport.queryForList("select * from es_regions where region_grade = ?",Regions.class, i);
			this.cache.put(this.REGIONLIDEPTH+i, regions);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.base.core.service.IRegionsManager#get(java.lang.String)
	 */
	@Override
	public Regions[] get(String area) {
		//这里什么也不做
		return null;
	}

	private List<Regions> getChildren(List<Regions> list,Integer id){
		List<Regions> children = new ArrayList<Regions>();
		for (Regions map : list) {
			if(map.getP_region_id().equals(id)){				
				map.setChildren(this.getChildren(list, map.getRegion_id()));
				children.add(map);
			}
		}
		return children;
	}

	@Override
	public List<RegionVo> getAll(Integer depth) {
		String sql = "select * from  es_regions";
		List<Regions> data = this.daoSupport.queryForList(sql.toString(),Regions.class);
		List<RegionVo> tree = new ArrayList<>();
		this.sort(1,depth, tree, data);
		return tree;
	}

	@Override
	public String getRegions(Integer depth) {
		Object obj = this.cache.get(REGIONALL+depth);
		List<RegionVo> regions = null;
		/** 如果从缓存中拿到数据则直接取值，否则需要放到缓存中一份在返回取值 */
		if(obj != null) {
			regions = (List<RegionVo>)obj;
		}else {
			List<RegionVo> rgs = this.getAll(depth);
			this.cache.put(REGIONALL+depth, rgs);
			regions=rgs;
		}
		return JsonUtil.ListToJson(regions);
	}

	/**
	 * 负责递归的停止
	 * @param level	标示
	 * @param depth	深度
	 * @param tree	新的树结构
	 * @param data	原始数据
	 */
	private void sort(int level,int depth, List<RegionVo> tree, List<Regions> data) {
		if(level+1>depth) {
			/** 如果是第一级的情况直接返回 */
			if(depth == 1) {
				for (Regions regions : data) {
					if (regions.getP_region_id() == 0) {
						tree.add(regions.toVo());
					}
				}
			}
			return;
		}
		// 如果为0 则代表初始化 初始化顶级数据
		if (level== 1) {
			for (Regions regions : data) {
				if (regions.getP_region_id() == 0) {
					tree.add(regions.toVo());
				}
			}
		} 
		this.recursion(level, tree, data);
		level++;
		this.sort(level,depth,tree, data);
	} 
	/**
	 * 负责树结构的创建
	 * @param level	标示
	 * @param tree	树结构
	 * @param data	原始数据
	 */
	private void recursion(int level,List<RegionVo> tree,List<Regions> data) {
		for (RegionVo vo : tree) {
			if(vo.getLevel()!=level) {
				if(vo.getChildren().size()!=0) {
					this.recursion(level, vo.getChildren(), data);
				}
				continue;
			}
			for (Regions regions : data) {
				if(regions.getP_region_id().equals(vo.getRegion_id())){
					vo.getChildren().add(regions.toVo());
				}
			}
		}	
	}

	@Override
	public String getRegionsChildren(Integer region_id) {
		Integer depth = this.daoSupport.queryForInt("select region_grade from es_regions where region_id = ?", region_id);
		/** 因为需要查找下一级，所以需要将深度+1 */
		depth = depth + 1;
		Object obj =  this.cache.get(this.REGIONLIDEPTH+depth);
		List<Regions> regions = null;
		/** 如果为空的话需要重数据库中查出数据 然后放入缓存 */
		if(obj == null) {
			for (int i = 1; i <= 4; i++) {
				List<Regions> rgs = this.daoSupport.queryForList("select * from es_regions where region_grade = ?",Regions.class, i);
				this.cache.put(this.REGIONLIDEPTH+i, rgs);
				if(i == depth) {
					regions = rgs;
				}
			}
		}else {
			regions = (List<Regions>)obj;
		}
		List<Regions> rgs =new ArrayList<>();
		for (int i = 0; i < regions.size(); i++) {
			if(regions.get(i).getP_region_id().equals(region_id)) {
				rgs.add(regions.get(i));
			}
		}
		return JsonUtil.ObjectToJson(rgs);
	}
}
