package com.enation.app.shop.connect.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.connect.model.ConnectType;
import com.enation.app.shop.connect.model.ConnectUser;
import com.enation.app.shop.connect.service.IConnectMemberManager;
import com.enation.app.shop.member.plugin.MemberPluginBundle;
import com.enation.app.shop.member.service.IMemberManager;
import com.enation.app.shop.shop.apply.service.IShopManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.app.shop.shop.seller.impl.SellerManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.util.DateUtil;

/**
 * Author: Dawei
 * Datetime: 2016-03-14 16:44
 */
@Service
public class ConnectMemberManager implements IConnectMemberManager {

    private IMemberManager memberManager;
    private MemberPluginBundle memberPluginBundle;
    
    
    @Autowired
	private IDaoSupport daoSupport;
    
    @Autowired
  	private SellerManager sellerManager;
      
      @Autowired
  	private IShopManager shopManager;
    

    /**
     * 绑定会员的openid
     * @param member
     * @param connectUser
     * @return
     */
    public boolean bind(Member member, ConnectUser connectUser){
        if(member == null)
            return false;
        this.daoSupport.execute("UPDATE es_member SET " + getField(connectUser.getType()) + "=? WHERE member_id=?", connectUser.getOpenId(), member.getMember_id());
        return true;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int loginWithOpenId(ConnectUser connectUser) {
        String sql = "select m.*,l.name as lvname from "
                + ("es_member") + " m left join "
                + ("es_member_lv")
                + " l on m.lv_id = l.lv_id where m." + getField(connectUser.getType()) + "=?";
        List<Member> list = this.daoSupport.queryForList(sql, Member.class, connectUser.getOpenId());
        if (list == null || list.isEmpty()) {
            return 0;
        }

        Member member = list.get(0);
        long ldate = ((long) member.getLastlogin()) * 1000;
        Date date = new Date(ldate);
        Date today = new Date();
        int logincount = member.getLogincount();
        if (DateUtil.toString(date, "yyyy-MM").equals(DateUtil.toString(today, "yyyy-MM"))) {// 与上次登录在同一月内
            logincount++;
        } else {
            logincount = 1;
        }
        Long upLogintime = member.getLastlogin();// 登录积分使用
        member.setLastlogin(DateUtil.getDateline());
        member.setLogincount(logincount);
        memberManager.edit(member);
        
        this.setStoreSession(member);

        this.memberPluginBundle.onLogin(member, upLogintime);
        return 1;
    }

    private String getField(int type){
        switch (type){
            case ConnectType.WECHAT:
            case ConnectType.WECHAT_MP:
                return "wechat_id";
            case ConnectType.WEIBO:
                return "weibo_id";
            default:
                return "qq_id";
        }
    }

    public IMemberManager getMemberManager() {
        return memberManager;
    }

    public void setMemberManager(IMemberManager memberManager) {
        this.memberManager = memberManager;
    }

    public MemberPluginBundle getMemberPluginBundle() {
        return memberPluginBundle;
    }

    public void setMemberPluginBundle(MemberPluginBundle memberPluginBundle) {
        this.memberPluginBundle = memberPluginBundle;
    }
    
    /**
   	 * 登陆在session中存入店铺信息
   	 * @param member 会员实体
   	 */
   	private void setStoreSession(Member member) {
   		ThreadContextHolder.getSession().setAttribute(UserConext.CURRENT_MEMBER_KEY, member);

   		Seller storeMember = sellerManager.getSeller(member.getMember_id());
   		ThreadContextHolder.getSession().setAttribute(ISellerManager.CURRENT_STORE_MEMBER_KEY, storeMember);

   		/** 如果登录的会员拥有店铺则在session 中存入店铺信息 */
   		if (storeMember.getStore_id() != null && !storeMember.getIs_store().equals(0)) {
   			ThreadContextHolder.getSession().setAttribute(IShopManager.CURRENT_STORE_KEY,
   					shopManager.getShop(storeMember.getStore_id()));
   		}
   	}
}
