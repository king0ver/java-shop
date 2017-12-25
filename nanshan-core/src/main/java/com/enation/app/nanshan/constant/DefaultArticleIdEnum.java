package com.enation.app.nanshan.constant;

public enum DefaultArticleIdEnum {
	GAME_RANK(2,"游戏排行"),
	MOVIETIME(3,"电影排行"),
	CINEMA4D(4,"四维影院")
	;
	private final int id;
	private final String desc;
	
	DefaultArticleIdEnum(int id, String desc) {
		this.id = id;
		this.desc = desc;
	}
	public int getId() {
		return id;
	}
	public String getDesc() {
		return desc;
	}

}
