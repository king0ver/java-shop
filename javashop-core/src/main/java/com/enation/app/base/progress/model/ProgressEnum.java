package com.enation.app.base.progress.model;
/**
 * 
 * 进度枚举 
 * @author zh
 * @version v1.0
 * @since v1.0
 * 2017年9月6日 下午8:44:42
 */
public enum ProgressEnum {

	ing("正在进行"),
	complete("完成"),
	error("出错");

	private String task_status;

	ProgressEnum(String _taskStatus) {
		this.task_status = _taskStatus;
	}

	public String getTask_status() {
		return task_status;
	}

	public void setTask_status(String task_status) {
		this.task_status = task_status;
	}



}
