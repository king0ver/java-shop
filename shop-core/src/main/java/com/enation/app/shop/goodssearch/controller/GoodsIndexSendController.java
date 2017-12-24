package com.enation.app.shop.goodssearch.controller;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.shop.goodssearch.service.IGoodsIndexSendManager;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.JsonResultUtil;

/**
 * 
 * 商品索引控制器
 * @author zh
 * @version v1.0
 * @since v6.4.0
 * 2017年9月21日 下午6:43:04
 */
@Controller
@RequestMapping("/shop/admin/goods-index")
public class GoodsIndexSendController {

	protected final Logger logger = Logger.getLogger(getClass());
	@Autowired
	private IGoodsIndexSendManager  goodsIndexSendManager;

	/**
	 * 转向生成页面
	 */
	@RequestMapping(value="input")
	public String execute(){
		return "/shop/admin/goodsindex/create";
	}

	/**
	 * 生成		
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/create")
	public JsonResult create(){
		try {
			boolean result = goodsIndexSendManager.startCreate();
			if(result){
				return JsonResultUtil.getSuccessJson("生成成功");
			}else{
				return JsonResultUtil.getErrorJson("有索引任务正在进行中，需等待本次任务完成后才能再次生成。");
			}
		} catch (Exception e) {
			logger.error(e);
			return JsonResultUtil.getErrorJson("生成失败"+e.getMessage());
		}

	}


}
