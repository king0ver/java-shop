package com.enation.app.shop.goods.model.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * 规格实体vo
 * 
 * @author yanlin
 * @version v1.0 微服务迁移
 * @since v6.4.0
 * @date 2017年8月28日 下午2:52:36
 */
public class SpecificationVo implements Serializable {

	private static final long serialVersionUID = 8008287941472630697L;
	/**
	 * 主键
	 */
	private Integer spec_id;
	/**
	 * 规格名称
	 */
	private String spec_name;
	/**
	 * 0文字 1图片
	 */
	private Integer spec_type;
	/**
	 * 描述
	 */
	private String spec_memo;
	/**
	 * 选中
	 */
	private List<Integer> check_ids;
	/**
	 * 分类id
	 */
	private Integer category_id;
	/**
	 * 规格值
	 */
	private List<SpecValueVo> specValues;

	public List<SpecValueVo> getSpecValues() {
		return specValues;
	}

	public void setSpecValues(List<SpecValueVo> specValues) {
		this.specValues = specValues;
	}

	public Integer getSpec_id() {
		return spec_id;
	}

	public void setSpec_id(Integer specId) {
		spec_id = specId;
	}

	public String getSpec_name() {
		return spec_name;
	}

	public void setSpec_name(String specName) {
		spec_name = specName;
	}

	public Integer getSpec_type() {
		return spec_type;
	}

	public void setSpec_type(Integer specType) {
		spec_type = specType;
	}

	public String getSpec_memo() {
		return spec_memo;
	}

	public void setSpec_memo(String specMemo) {
		spec_memo = specMemo;
	}

	public List<Integer> getCheck_ids() {
		return check_ids;
	}

	public void setCheck_ids(List<Integer> check_ids) {
		this.check_ids = check_ids;
	}

	public Integer getCategory_id() {
		return category_id;
	}

	public void setCategory_id(Integer category_id) {
		this.category_id = category_id;
	}

}
