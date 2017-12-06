package com.enation.app.cms.floor.service.builder;

import com.enation.app.cms.floor.model.vo.SingleImage;
import com.enation.app.cms.floor.model.vo.SingleImageBlock;
import com.enation.app.cms.floor.service.Element;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * 单图片builder
 * @author kingapex
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月18日 下午9:37:25
 */
@Service
public class SignleImageBuilder implements Builder {

	@Override
	public Element build(Map map) {

		SingleImageBlock signleImageBlock = new SingleImageBlock();
		signleImageBlock.buildSelf(map);
		Map imageMap = (Map) map.get("image");
		String image_url = (String)imageMap.get("image_url");
		image_url=image_url==null?"https://www.baidu.com/img/bd_logo1.png":image_url;
		String op_type = (String)imageMap.get("op_type");
		op_type=op_type==null?"keyword":op_type;
		String op_value = (String)imageMap.get("op_value");
		op_value=op_value==null?"op_value":op_value;
		SingleImage singleImage = new SingleImage(image_url, op_type, op_value);
		signleImageBlock.setImage(singleImage);

		return signleImageBlock;
	}

	
	
}
