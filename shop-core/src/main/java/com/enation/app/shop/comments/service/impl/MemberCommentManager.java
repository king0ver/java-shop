package com.enation.app.shop.comments.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.comments.model.po.MemberComment;
import com.enation.app.shop.comments.model.po.ShopScore;
import com.enation.app.shop.comments.model.vo.CommentCount;
import com.enation.app.shop.comments.model.vo.CommentVo;
import com.enation.app.shop.comments.model.vo.MemberCommentVo;
import com.enation.app.shop.comments.service.IMemberCommentManager;
import com.enation.app.shop.comments.service.IMemeberCommentGalleryManager;
import com.enation.app.shop.comments.service.IShopScoreManager;
import com.enation.app.shop.goods.model.po.Goods;
import com.enation.app.shop.goods.model.vo.GoodsSkuVo;
import com.enation.app.shop.goods.service.IGoodsSkuManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.app.shop.trade.model.enums.OrderStatus;
import com.enation.app.shop.trade.model.enums.ShipStatus;
import com.enation.app.shop.trade.model.po.OrderPo;
import com.enation.app.shop.trade.model.vo.OrderDetail;
import com.enation.app.shop.trade.model.vo.OrderLineSeller;
import com.enation.app.shop.trade.model.vo.Product;
import com.enation.app.shop.trade.service.IOrderQueryManager;
import com.enation.framework.annotation.Log;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.log.LogType;
import com.enation.framework.util.StringUtil;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.NoPermissionException;

/**
 * 
 * @author LiFenLong yanlin
 * @version v4.0，版本改造,修改delete方法 v5.0 ,迁移插件
 * @since v6.0 v6.4.0
 * @date 2014-4-1 2017年8月17日 下午4:28:50
 */
@Service("memberCommentManager")
public class MemberCommentManager implements IMemberCommentManager {

	@Autowired
	private IDaoSupport daoSupport;

	@Autowired
	private ISellerManager storeMemberManager;

	@Autowired
	private IShopScoreManager shopScoreManager;

	@Autowired
	private IGoodsSkuManager goodsSkuManager;

	@Autowired
	private IOrderQueryManager orderQueryManager;

	@Autowired
	private IMemeberCommentGalleryManager memeberCommentGalleryManager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.shop.core.member.service.IMemberCommentManager#
	 * getGoodsComments(int, int, int, int, java.lang.Integer)
	 */
	@Override
	public Page getGoodsComments(int goods_id, int page, int pageSize, int type, Integer grade) {
		String sql = "SELECT c.* FROM es_member_comment c" + "WHERE c.goods_id=? AND c.type=? AND c.status=1 ";
		if (grade != null && grade != 0) {
			sql += " and c.grade = " + grade + "";
		}
		sql += " ORDER BY c.comment_id DESC";
		return this.daoSupport.queryForPage(sql, page, pageSize, goods_id, type);
	}

	@Override
	public Page getImageGoodsComments(int goods_id, int page, int pageSize) {
		// svn上种方式分页会有问题，所以逻辑修改
		String commentSql = "select distinct(c.comment_id) from es_member_comment c inner JOIN es_member_comment_gallery g "
				+ "on c.comment_id=g.comment_id where c.goods_id=? and  c.type=1 and c.status=1 ";

		List<Map> list = this.daoSupport.queryForList(commentSql, goods_id);

		String sql = "select c.* from  es_member_comment c  ";
		String comment = "0";
		if (list != null && list.size() > 0) {
			int i = 0;
			for (Map map : list) {
				Integer id = (Integer) map.get("comment_id");
				if (i == 0) {
					comment = id + "";
				} else {
					comment += "," + id;
				}
				i++;
			}
		}
		sql += " where c.comment_id in(" + comment + ") ORDER BY c.comment_id DESC ";
		return this.daoSupport.queryForPage(sql, page, pageSize);
	}

	@Override
	public int getImageCommentsCount(int goods_id) {
		String sql = "select count(distinct(c.comment_id)) from es_member_comment c INNER JOIN es_member_comment_gallery g on c.comment_id=g.comment_id WHERE c.status=1 and c.goods_id=?";
		return daoSupport.queryForInt(sql, goods_id);
	}

	@Override
	public List<Map> hot(int goods_id, int number) {

		String sql = "SELECT c.* FROM  es_member_comment  c  "
				+ "WHERE c.goods_id=? AND c.type=1 AND c.status=1 ORDER BY c.grade DESC, c.img desc";

		return this.daoSupport.queryForListPage(sql, 1, number, goods_id);
	}

	@Override
	public void addGallery(Integer comment_id, List<String> imageList) {
		if (imageList == null || imageList.size() == 0)
			return;
		for (int i = 0; i < imageList.size(); i++) {
			Map map = new HashMap();
			map.put("comment_id", comment_id);
			map.put("original", imageList.get(i));
			map.put("sort", i);
			this.daoSupport.insert("es_member_comment_gallery", map);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.shop.core.member.service.IMemberCommentManager#getGoodsGrade(
	 * int)
	 */
	@Override
	public int getGoodsGrade(int goods_id) {
		int sumGrade = this.daoSupport.queryForInt(
				"SELECT SUM(grade) FROM es_member_comment WHERE status=1 AND goods_id=? AND type=1", goods_id);
		int total = this.daoSupport.queryForInt(
				"SELECT COUNT(0) FROM es_member_comment WHERE status=1 AND goods_id=? AND type=1", goods_id);
		if (sumGrade != 0 && total != 0) {
			return (sumGrade / total);
		} else {
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.shop.core.member.service.IMemberCommentManager#getAllComments
	 * (int, int, int)
	 */
	@Override
	public Page getAllComments(int page, int pageSize, int type) {
		String sql = "SELECT c.* FROM es_member_comment" + "WHERE  c.type=? ORDER BY c.comment_id DESC";
		return this.daoSupport.queryForPage(sql, page, pageSize, type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.shop.core.member.service.IMemberCommentManager#get(int)
	 */
	@Override
	public MemberComment get(int comment_id) {
		return this.daoSupport.queryForObject("select * from es_member_comment where comment_id = ?",
				MemberComment.class, comment_id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.shop.core.member.service.IMemberCommentManager#update(com.
	 * enation.app.shop.core.member.model.MemberComment)
	 */
	@Override
	@Log(type = LogType.MEMBER, detail = "审核商品ID为${memberComment.goods_id}的评论或咨询")
	public void update(MemberComment memberComment) {
		this.daoSupport.update("es_member_comment", memberComment, "comment_id=" + memberComment.getComment_id());
		if (memberComment.getStatus() == 1) {
			String updatesql = "update es_goods set grade=grade+1 where goods_id=?";
			this.daoSupport.execute(updatesql, memberComment.getGoods_id());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.shop.core.member.service.IMemberCommentManager#statistics(
	 * int)
	 */
	@Override
	public Map statistics(int goodsid) {
		String sql = "select grade,count(grade) num from es_member_comment c where c.goods_id =? GROUP BY c.grade ";
		List<Map> gradeList = this.daoSupport.queryForList(sql, goodsid);
		Map gradeMap = new HashMap();
		for (Map grade : gradeList) {
			Object gradeValue = grade.get("grade");
			long num = Long.parseLong(grade.get("num").toString().trim());
			gradeMap.put("grade_" + gradeValue, num);
		}
		return gradeMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.shop.core.member.service.IMemberCommentManager#
	 * getGoodsCommentsCount(int)
	 */
	@Override
	public int getGoodsCommentsCount(int goods_id) {
		return this.daoSupport.queryForInt(
				"SELECT COUNT(0) FROM es_member_comment WHERE goods_id=? AND status=1 AND type=1", goods_id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.shop.core.member.service.IMemberCommentManager#delete(java.
	 * lang.Integer[])
	 */
	@Override
	@Log(type = LogType.MEMBER, detail = "删除评论或咨询")
	public void delete(Integer[] comment_id) {
		if (comment_id == null) {
			return;
		}

		String id_str = StringUtil.arrayToString(comment_id, ",");
		String sql = "DELETE FROM es_member_comment where comment_id in (" + id_str + ")";
		this.daoSupport.execute(sql);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.shop.core.member.service.IMemberCommentManager#
	 * getMemberComments(int, int, int, int)
	 */
	@Override
	public Page getMemberComments(int page, int pageSize, int type, int member_id) {
		return this.daoSupport.queryForPage(
				"SELECT c.* FROM es_member_comment c  " + "WHERE c.type=? AND c.member_id=? ORDER BY c.comment_id DESC",
				page, pageSize, type, member_id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.shop.core.member.service.IMemberCommentManager#
	 * getMemberCommentTotal(int, int)
	 */
	@Override
	public int getMemberCommentTotal(int member_id, int type) {
		return this.daoSupport.queryForInt("SELECT COUNT(0) FROM es_member_comment WHERE member_id=? AND type=?",
				member_id, type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.shop.core.member.service.IMemberCommentManager#
	 * getCommentsByStatus(int, int, int, int)
	 */
	@Override
	public Page getCommentsByStatus(int page, int pageSize, int type, int status) {
		return this.daoSupport.queryForPage(
				"SELECT c.* FROM es_member_comment c WHERE m.disabled!=1 and c.type=? and c.status = ? ORDER BY c.comment_id DESC",
				page, pageSize, type, status);
	}

	/**
	 * @author LiFenLong
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.shop.core.member.service.IMemberCommentManager#deletealone(
	 * int)
	 */
	@Override
	public void deletealone(int comment_id) {

		this.daoSupport.execute("DELETE FROM es_member_comment WHERE comment_id=?", comment_id);
	}

	/**
	 * 根据商品id获取商品评论总数
	 * 
	 * @param goods_id
	 * @return
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.shop.core.member.service.IMemberCommentManager#
	 * getCommentsCount(int)
	 */
	@Override
	public int getCommentsCount(int goods_id) {
		return this.daoSupport.queryForInt(
				"SELECT COUNT(0) FROM es_member_comment WHERE goods_id=? AND status=1 AND type=1", goods_id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.shop.core.member.service.IMemberCommentManager#
	 * getCommentsCount(int, int)
	 */
	@Override
	public int getCommentsCount(int goods_id, int grade) {
		return this.daoSupport.queryForInt(
				"SELECT COUNT(0) FROM es_member_comment WHERE goods_id=? AND status=1 AND type=1 AND grade = ?",
				goods_id, grade);
	}

	/**
	 * 根据评分、商品ID或者各个评论的数量
	 * 
	 * @param count
	 *            数量
	 * @param goods_id
	 *            商品ID
	 * @param count
	 *            评论数量 whj 2015-10-15
	 * @return
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.shop.core.member.service.IMemberCommentManager#
	 * getCommentsGradeCount(int, int)
	 */
	@Override
	public int getCommentsGradeCount(int goods_id, int grade) {
		return this.daoSupport.queryForInt(
				"SELECT COUNT(0) FROM es_member_comment WHERE goods_id=? AND status=1 AND type=1 AND grade=?", goods_id,
				grade);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.shop.core.member.service.IMemberCommentManager#
	 * getGoodsCommentsGradeList(int, int, int, int, int)
	 */
	@Override
	public Page getGoodsCommentsGradeList(int goods_id, int page, int pageSize, int type, int grade) {
		return this.daoSupport.queryForPage(
				"SELECT c.* FROM es_member_comment c WHERE c.goods_id=? AND c.type=? AND c.grade=? AND c.status=1 ORDER BY c.comment_id DESC",
				page, pageSize, goods_id, type, grade);
	}

	@Override
	public List getCommentGallery(Integer comment_id) {
		String sql = "select * from es_member_comment_gallery where comment_id = ?";
		return this.daoSupport.queryForList(sql, comment_id);
	}

	@Override
	public Page pageCommentOrders(int pageNo, int pageSize) {

		/**
		 * 查询当前会员的订单
		 */
		Seller member = storeMemberManager.getSeller();
		StringBuffer sql = new StringBuffer("SELECT * FROM es_order o where o.member_id=? and ");

		sql.append(" order by o.create_time desc");

		/**
		 * 分页查询买家订单
		 */
		Page page = this.daoSupport.queryForPage(sql.toString(), pageNo, pageSize, OrderPo.class, member.getMember_id(),
				member.getMember_id());
		// 先按PO进行查询

		// 转为VO
		List<OrderPo> orderList = (List) page.getResult();
		List<OrderLineSeller> lineList = new ArrayList();
		for (OrderPo orderPo : orderList) {
			OrderLineSeller line = new OrderLineSeller(orderPo);
			lineList.add(line);
		}

		// 生成新的Page
		long totalCount = page.getTotalCount();
		Page linePage = new Page(pageNo, totalCount, pageSize, lineList);

		return linePage;
	}

	@Override
	public CommentVo add(CommentVo commentVo) {
		ShopScore shopScore = new ShopScore(commentVo);
		/** 获取当前登陆的买家会员信息 */
		Seller member = storeMemberManager.getSeller();
		/* 查询出订单 */
		OrderDetail orderDetail = orderQueryManager.getOneBySn(commentVo.getOrder_sn());
		if (member.getMember_id() != orderDetail.getMember_id()) {
			throw new RuntimeException("您的会员【" + orderDetail.getMember_name() + "】没有登陆,请登录！！");
		}
		shopScore.setMember_id(member.getMember_id());
		shopScore.setShop_id(orderDetail.getSeller_id());
		shopScoreManager.add(shopScore);
		this.add(commentVo.getComments(), orderDetail);
		return commentVo;
	}

	/**
	 * 添加评论咨询表
	 * 
	 * @param commentList
	 *            发起的评论
	 * @param order_sn
	 *            订单编号
	 */
	private void add(List<MemberCommentVo> commentList, OrderDetail orderDetail) {

		Map<Integer, Object> productMap = new HashMap<Integer, Object>();
		/* 将product循环放入map */
		for (Product product : orderDetail.getProductList()) {
			productMap.put(product.getProduct_id(), product);
		}

		for (MemberCommentVo comment : commentList) {
			Product product = (Product) productMap.get(comment.getSku_id());
			if (product == null) {
				throw new RuntimeException("没有相应的规格的商品");
			}
			MemberComment memberComment = new MemberComment(comment, product, orderDetail);
			/*  */
			if (memberComment.getGrade() == 1 && StringUtil.isEmpty(memberComment.getContent())) {
				memberComment.setContent("此评论默认好评！！");
			} else if (memberComment.getGrade() > 1 && StringUtil.isEmpty(memberComment.getContent())) {
				throw new RuntimeException("非好评必填评论内容!!");
			}
			this.daoSupport.insert("es_member_comment", memberComment);

			int comment_id = this.daoSupport.getLastId("es_member_comment");
			/* 添加评论图片 */
			if (!comment.getImages().isEmpty() && comment.getImages().size() > 0) {
				this.memeberCommentGalleryManager.add(comment_id, comment.getImages());
			}
		}
	}

	@Override
	public CommentCount getCommentCount(Integer goods_id) {
		String sql = "select grade ,count(*) count from es_member_comment where type = 1 and status = 1 and goods_id =? GROUP BY grade ";
		List<Map> countList = this.daoSupport.queryForList(sql, goods_id);
		CommentCount comentCount = new CommentCount();
		for (Map map : countList) {
			if (Integer.parseInt(map.get("grade").toString()) == 1) {
				comentCount.setPoor(Integer.parseInt(map.get("count").toString()));
			}
			if (Integer.parseInt(map.get("grade").toString()) == 2) {
				comentCount.setGeneral(Integer.parseInt(map.get("count").toString()));
			}
			if (Integer.parseInt(map.get("grade").toString()) == 3) {
				comentCount.setGood(Integer.parseInt(map.get("count").toString()));
			}
		}
		comentCount.setAll(comentCount.getGeneral() + comentCount.getGood() + comentCount.getPoor());
		return comentCount;
	}

	@Override
	public Page getGoodsComments(Integer goods_id, Integer page, Integer pageSize, Integer grade) {
		return this.daoSupport.queryForPage(
				"SELECT c.* FROM es_member_comment c WHERE c.goods_id=? AND c.grade=? AND c.status=1 ORDER BY c.comment_id DESC",
				page, pageSize, goods_id, grade);
	}

	@Override
	public void addConsult(Integer goods_id, String content) {
		Seller member = storeMemberManager.getSeller();
		if(member == null) {
			throw new RuntimeException("请登录会员以后再做此操作！");
		}
		String sql = "select * from es_goods where goods_id = ?";
		Goods goods = this.daoSupport.queryForObject(sql, Goods.class, goods_id);
		if(goods != null) {
			MemberComment memberComment = new MemberComment(member,goods,content);
			this.daoSupport.insert("es_member_comment", memberComment);
		}
		
	}

	@Override
	public int getWaitCommentCount() {
		String sql = "select count(*) from es_member_comment m left join es_order o on o.sn = m.order_sn where"
				+ " o.ship_status = '" + ShipStatus.SHIP_NO.value() + "'and o.order_status = '"+OrderStatus.ROG.value()+"' and m.status = 1";//TODO 这里还缺条件，我实在是想不起来了
		return this.daoSupport.queryForInt(sql);
	}
}
