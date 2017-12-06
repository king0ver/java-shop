package com.enation.app.shop.comments.service;

import java.util.List;

/**
 * 评论图片管理类
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年11月28日 下午7:31:32
 */
public interface IMemeberCommentGalleryManager {

	/**
	 * 添加商品评论的图片
	 * @param comment_id
	 * @param images
	 */
	public void add(int comment_id, List<String> images);
}
