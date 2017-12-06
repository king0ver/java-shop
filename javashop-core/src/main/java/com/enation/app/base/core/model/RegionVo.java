package com.enation.app.base.core.model;

import java.io.Serializable;
import java.util.List;

public class RegionVo implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 3444861223072695184L;

	public String local_name ;

	public int region_id;

	private int p_regions_id;

	private int level;

	private List<RegionVo> children;

	public String getLocal_name() {
		return local_name;
	}

	public void setLocal_name(String local_name) {
		this.local_name = local_name;
	}

	

	public int getP_regions_id() {
		return p_regions_id;
	}

	public void setP_regions_id(int p_regions_id) {
		this.p_regions_id = p_regions_id;
	}

	public List<RegionVo> getChildren() {
		return children;
	}

	public void setRegionVos(List<RegionVo> children) {
		this.children = children;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getRegion_id() {
		return region_id;
	}

	public void setRegion_id(int region_id) {
		this.region_id = region_id;
	}


}
