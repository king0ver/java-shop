package com.enation.app.shop.comments.controller;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.enation.app.shop.comments.model.po.MemberComment;
import com.enation.app.shop.comments.model.vo.CommentVo;
import com.enation.app.shop.comments.service.IMemberCommentManager;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 商品评论咨询操作api
 * 
 * @author dongxin
 * @version v1.0
 * @since v6.4.0 2017年11月28日 下午3:09:02
 */
@Api(description = "商品评论咨询操作api")
@RestController
@RequestMapping("/comment-operation")
@Validated
public class CommentOperationController {

	@Autowired
	private IMemberCommentManager memberCommentManager;

	/**
	 * 提交评论接口
	 * 
	 * @param commentVo
	 * @return
	 */
	@ApiOperation(value = "提交评论接口")
	@PostMapping(value = "/mine/comment")
	public CommentVo addComments(@RequestBody CommentVo commentVo) {
		return memberCommentManager.add(commentVo);
	}

	/**
	 * 提交咨询接口
	 * 
	 * @param commentVo
	 * @return
	 */
	@ApiOperation(value = "提交咨询接口")
	@PostMapping(value = "/mine/comment/consult")
	public void addComments(Integer goods_id, String content) {
		 memberCommentManager.addConsult(goods_id, content);
	}
	
	@ApiOperation(value = "回复咨询评论", notes = "商家回复咨询")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "comment_id", value = "评论id", required = true, dataType = "Integer", paramType = "path"),
			@ApiImplicitParam(name = "status", value = "评论删除状态", required = true, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "reply", value = "商家回复内容", required = true, dataType = "String", paramType = "query") })
	@ResponseBody
	@PostMapping(value = "/mine/comment/{comment_id}")
	public MemberComment replyCommen(@NotNull(message = "必须指定评论ID") @PathVariable(name = "comment_id") Integer comment_id,
			Integer status, String reply) {
		MemberComment memberComment = memberCommentManager.get(comment_id);
		memberComment.setStatus(status);
		memberComment.setReply(reply);
		if (memberComment == null) {
			throw new RuntimeException("没有该评论！！！");
		}
		memberCommentManager.update(memberComment);
		return memberComment;
	}

}
