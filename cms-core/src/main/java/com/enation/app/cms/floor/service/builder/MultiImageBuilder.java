package com.enation.app.cms.floor.service.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.enation.app.cms.floor.model.vo.MultiImageBlock;
import com.enation.app.cms.floor.model.vo.SingleImage;
import com.enation.app.cms.floor.service.Element;

/**
 * 
 * 多图片 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月13日 下午9:37:25
 */
@Service
public class MultiImageBuilder implements Builder {

	@Override
	public Element build(Map map) {
		MultiImageBlock multiImageBlock = new MultiImageBlock();
		multiImageBlock.buildSelf(map);
		
		List<Map> imageList = (List) map.get("image");
		if(imageList != null&&imageList.size()>0){
			List<SingleImage> image_list = new ArrayList();
			for(Map imageMap : imageList){
				String image_url = imageMap.get("image_url").toString();
				String op_type = imageMap.get("op_type").toString();
				String op_value = imageMap.get("op_value").toString();
				SingleImage singleImage = new SingleImage(image_url, op_type, op_value);
				image_list.add(singleImage);
			}
			multiImageBlock.setImage(image_list);
		}
		return multiImageBlock;
	}

	
	
}
