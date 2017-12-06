package com.enation.app.shop.goods.model.vo;

import java.util.List;

public class CatNav {
	private List<BrandNav> brands;
	private String url;

	public List<BrandNav> getBrands() {
		return brands;
	}

	public void setBrands(List<BrandNav> brands) {
		this.brands = brands;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
