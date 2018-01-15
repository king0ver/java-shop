package com.enation.app.nanshan.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yulong on 17/12/18.
 */
public class NCatVo implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;

    private String name;

    private String pcUrl;

    private String wapUrl;

    private List<NCatVo> leafs;

    private NCatVo parent;
    
    private long parentId;
    
    private String pc_icon;
    
	private String wap_icon;


    public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPcUrl() {
        return pcUrl;
    }

    public void setPcUrl(String pcUrl) {
        this.pcUrl = pcUrl;
    }

    public String getWapUrl() {
        return wapUrl;
    }

    public void setWapUrl(String wapUrl) {
        this.wapUrl = wapUrl;
    }

    public List<NCatVo> getLeafs() {
        return leafs;
    }

    public void setLeafs(List<NCatVo> leafs) {
        this.leafs = leafs;
    }

    public NCatVo getParent() {
        return parent;
    }

    public void setParent(NCatVo parent) {
        this.parent = parent;
    }

	public String getPc_icon() {
		return pc_icon;
	}

	public void setPc_icon(String pc_icon) {
		this.pc_icon = pc_icon;
	}

	public String getWap_icon() {
		return wap_icon;
	}

	public void setWap_icon(String wap_icon) {
		this.wap_icon = wap_icon;
	}
    
}
