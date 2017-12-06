package com.enation.app.cms.floor.model.vo;

import java.io.Serializable;
import java.util.List;

import org.jsoup.nodes.Element;

import com.enation.app.cms.floor.model.enumeration.BlockType;

/**
 * 
 * 品牌 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月13日 下午4:20:04
 */
public class BrandBlock extends Block implements Serializable {

	private static final long serialVersionUID = 79050238391653421L;

	private List<Brand> brand;

	@Override
	protected BlockType definitionType() {
		return BlockType.BRAND;
	}

	@Override
	public void convertToElemnet(Element blockEle) {
		if (brand != null) {
			for (Brand b : brand) {
				Element li = blockEle.select("li").first().clone();
				li.select("img").attr("src", b.getBrand_image());
				li.select("a").attr("href", getContextPath()+"/goods_list.html?brand="+b.getBrand_id());
				li.removeClass("brand-hidden");
				li.addClass("brand-show");
				blockEle.append(li.toString());
			}
		}
	}

	public List<Brand> getBrand() {
		return brand;
	}

	public void setBrand(List<Brand> brand) {
		this.brand = brand;
	}

}
