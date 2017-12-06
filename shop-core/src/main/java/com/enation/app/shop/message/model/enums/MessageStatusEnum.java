package com.enation.app.shop.message.model.enums;

/**
 * 消息设置枚举
 * @author Kanon
 * @since 6.4.0
 * @version 1.0
 * 2017-8-3
 */
public enum MessageStatusEnum {
	
	OPEN("开启",1),CLOSE("关闭",0);
	private String name;
    private int index;
    
    // 构造方法
    private MessageStatusEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
}
