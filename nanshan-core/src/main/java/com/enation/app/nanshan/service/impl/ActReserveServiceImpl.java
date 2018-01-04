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
	public void reserve(NanShanActReserve NanShanActReserve) {
		this.daoSupport.insert("es_nanshan_act_reserve", NanShanActReserve);
		this.daoSupport.execute(" update es_nanshan_article_ext t set t.reserved_num=t.reserved_num+1 where t.article_id="+NanShanActReserve.getActivity_id());
	}

	@Override
	public ArticleExt queryArticleExt(int activityId) {
		ArticleExt ext=this.daoSupport.queryForObject("select reserve_num,reserved_num from es_nanshan_article_ext where article_id=?", ArticleExt.class, activityId);
		return ext;
	}


	@Override
	public void cancelReserve(NanShanActReserve NanShanActReserve) {
		this.daoSupport.execute("update es_nanshan_act_reserve set is_del=1 where activity_id="+NanShanActReserve.getActivity_id()+" and member_id="+NanShanActReserve.getMember_id());
		this.daoSupport.execute("update es_nanshan_article_ext t set reserved_num=reserved_num-1 where article_id="+NanShanActReserve.getActivity_id());
	}

	@Override
	public Page<ActReserveVo> queryReserveListById(Integer userId, int pageNo,
			int pageSize) {
		String sql ="select a.id activityId, a.title activityName, t.is_del isDel, t.activity_time  activityTime from es_nanshan_article a ,es_nanshan_act_reserve t where a.id=t.activity_id and t.member_id="+userId+" order by t.activity_time desc";
		Page<ActReserveVo> page=daoSupport.queryForPage(sql, pageNo,pageSize);
		return page;
	}


	public NanShanActReserve queryReserveByMemberId(int memberId, int activityId){
		String sql ="select a.id activityId, a.title activityName, t.is_del isDel, t.activity_time  activityTime from es_nanshan_article a ,es_nanshan_act_reserve t where a.id=t.activity_id and t.is_del = 1 and t.member_id= ? and t.activity_id = ? limit 1";
		return daoSupport.queryForObject(sql, NanShanActReserve.class, memberId, activityId);
	}

	



}
