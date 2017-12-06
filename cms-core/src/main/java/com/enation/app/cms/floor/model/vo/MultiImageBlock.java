package com.enation.app.cms.floor.model.vo;

import java.io.Serializable;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.enation.app.cms.floor.model.enumeration.BlockType;

import io.swagger.annotations.ApiModel;

/**
 * 
 * 多图片 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月13日 下午9:31:34
 */
@ApiModel( description = "多图片")
public class MultiImageBlock extends Block  implements Serializable  {

	private static final long serialVersionUID = -3963541631746143772L;

	private List<SingleImage> image;
	
	@Override
	protected BlockType definitionType() {
		return BlockType.MULTI_IMAGE;
	}

	@Override
	public void convertToElemnet(Element blockEle) {
		if(image!=null&&image.size()>0){
			int i = 0;
			for(SingleImage singleImage : image){
				Elements li = blockEle.select("li");
				if(i == 0){
					li.select("a").attr("href",buildLink(singleImage.getOp_type(),singleImage.getOp_value()));
					li.select("a img").attr("src",singleImage.getImage_url());
				}else{
					Elements cloneLi = li.clone();
					cloneLi.select("a").attr("href",buildLink(singleImage.getOp_type(),singleImage.getOp_value()));
					cloneLi.select("a img").attr("src",singleImage.getImage_url());
					blockEle.select("ul").append(cloneLi.toString());
				}
				i++;
			}
		}
		
		
	}

	public List<SingleImage> getImage() {
		return image;
	}

	public void setImage(List<SingleImage> image) {
		this.image = image;
	}

}
