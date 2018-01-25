package com.enation.app.nanshan.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.nanshan.model.ArticleExt;
import com.enation.app.nanshan.model.NanShanActReserve;
import com.enation.app.nanshan.service.IActReserveService;
import com.enation.app.nanshan.vo.ActReserveVo;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;

@Service("actReserveService")
public class ActReserveServiceImpl implements IActReserveService  {

	@Autowired
	private IDaoSupport daoSupport;

	@Override
	public void reserve(NanShanActReserve reserve) {
		this.daoSupport.insert("es_nanshan_act_reserve", reserve);
		this.daoSupport.execute(" update es_nanshan_article_ext t set t.reserved_num=t.reserved_num + "+
				reserve.getNum() + " where t.article_id="+ reserve.getActivity_id());
	}

	@Override
	public ArticleExt queryArticleExt(int activityId) {
		ArticleExt ext=this.daoSupport.queryForObject("select reserve_num,reserved_num from es_nanshan_article_ext where article_id=?", ArticleExt.class, activityId);
		return ext;
	}


	@Override
	public void cancelReserve(NanShanActReserve NanShanActReserve) {

		this.daoSupport.execute("update es_nanshan_article_ext t set reserved_num=reserved_num - (" +
				"select a.num from  es_nanshan_act_reserve a  where a.is_del = 0 and a.activity_id= t.article_id and a.member_id = " + NanShanActReserve.getMember_id() +
				") where article_id= "+NanShanActReserve.getActivity_id());
		this.daoSupport.execute("update es_nanshan_act_reserve set is_del=1 where activity_id="+NanShanActReserve.getActivity_id()+" and member_id="+NanShanActReserve.getMember_id());

	}

	@Override
	public Page<ActReserveVo> queryReserveListById(Integer userId, String isDel, int pageNo,
			int pageSize) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select a.id, a.title, t.is_del isDel, t.activity_time  activityTime,t.num, a.pic_url imgUrl")
			  .append(",b.expiry_date expiryDate, b.act_name actName, b.act_cost actCost, b.act_address actAddress")
			  .append(" from es_nanshan_article a ,es_nanshan_act_reserve t, es_nanshan_article_ext b")
			  .append(" where a.id=t.activity_id and a.id = b.article_id and ")
			  .append(" t.member_id = ")
			  .append(userId);
		if(isDel != null){
			buffer.append(" and t.is_del = 0");
		}
		buffer.append(" order by t.id desc");

		Page<ActReserveVo> page=daoSupport.queryForPage(buffer.toString(), pageNo,pageSize);
		return page;
	}


	public NanShanActReserve queryReserveByMemberId(int memberId, int activityId){
		String sql ="select a.id activityId, a.title activityName, t.is_del isDel, t.activity_time  activityTime from es_nanshan_article a ,es_nanshan_act_reserve t where a.id=t.activity_id and t.is_del = 0 and t.member_id= ? and t.activity_id = ? limit 1";
		return daoSupport.queryForObject(sql, NanShanActReserve.class, memberId, activityId);
	}

	



}
