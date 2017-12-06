package com.enation.app.shop.member.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.base.AmqpExchange;
import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.model.MemberLv;
import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.member.model.enums.MemberSendCodeEnum;
import com.enation.app.shop.member.model.vo.MemberLoginMsg;
import com.enation.app.shop.member.model.vo.MemberLoginVo;
import com.enation.app.shop.member.model.vo.MemberRegistVo;
import com.enation.app.shop.member.plugin.MemberPluginBundle;
import com.enation.app.shop.member.service.IMemberLvManager;
import com.enation.app.shop.member.service.IMemberManager;
import com.enation.app.shop.message.service.IEmailSendlManager;
import com.enation.app.shop.message.service.IMessageManager;
import com.enation.app.shop.message.service.IMobileSmsSendManager;
import com.enation.app.shop.shop.apply.service.IShopManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.app.shop.shop.seller.impl.SellerManager;
import com.enation.eop.SystemSetting;
import com.enation.eop.sdk.context.UserConext;
import com.enation.eop.sdk.utils.ValidCodeServlet;
import com.enation.framework.annotation.Log;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.log.LogType;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;
import com.enation.framework.util.TestUtil;
import com.enation.framework.util.Validator;


/**
 * 会员管理
 * 
 * @author kingapex 2010-4-30上午10:07:24
 * @version v2.0,2016年2月18日 版本改造
 * @since v6.0
 */
@Service("memberManager")
public class MemberManager implements IMemberManager{
	/** 短信验证码session前缀 */
	private static final String SMS_CODE_PREFIX = "es_sms_";
	/** 短信验证间隔时间session前缀 */
	private static final String INTERVAL_TIME_PREFIX = "es_interval_";
	private final Logger logger = Logger.getLogger(getClass());
	/** 短信超时时间前缀 */
	private static final String SENDTIME_PREFIX = "es_sendtime";
	/** 短信过期时间 */
	private static final Long SMS_CODE_TIMEOUT = 120l;

	
	@Autowired
	private IDaoSupport daoSupport;
	@Autowired
	protected IMemberLvManager memberLvManager; 
	@Autowired
	private MemberPluginBundle memberPluginBundle; 
	@Autowired
	private SellerManager sellerManager;
	@Autowired
	private IShopManager shopManager;
	@Autowired
	private IMobileSmsSendManager mobileSmsSendManager;
	@Autowired
	private IMessageManager messageManager;
	@Autowired
	private IEmailSendlManager emailSendManager;
	@Autowired
	private AmqpTemplate amqpTemplate;
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.base.core.service.IMemberManager#logout()
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void logout() {

		Member member = UserConext.getCurrentMember();
		member = this.get(member.getMember_id());
		ThreadContextHolder.getSession().removeAttribute(UserConext.CURRENT_MEMBER_KEY);

		ThreadContextHolder.getSession().removeAttribute(ISellerManager.CURRENT_STORE_MEMBER_KEY);
		//this.memberPluginBundle.onLogout(member);
	}

	

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.base.core.service.IMemberManager#add(com.enation.app.base.
	 * core.model.Member)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@Log(type = LogType.MEMBER, detail = "添加一个${member.uname}会员")
	public int add(Member member) {
		if (member == null)
			throw new IllegalArgumentException("member is null");
		if (member.getUname() == null)
			throw new IllegalArgumentException("member' uname is null");
		if (member.getPassword() == null)
			throw new IllegalArgumentException("member' password is null");

		if (!this.checkUsername(member.getUname())) {
			return 0;
		}
		if (member.getLv_id() == null || member.getLv_id() == 0) {
			Integer lvid = memberLvManager.getDefaultLv();
			member.setLv_id(lvid);
		}

		// 如果会员昵称为空，就将会员登陆用户名设置为昵称 修改人:DMRain 2015-12-16
		if (member.getNickname() == null) {
			member.setNickname(member.getUname());
		}

		member.setPoint(0);
		member.setAdvance(0D);

		if (member.getRegtime() == null) {
			member.setRegtime(DateUtil.getDateline());
		}

		member.setLastlogin(DateUtil.getDateline());
		member.setLogincount(0);
		member.setPassword(StringUtil.md5(member.getPassword()));

		// Dawei Add
		member.setMp(0);
		member.setFace("");
		member.setMidentity(0);
		if (member.getSex() == null) {
			member.setSex(1); // 新注册用户性别默认为'男'
		}

		this.daoSupport.insert("es_member", member);
		int memberid = this.daoSupport.getLastId("es_member");
		member.setMember_id(memberid);

		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.base.core.service.IMemberManager#checkEmailSuccess(com.
	 * enation.app.base.core.model.Member)
	 */
	@Override
	public void checkEmailSuccess(Member member) {

		int memberid = member.getMember_id();
		String sql = "update es_member set is_cheked = 1 where member_id =  " + memberid;
		this.daoSupport.execute(sql);
		this.memberPluginBundle.onEmailCheck(member);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.base.core.service.IMemberManager#get(java.lang.Integer)
	 */
	@Override
	public Member get(Integer memberId) {
		String sql = "select * from es_member where member_id=? AND disabled!=1";
		Member m = this.daoSupport.queryForObject(sql, Member.class, memberId);
		return m;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.base.core.service.IMemberManager#getMember(java.lang.Integer)
	 */
	@Override
	public Map getMember(Integer memberId) {
		String sql = "select * from es_member where member_id=?";
		List list = this.daoSupport.queryForList(sql, memberId);
		if (!list.isEmpty()) {
			Map map = (Map) list.get(0);
			return map;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.base.core.service.IMemberManager#getMemberByUname(java.lang.
	 * String)
	 */
	@Override
	public Member getMemberByUname(String uname) {
		String sql = "select * from es_member where uname=? AND disabled!=1";
		List list = this.daoSupport.queryForList(sql, Member.class, uname);
		Member m = null;
		if (list != null && list.size() > 0) {
			m = (Member) list.get(0);
		}
		return m;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.base.core.service.IMemberManager#getMemberByEmail(java.lang.
	 * String)
	 */
	@Override
	public Member getMemberByEmail(String email) {
		String sql = "select * from es_member where email=? AND disabled!=1";
		List list = this.daoSupport.queryForList(sql, Member.class, email);
		Member m = null;
		if (list != null && list.size() > 0) {
			m = (Member) list.get(0);
		}
		return m;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.base.core.service.IMemberManager#edit(com.enation.app.base.
	 * core.model.Member)
	 */
	@Override
	@Log(type = LogType.MEMBER, detail = "修改了用户名为${member.uname}的会员信息")
	public Member edit(Member member) {
		// 前后台用到的是一个edit方法，请在action处理好
		this.daoSupport.update("es_member", member, "member_id=" + member.getMember_id());
		Integer memberpoint = member.getPoint();
		// 改变会员等级
		if (memberpoint != null) {
			MemberLv lv = this.memberLvManager.getByPoint(memberpoint);
			if (lv != null) {
				if ((member.getLv_id() == null || lv.getLv_id().intValue() > member.getLv_id().intValue())) {
					this.updateLv(member.getMember_id(), lv.getLv_id());
				}
			}
		}
		Member currentMember = UserConext.getCurrentMember();
		// FIXME 如果当前Session中，从前台登录了该用户，应从Session中移除，给出提示告知需重新登录。
		if (currentMember != null && currentMember.getMember_id().equals(member.getMember_id())) {
			// 这里不能直接将member直接放入session 因为这个member不是从数据库查出的 有很多字段为null 而从sql中查出的Integer会封装成0
			String sql = "select m.*,l.name as lvname from " + "es_member m left join " + "es_member_lv"
					+ " l on m.lv_id = l.lv_id where m.disabled!=1 and m.member_id=?";

			Member newMember = this.daoSupport.queryForObject(sql, Member.class, member.getMember_id());

			ThreadContextHolder.getSession().setAttribute(UserConext.CURRENT_MEMBER_KEY, newMember);
		}
		return null;
	}

	/*1
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.base.core.service.IMemberManager#checkname(java.lang.String)
	 */
	@Override
	public boolean checkUsername(String name) {
		String sql = "select count(0) from es_member where uname=?";
		int count = this.daoSupport.queryForInt(sql, name);
		if(count > 0) {
			return false;
		}
		return true;
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.base.core.service.IMemberManager#checkemail(java.lang.String)
	 */
	@Override
	public boolean checkEmail(String email) {
		String sql = "select count(0) from es_member where email=? AND disabled!=1";
		int count = this.daoSupport.queryForInt(sql, email);
		if(count > 0) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.base.core.service.IMemberManager#checkMobile(java.lang.
	 * String)
	 */
	@Override
	public boolean checkMobile(String mobile) {
		String sql = "select count(0) from es_member where mobile=? AND disabled!=1";
		int count = this.daoSupport.queryForInt(sql, mobile);
		if(count>0) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.base.core.service.IMemberManager#delete(java.lang.Integer[])
	 */
	@Override
	@Log(type = LogType.MEMBER, detail = "删除会员")
	public void delete(Integer[] id) {
		if (id == null) {
			return;
		}
		String str = "";
		for (int i = 0; i < id.length; i++) {
			str += "?,";
		}
		str = str.substring(0, str.length() - 1);
		String sql = "update es_member set disabled=1 where member_id in (" + str + ")";

		this.daoSupport.execute(sql, id);
		// 修改店铺的状态为禁用
		sql = "update es_shop set shop_disable = 'refused' where shop_id in (" + str + ")";
		this.daoSupport.execute(sql, id);
		// 修改此店铺的所有商品为下架状态
		sql = "update es_goods set market_enable = 0 where seller_id  in (" + str + ")";
		this.daoSupport.execute(sql, id);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.base.core.service.IMemberManager#updatePassword(java.lang.
	 * String)
	 */
	@Override
	public void updatePassword(String password) {
		// 获取当前登录的会员信息
		Member member = UserConext.getCurrentMember();

		// 修改密码
		this.updatePassword(member.getMember_id(), password);

		// 将修改后的密码以MD5方式加密set进会员信息中
		member.setPassword(StringUtil.md5(password));

		// 将会员信息重新set进session中
		ThreadContextHolder.getSession().setAttribute(UserConext.CURRENT_MEMBER_KEY, member);

		// 修改密码成功后，删除session中的加密信息 add_by DMRain 2016-7-11
		ThreadContextHolder.getSession().removeAttribute("account_detail");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.base.core.service.IMemberManager#updatePassword(java.lang.
	 * Integer, java.lang.String)
	 */
	@Override
	public void updatePassword(Integer memberid, String password) {
		String md5password = password == null ? StringUtil.md5("") : StringUtil.md5(password);
		String sql = "update es_member set password = ? where member_id =? ";
		this.daoSupport.execute(sql, md5password, memberid);
		this.memberPluginBundle.onUpdatePassword(password, memberid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.base.core.service.IMemberManager#updateFindCode(java.lang.
	 * Integer, java.lang.String)
	 */
	@Override
	public void updateFindCode(Integer memberid, String code) {
		String sql = "update es_member set find_code = ? where member_id =? ";
		this.daoSupport.execute(sql, code, memberid);
	}

	


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.base.core.service.IMemberManager#login(java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int login(String username) {
		String sql = "select m.*,l.name as lvname from " + "es_member m left join " + "es_member_lv"
				+ " l on m.lv_id = l.lv_id where m.disabled!=1 and m.uname=? ";
		// 用户名中包含@，说明是用邮箱登录的
		if (username.contains("@")) {
			sql = "select m.*,l.name as lvname from " + "es_member m left join " + "es_member_lv"
					+ " l on m.lv_id = l.lv_id where m.email=?";
		}

		List<Member> list = this.daoSupport.queryForList(sql, Member.class, username);
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
		this.edit(member);
		ThreadContextHolder.getSession().setAttribute(UserConext.CURRENT_MEMBER_KEY, member);

		Seller storeMember = sellerManager.getSeller(member.getMember_id());
		ThreadContextHolder.getSession().setAttribute(ISellerManager.CURRENT_STORE_MEMBER_KEY, storeMember);

		// 如果登录的会员拥有店铺则在session 中存入店铺信息
		if (storeMember.getStore_id() != null && !storeMember.getIs_store().equals(0)) {
			ThreadContextHolder.getSession().setAttribute(IShopManager.CURRENT_STORE_KEY,
					shopManager.getShop(storeMember.getStore_id()));
		}
		// HttpCacheManager.sessionChange();
		//this.memberPluginBundle.onLogin(member, upLogintime);

		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.base.core.service.IMemberManager#loginWithCookie(java.lang.
	 * String, java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int loginWithCookie(String username, String password) {
		String sql = "select m.*,l.name as lvname from " + "es_member m left join " + "es_member_lv"
				+ " l on m.lv_id = l.lv_id where m.disabled!=1 and m.uname=? and password=?";
		// 用户名中包含@，说明是用邮箱登录的
		if (username.contains("@")) {
			sql = "select m.*,l.name as lvname from " + "es_member m left join " + "es_member_lv"
					+ " l on m.lv_id = l.lv_id where m.disabled!=1 and m.email=? and password=?";
		}
		List<Member> list = this.daoSupport.queryForList(sql, Member.class, username, password);
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
		this.edit(member);
		ThreadContextHolder.getSession().setAttribute(UserConext.CURRENT_MEMBER_KEY, member);

		Seller storeMember = sellerManager.getSeller(member.getMember_id());
		ThreadContextHolder.getSession().setAttribute(ISellerManager.CURRENT_STORE_MEMBER_KEY, storeMember);

		// 如果登录的会员拥有店铺则在session 中存入店铺信息
		if (storeMember.getStore_id() != null && !storeMember.getIs_store().equals(0)) {
			ThreadContextHolder.getSession().setAttribute(IShopManager.CURRENT_STORE_KEY,
					shopManager.getShop(storeMember.getStore_id()));
		}
		// this.memberPluginBundle.onLogin(member, upLogintime);

		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.base.core.service.IMemberManager#loginbysys(java.lang.String)
	 */
	@Override
	public int loginbysys(String username) {

		if (UserConext.getCurrentAdminUser() == null) {
			throw new RuntimeException("您无权进行此操作，或者您的登录已经超时");
		}

		String sql = "select m.*,l.name as lvname from " + "es_member m left join " + "es_member_lv"
				+ " l on m.lv_id = l.lv_id where m.disabled!=1 and m.uname=?";
		List<Member> list = this.daoSupport.queryForList(sql, Member.class, username);
		if (list == null || list.isEmpty()) {
			return 0;
		}

		Member member = list.get(0);
		ThreadContextHolder.getSession().setAttribute(UserConext.CURRENT_MEMBER_KEY, member);
		// HttpCacheManager.sessionChange();
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.base.core.service.IMemberManager#addMoney(java.lang.Integer,
	 * java.lang.Double)
	 */
	@Override
	public void addMoney(Integer memberid, Double num) {
		String sql = "update es_member set advance=advance+? where member_id=?";
		this.daoSupport.execute(sql, num, memberid);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.base.core.service.IMemberManager#cutMoney(java.lang.Integer,
	 * java.lang.Double)
	 */
	@Override
	public void cutMoney(Integer memberid, Double num) {
		Member member = this.get(memberid);
		if (member.getAdvance() < num) {
			throw new RuntimeException("预存款不足:需要[" + num + "],剩余[" + member.getAdvance() + "]");
		}
		String sql = "update es_member set advance=advance-? where member_id=?";
		this.daoSupport.execute(sql, num, memberid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.base.core.service.IMemberManager#searchMember(java.util.Map,
	 * java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String)
	 */
	@Override
	public Page searchMember(Map memberMap, Integer page, Integer pageSize, String other, String order) {
		String sql = createTemlSql(memberMap);
		if (other != null && order != null) {
			sql += " order by " + other + " " + order;
		} else {
			sql += " order by m.member_id desc";
		}
		Page webpage = this.daoSupport.queryForPage(sql, page, pageSize);

		return webpage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.base.core.service.IMemberManager#searchMemberNoShop(java.util
	 * .Map, java.lang.Integer, java.lang.Integer, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public Page searchMemberNoShop(Map memberMap, Integer page, Integer pageSize, String other, String order) {
		String sql = createTemlSqlNoShop(memberMap);
		if (other != null && order != null) {
			sql += " order by " + other + " " + order;
		}
		// System.out.println(sql);
		Page webpage = this.daoSupport.queryForPage(sql, page, pageSize);

		return webpage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.base.core.service.IMemberManager#search(java.util.Map)
	 */
	@Override
	public List<Member> search(Map memberMap) {
		String sql = createTemlSql(memberMap);
		return this.daoSupport.queryForList(sql, Member.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.base.core.service.IMemberManager#updateLv(int, int)
	 */
	@Override
	public void updateLv(int memberid, int lvid) {
		String sql = "update es_member set lv_id=? where member_id=?";
		this.daoSupport.execute(sql, lvid, memberid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.base.core.service.IMemberManager#getMemberByMobile(java.lang.
	 * String)
	 */
	@Override
	public Member getMemberByMobile(String mobile) {
		String sql = "select * from es_member where mobile=? and disabled!=1";
		List list = this.daoSupport.queryForList(sql, Member.class, mobile);
		Member m = null;
		if (list != null && list.size() > 0) {
			m = (Member) list.get(0);
		}
		return m;
	}

	/**
	 * 
	 * @param memberMap
	 * @return
	 */
	private String createTemlSql(Map memberMap) {

		Integer stype = (Integer) memberMap.get("stype");
		String keyword = (String) memberMap.get("keyword");
		String uname = (String) memberMap.get("uname");
		String mobile = (String) memberMap.get("mobile");
		Integer lv_id = (Integer) memberMap.get("lvId");
		String email = (String) memberMap.get("email");
		String start_time = (String) memberMap.get("start_time");
		String end_time = (String) memberMap.get("end_time");
		Integer sex = (Integer) memberMap.get("sex");

		Integer province_id = (Integer) memberMap.get("province_id");
		Integer city_id = (Integer) memberMap.get("city_id");
		Integer region_id = (Integer) memberMap.get("region_id");

		String sql = "select m.*,lv.name as lv_name from " + "es_member m left join " + "es_member_lv"
				+ " lv on m.lv_id = lv.lv_id where 1=1 and m.disabled!='1' ";

		if (stype != null && keyword != null) {
			if (stype == 0) {
				sql += " and (m.uname like '%" + keyword + "%'";
				sql += " or m.mobile like '%" + keyword + "%'";
				sql += " or m.name like '%" + keyword + "%')";
			}
		}

		if (lv_id != null && lv_id != 0) {
			sql += " and m.lv_id=" + lv_id;
		}

		if (uname != null && !uname.equals("")) {
			// sql += " and m.name like '%" + uname + "%'";
			sql += " and m.uname like '%" + uname + "%'";
		}
		if (mobile != null && !mobile.equals("")) {
			sql += " and m.mobile like '%" + mobile + "%'";
		}

		if (email != null && !StringUtil.isEmpty(email)) {
			sql += " and m.email = '" + email + "'";
		}

		if (sex != null && sex != 2) {
			sql += " and m.sex = " + sex;
		}

		if (start_time != null && !StringUtil.isEmpty(start_time)) {
			long stime = DateUtil.getDateline(start_time + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
			sql += " and m.regtime>" + stime;
		}
		if (end_time != null && !StringUtil.isEmpty(end_time)) {
			long etime = DateUtil.getDateline(end_time + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
			sql += " and m.regtime<" + etime;
		}
		if (province_id != null && province_id != 0) {
			sql += " and province_id=" + province_id;
		}
		if (city_id != null && city_id != 0) {
			sql += " and city_id=" + city_id;
		}
		if (region_id != null && region_id != 0) {
			sql += " and region_id=" + region_id;
		}
		return sql;
	}

	/**
	 * 
	 * @param memberMap
	 * @return
	 */
	private String createTemlSqlNoShop(Map memberMap) {

		Integer stype = (Integer) memberMap.get("stype");
		String keyword = (String) memberMap.get("keyword");
		String uname = (String) memberMap.get("uname");
		String mobile = (String) memberMap.get("mobile");
		Integer lv_id = (Integer) memberMap.get("lvId");
		String email = (String) memberMap.get("email");
		String start_time = (String) memberMap.get("start_time");
		String end_time = (String) memberMap.get("end_time");
		Integer sex = (Integer) memberMap.get("sex");

		Integer province_id = (Integer) memberMap.get("province_id");
		Integer city_id = (Integer) memberMap.get("city_id");
		Integer region_id = (Integer) memberMap.get("region_id");

		String sql = "select m.*,lv.name as lv_name from " + "es_member m left join " + "es_member_lv"
				+ " lv on m.lv_id = lv.lv_id where 1=1 and m.disabled!='1' ";

		if (stype != null && keyword != null) {
			if (stype == 0) {
				sql += " and (m.uname like '%" + keyword + "%'";
				sql += " or m.name like '%" + keyword + "%'";
				sql += " or m.mobile like '%" + keyword + "%')";
			}
		}

		if (lv_id != null && lv_id != 0) {
			sql += " and m.lv_id=" + lv_id;
		}

		if (uname != null && !uname.equals("")) {
			sql += " and m.name like '%" + uname + "%'";
			sql += " or m.uname like '%" + uname + "%'";
		}
		if (mobile != null) {
			sql += " and m.mobile like '%" + mobile + "%'";
		}

		if (email != null && !StringUtil.isEmpty(email)) {
			sql += " and m.email = '" + email + "'";
		}

		if (sex != null && sex != 2) {
			sql += " and m.sex = " + sex;
		}

		if (start_time != null && !StringUtil.isEmpty(start_time)) {
			long stime = DateUtil.getDateline(start_time + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
			sql += " and m.regtime>" + stime;
		}
		if (end_time != null && !StringUtil.isEmpty(end_time)) {
			long etime = DateUtil.getDateline(end_time + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
			sql += " and m.regtime<" + etime;
		}
		if (province_id != null && province_id != 0) {
			sql += " and province_id=" + province_id;
		}
		if (city_id != null && city_id != 0) {
			sql += " and city_id=" + city_id;
		}
		if (region_id != null && region_id != 0) {
			sql += " and region_id=" + region_id;
		}
		sql += " and m.member_id NOT IN(select member_id from es_shop s)";
		return sql;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.core.member.service.IMemberManager#getDisabled(java.lang.Integer)
	 */
	@Override
	public Member getDisabled(Integer member_id) {
		String sql = "select m.*,l.name as lvname from " + "es_member" + " m left join " + "es_member_lv"
				+ " l on m.lv_id = l.lv_id where member_id=?";
		Member m = this.daoSupport.queryForObject(sql, Member.class, member_id);
		return m;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.core.member.service.IMemberManager#checkemailInEdit(java.lang.String, java.lang.Integer)
	 */
	@Override
	public boolean checkemailInEdit(String email, Integer member_id) {
		// TODO Auto-generated method stub
		String sql = "select * from es_member m where m.email=? and m.member_id!=? and m.disabled!=1";
		List list = this.daoSupport.queryForList(sql, email, member_id);
		return list.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.core.member.service.IMemberManager#getByGoodsId(int)
	 */
	@Override
	public Member getByGoodsId(int goodsId) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from es_member m ").append("left join es_goods g on m.store_id = g.store_id ")
				.append("where g.goods_id = ?");
		return (Member) this.daoSupport.queryForObject(sql.toString(), Member.class, goodsId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.base.core.service.IMemberManager#changeMobile(java.lang.
	 * Integer, java.lang.String)
	 */
	@Override
	public void changeMobile(Integer member_id, String mobile) {
		String sql = "update es_member set mobile = ? where member_id = ?";
		this.daoSupport.execute(sql, mobile, member_id);
		ThreadContextHolder.getSession().setAttribute(UserConext.CURRENT_MEMBER_KEY, this.get(member_id));

		// 修改手机验证成功后，删除session中的加密信息
		ThreadContextHolder.getSession().removeAttribute("account_detail");
	}

	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.core.member.service.IMemberManager#searchMemberRecycle(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Page searchMemberRecycle(Integer page, Integer pageSize) {
		String sql = "select m.*,lv.name as lv_name from " + "es_member m left join " + "es_member_lv"
				+ " lv on m.lv_id = lv.lv_id where disabled=1 order by member_id desc";
		Page webpage = this.daoSupport.queryForPage(sql, page, pageSize);
		return webpage;
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.core.member.service.IMemberManager#regain(java.lang.Integer[])
	 */
	@Override
	@Log(type = LogType.MEMBER, detail = "恢复会员信息")
	public void regain(Integer[] member_id) {
		String str = "";
		for (int i = 0; i < member_id.length; i++) {
			str += "?,";
		}
		str = str.substring(0, str.length() - 1);
		String sql = "  update es_member set disabled=0 where member_id in (" + str + ")";
		this.daoSupport.execute(sql, member_id);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.core.member.service.IMemberManager#getMemberByMemberId(java.lang.Integer)
	 */
	@Override
	public Member getMemberByMemberId(Integer member_id) {
		String sql = "select * from es_member where member_id=?";
		Member member = this.daoSupport.queryForObject(sql, Member.class, member_id);
		return member;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.core.member.service.IMemberManager#checkMobileExceptSelf(java.lang.String, java.lang.Integer)
	 */
	@Override
	public int checkMobileExceptSelf(String mobile, Integer member_id) {
		String sql = "select count(0) from es_member where mobile=? AND disabled!=1 AND member_id!=?";
		int count = this.daoSupport.queryForInt(sql, mobile, member_id);
		count = count > 0 ? 1 : 0;
		return count;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.core.member.service.IMemberManager#addPoint(int, int)
	 */
	@Override
	public void addPoint(int memberid, int value) {
		Member member = this.get(memberid);
		member.setPoint(member.getPoint() + value);
		member.setMp(member.getMp() + value);
		this.daoSupport.update("es_member", member, " member_id = " + member.getMember_id());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.base.core.service.IMemberManager#editMemberImg(java.lang.
	 * Integer, java.lang.String)
	 */
	@Override
	public void editMemberImg(Integer member_id, String img) {
		String sql = "update es_member set face=? where member_id=?";
		this.daoSupport.execute(sql, img, member_id);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.core.member.service.IMemberManager#getCurrentMember()
	 */
	@Override
	public MemberLoginVo getCurrentMember() {
		MemberLoginVo member = new MemberLoginVo();
		member.setMember_id(26);
		member.setUsername("模拟会员");
		return member;
	}

	
	/**
	 * 新迁移的功能
	 */

	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.core.member.service.IMemberManager#checkCode(java.lang.String)
	 */
	@Override
	public boolean checkCode(String code) {
		if (code == null) {
			return false;
		}
		String validcode = (String) ThreadContextHolder.getSession().getAttribute(ValidCodeServlet.SESSION_VALID_CODE + "memberlogin");
		if (validcode == null) {
			return false;
		} else {
			if (!code.equalsIgnoreCase(validcode)) {
				return false;
			}
		}
		return true;
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.core.member.service.IMemberManager#staticPwdLogin(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean staticPwdLogin(String username, String password) {
		String sql = "select m.*,l.name as lvname from " + "es_member m left join " + "es_member_lv"
				+ " l on m.lv_id = l.lv_id where m.disabled!=1 and m.uname=? and password=?";
		/** 用户名中包含@，说明是用邮箱登录的 */
		if (username.contains("@")) {
			sql = "select m.*,l.name as lvname from " + "es_member m left join " + "es_member_lv"
					+ " l on m.lv_id = l.lv_id where m.disabled!=1 and m.email=? and password=?";
		}
		String pwdmd5 = com.enation.framework.util.StringUtil.md5(password);
		List<Member> list = this.daoSupport.queryForList(sql, Member.class, username, pwdmd5);
		if (list == null || list.isEmpty()) {
			/** 如果没有查到用户 可能是使用手机号码＋密码登录 */
			sql = "select m.*,l.name as lvname from " + "es_member m left join " + "es_member_lv"
					+ " l on m.lv_id = l.lv_id where m.disabled!=1 and m.mobile=? and password=?";
			list = this.daoSupport.queryForList(sql, Member.class, username, pwdmd5);
			if (list == null || list.isEmpty()) {
				return false;
			}
		}
		
		MemberLoginMsg loginMsg = new MemberLoginMsg();
		
		Member member = list.get(0);
		Long lastlogin = member.getLastlogin();
		loginMsg.setLast_login_time(lastlogin);
		
		long ldate = lastlogin * 1000;
		Date date = new Date(ldate);
		Date today = new Date();
		int logincount = member.getLogincount();
		if (DateUtil.toString(date, "yyyy-MM").equals(DateUtil.toString(today, "yyyy-MM"))) {// 与上次登录在同一月内
			logincount++;
		} else {
			logincount = 1;
		}
		member.setLastlogin(DateUtil.getDateline());
		member.setLogincount(logincount);
		this.edit(member);
		this.setStoreSession(member);
		//发送会员登陆消息
		loginMsg.setMember_id(member.getMember_id());
		this.amqpTemplate.convertAndSend(AmqpExchange.MEMEBER_LOGIN.name(), "member-login-routingkey",loginMsg);
		
		return true;
	}


	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.core.member.service.IMemberNewManager#checkSms(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean checkSms(String mobile, String code,String key) {
		/** 如果手机号格式不对 */
		if (!Validator.isMobile(mobile) ) {
			throw new RuntimeException("手机号码格式错误");
		}
		/** 防止空值 */
		if (key == null || "".equals(key)) {
			/** 默认为登录 */
			key = MemberSendCodeEnum.LOGINMOBILE.name();
		}
		/** 如果验证码为空 */
		if (code == null || "".equals(code)) {
			return false;
		}
		String sessionCode = (String) ThreadContextHolder.getSession().getAttribute(SMS_CODE_PREFIX +key+ mobile);
		/** 验证码为空 */
		if (sessionCode == null) {
			return false;
		}else {
			/** 忽略大小写,判断不正确 */
			if (!code.equalsIgnoreCase(sessionCode)) {
				return false;
			}
		}
		/** 验证短信是否超时 */
		Long sendtime = (Long) ThreadContextHolder.getSession().getAttribute(SENDTIME_PREFIX +key+ mobile);
		Long checktime = DateUtil.getDateline();
		/** 验证session但中是否存在当前注册用户的验证码 */
		if(sendtime==null){
			return false;
		};
		if((checktime-sendtime >= SMS_CODE_TIMEOUT)){
			throw new RuntimeException("验证码超时");
		}	
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.core.member.service.IMemberNewManager#dynamicLogin(java.lang.String)
	 */
	@Override
	public boolean dynamicLogin(String phone) {
		String sql = "select m.*,l.name as lvname from " + "es_member m left join " + "es_member_lv"
				+ " l on m.lv_id = l.lv_id where m.disabled!=1 and m.mobile=? ";

		List<Member> list = this.daoSupport.queryForList(sql, Member.class, phone);
		if (list == null || list.isEmpty()) {
			return false;
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
		/** 修改店铺信息 */
		this.edit(member);
		/** session中存入店铺信息 */
		this.setStoreSession(member);
		//发送登陆消息
		MemberLoginMsg loginMsg = new MemberLoginMsg();
		loginMsg.setLast_login_time(upLogintime);
		loginMsg.setMember_id(member.getMember_id());
		this.amqpTemplate.convertAndSend(AmqpExchange.MEMEBER_LOGIN.name(), "member-login-routingkey",loginMsg);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.core.member.service.IMemberNewManager#getMemberByPhone(java.lang.String)
	 */
	@Override
	public Member getMemberByPhone(String mobile) {
		String sql = "select * from es_member where mobile=? and disabled != 1";
		List list = this.daoSupport.queryForList(sql, Member.class, mobile);
		Member m = null;
		if (list != null && list.size() > 0) {
			m = (Member) list.get(0);
		}
		return m;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.core.member.service.IMemberManager#validMobileIsRegister(java.lang.String)
	 */
	@Override
	public boolean validMobileIsRegister(String mobile) {

		/** 如果手机号格式不对 */
		if (!Validator.isMobile(mobile)) {
			throw new RuntimeException("手机号码格式错误");
		}
		boolean isExists = this.checkMobile(mobile);
		return isExists;
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.base.core.service.IMemberManager#register(com.enation.app.
	 * base.core.model.Member)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean register(Member member) {
		if(member.getUname() != null) {
			if(!this.checkUsername(member.getUname())) {
				throw new RuntimeException("用户名重复");
			}
		}
		if(member.getEmail() != null) {
			if(!this.checkEmail(member.getEmail())) {
				throw new RuntimeException("该邮箱已被注册");
			}
		}
		String sessionCode = (String) ThreadContextHolder.getSession().getAttribute(SMS_CODE_PREFIX +MemberSendCodeEnum.REGISTERMOBILE.name()+ member.getMobile());
		if(!StringUtil.isEmpty(sessionCode) && member.getMobile() != null) {
			if(!this.checkSms(member.getMobile(), sessionCode, MemberSendCodeEnum.REGISTERMOBILE.name())) {
				return false;
			}
		}
		int result = add(member);
		try {
			if (result == 1) {
				Map map = new HashMap();
				map.put("is_store", 0);
				daoSupport.update("es_member", map, "member_id=" + member.getMember_id());
				/** 登陆店铺会员 */
				ThreadContextHolder.getSession().setAttribute(ISellerManager.CURRENT_STORE_MEMBER_KEY,
						sellerManager.getSeller(member.getMember_id()));
				//发送会员注册消息
				MemberRegistVo vo = new MemberRegistVo();
				vo.setMember_id(member.getMember_id());
				vo.setSession_id(ThreadContextHolder.getSession().getId());
				this.amqpTemplate.convertAndSend(AmqpExchange.MEMEBER_REGISTER.name(), "member-register-routingkey",vo);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}



	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.core.member.service.IMemberManager#getContent(java.lang.String, java.lang.String, java.lang.Integer)
	 */
	@Override
	public Map<String, Object> sendSmsCode(String mobile, String key, Integer isCheckRegister) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		try {
			/* 防止空值 */
			if (key == null || "".equals(key)) {
				/* 默认为登录 */
				key = MemberSendCodeEnum.LOGINMOBILE.name();
			}
			/* 如果手机号格式不对 */
			if (!Validator.isMobile(mobile) ) {
				result.put("state_code", 2);
				result.put("msg", "手机号码格式错误");
				return result;
			}
			/* 随机生成的动态码*/
			String dynamicCode = "" + (int)((Math.random() * 9 + 1) * 100000);
			/* 如果是测试模式，验证码为1111 */
			if(SystemSetting.getTest_mode()==1){
				dynamicCode="1111";
			}
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("code",dynamicCode);
			data.put("mobile",mobile);
			/* 1如果是登录 */
			if (key.equals(MemberSendCodeEnum.LOGINMOBILE.name())){
				/* 校验手机是否注册过 */
				if (validMobileIsRegister(mobile)) {
					result.put("state_code", 2);
					result.put("msg", "当前手机号没有绑定相关帐号");
					return result;
				}
				data.put("byName","通过手机登陆");
			/* 2如果是注册 */
			} else if (key.equals(MemberSendCodeEnum.REGISTERMOBILE.name())) {
				/* 校验手机是否注册过 */
				if (!validMobileIsRegister(mobile)) {
					result.put("state_code", 2);
					result.put("msg", "当前输入手机号码已绑定有帐号，可直接登录");
					return result;
				}
				data.put("byName","通过手机注册");
			/* 3如果是找回密码 */
			} else if (key.equals(MemberSendCodeEnum.FINDPASSWORDMOBILE.name())) {
				/* 校验手机是否注册过  */
				if (validMobileIsRegister(mobile)) {
					result.put("state_code", 2);
					result.put("msg", "当前手机号没有绑定相关帐号");
					return result;
				}
				data.put("byName","通过手机修改密码");
			/* 4是绑定帐号 */
			} else if (key.equals(MemberSendCodeEnum.BINDINGMOBILE.name())) {
				/* 校验手机是否注册过 */
				if (!validMobileIsRegister(mobile)) {
					result.put("state_code", 2);
					result.put("msg", "当前输入手机号码已绑定有帐号，请解绑后再绑定");
					return result;
				}
				data.put("byName","通过手机绑定帐号");
			/* 5是修改密码 */
			} else if (key.equals(MemberSendCodeEnum.UPDATEPASSWORDMOBILE.name())) {
				/* 校验手机是否注册过 */
				if (validMobileIsRegister(mobile)) {
					result.put("state_code", 2);
					result.put("msg", "没有找到该手机号绑定的账户");
					return result;
				}
				data.put("byName","通过手机修改密码");
			/* 6.普通校验 */
			} else if (key.equals(MemberSendCodeEnum.CHECKMOBILE.name())) {
				/* 如果需要验证用户是否注册 */
				if (isCheckRegister == 1) {
					/* 校验手机是否注册过 */
					if (validMobileIsRegister(mobile)) {
						result.put("state_code", 2);
						result.put("msg", "没有找到该手机号绑定的账户");
						return result;
					}
				}
				data.put("byName","通过手机验证");
			}
			/* 发送手机短信 */
			this.amqpTemplate.convertAndSend(AmqpExchange.SMS_SEND_MESSAGE.name(), "smsSendMessageMsg",data);
			String ip = ThreadContextHolder.getHttpRequest().getServerName();
			logger.debug("已发送短信:验证码:" + dynamicCode + "手机号:" + mobile + ",ip:" + ip);
			System.out.println("已发送短信:验证码:" + dynamicCode + "手机号:" + mobile + ",ip:" + ip);
			result.put("state_code", 1);
			result.put("msg", "发送成功");
			HttpSession session = ThreadContextHolder.getSession();
			/* session中的格式是  前缀+key+手机号  例子:  es_sms_login_13123456789 */
			String codeSessionKey = SMS_CODE_PREFIX+key+mobile;
			session.setAttribute(codeSessionKey, dynamicCode);
			session.setAttribute(INTERVAL_TIME_PREFIX+key , DateUtil.getDateline());
			session.setAttribute(SENDTIME_PREFIX+key+mobile, DateUtil.getDateline());
		} catch(RuntimeException e) {
			TestUtil.print(e);
			result.put("state_code", 0);
			result.put("msg", "发送失败,短信系统出现异常");
		}
		return result;
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
