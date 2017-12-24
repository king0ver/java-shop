package com.enation.app.shop.statistics.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.enation.app.shop.aftersale.model.enums.RefundStatus;
import com.enation.app.shop.statistics.model.StatisticsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.statistics.model.DayAmount;
import com.enation.app.shop.statistics.model.MonthAmount;
import com.enation.app.shop.statistics.service.IB2b2cSalesStatisticsManager;
import com.enation.app.shop.trade.model.enums.OrderStatus;
import com.enation.app.shop.trade.model.enums.PayStatus;
import com.enation.app.shop.trade.model.vo.Order;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

@Service
public class B2b2cSalesStatisticsManager implements IB2b2cSalesStatisticsManager{
	@Autowired
	private IDaoSupport daoSupport;

	public List<MonthAmount> statisticsMonth_Amount() {
		SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy");
		String year = sdfInput.format(new Date());
		String sql = "";
		if(EopSetting.DBTYPE.equals("1")){//是mysql
			sql = "select sum(a.order_amount) as amount, Date_format(FROM_UNIXTIME(a.create_time / 1000),'%Y-%m') as mo from es_order a where Date_format(FROM_UNIXTIME(a.create_time / 1000),'%Y') = ?  group by mo";
		}else if(EopSetting.DBTYPE.equals("3")){//是mssql
			sql = "select sum(a.order_amount) as amount, substring(convert(varchar(10),dateadd(ss,create_time/1000 + 28800,'1970-01-01'),120),1,7) as mo from es_order a where substring(convert(varchar(10),dateadd(ss,create_time/1000 + 28800,'1970-01-01'),120),1,4) = ?  group by substring(convert(varchar(10),dateadd(ss,create_time/1000 + 28800,'1970-01-01'),120),1,7)";
		}else{//是oracle
			//注意：需先在oracle中建立function
			/*代码如下：
			create or replace function FROM_UNIXTIME(mydate IN number) return date is
  				Result date;
			begin
  				Result := TO_DATE('01011970','mmddyyyy')+1/24/60/60*(MYDATE);
  				return(Result);
			end FROM_UNIXTIME;
			 */
			//			String createfunction = "create or replace function FROM_UNIXTIME(mydate IN number) return date is"
			//  				 +" Result date;"
			//  				 +" begin"
			//  				 +" Result := TO_DATE('01011970','mmddyyyy')+1/24/60/60*(MYDATE);"
			//  				 +" return(Result);"
			//  				 +" end FROM_UNIXTIME;";
			//			this.daoSupport.execute(createfunction);
			sql = "select sum(a.order_amount) as amount, to_char(TO_DATE('01011970','mmddyyyy')+1/24/60/60*(a.create_time / 1000),'yyyy-mm') as mo from es_order a where to_char(TO_DATE('01011970','mmddyyyy')+1/24/60/60*(a.create_time / 1000),'yyyy') = ?  group by to_char(TO_DATE('01011970','mmddyyyy')+1/24/60/60*(a.create_time / 1000),'yyyy-mm')";
		}
		List<Map> list = this.daoSupport.queryForList(sql, year);
		List<MonthAmount> target = new ArrayList<MonthAmount>();
		List<String> monthList = getMonthList();
		for(String month:monthList){
			MonthAmount ma = new MonthAmount();
			ma.setMonth(month);
			ma.setAmount(new Double(0));
			for(Map mapdata:list){
				if(mapdata.get("mo").equals(month)){
					ma.setAmount(Double.valueOf(mapdata.get("amount").toString()));
				}
			}
			target.add(ma);
		}
		return target;
	}


	public List<MonthAmount> statisticsMonth_Amount(String monthinput) {
		String year = monthinput.substring(0,4);
		String sql = "";
		if("1".equals(EopSetting.DBTYPE)){//是mysql
			sql = "select sum(a.order_amount) as amount, Date_format(FROM_UNIXTIME(a.create_time / 1000),'%Y-%m') as mo from es_order a where a.status = '" + OrderStatus.COMPLETE + "' and Date_format(FROM_UNIXTIME(a.create_time / 1000),'%Y') = ?  group by mo";
		}else if("2".equals(EopSetting.DBTYPE)){//是oracle
			sql = "select sum(a.order_amount) as amount, to_char(TO_DATE('01011970','mmddyyyy')+1/24/60/60*(a.create_time / 1000),'yyyy-mm') as mo from es_order a where a.status = '" + OrderStatus.COMPLETE + "' and to_char(TO_DATE('01011970','mmddyyyy')+1/24/60/60*(a.create_time / 1000),'yyyy') = ?  group by to_char(TO_DATE('01011970','mmddyyyy')+1/24/60/60*(a.create_time / 1000),'yyyy-mm')";
		}else if("3".equals(EopSetting.DBTYPE)){//SQLServer
			sql = "select sum(order_amount) as amount, substring(convert(varchar(10),dateadd(ss,create_time/1000 + 28800,'1970-01-01'),120),1,7) as mo from es_order where status = '" + OrderStatus.COMPLETE + "' and substring(convert(varchar(10),dateadd(ss,create_time/1000 + 28800,'1970-01-01'),120),1,7) = ? group by substring(convert(varchar(10),dateadd(ss,create_time/1000 + 28800,'1970-01-01'),120),1,7)";
		}
		List<Map> list = this.daoSupport.queryForList(sql, year);
		List<MonthAmount> target = new ArrayList<MonthAmount>();
		List<String> monthList = getMonthList(monthinput);
		for(String month:monthList){
			MonthAmount ma = new MonthAmount();
			ma.setMonth(month);
			ma.setAmount(new Double(0));
			for(Map mapdata:list){
				if(mapdata.get("mo").equals(month)){
					ma.setAmount(Double.valueOf(mapdata.get("amount").toString()));
				}
			}
			target.add(ma);
		}
		return target;
	}


	public List<DayAmount> statisticsDay_Amount() {
		SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM");
		String year = sdfInput.format(new Date());
		String sql = "";
		if(EopSetting.DBTYPE.equals("1")){//是mysql
			sql = "select sum(a.order_amount) as amount, Date_format(FROM_UNIXTIME(a.create_time / 1000),'%Y-%m-%d') as mo from es_order a where a.status = '" + OrderStatus.COMPLETE + "' and Date_format(FROM_UNIXTIME(a.create_time / 1000),'%Y-%m') = ?  group by mo";
		}else if(EopSetting.DBTYPE.equals("2")){
			sql = "select sum(a.order_amount) as amount, to_char(TO_DATE('01011970','mmddyyyy')+1/24/60/60*(a.create_time / 1000),'yyyy-mm-dd') as mo from es_order a where a.status = '" + OrderStatus.COMPLETE + "' and to_char(TO_DATE('01011970','mmddyyyy')+1/24/60/60*(a.create_time / 1000),'yyyy-mm') = ?  group by to_char(TO_DATE('01011970','mmddyyyy')+1/24/60/60*(a.create_time / 1000),'yyyy-mm-dd')";
		}else{
			sql = "select sum(a.order_amount) as amount, substring(convert(varchar(10),dateadd(ss,create_time/1000 + 28800,'1970-01-01'),120),1,10) as mo from es_order a where a.status = '" + OrderStatus.COMPLETE + "' and substring(convert(varchar(10),dateadd(ss,create_time/1000 + 28800,'1970-01-01'),120),1,7) = ?  group by substring(convert(varchar(10),dateadd(ss,create_time/1000 + 28800,'1970-01-01'),120),1,10)";
		}
		List<Map> list = this.daoSupport.queryForList(sql, year);
		List<DayAmount> target = new ArrayList<DayAmount>();
		List<String> dayList = getDayList();
		for(String day:dayList){
			DayAmount da = new DayAmount();
			da.setDay(day);
			da.setAmount(new Double(0));
			for(Map mapdata:list){
				if(mapdata.get("mo").equals(day)){
					da.setAmount(Double.valueOf(mapdata.get("amount").toString()));
				}
			}
			target.add(da);
		}
		return target;
	}


	public List<DayAmount> statisticsDay_Amount(String monthinput) {
		String sql = "";
		if("1".equals(EopSetting.DBTYPE)){//是mysql
			sql = "select sum(a.order_amount) as amount, Date_format(FROM_UNIXTIME(a.create_time / 1000),'%Y-%m-%d') as mo from es_order a where a.status = '" + OrderStatus.COMPLETE + "' and Date_format(FROM_UNIXTIME(a.create_time / 1000),'%Y-%m') = ?   group by mo";
		}else if("2".equals(EopSetting.DBTYPE)){//Oracle
			sql = "select sum(a.order_amount) as amount, to_char(TO_DATE('01011970','mmddyyyy')+1/24/60/60*(a.create_time / 1000),'yyyy-mm-dd') as mo from es_order  a where a.status = '" + OrderStatus.COMPLETE + "' and to_char(TO_DATE('01011970','mmddyyyy')+1/24/60/60*(a.create_time / 1000),'yyyy-mm') = ?   group by to_char(TO_DATE('01011970','mmddyyyy')+1/24/60/60*(a.create_time / 1000),'yyyy-mm-dd')";
		}else if("3".equals(EopSetting.DBTYPE)){//SQLServer
			sql = "select sum(order_amount) as amount, substring(convert(varchar(10),dateadd(ss,create_time/1000 + 28800,'1970-01-01'),120),1,10) as mo from es_order a where status = '" + OrderStatus.COMPLETE + "' and substring(convert(varchar(10),dateadd(ss,create_time/1000 + 28800,'1970-01-01'),120),1,7) = ? group by substring(convert(varchar(10),dateadd(ss,create_time/1000 + 28800,'1970-01-01'),120),1,10)";
		}
		List<Map> list = this.daoSupport.queryForList(sql, monthinput);
		List<DayAmount> target = new ArrayList<DayAmount>();
		List<String> dayList = getDayList(monthinput);
		for(String day:dayList){
			DayAmount da = new DayAmount();
			da.setDay(day);
			da.setAmount(new Double(0));
			for(Map mapdata:list){
				if(mapdata.get("mo").equals(day)){
					da.setAmount(Double.valueOf(mapdata.get("amount").toString()));
				}
			}
			target.add(da);
		}
		return target;
	}


	private static List<String> getMonthList(){
		List<String> monthList = new ArrayList<String>();
		SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy");
		String year = sdfInput.format(new Date());
		DecimalFormat df = new DecimalFormat("00");
		for(int i=1;i<=12;i++){
			monthList.add(year + "-" + df.format(i));
		}
		return monthList;
	}

	private static List<String> getMonthList(String monthinput){
		List<String> monthList = new ArrayList<String>();
		String year = monthinput.substring(0,4);
		DecimalFormat df = new DecimalFormat("00");
		for(int i=1;i<=12;i++){
			monthList.add(year + "-" + df.format(i));
		}
		return monthList;
	}

	private static List<String> getDayList(){
		List<String> dayList = new ArrayList<String>();
		Date date = new Date();
		Calendar cal =Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM");
		String str_month = sdfInput.format(date);
		DecimalFormat df = new DecimalFormat("00");
		int count = days(year, month);
		for(int i=1;i<=count;i++){
			dayList.add(str_month + "-" + df.format(i));
		}
		return dayList;
	}

	private static List<String> getDayList(String monthinput){
		List<String> dayList = new ArrayList<String>();

		Date date =DateUtil.toDate(monthinput+"-01", "yyyy-MM-dd");// new Date(monthinput + "-01");

		Calendar cal =Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		String str_month = monthinput;
		DecimalFormat df = new DecimalFormat("00");
		int count = days(year, month);
		for(int i=1;i<=count;i++){
			dayList.add(str_month + "-" + df.format(i));
		}
		return dayList;
	}

	public static int days(int year,int month){
		int days = 0;
		if(month!=2){
			switch(month){
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:days = 31 ;break;
			case 4:
			case 6:
			case 9:
			case 11:days = 30;

			}
		}else{
			if(year%4==0 && year%100!=0 || year%400==0)
				days = 29;
			else  
				days = 28;
		}
		return days;
	}


	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.core.service.ISalesStatisticsManager#orderStatByPayment()
	 */
	public List<Map> orderStatByPayment(){
		String sql ="select count(0) num,sum(order_amount) amount,max(payment_name) payment_name from es_order where disabled=0 group by shipping_id";
		return this.daoSupport.queryForList(sql);
	}



	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.core.service.ISalesStatisticsManager#orderStatByShip()
	 */
	public List<Map> orderStatByShip(){
		String sql ="select count(0) num,sum(order_amount) amount,max(shipping_type) shipping_type from es_order where disabled=0 group by shipping_id";
		return this.daoSupport.queryForList(sql);
	}

	/**
	 * 订单统计   下单量 按年的统计
	 * @author LSJ
	 * @param year 年
	 * @param store_id 店铺ID
	 * @return 下单量 list
	 * 2016年12月6日下午15:16
	 */
	@Override
	public List<Map> statisticsYear_Amount(String status, int year,String store_id) {
		String condition_sql = createSqlByYear(1, year+"");
		String sql =  "select count(0) as t_num,SUM(need_pay_money) as t_money, case "+ condition_sql +" as month  from es_order o where 1=1 ";
		String storeWhere="";
		if(store_id != null && !"0".equals(store_id)){
			storeWhere = " AND o.seller_id = "+store_id;
		}
		if( status!=null ){
			sql += " and o.order_status="+status;
		}
		sql += storeWhere;

		sql +=  " group by case "+condition_sql;
		List list = this.daoSupport.queryForList(sql);
		return list;
	}

	/**
	 * 订单统计   下单量 按月的统计
	 * @author LSJ
	 * @param year 年
	 * @param month 月
	 * @param store_id 店铺ID
	 * @return 下单量list
	 * 2016年12月6日下午15:16
	 */
	@Override
	public List<Map> statisticsMonth_Amount(String status, int year,int month,String store_id) {
		String condition_sql = createSql(1, year,month);
		String sql =  "select count(0) as t_num,SUM(need_pay_money) as t_money, case "+ condition_sql +" as month  from es_order o where 1=1";
		String storeWhere="";
		if(store_id != null && !"0".equals(store_id)){
			storeWhere = " AND o.seller_id = "+store_id;
		}
		if( !status.equals(null) && !status.equals("99") ){
			sql += " and o.order_status= '"+status+"'";
		}
		sql += storeWhere;
		sql+= " group by case "+condition_sql;
		List list = this.daoSupport.queryForList(sql);
		return list;
	}

	/**
	 * 热卖商品排行—下单金额
	 * @author LSJ
	 * @return 热卖商品排行—下单金额list
	 * 2016年12月6日下午15:16
	 */
	@Override
	public List<Map> hotGoodsTop_Money() {
		StringBuffer sql = new StringBuffer();
		sql.append("select sum(i.price*i.num) as t_price,i.`name`,c.`name` from es_order_items i left join es_order o on i.order_id=o.order_id left join es_goods_category c on c.category_id = i.cat_id ");
		sql.append("");
		sql.append(" GROUP BY i.goods_id  ORDER BY t_price DESC LIMIT 0,50");
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.core.service.IStatisticsManager#hotGoodsTop_Num()
	 */
	@Override
	public List<Map> hotGoodsTop_Num() {
		return null;
	}



	/**
	 * 创建SQL语句
	 * @param type 1.按照月查询(查询出此月每天的下单金额)
	 */
	public static String createSql(int type,int year,int month){
		StringBuffer sql =new StringBuffer();
		String date = year+"-"+month;
		int day = getDaysByYearMonth(year, month);
		for(int i=1;i<=day;i++){
			String day_date= date+"-"+i;
			long start = DateUtil.getDateline(  day_date+" 00:00:00", "yyyy-MM-dd HH:mm:ss");
			long end = DateUtil.getDateline(  day_date+" 23:59:59", "yyyy-MM-dd HH:mm:ss");
			sql.append(" when create_time >= "+start +" and   create_time <="+ end +" then "+i );
		}

		sql.append(" else 0 end");
		return sql.toString();
	}

	/**
	 * 创建SQL语句
	 * @param type 1.按照年查询(查询出此年每月的下单金额)
	 * @param date
	 */
	public static String createSqlByYear(int type,String date){
		StringBuffer sql =new StringBuffer();
		for(int i=1;i<=12;i++){
			String day = "0"+i;
			day = day.substring( day.length()-2,day.length());
			String day_date = date+"-"+day;
			long start = DateUtil.getDateline(day_date+"-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
			long end = DateUtil.getDateline(day_date+"-31 23:59:59", "yyyy-MM-dd HH:mm:ss");
			sql.append(" when create_time >= "+start +" and  create_time <="+ end +" then "+i );
		}
		sql.append(" else 0 end");
		return sql.toString();
	}

	/**
	 * 销售收入统计json数据
	 * @author LSJ
	 * @param year 年
	 * @param month 月
	 * @param page 当前页数
	 * @param pageSize 分页大小
	 * @param map
	 * @param store_id 店铺ID
	 * @return 销售收入统计json数据分页
	 * 2016年12月6日下午15:16
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Page getSalesIncome(int year, int month, int page, int pageSize,
			Map map,String store_id) {

		String  date = year+"-"+month;
		long start = DateUtil.getDateline(date+"-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
		long end = DateUtil.getDateline(date+"-31 23:59:59", "yyyy-MM-dd HH:mm:ss");

		String storeWhere = "";
		if(store_id != null && !"0".equals(store_id)){
			storeWhere =" AND o.seller_id ="+store_id;
		}

		String sql = "select oi.goods_id,oi.name,oi.price,SUM(oi.num) t_num,SUM(oi.num*oi.price) t_price from es_order_items oi "
				+ " left join es_order o on oi.order_sn=o.sn "
				+ " where o.create_time >=? and  o.create_time <=? and pay_status = '"+PayStatus.PAY_YES+"' "
				+storeWhere
				+ " group by oi.goods_id,oi.name,oi.price ";

		String db_type = EopSetting.DBTYPE;
		if(db_type.equals("3")){
			sql += " order by oi.goods_id desc";
		}
		List list = this.daoSupport.queryForListPage(sql, page, pageSize, start, end );

		Page salesPage= new Page(0, daoSupport.queryForList(sql, start, end).size(), pageSize, list);

		return salesPage;
	}


	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.statistics.service.IB2b2cSalesStatisticsManager#getReceivables(int, int, java.util.Map, java.lang.String)
	 */
	@Override
	public Double getReceivables(StatisticsQueryParam statisticsQueryParam) {

		String  date = statisticsQueryParam.getYear()+"-"+statisticsQueryParam.getMonth();
		long start = DateUtil.getDateline(date+"-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
		long end = DateUtil.getDateline(date+"-31 23:59:59", "yyyy-MM-dd HH:mm:ss");

		String storeWhere="";
		if(statisticsQueryParam.getSeller_id() != null && !"0".equals(statisticsQueryParam.getSeller_id())){
			storeWhere = " AND o.seller_id = " + statisticsQueryParam.getSeller_id();
		}
		String sql = "select SUM(o.paymoney) as receivables from es_order o where create_time >=? and  create_time <=? and pay_status = '"+PayStatus.PAY_YES+"'";
		sql+=storeWhere;
		//String sql = "select SUM(pl.money) as receivables from es_payment_logs pl where pl.pay_date >=? and pl.pay_date <=?";
		Map map = this.daoSupport.queryForMap(sql, start,end);
		Double receivables = 0.0;
		BigDecimal b = new BigDecimal(receivables);
		receivables =  b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		if(map!=null){
//			receivables = Double.parseDouble(map.get("receivables").toString());
			receivables = StringUtil.toDouble(map.get("receivables"), false);
		}
		return receivables;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.statistics.service.IB2b2cSalesStatisticsManager#getRefund(int, int, java.util.Map, java.lang.String)
	 */
	@Override
	public Double getRefund(StatisticsQueryParam statisticsQueryParam) {

		String  date = statisticsQueryParam.getYear()+"-"+statisticsQueryParam.getMonth();
		long start = DateUtil.getDateline(date+"-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
		long end = DateUtil.getDateline(date+"-31 23:59:59", "yyyy-MM-dd HH:mm:ss");
		String storeWhere="";
		if(statisticsQueryParam.getSeller_id() != null && !"0".equals(statisticsQueryParam.getSeller_id())){
			storeWhere= " AND rf.seller_id = "+statisticsQueryParam.getSeller_id();
		}
		String sql = "select SUM(rf.refund_price) as refund from es_refund rf   where rf.refund_status = ? and rf.create_time >=? and rf.create_time <=?";
		sql+=storeWhere;
		Map map = this.daoSupport.queryForMap(sql, RefundStatus.completed.value(),start,end);
		Double refund = 0.0;
		BigDecimal b = new BigDecimal(refund);
		refund =  b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		if(map!=null){
			refund = StringUtil.toDouble(map.get("refund"), false);
		}
		return refund;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.statistics.service.IB2b2cSalesStatisticsManager#getPaid(int, int, java.util.Map, java.lang.String)
	 */
	@Override
	public Double getPaid(StatisticsQueryParam statisticsQueryParam) {
		String  date = statisticsQueryParam.getYear()+"-"+statisticsQueryParam.getMonth();
		long start = DateUtil.getDateline(date+"-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
		long end = DateUtil.getDateline(date+"-31 23:59:59", "yyyy-MM-dd HH:mm:ss");

		String storeWhere="";
		if(statisticsQueryParam.getSeller_id() != null && !"0".equals(statisticsQueryParam.getSeller_id())){
			storeWhere = " AND o.seller_id = " + statisticsQueryParam.getSeller_id();
		}

		String sql = "select SUM(o.need_pay_money) as paid from es_order o where create_time >=? and  create_time <=? "
				+ " and pay_status= '"+PayStatus.PAY_YES
				+ "' and order_status!= '"+OrderStatus.CANCELLED
				+ "' and order_status!= '"+OrderStatus.AFTE_SERVICE +"' ";
		sql+=storeWhere;
		Map map = this.daoSupport.queryForMap(sql, start,end);
		Double paid = 0.0;
		BigDecimal b = new BigDecimal(paid);
		paid =  b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		if(map!=null){
			paid = StringUtil.toDouble(map.get("paid"), false);
		}
		return paid;
	}


	//获取当前年月的最大的天数
	public static int getDaysByYearMonth(int year, int month) {  
		Calendar a = Calendar.getInstance();  
		a.set(Calendar.YEAR, year);  
		a.set(Calendar.MONTH, month - 1);  
		a.set(Calendar.DATE, 1);  
		a.roll(Calendar.DATE, -1);  
		int maxDate = a.get(Calendar.DATE);  
		return maxDate;  
	}

	/**
	 * @author LSJ
	 * @param page 当前页数
	 * @param pageSize 分页大小
	 * @param order
	 * @return
	 * 2016年12月6日下午15:16
	 */
	@Transactional(propagation = Propagation.REQUIRED) 
	@Override
	public Page listOrder(Map map, int page, int pageSize, String other, String order) {
		String sql = createTempSql(map, other,order);
		Page webPage = this.daoSupport.queryForPage(sql, page, pageSize);
		return webPage;
	}
	
	public Order get(Integer orderId) {
		String sql = "select * from es_order where order_id=?";
		Order order = (Order) this.daoSupport.queryForObject(sql,
				Order.class, orderId);
		return order;
	}
	
	/**
	 * 
	 * @param map
	 * @param other
	 * @param order
	 * @return
	 */
	private String  createTempSql(Map map,String other,String order){
		
		Integer stype = (Integer) map.get("stype");
		String keyword = (String) map.get("keyword");
		String orderstate =  (String) map.get("order_state");//订单状态
		String start_time = (String) map.get("start_time");
		String end_time = (String) map.get("end_time");
		String status = (String) map.get("status");
		String sn = (String) map.get("sn");
		String ship_name = (String) map.get("ship_name");
		Integer paystatus = (Integer) map.get("paystatus");
		Integer shipstatus = (Integer) map.get("shipstatus");
		Integer shipping_type = (Integer) map.get("shipping_type");
		Integer payment_id = (Integer) map.get("payment_id");
		Integer depotid = (Integer) map.get("depotid");
		String complete = (String) map.get("complete");
		String storeWhere=(String)map.get("storeWhere");
		
		StringBuffer sql =new StringBuffer();
		sql.append("select o.*, m.uname from es_order o left join es_member m on o.member_id = m.member_id where o.disabled=0");
		
		if(stype!=null && keyword!=null){			
			if(stype==0){
				sql.append(" and (o.sn like '%"+keyword+"%'");
				sql.append(" or o.ship_name like '%"+keyword+"%')");
			}
		}
		
		if(status!=null && !status.equals("99")){
			sql.append(" and o.order_status='"+status+"'");
		}
		
		if(!StringUtil.isEmpty(storeWhere)){
			sql.append(storeWhere);
		}
		
		if(sn!=null && !StringUtil.isEmpty(sn)){
			sql.append(" and o.sn like '%"+sn+"%'");
		}
		
		if(ship_name!=null && !StringUtil.isEmpty(ship_name)){
			sql.append(" and o.ship_name like '"+ship_name+"'");
		}
		
		if(paystatus!=null){
			sql.append(" and o.pay_status="+paystatus);
		}
		
		if(shipstatus!=null){
			sql.append(" and o.ship_status="+shipstatus);
		}
		
		if(shipping_type!=null){
			sql.append(" and o.shipping_id="+shipping_type);
		}
		
		if(payment_id!=null){
			sql.append(" and o.payment_id="+payment_id);
		}
		
		if (depotid!=null && depotid > 0) {
			sql.append(" and o.depotid=" + depotid);
		}
		
		if(start_time!=null&&!StringUtil.isEmpty(start_time)){			
			long stime = com.enation.framework.util.DateUtil.getDateline(start_time+" 00:00:00","yyyy-MM-dd HH:mm:ss");
			sql.append(" and o.create_time>"+stime);
		}
		if(end_time!=null&&!StringUtil.isEmpty(end_time)){			
			long etime = com.enation.framework.util.DateUtil.getDateline(end_time +" 23:59:59", "yyyy-MM-dd HH:mm:ss");
			sql.append(" and o.create_time<"+etime);
		}
		if( !StringUtil.isEmpty(orderstate)){
			if(orderstate.equals("wait_ship") ){ //对待发货的处理
				sql.append(" and ( ( payment_type!='cod'  and  order_status= '"+OrderStatus.PAID_OFF +"') ");//非货到付款的，要已结算才能发货
				sql.append(" or ( payment_type='cod' and  order_status= '"+OrderStatus.CONFIRM +"')) ");//货到付款的，已确认就可以发货
			}else if(orderstate.equals("wait_pay") ){
				sql.append(" and ( ( payment_type!='cod' and  order_status= '"+OrderStatus.CONFIRM +"') ");//非货到付款的，未付款状态的可以结算
				sql.append(" or ( payment_type='cod' and   order_status= '" + OrderStatus.ROG + "'  ) )");//货到付款的要发货或收货后才能结算
			}else if(orderstate.equals("wait_rog") ){ 
				sql.append(" and o.order_status= '" + OrderStatus.SHIPPED + "'"  ); 
			}else{
				sql.append(" and o.order_status= '"+orderstate + "'");
			}
		
		
		}
		
		if(!StringUtil.isEmpty(complete)){
			sql.append(" and o.order_status= '"+OrderStatus.COMPLETE + "'");
		}
		if(!StringUtil.isEmpty(order)){
			sql.append(" ORDER BY o."+other+" "+order);
		}
		
//		System.out.println(sql.toString());
		return sql.toString();
	}


}
