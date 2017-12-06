package com.enation.app.base.progress.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.base.progress.model.ProgressEnum;
import com.enation.app.base.progress.model.TaskProgress;
import com.enation.app.base.progress.service.IProgressManager;
import com.enation.framework.util.JsonMessageUtil;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.StringUtil;

/**
 * 
 * 进度
 * @author kingapex
 * @version v1.0
 * @since v1.0
 * 2015年5月13日 下午5:57:48
 */
@Controller
@RequestMapping("/core/admin/progress")
public class ProgressController {

	@Autowired
	private IProgressManager progressManager;

	/**
	 * 检测是否有任务正在进行
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/has-task")
	public Object hasTask(String progressid){
		if( StringUtil.isEmpty(progressid) ){
			return JsonResultUtil.getErrorJson("progressid 不能为空"+this);
		}
		/** 如果redis中有此id 视为有任务进行 */
		int hastask = progressManager.getProgress(progressid)==null?0:1;
		return JsonResultUtil.getNumberJson("hastask", hastask);
	}


	/**
	 * 查看生成进度
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="view-progress")
	public Object viewProgress(String progressid){
		if( StringUtil.isEmpty(progressid) ){
			return JsonResultUtil.getErrorJson("progressid 不能为空"+this);
		}
		TaskProgress taskProgress =  progressManager.getProgress(progressid);
		if( taskProgress== null ){
			taskProgress= new TaskProgress(100);
		}
		/** 如果是完成或者出错 需要移除任务 */
		if(!taskProgress.getTask_status().equals(ProgressEnum.ing.name())){
			progressManager.remove(progressid);
		}
		return JsonMessageUtil.getObjectJson(taskProgress);

	}
}
