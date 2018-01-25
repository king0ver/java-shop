package com.enation.app.nanshan.plugin;

import com.enation.app.base.core.plugin.job.IEveryHourExecuteEvent;
import com.enation.app.nanshan.core.service.IArticleManager;
import com.enation.framework.plugin.AutoRegister;
import com.enation.framework.plugin.AutoRegisterPlugin;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by yulong on 18/1/25.
 */
@AutoRegister
public class ActivityExpirePlugin extends AutoRegisterPlugin implements IEveryHourExecuteEvent {

    @Autowired
    private IArticleManager articleManager;

    @Override
    public void everyHour() {

        articleManager.updateActivityExpire();

    }
}
