package com.enation.app.shop.goods.model.vo;

import java.util.List;

import com.enation.app.shop.goods.model.po.Category;
import com.enation.framework.database.NotDbField;


/**
 * 商品分类vo
 * @author fk
 * @version v1.0
 * 2017年4月5日 下午4:33:52
 */
public class CategoryVo extends Category{

	
	private static final long serialVersionUID = 3843585201476087204L;

	/**
	 * 子分类数量
	 */
	private Integer total_num;
	
	/**
	 * 打开或关闭状态
	 */
	private String state;
	/**
	 * 子分类
	 */
	private List<CategoryVo> children;
	/**
	 * 链接地址
	 */
	private String url;
	private boolean hasChildren ;
	 
	public CategoryVo(){
		hasChildren =false;
	}
	
	public CategoryVo(Category cat) {
		this.setCategory_id(cat.getCategory_id());
		this.setCategory_path(cat.getCategory_path());
		this.setName(cat.getName());
		this.setParent_id(cat.getParent_id());
		this.setBrands(cat.getBrands());
		this.setImage(cat.getImage());
	}

	public List<CategoryVo> getChildren() {
		return children;
	}

	public void setChildren(List<CategoryVo> children) {
		this.children = children;
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
		hasChildren = this.children==null|| this.children.isEmpty() ?false:true;
		return hasChildren;
	}

	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}
	
}
