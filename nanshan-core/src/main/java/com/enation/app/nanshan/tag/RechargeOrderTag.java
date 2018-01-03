package com.enation.app.nanshan.tag;

import com.enation.app.nanshan.service.RechargeService;
import com.enation.app.nanshan.vo.RechargeVo;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import freemarker.template.TemplateModelException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yulong on 18/1/2.
 */
@Component("rechargeOrderTag")
public class RechargeOrderTag extends BaseFreeMarkerTag {

    @Autowired
    private RechargeService rechargeService;

    @Override
    protected Object exec(Map params) throws TemplateModelException {

        String rechargeSn = ThreadContextHolder.getHttpRequest().getParameter("recharge_sn");

        RechargeVo rechargeVo =  rechargeService.queryRechargeVoBySn(rechargeSn);

        Map<String, Object> map = new HashMap<>();
        map.put("item", rechargeVo);

        return map;
    }
}
