package com.enation.app.cms.floor.service.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.enation.app.cms.floor.model.vo.Brand;
import com.enation.app.cms.floor.model.vo.BrandBlock;
import com.enation.app.cms.floor.service.Element;

/**
 * 
 * 品牌 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月12日 下午1:11:40
 */

@Service
public class BrandBuilder implements Builder {

	@Override
	public Element build(Map map) {
		BrandBlock brandBlock = new BrandBlock();
		brandBlock.buildSelf(map);
		
		List<Map> brandList = (List) map.get("brandList");
		if(brandList != null&&brandList.size()>0){
			List<Brand> brand_list = new ArrayList();
			for(Map brandMap : brandList){
				double brand_id = Double.parseDouble(brandMap.get("brand_id").toString());
				String brand_image = brandMap.get("brand_image").toString();
				Brand brand = new Brand((int)brand_id, brand_image);
				brand_list.add(brand);
				brandBlock.setBrand(brand_list);
			}
		}
		return brandBlock;
	}

}
