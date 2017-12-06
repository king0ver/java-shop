package com.enation.app.shop.goods.model.po;

import java.io.Serializable;

import com.enation.framework.database.PrimaryKeyField;

/**
 * 
 * 商品草稿箱参数po
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月30日 上午10:28:17
 */
public class DraftGoodsParams implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2954438244272607800L;
	private Integer id;
	private Integer draft_goods_id;
	private Integer param_id;
	private String param_name;
	private String param_value;

	public DraftGoodsParams() {

	}

	public DraftGoodsParams(GoodsParams draftGoodsParams) {
		this.param_id = draftGoodsParams.getParam_id();
		this.param_name = draftGoodsParams.getParam_name();
		this.param_value = draftGoodsParams.getParam_value();
	}

	public DraftGoodsParams(Integer id, Integer drft_goods_id, Integer param_id, String param_name,
			String param_value) {
		super();
		this.id = id;
		this.draft_goods_id = draft_goods_id;
		this.param_id = param_id;
		this.param_name = param_name;
		this.param_value = param_value;
	}

	@PrimaryKeyField
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDraft_goods_id() {
		return draft_goods_id;
	}

	public void setDraft_goods_id(Integer draft_goods_id) {
		this.draft_goods_id = draft_goods_id;
	}

	public Integer getParam_id() {
		return param_id;
	}

	public void setParam_id(Integer param_id) {
		this.param_id = param_id;
	}

	public String getParam_name() {
		return param_name;
	}

	public void setParam_name(String param_name) {
		this.param_name = param_name;
	}

	public String getParam_value() {
		return param_value;
	}

	public void setParam_value(String param_value) {
		this.param_value = param_value;
	}

}
