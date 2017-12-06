package com.enation.app.cms.floor.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.cms.floor.model.floor.FloorPo;
import com.enation.app.cms.floor.model.floor.FrontFloor;
import com.enation.app.cms.floor.model.floor.PanelPo;
import com.enation.app.cms.floor.model.vo.Block;
import com.enation.app.cms.floor.model.vo.Floor;
import com.enation.app.cms.floor.model.vo.FloorAnchor;
import com.enation.app.cms.floor.model.vo.Layout;
import com.enation.app.cms.floor.model.vo.MobilePanel;
import com.enation.app.cms.floor.model.vo.Panel;
import com.enation.app.cms.floor.service.IFloorContentManager;
import com.enation.app.cms.floor.service.IFloorManager;
import com.enation.app.cms.floor.service.builder.Builder;
import com.enation.framework.cache.ICache;
import com.enation.framework.context.spring.SpringContextHolder;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * 楼层维护的操作
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月14日 下午1:28:07
 */
@Service
public class FloorManager implements IFloorManager {

	@Autowired
	private IDaoSupport daoSupport;

	@Autowired
	private IFloorContentManager floorContentManager;
	
	@Autowired
	private ICache cache;
	
	public static final String APP_FlOOR_DATA = "app_floor_data";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.cms.floor.service.IFloorManager#addFloor(com.enation.app.cms.
	 * floor.model.floor.FloorPo)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void addFloor(FloorPo floorPo) {
		Integer tpl_id = floorPo.getTpl_id();
		this.daoSupport.insert("es_cms_floor", floorPo);
		int floor_id = this.daoSupport.getLastId("es_cms_floor");
		floorPo.setId(floor_id);

		PanelPo panel = new PanelPo();
		panel.setFloor_id(floor_id);
		panel.setSort(0);
		panel.setPanel_data("[]");
		panel.setPanel_tpl_id(tpl_id);
		floorContentManager.addPanel(panel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.cms.floor.service.IFloorManager#delete(java.lang.Integer)
	 */
	@Override
	public void delete(Integer id) {
		String sql = "delete from es_cms_floor where id=?";
		this.daoSupport.execute(sql, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.cms.floor.service.IFloorManager#updateFloor(com.enation.app.
	 * cms.floor.model.floor.FloorPo)
	 */
	@Override
	public void updateFloor(FloorPo floorPo) {
		this.daoSupport.update("es_cms_floor", floorPo, "id=" + floorPo.getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.cms.floor.service.IFloorManager#getCmsFloor(java.lang.
	 * Integer)
	 */
	@Override
	public FloorPo getCmsFloor(Integer id) {
		String sql = "select * from es_cms_floor where id=?";
		return this.daoSupport.queryForObject(sql, FloorPo.class, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.cms.floor.service.IFloorManager#getCmsFloorList(java.lang.
	 * Integer, java.lang.Integer, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public Page getCmsFloorList(Integer page_no, Integer page_size, String order_by, String order, String client_type) {
		if (StringUtil.isEmpty(page_no, true)) {
			page_no = 1;
		}
		if (StringUtil.isEmpty(page_size, true)) {
			page_size = 10;
		}
		StringBuffer sqlsb = new StringBuffer("select * from es_cms_floor where ");
		List<String> paramList = new ArrayList<String>();
		if (StringUtil.isEmpty(order_by)) {
			order_by = "id";
		}
		if (StringUtil.isEmpty(order)) {
			order = "asc";
		}
		sqlsb.append(" client_type = ? ");
		paramList.add(client_type);
		sqlsb.append(" order by ");
		sqlsb.append(order_by);
		sqlsb.append(" ");
		sqlsb.append(order);

		return this.daoSupport.queryForPage(sqlsb.toString(), page_no, page_size, paramList.toArray());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.cms.floor.service.IFloorManager#getAllCmsFrontFloor(java.lang
	 * .String)
	 */
	@Override
	public List<FrontFloor> getAllCmsFrontFloor(String client_type) {
		// 获取全部楼层信息逻辑
		StringBuffer sql = new StringBuffer("select * from es_cms_floor where is_display=1 ");
		List<String> paramList = new ArrayList<String>();
		sql.append(" and client_type = ? ");
		paramList.add(client_type);
		sql.append(" order by sort asc ");

		List<FrontFloor> frontFloorList = this.daoSupport.queryForList(sql.toString(), FrontFloor.class,
				paramList.toArray());
		this.getFrontFloorContent(frontFloorList);

		return frontFloorList;
	}

	private void getFrontFloorContent(List<FrontFloor> frontFloorList) {

		for (FrontFloor frontFloor : frontFloorList) {

			Integer floor_id = frontFloor.getId();
			Floor floor = this.getFloorDesignInfo(floor_id);
			List<Panel> panelDataList = floor.getPanelList();
			List panelList = new ArrayList();// panel_name,panel_html
			for (Panel panelData : panelDataList) {
				Map panel = new HashMap();
				String tplHtml = panelData.getTpl_content();
				String html = this.convertPanelHtml(tplHtml, panelData);
				panel.put("panel_html", html);
				panel.put("panel_name", panelData.getPanel_name());
				panelList.add(panel);
			}

			frontFloor.setPanelList(panelList);

		}
	}

	private String convertPanelHtml(String tplHtml, Panel panelData) {
		List<Layout> layoutList = panelData.getLayoutList();
		Document doc = Jsoup.parseBodyFragment(tplHtml);
		for (Layout layout : layoutList) {
			String layoutId = layout.getLayout_id();

			List<Block> blockList = layout.getBlock_list();
			for (Block block : blockList) {
				String blockId = block.getBlock_id();
				Elements blockEls = doc.select("[layout_id=" + layoutId + "] [block_id=" + blockId + "]");
				if (blockEls.size() > 0) {
					Element blockEl = blockEls.get(0);
					block.convertToElemnet(blockEl);
				}
			}

		}
		String html = doc.select("body").html();

		return html;

	}

	@Resource
	private Builder panelBuilder;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.cms.floor.service.IFloorManager#getFloorDesignInfo(java.lang.
	 * Integer)
	 */
	@Override
	public Floor getFloorDesignInfo(Integer floor_id) {

		String sql = "select * from es_cms_floor where id = ?";
		Floor floor = this.daoSupport.queryForObject(sql, Floor.class, floor_id);
		sql = "select p.*,tpl.tpl_type,tpl.tpl_content from es_cms_floor_panel p,es_cms_panel_tpl tpl  where p.panel_tpl_id =tpl.tpl_id and  p.floor_id = ? order by p.sort asc";
		List<Map> panelList = this.daoSupport.queryForList(sql, floor_id);
		for (Map map : panelList) {
			Panel panel = (Panel) panelBuilder.build(map);
			floor.addPanel(panel);
		}

		return floor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.cms.floor.service.IFloorManager#getMobileFloorId()
	 */
	@Override
	public Integer getMobileFloorId() {
		String sql = "select id from es_cms_floor where client_type='mobile'";
		Integer id = this.daoSupport.queryForInt(sql);
		if (id == 0) {
			String sqlinsert = "INSERT INTO es_cms_floor (name, sort,is_display,client_type) VALUES ('手机端楼层', 1,1,'mobile')";
			daoSupport.execute(sqlinsert);
		}
		return id;
	}

	@Override
	public List<FloorPo> getCmsFloorAll() {
		String sql = "select * from es_cms_floor";
		return this.daoSupport.queryForList(sql, FloorPo.class);
	}

	@Override
	public List<FloorAnchor> getFloorAnchor(String client_type) {
		String sql = "select id ,name from es_cms_floor where client_type=?";
		List<FloorAnchor> floorAnchorList = this.daoSupport.queryForList(sql.toString(), FloorAnchor.class,
				client_type);
		return floorAnchorList;
	}

	/**
	 * 组装app端数据格式
	 * @param floor
	 * @return
	 */
	private List<MobilePanel> getFloorMobileDesignInfo(Floor floor) {
		String sql = "select p.* from es_cms_floor_panel p  where p.floor_id = ? order by p.sort asc";
		List<Panel> panelList = this.daoSupport.queryForList(sql,Panel.class, floor.getId());
		List<MobilePanel> resList = new ArrayList<>();
		for (Panel panel : panelList) {
			MobilePanel mobilePanel = new MobilePanel();
			mobilePanel.setPanel_tpl_id(panel.getPanel_tpl_id());
			
			String  panel_data = panel.getPanel_data();
			
			Gson gson = new Gson();
		    Type type = new TypeToken<ArrayList<Map>>() {}.getType();  
			List<Map> layoutMapList = gson.fromJson(panel_data, type);
			
			for (Map layoutMap : layoutMapList) {
				List<Map> blockListData = (List<Map>) layoutMap.get("block_list");
				for(Map block : blockListData){
					if(block.get("block_type").equals("SINGLE_IMAGE")){
						Map imageMap = (Map) block.get("image");
						String op_type = (String) imageMap.get("op_type");
						String op_value = (String) imageMap.get("op_value");
						String link = buildLink(op_type, op_value);
						imageMap.put("link", link);
					}else if(block.get("block_type").equals("TEXT")){
						Map textMap = (Map) block.get("text");
						String op_type = (String) textMap.get("text_type");
						String op_value = (String) textMap.get("value");
						String link = buildLink(op_type, op_value);
						textMap.put("link", link);
					}
				}
				mobilePanel.setBlockList(blockListData);
			}
			resList.add(mobilePanel);
		}
		return resList;
	}
	
	/**
	 * 生成连接
	 * @param type
	 * @param str
	 * @return
	 */
	public String buildLink(String type,String key){
		
		if("none".equals(type)){
			return "";
		}
		
		if("link".equals(type)){
			if(key!=null){
				return key;
			}
			return "";
		}
		if("keyword".equals(type)){
			return "/goods-list.html?keyword="+key;
		}
		IDaoSupport daoSupport = SpringContextHolder.getBean("daoSupport");
		if("goods-sn".equals(type)){
			Integer goods_id = daoSupport.queryForInt("select goods_id from es_goods where sn = ?", key);
			return "/goods-"+goods_id+".html";
		}
		if("goods-cat".equals(type)){
			return "/goods-list.html?cat="+key;
		}
		return "";
	}

	@Override
	public List<MobilePanel> getAllCmsFrontMobilePanel(boolean is_refresh) {
		
		if(!is_refresh){
			Object list = cache.get(APP_FlOOR_DATA);
			if(list != null){
				return (List<MobilePanel>)list;
			}
		}
		
		StringBuffer sql = new StringBuffer("select * from es_cms_floor where is_display=1 and client_type = ? order by sort asc");
		
		List<Floor> frontFloorList = this.daoSupport.queryForList(sql.toString(), Floor.class, "mobile");
		
		if(frontFloorList!=null&&frontFloorList.size()>0){
			Floor floor = frontFloorList.get(0);
			
			List<MobilePanel> reslist = this.getFloorMobileDesignInfo(floor);
			
			cache.put(APP_FlOOR_DATA, reslist);
			
			return reslist;
		}
		
		return null;
	}

}
