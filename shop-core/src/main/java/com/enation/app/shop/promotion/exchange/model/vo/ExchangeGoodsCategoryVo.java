package com.enation.app.shop.promotion.exchange.model.vo;

import java.util.List;

import com.enation.app.shop.promotion.exchange.model.po.ExchangeGoodsCategory;
import com.enation.framework.database.NotDbField;

/**
 * 
 * 积分商品分类实体
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月24日 下午4:56:25
 */
public class ExchangeGoodsCategoryVo extends ExchangeGoodsCategory {
	private static final long serialVersionUID = -7878650879284562326L;
	/**
	 * 子分类数量
	 */
	private Integer total_num;

	/**
	 * 打开或关闭状态
	 */
	private String state;
	private Integer id;
	private String text;
	/**
	 * 子分类
	 */
	private List<ExchangeGoodsCategoryVo> children;
	/**
	 * 链接地址
	 */
	private String url;
	private boolean hasChildren;

	public ExchangeGoodsCategoryVo() {
		hasChildren = false;
	}

	public ExchangeGoodsCategoryVo(ExchangeGoodsCategory cat) {
		this.setCategory_id(cat.getCategory_id());
		this.setCategory_path(cat.getCategory_path());
		this.setName(cat.getName());
		this.setParent_id(cat.getParent_id());
	}

	public List<ExchangeGoodsCategoryVo> getChildren() {
		return children;
	}

	public void setChildren(List<ExchangeGoodsCategoryVo> children) {
		this.children = children;
	}

	public Integer getId() {
		id = this.getCategory_id();
		return id;
	}

	public String getText() {
		text = this.getName();
		return text;
	}

	public Integer getTotal_num() {
		return total_num;
	}

	public void setTotal_num(Integer total_num) {
		this.total_num = total_num;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@NotDbField
	public boolean getHasChildren() {
		hasChildren = this.children == null || this.children.isEmpty() ? false : true;
		return hasChildren;
	}

	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

}
