package com.enation.app.shop.member.plugin;

import com.enation.app.shop.comments.model.po.MemberComment;

public interface IMemberCommentEvent {
	public void onMemberComment(MemberComment comment);
}
