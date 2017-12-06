package com.enation.app.shop.comments.service;

import java.util.List;
import java.util.Map;

import com.enation.app.shop.comments.model.po.MemberComment;
import com.enation.app.shop.comments.model.vo.CommentCount;
import com.enation.app.shop.comments.model.vo.CommentVo;
import com.enation.app.shop.trade.model.vo.Order;
import com.enation.framework.database.Page;

public interface IMemberCommentManager {

	
	/**
	 * 取一个会员的评论列表
	 * @param page
	 * @param pageSize
	 * @param type
	 * @param member_id
	 * @return
	 */
	public Page getGoodsComments(int goods_id, int page, int pageSize, int type, Integer grade);
	
	/**
	 * 获取带图片的评论
	 * @param goods_id
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public Page getImageGoodsComments(int goods_id, int page, int pageSize);
	
	/**
	 * 获取一个商品的晒图数量
	 * @param goods_id
	 * @return
	 */
	public int getImageCommentsCount(int goods_id);
	
	/**
	 * 取某个商品最热门的几个评论
	 * @param goods_id
	 * @param number
	 * @return
	 */
	public List<Map> hot(int goods_id, int number);
	
	/**
	 * 添加评论晒图
	 * @param comment_id
	 * @param imageList
	 */
	public void addGallery(Integer comment_id, List<String> imageList);
	

	/**
	 * 获取一个商品的总得分
	 * @param goods_id
	 * @return
	 */
	public int getGoodsGrade(int goods_id);
	
	/**
	 * 在后台显示所有的评论列表
	 * @param page
	 * @param pageSize
	 * @param type
	 * @return
	 */
	public Page getAllComments(int page, int pageSize, int type);
	
	/**
	 * 查看所有带状态的评论或问答
	 * @param page
	 * @param pageSize
	 * @param type
	 * @param status
	 * @return
	 */
	public Page getCommentsByStatus(int page, int pageSize, int type,int status);
	
	
	
	
	/**
	 * 删除评论
	 * @param comment_id
	 */
	public void delete(Integer[] comment_id);
	
	/**
	 * 删除评论、咨询
	 * @param comment_id 评论、咨询 Id
	 */
	public void deletealone(int comment_id);
	
	
	/**
	 * 取一个会员的评论数
	 * @param member_id
	 * @param type
	 * @return
	 */
	public int getMemberCommentTotal(int member_id, int type);
	/**
	 * 评论分数列表
	 * @param goodsid 商品Id
	 * @return Map
	 */
	public Map statistics(int goodsid);

	
	
	/**
	 * 根据商品id获取评论总数
	 * @param goods_id
	 * @return
	 */
	public int getCommentsCount(int goods_id);

	public int getCommentsCount(int goods_id, int grade);
	
	
	/**
	 * 根据评分、商品ID或者各个评论的数量
	 * @param count  数量
	 * @param goods_id  商品ID
	 * @param grade    评级  3表示好评，2表示中品，1表示差评
	 * @return count
	 * whj 2015-10-15
	 */
	public int getCommentsGradeCount(int goods_id, int grade);
	
	/**
	 * 获取某个评级下的一个商品的评论列表
	 * @param goods_id   商品ID
	 * @param page       
	 * @param pageSize
	 * @param type      状态
	 * @param grade     评级  3表示好评，2表示中品，1表示差评
	 * @param type     
	 * @return <list> 评论列表
	 * whj 2015-10-15
	 */
	public Page getGoodsCommentsGradeList(int goods_id, int page, int pageSize, int type, int grade);

	/**
	 * 获取评论的图片集合
	 * add by DMRain 2016-5-3
	 * @param comment_id 评论ID
	 * @return
	 */
	public List getCommentGallery(Integer comment_id);
	/**
	 * 查询买家待评论订单<br>
	 * @author kingapex
	 * 2015-11-15
	 * @see Order#getItemList()
	 * @param pageNo 当前页码
	 * @param pageSize 页大小
	 * @return 订单分页列表
	 */
	public Page pageCommentOrders(int pageNo, int pageSize);
	
	/**
	 * 获取某一个商品的审核通过的总的评论数
	 * @return
	 */
	public int getGoodsCommentsCount(int goods_id);
	
	
	
	
	
	/**
	 * 根据ID获取评论对象
	 * @param comment_id
	 * @return
	 */
	public MemberComment get(int comment_id);
	
	/**
	 * 更新一个评论对象
	 * @param memberComment
	 */
	public void update(MemberComment memberComment);
	
	
	/**
	 * 获取一个商品的评论列表
	 * @param goods_id
	 * @param page
	 * @param pageSize
	 * @param grade 评论类型
	 * @return
	 */
	public Page getGoodsComments(Integer goods_id, Integer page, Integer pageSize, Integer grade);
	
	/**
	 * 取一个会员的评论列表
	 * @param page
	 * @param pageSize
	 * @param type
	 * @param member_id
	 * @return
	 */
	public Page getMemberComments(int page, int pageSize, int type, int member_id);
	
	/**
	 * 添加评论
	 * @param commentVo
	 * @return
	 */
	public CommentVo add(CommentVo commentVo);
	
	/**
	 * 添加咨询
	 * @param goods_id 商品id
	 * @param content 咨询内容
	 * @return
	 */
	public void addConsult(Integer goods_id, String content);
	
	/**
	 * 获取好中差评的数量
	 * @return
	 */
	public CommentCount getCommentCount(Integer goods_id);
	
	/**
	 * 获取待评论的订单数量
	 * @return
	 */
	public int getWaitCommentCount();
}
