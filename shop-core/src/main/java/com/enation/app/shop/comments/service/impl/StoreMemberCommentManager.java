package com.enation.app.shop.comments.service.impl;

import java.util.Map;


import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.shop.comments.model.po.MemberComment;
import com.enation.app.shop.comments.service.IStoreMemberCommentManager;
import com.enation.eop.SystemSetting;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.StringUtil;

/**
 * 
 * 多店会员评价管理类
 * 
 * @author Kanon 版本改造 yanlin
 * @version v1.0 v2.0
 * @since v6.0 v6.4.0
 * @date 2016-3-2 2017年8月17日 下午4:23:26
 */
@Service("storeMemberCommentManager")
public class StoreMemberCommentManager implements IStoreMemberCommentManager {

	@Autowired
	private IDaoSupport daoSupport;
	
	@Autowired
	private AmqpTemplate amqpTemplate;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.b2b2c.core.service.member.IStoreMemberCommentManager#
	 * getAllComments(int, int, java.util.Map, java.lang.Integer)
	 */
	@Override
	public Page getAllComments(int page, int pageSize, Map map, Integer store_id) {
		String sql = this.createTemlSql(map, store_id);
		return this.daoSupport.queryForPage(sql, page, pageSize, store_id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.b2b2c.core.service.member.IStoreMemberCommentManager#get(java
	 * .lang.Integer)
	 */
	@Override
	public Map get(Integer comment_id) {
		return this.daoSupport.queryForMap("SELECT * FROM es_member_comment WHERE comment_id=?", comment_id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.b2b2c.core.service.member.IStoreMemberCommentManager#edit(
	 * java.util.Map, java.lang.Integer)
	 */
//	@Override
//	public void edit(MemberComment memberComment, Integer comment_id) {
//		this.daoSupport.update("es_member_comment", memberComment, "comment_id=" + comment_id);
//	}

	private String createTemlSql(Map map, Integer store_id) {
		StringBuffer sql = new StringBuffer();
		sql.append(
				"SELECT m.uname as uname,g.goods_name as goods_name,c.* FROM es_member_comment c LEFT JOIN es_goods g ON c.goods_id=g.goods_id"
						+ " LEFT JOIN es_member m ON c.member_id=m.member_id WHERE m.disabled!=1 and g.seller_id=? and c.type="
						+ map.get("type"));
		if (map.get("stype") != null) {
			if (map.get("stype").equals("0")) {
				if (map.get("keyword") != null) {
					sql.append(" and (m.uname like '%" + map.get("keyword") + "%'");
					sql.append(" or c.content like '%" + map.get("keyword") + "%'");
					sql.append(" or g.goods_name like '%" + map.get("keyword") + "%')");
				}
			} else {
				if (map.get("mname") != null) {
					sql.append(" and m.uname like '%" + map.get("mname") + "%'");
				}
				if (map.get("gname") != null) {
					sql.append(" and g.goods_name like '%" + map.get("gname") + "%'");
				}
				if (map.get("content") != null) {
					sql.append(" and c.content like '%" + map.get("content") + "%'");
				}
			}
			if (map.get("status") != null) {
				sql.append(" and c.status=" + map.get("status"));
			}
			if (map.get("replyStatus") != null && !map.get("replyStatus").equals("2")) {
				sql.append(" and c.replystatus =" + map.get("replyStatus"));
			}
		}
		if (map.get("grade") != null)
			if (Integer.parseInt(map.get("grade").toString()) != -1) {
				sql.append(" and c.grade=" + map.get("grade"));
			}
		sql.append(" ORDER BY c.comment_id DESC");
		// System.out.println(sql);
		return sql.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.b2b2c.core.service.member.IStoreMemberCommentManager#add(com.
	 * enation.app.b2b2c.core.model.member.StoreMemberComment)
	 */
//	@Override
//	@Transactional(propagation = Propagation.REQUIRED)
//	@Log(type = LogType.MEMBER, detail = "添加商品ID为${memberComment.goods_id}的评论或咨询")
//	public void add(StoreMemberComment memberComment) {
//
//		// 添加评论、咨询
//		this.daoSupport.insert("es_member_comment", memberComment);
//		
//		// 获取新添加的评论或咨询ID添加到评论或咨询信息中 add by DMRain 2016-5-3
//		memberComment.setComment_id(this.daoSupport.getLastId("es_member_comment"));
//		
//		// 如果该条信息是评论（咨询不算评论 不增量）
//		if (memberComment.getGrade() > 0) {
//
//			// 添加商品评论次数d
//			updateStoreComment(memberComment);
//		}
//
//		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
//		String[] picnames = request.getParameterValues("picnames_" + memberComment.getProduct_id());
//		if (picnames != null) {
//			for (int i = 0; i < picnames.length; i++) {
//				Map map = new HashMap();
//				map.put("comment_id", memberComment.getComment_id());
//				map.put("original", this.transformPath(picnames[i]));
//				map.put("sort", i);
//				this.daoSupport.insert("es_member_comment_gallery", map);
//			}
//		}
//		
//		if(memberComment.getType()==1) {
//			GoodsCommentMsg goodsCommentMsg = new GoodsCommentMsg();
//			goodsCommentMsg.setGoods_id(memberComment.getGoods_id());
//			goodsCommentMsg.setComment_id(memberComment.getComment_id());
//			goodsCommentMsg.setMember_id(memberComment.getMember_id());
//			goodsCommentMsg.setSku_id(memberComment.getProduct_id());
//			this.amqpTemplate.convertAndSend(AmqpExchange.GOODS_COMMENT_COMPLETE.name(), "goods-comment-routingkey",goodsCommentMsg);
//		}
//		
//	}

	/**
	 * 页面中传递过来的图片地址为:http://<staticserver>/<image path>如:
	 * http://static.enationsoft.com/attachment/goods/1.jpg
	 * 存在库中的为fs:/attachment/goods/1.jpg 生成fs式路径 此方法 是给当前类的add(StoreMemberComment
	 * memberComment) 方法使用的
	 * 
	 * @param path
	 * @return
	 */
	private String transformPath(String path) {
		String static_server_domain = SystemSetting.getStatic_server_domain();

		String regx = static_server_domain;
		path = path.replace(regx, EopSetting.FILE_STORE_PREFIX);
		return path;
	}

//	/**
//	 * 修改店铺评价
//	 * 
//	 * @param memberComment
//	 */
//	private void updateStoreComment(StoreMemberComment memberComment) {
//		// orcale 数据库异常解决
//		String sql = "select store_id,(SELECT COUNT(comment_id)from es_member_comment WHERE grade=3)as grade,sum(store_desccredit) as store_desccredit ,SUM(store_servicecredit)as store_servicecredit,SUM(store_deliverycredit) as store_deliverycredit ,COUNT(comment_id) as comment_num from es_member_comment WHERE store_id=? GROUP BY  STORE_ID";
//		Map map = this.daoSupport.queryForMap(sql, memberComment.getStore_id());
//		double grade = Integer.parseInt(map.get("grade").toString());// 除法运算 int 会得出0 liushuai 2015-9-24
//		Double store_desccredit = Double.parseDouble(map.get("store_desccredit").toString());
//		Double store_servicecredit = Double.parseDouble(map.get("store_servicecredit").toString());
//		Double store_deliverycredit = Double.parseDouble(map.get("store_deliverycredit").toString());
//		double comment_num = Integer.parseInt(map.get("comment_num").toString());// 除法运算 int 会得出0 liushuai 2015-9-24
//
//		Double store_credit = Double
//				.valueOf((store_desccredit + store_servicecredit + store_deliverycredit) / (double) 3 / comment_num); // 店铺信用由描述+服务+发货的平均值构成d
//																														// whj
//																														// 2015-06-12
//		int store_credit_num = Integer.parseInt(new java.text.DecimalFormat("0").format(store_credit)); // 将浮点型店铺信用转换成int型.小叔四舍五入变成整数(store模型就是int型)。
//																										// whj
//																										// 2015-06-12
//
//		Map storeInfo = new HashMap();
//		storeInfo.put("shop_praise_rate", grade / comment_num);
//		storeInfo.put("shop_desccredit", store_desccredit / comment_num);
//		storeInfo.put("shop_servicecredit", store_servicecredit / comment_num);
//		storeInfo.put("shop_deliverycredit", store_deliverycredit / comment_num);
//		storeInfo.put("shop_credit", store_credit_num);
//		this.daoSupport.update("es_shop_detail", storeInfo, "shop_id=" + memberComment.getStore_id());
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.b2b2c.core.service.member.IStoreMemberCommentManager#
	 * getCommentCount(java.lang.Integer)
	 */
	@Override
	public Integer getCommentCount(Integer type, Integer store_id) {
		String sql = "SELECT count(0) from es_member_comment c WHERE c.type=? AND c.replystatus=0 and shop_id=?";
		return this.daoSupport.queryForInt(sql, type, store_id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.b2b2c.core.service.member.IStoreMemberCommentManager#
	 * getGoodsStore_desccredit(java.lang.Integer)
	 */
	@Override
	public Double getGoodsStore_desccredit(Integer goods_id) {

		// 把商品综合评分改为计算三个服务平均值 edit_by Sylow 2016-07-21
		String sql = "SELECT SUM(store_deliverycredit+store_desccredit+store_servicecredit)/3/count(comment_id) AS store_deliverycredit FROM es_member_comment WHERE type = 1 AND goods_id =?";
		Map map = this.daoSupport.queryForMap(sql, goods_id);

		// 四舍五入 不用sql语句计算 防止不兼容其他数据库
		Object o = map.get("store_deliverycredit");
		double grade = 0d;
		if (o != null) {
			grade = StringUtil.toDouble(o.toString(), 0d);
		}

		grade = Math.round(grade);
		return grade;
	}

	@Override
	public MemberComment getMemberComment(Integer comment_id) {
		String sql = "SELECT * FROM es_member_comment WHERE comment_id=?";
		MemberComment memberComment = this.daoSupport.queryForObject(sql, MemberComment.class, comment_id);
		return memberComment;
	}
	
	
}
