package com.enation.app.cms.floor.model.enumeration;
/**
 * 
 * 发送消息的类型 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月14日 上午9:55:24
 */
public enum CmsManageMsgType {
	INDEX("首页", 1), NAV("导航", 2), CHANNEL("频道", 3), SPEC("专题", 4), ARTICLE("文章", 5);
	private String description;
	private int index;

	private CmsManageMsgType(String description, int index) {
		this.description = description;
		this.index = index;
	}

	public String getDescription() {
		return description;
	}

	public int getIndex() {
		return index;
	}
}
