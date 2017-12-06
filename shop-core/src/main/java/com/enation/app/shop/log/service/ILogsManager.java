package com.enation.app.shop.log.service;

import java.util.Map;

import com.enation.framework.database.Page;

/**
 * 操作日志
 * @author fk
 * @version v1.0
 * @since v6.2
 * 2016年12月8日 下午4:37:54
 */
public interface ILogsManager {

	/**
	 * 查询历史操作记录
	 * @param logsMap 条件map
	 * @param page 页码 
	 * @param pageSize条数
	 * @param order 排序字段
	 * @param sort  升序或降序
	 * @return
	 */
	public Page getAllLogs(Map logsMap, int page, int pageSize, String sort, String order);
}
