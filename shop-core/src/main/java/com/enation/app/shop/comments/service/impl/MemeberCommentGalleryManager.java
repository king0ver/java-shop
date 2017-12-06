package com.enation.app.shop.comments.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.shop.comments.model.po.MemberCommentGallery;
import com.enation.app.shop.comments.service.IMemeberCommentGalleryManager;
import com.enation.framework.database.IDaoSupport;

/**
 * 会员评论图片管理类
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年11月28日 下午7:33:00
 */
@Service("memeberCommentGalleryManager")
public class MemeberCommentGalleryManager implements IMemeberCommentGalleryManager {

	@Autowired
	private IDaoSupport daoSupport;
	
	@Override
	public void add(int comment_id, List<String> images) {
		MemberCommentGallery memberCommentGallery = new MemberCommentGallery(comment_id,images);
		this.daoSupport.insert("es_member_comment_gallery", memberCommentGallery);
	}

}
