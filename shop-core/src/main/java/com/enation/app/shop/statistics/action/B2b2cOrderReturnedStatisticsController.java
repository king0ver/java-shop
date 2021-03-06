package com.enation.app.shop.statistics.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.shop.shop.apply.model.po.Shop;
import com.enation.app.shop.shop.apply.service.IShopManager;
import com.enation.app.shop.statistics.service.IB2b2cReturnedStatisticsManager;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Controller
@Scope("prototype")
@RequestMapping("/b2b2c/admin/orderReturnedStatistics")
public class B2b2cOrderReturnedStatisticsController {
	@Autowired
	private IB2b2cReturnedStatisticsManager b2b2cReturnedStatisticsManager;
	
	@Autowired
	private IShopManager shopManager;
	/**
	 * 获取退款统计列表
	 * @author kanon
	 * @param cycle_type 周期模式
	 * @param year 年
	 * @param month 月
	 * @return 退款统计列表页面
	 */
	@RequestMapping(value="/returned-statistics")
	public ModelAndView returnedStatistics(Integer cycle_type, Integer year, Integer month){
		ModelAndView view = new ModelAndView();
		Map map  = new HashMap();
		map.put("cycle_type", cycle_type);
		map.put("year", year);
		map.put("month", month);
		List<Shop> storeList = this.shopManager.listAll();
		view.addObject("storeList", storeList);
		view.addObject("map", map);
		view.setViewName("/b2b2c/admin/statistics/sales/tuikuan");
		return view;
	}
	
	/**
	 * 获取退款统计列表JSON列表
	 * @author kanon
	 * @param cycle_type 周期模式 
	 * @param year 年
	 * @param month 月
	 * @return 退款统计列表JSON列表
	 */
	@ResponseBody
	@RequestMapping(value="/returned-statistics-json")
	public Object returnedStatisticsJson(Integer cycle_type, Integer year, Integer month,String store_id){
		String message = "[";
		//如果月的周期模式 
		if(cycle_type.intValue()==1){
			List<Map> list = b2b2cReturnedStatisticsManager.statisticsMonth_Amount( year, month,store_id);
			message += getMessage(cycle_type, "t_money", list);
		}else{
		//如果年的周期模式
			List<Map> list = b2b2cReturnedStatisticsManager.statisticsYear_Amount( year,store_id);
			message += getMessage(cycle_type, "t_money", list);
		}
		message = message.substring(0, message.length()-1)+"]";
		return "{\"result\":1,\"message\":"+message+"}";
	}
	
	/**
	 * 判断周期模式（按年或者按月），并返回相应的字串
	 * @author xulipeng
	 * @param cycle_type 	周期模式
	 * @param param		t_num：总订单数，t_money：总金额
	 * @param list	数据集合
	 * @return
	 */
	private String getMessage(int cycle_type,String param,List<Map> list){
		int num = 0;
		if(cycle_type==1){
			num=31;
		}else{
			num=12;
		}

		String message = "";
		for (int i = 1; i <= num; i++) {
			boolean flag = true;
			for (int j =0;j<list.size();j++) {
				Map map = list.get(j);
				if(!map.get("month").toString().equals("0") && i==Integer.parseInt(map.get("month").toString())){
					message = message+map.get(param).toString()+",";
					flag = false;
				}
			}
			if(flag){
				message = message+"0,";
			}
		}
		return message;
	}
}
