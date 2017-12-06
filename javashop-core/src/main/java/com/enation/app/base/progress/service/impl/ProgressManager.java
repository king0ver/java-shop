package com.enation.app.base.progress.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.progress.model.TaskProgress;
import com.enation.app.base.progress.service.IProgressManager;
import com.enation.framework.cache.ICache;
/**
 * 
 * 进度管理实现 
 * @author zh
 * @version v1.0
 * @since v1.0
 * 2017年9月1日 下午5:43:11
 */
@Component
public class ProgressManager implements IProgressManager {

	@Autowired
	private ICache cache;



	@Override
	public TaskProgress getProgress(String id) {
		id=TaskProgress.PROCESS+id;
		return (TaskProgress) cache.get(id);

	}

	@Override
	public void putProgress(String id, TaskProgress progress) {
		id=TaskProgress.PROCESS+id;
		progress.setId(id);
		cache.put(id, progress);


	}

	@Override
	public void remove(String id) {
		id=TaskProgress.PROCESS+id;
		cache.remove(id);

	}

}
