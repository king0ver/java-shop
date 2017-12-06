package com.enation.app.cms.floor.model.vo;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.nodes.Element;

import com.enation.app.cms.floor.model.enumeration.BlockType;
import com.enation.framework.context.spring.SpringContextHolder;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.IDaoSupport;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * 楼层区块
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0 
 * @date 2017年8月13日 下午9:27:10
 */
@ApiModel(description = "楼层区块")
public abstract class Block implements com.enation.app.cms.floor.service.Element {
	/** 区块标识 */
	@ApiModelProperty(value = "区块标识")
	private String block_id;
	/** 区块类 */
	@ApiModelProperty(value = "区块类型")
	private BlockType block_type;

	@Override
	public void buildSelf(Map map) {
		this.block_id = (String) map.get("block_id");
		String block_type_str = (String) map.get("block_type");
		this.block_type = BlockType.valueOf(block_type_str);
	}

	public Block() {
		this.block_type = this.definitionType();
	}

	/**
	 * 定义区块类型
	 * 
	 * @return
	 */
	protected abstract BlockType definitionType();

	/**
	 * 提供根据数据 转换好 jsoup的element
	 * 
	 * @param blockEle
	 */
	public abstract void convertToElemnet(Element blockEle);
	
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
			return getContextPath()+"/goods-list.html?keyword="+key;
		}
		IDaoSupport daoSupport = SpringContextHolder.getBean("daoSupport");
		if("goods-sn".equals(type)){
			Integer goods_id = daoSupport.queryForInt("select goods_id from es_goods where sn = ?", key);
			return getContextPath()+"/goods-"+goods_id+".html";
		}
		if("goods-cat".equals(type)){
			return getContextPath()+"/goods-list.html?cat="+key;
		}
		return "";
	}
	
	/**
	 * 得到服务的ContextPath
	 * @return
	 */
	public String getContextPath(){
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String serverName = request.getContextPath();
		return serverName;
	}

	public String getBlock_id() {
		return block_id;
	}

	public void setBlock_id(String block_id) {
		this.block_id = block_id;
	}

	public BlockType getBlock_type() {
		return block_type;
	}

}
