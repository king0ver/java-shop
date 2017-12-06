package com.enation.app.shop.goods.model.vo;

import java.text.DecimalFormat;
import java.util.List;

import com.enation.app.shop.goods.model.po.DraftGoodsSku;
import com.enation.app.shop.goods.model.po.GoodsSku;
import com.enation.framework.database.NotDbField;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 商品sku的vo
 * 
 * @author fk
 * @version v1.0 2017年4月5日 上午11:20:43
 */
public class GoodsSkuVo extends GoodsSku {

	private static final long serialVersionUID = -2058821615018744293L;

	private List<SpecValueVo> specList;

	private Integer goods_transfee_charge;// 谁承担运费

	private String goodsPrice;
	private Integer market_enable; // 上下架 上架：1 下架：0 预览：2
	private Integer disabled; // 删除： 回收站：1 正常 ：0

	public GoodsSkuVo() {
	}

	public GoodsSkuVo(GoodsSku sku) {
		this.setCategory_id(sku.getCategory_id());
		this.setCost(sku.getCost());
		this.setEnable_quantity(sku.getEnable_quantity());
		this.setGoods_id(sku.getGoods_id());
		this.setGoods_name(sku.getGoods_name());
		this.setPrice(sku.getPrice());
		this.setQuantity(sku.getQuantity());
		this.setSeller_id(sku.getSeller_id());
		this.setSeller_name(sku.getSeller_name());
		this.setSku_id(sku.getSku_id());
		this.setSn(sku.getSn());
		this.setWeight(sku.getWeight());
		this.setThumbnail(sku.getThumbnail());
		Gson gson = new Gson();
		List<SpecValueVo> speclist = gson.fromJson(sku.getSpecs(), new TypeToken<List<SpecValueVo>>() {
		}.getType());
		this.setSpecList(speclist);

	}

	public GoodsSkuVo(DraftGoodsSku draftSku) {
		this.setCost(draftSku.getCost());
		this.setGoods_id(draftSku.getDraft_goods_id());
		this.setGoods_name(draftSku.getGoods_name());
		this.setPrice(draftSku.getPrice());
		this.setQuantity(draftSku.getQuantity());
		this.setSku_id(draftSku.getSku_id());
		this.setSn(draftSku.getSn());
		this.setWeight(draftSku.getWeight());
		Gson gson = new Gson();
		List<SpecValueVo> speclist = gson.fromJson(draftSku.getSpecs_ids(), new TypeToken<List<SpecValueVo>>() {
		}.getType());
		this.setSpecList(speclist);
	}

	@NotDbField
	public List<SpecValueVo> getSpecList() {
		return specList;
	}

	@NotDbField
	public void setSpecList(List<SpecValueVo> specList) {
		this.specList = specList;
	}

	@NotDbField
	public String getGoodsPrice() {
		DecimalFormat df1 = new DecimalFormat("￥#0.00");
		if (this.getPrice() != null) {
			goodsPrice = df1.format(this.getPrice());
		}
		return goodsPrice;
	}

	public Integer getGoods_transfee_charge() {
		return goods_transfee_charge;
	}

	public void setGoods_transfee_charge(Integer goods_transfee_charge) {
		this.goods_transfee_charge = goods_transfee_charge;
	}

	public Integer getMarket_enable() {
		return market_enable;
	}

	public void setMarket_enable(Integer market_enable) {
		this.market_enable = market_enable;
	}

	public Integer getDisabled() {
		return disabled;
	}

	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}

}
