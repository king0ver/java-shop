package com.enation.app.cms.floor.model.vo;

import java.io.Serializable;

import org.jsoup.nodes.Element;

import com.enation.app.cms.floor.model.enumeration.BlockType;

import io.swagger.annotations.ApiModel;

/**
 * 
 * 单个图片 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月13日 下午9:33:47
 */
@ApiModel( description = "单个图片")
public class SingleImageBlock  extends Block implements Serializable{

	
	private static final long serialVersionUID = -9036107767750840491L;

	public SingleImageBlock(){
		super();
	}
	
	
	@Override
	protected BlockType definitionType() {
		return BlockType.SINGLE_IMAGE;
	}
	
	private SingleImage image;

	public SingleImage getImage() {
		return image;
	}


	public void setImage(SingleImage image) {
		this.image = image;
	}


	@Override
	public void convertToElemnet(Element blockEle) {
		if(image!=null){
			blockEle.select(".single_image>a").attr("href",buildLink(image.getOp_type(),image.getOp_value()));
			blockEle.select(".single_image img").attr("src",image.getImage_url());
		}
	}

	
}
