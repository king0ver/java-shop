package com.enation.app.cms.floor.model.enumeration;

/**
 * 
 * 楼层区块类型
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月15日 下午9:34:17
 */
public enum BlockType {
	MANUAL_GOODS("手动推荐商品"),
	AUTO_GOODS("自动规则商品"),
	STATIC_ADV("静态广告"),
	SLIDER_ADV("轮播广告"),
	SINGLE_IMAGE("单个图片"),
	MULTI_IMAGE("多个图片"),
	BRAND("品牌"),
	TEXT("文本");
	
	private String description;

	BlockType(String _description){
		  this.description=_description;
		  
	}
	
	public String description(){
		return this.description;
	}
	
	public String value(){
		return this.name();
	}
}
