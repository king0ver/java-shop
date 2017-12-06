package com.enation.app.shop.promotion.groupbuy.controller;

import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.shop.promotion.groupbuy.model.po.GroupBuyActive;
import com.enation.app.shop.promotion.groupbuy.service.IGroupBuyActiveManager;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonResultUtil;

/**
 * 团购活动处理器 
 * 
 * @author Chopper
 * @version v1.0
 * @since v6. 2017年8月22日 下午2:45:48
 *
 */
@Controller
@RequestMapping("/shop/admin/group-buy-act")
public class GroupBuyActiveController extends GridController {

	@Autowired
	private IGroupBuyActiveManager groupBuyActiveManager;

	/**
	 * 
	 * @Title: list
	 * @Description: 跳转至团购活动列表
	 * @return 团购活动页
	 */
	@RequestMapping(value = "/list")
	public ModelAndView list() {

		ModelAndView view = this.getGridModelAndView();
		view.setViewName("/groupbuy/groupbuyActive/act_list");
		return view;
	}

	/**
	 * 
	 * @Title: listJson
	 * @Description: 团购活动列表Json
	 * @param map
	 *            团购活动搜索条件
	 * @param webpage
	 *            团购活动分页列表
	 * @return 团购活动列表Json
	 */
	@ResponseBody
	@RequestMapping("/list-json")
	public GridJsonResult listJson() {
		this.webpage = groupBuyActiveManager.groupBuyActive(this.getPage(), this.getPageSize(), new HashMap());
		return JsonResultUtil.getGridJson(this.webpage);
	}

	/**
	 * 
	 * @Title: add
	 * @Description: 跳转到活动添加页
	 * @param groupbuyActStartTime
	 *            团购开启时间（同一时间不能开启多个团购活动）
	 * @return 团购活动添加页
	 */
	@RequestMapping("/add")
	public ModelAndView add() {
		ModelAndView view = new ModelAndView();
		view.addObject("groupbuyActStartTime", groupBuyActiveManager.getLastEndTime());
		view.setViewName("/groupbuy/groupbuyActive/act_add");
		return view;
	}

	/**
	 * 
	 * @Title: saveAdd
	 * @Description: 添加团购活动
	 * @param groupBuyActive
	 *            团购活动
	 * @param act_name
	 *            活动名称
	 * @param start_time
	 *            开始时间
	 * @param end_time
	 *            结束时间
	 * @param join_end_time
	 *            活动报名截止时间
	 * @return json 1为成功.0为失败.
	 */
	@ResponseBody
	@RequestMapping("/save-add")
	public JsonResult saveAdd(GroupBuyActive groupBuyActive, String act_start_time, String act_end_time,
			String act_join_end_time) {
		try {

			// 判断团购开始时间
//			Long groupbuyActStartTime = groupBuyActiveManager.getLastEndTime();
//			if (groupbuyActStartTime > DateUtil.getDateline(act_start_time, "yyyy-MM-dd HH:mm:ss")) { 
//				return JsonResultUtil.getErrorJson("添加活动失败:团购时间不得小于开启时间");
//			}
			long start_time = DateUtil.getDateline(act_start_time, "yyyy-MM-dd HH:mm:ss");
			long end_time = DateUtil.getDateline(act_end_time, "yyyy-MM-dd HH:mm:ss");
			Integer checkAct = this.groupBuyActiveManager.checkAct(start_time, end_time);
			if(checkAct>0) {
				return JsonResultUtil.getErrorJson("添加活动失败:团购时间内存在其他团购活动");
			}
			// 添加开始时间、结束时间、报名截止时间
			groupBuyActive.setStart_time(start_time);
			groupBuyActive.setEnd_time(end_time);
			groupBuyActive.setJoin_end_time(DateUtil.getDateline(act_join_end_time, "yyyy-MM-dd HH:mm:ss"));

			groupBuyActiveManager.add(groupBuyActive);

			return JsonResultUtil.getSuccessJson("添加活动成功");
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("添加活动失败", e);
			return JsonResultUtil.getErrorJson("添加活动失败" + e.getMessage());
		}
	}

	/**
	 * 
	 * @Title: delete
	 * @Description: 删除团购活动
	 * @param act_id
	 *            团购活动Id数组
	 * @return json 1为成功.0为失败.
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public JsonResult delete(Integer act_id[]) {
		try {
			this.groupBuyActiveManager.delete(act_id[0]);
			return JsonResultUtil.getSuccessJson("删除成功");
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson("删除失败");
		}
	}

	/**
	 * 
	 * @Title: edit
	 * @Description: 跳转至团购活动修改页
	 * @param act_id
	 *            团购活动Id
	 * @param groupBuyActive
	 *            团购活动
	 * @param start_time
	 *            团购活动开启时间
	 * @param end_time
	 *            团购活动结束时间
	 * @return 团购活动修改页
	 */
	@RequestMapping("/edit")
	public ModelAndView edit(Integer act_id[], GroupBuyActive groupBuyActive) {
		ModelAndView view = new ModelAndView();
		view.addObject("groupBuyActive", groupBuyActiveManager.get(act_id[0]));
		view.addObject("start_time", groupBuyActive.getStart_time_str());
		view.addObject("end_time", groupBuyActive.getEnd_time_str());
		view.setViewName("/groupbuy/groupbuyActive/act_edit");
		return view;
	}

	/**
	 * @Title: saveEdit
	 * @Description: 保存修改团购活动
	 * @param cat_id
	 *            团购活动Id数组
	 * @param act_name
	 *            团购活动名称
	 * @param start_time
	 *            团购活动开始时间
	 * @param end_time
	 *            团购活动结束时间
	 * @param join_end_time
	 *            报名截止时间
	 * @return json 1为成功.0为失败
	 */
	@ResponseBody
	@RequestMapping("/save-edit")
	public JsonResult saveEdit(GroupBuyActive groupBuyActive, String act_start_time, String act_end_time,
			String act_join_end_time) {
		try {
			groupBuyActive.setStart_time(DateUtil.getDateline(act_start_time));
			groupBuyActive.setEnd_time(DateUtil.getDateline(act_end_time));
			groupBuyActive.setJoin_end_time(DateUtil.getDateline(act_join_end_time));
			groupBuyActiveManager.update(groupBuyActive);
			return JsonResultUtil.getSuccessJson("修改成功");
		} catch (Exception e) {
			this.logger.error("添加活动失败", e);
			return JsonResultUtil.getErrorJson("修改失败");
		}
	}

}
