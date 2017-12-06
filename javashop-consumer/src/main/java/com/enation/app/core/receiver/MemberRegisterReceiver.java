package com.enation.app.core.receiver;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.core.event.IMemberRegisterEvent;
import com.enation.app.shop.member.model.vo.MemberRegistVo;

/**
 * 会员注册消费者
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月29日 下午5:05:14
 */
@Component
public class MemberRegisterReceiver {

	protected final Logger logger = Logger.getLogger(getClass());
	
	@Autowired(required=false)
	private List<IMemberRegisterEvent> events;
	
	/**
	 * 会员注册
	 * @param member_id
	 */
	public void memberRegister(MemberRegistVo vo){
		try{
			if(events!=null){
				for(IMemberRegisterEvent event : events){
					event.memberRegister(vo);
				}
			}
		} catch (Exception e) {
			this.logger.error("处理会员注册出错",e);
			e.printStackTrace();
		}
		
	}
}
