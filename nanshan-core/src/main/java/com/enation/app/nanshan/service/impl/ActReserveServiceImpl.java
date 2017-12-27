package com.enation.app.nanshan.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.nanshan.model.NActReserve;
import com.enation.app.nanshan.service.IActReserveService;
import com.enation.app.nanshan.vo.ActReserveVo;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.util.DateUtil;

@Service("actReserveService")
public class ActReserveServiceImpl implements IActReserveService  {

	@Autowired
	private IDaoSupport daoSupport;
	
	@Override
	public void reserveAct(ActReserveVo actReserveVo) {
		NActReserve NActReserve=new NActReserve();
		NActReserve.setArticle_id(actReserveVo.getArticleId());
		NActReserve.setAct_time(actReserveVo.getActTime().getTime()/1000);
		NActReserve.setEmail(actReserveVo.getEmail());
		NActReserve.setMember_name(actReserveVo.getMemberName());
		NActReserve.setAge(actReserveVo.getAge());
		NActReserve.setPhone_number(actReserveVo.getPhoneNumber());
		this.daoSupport.insert("es_nanshan_act_reserve", NActReserve);
		this.daoSupport.execute(" update es_nanshan_article_ext t set t.reserved_num=t.reserved_num+1 where t.article_id="+actReserveVo.getArticleId());
		
	}

}
