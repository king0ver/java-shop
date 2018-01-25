package com.enation.app.nanshan.tag;

import java.util.HashMap;
import java.util.Map;

import com.enation.app.base.core.model.Member;
import com.enation.eop.sdk.context.UserConext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.nanshan.service.IMessageBgService;
import com.enation.app.nanshan.vo.MessageBgVo;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;

import freemarker.template.TemplateModelException;

/**
 * 消息
 * @author jianjianming
 * @version $Id: MessageBgTag.java,v 0.1 2018年1月4日 上午11:23:13$
 */
@Component("messageBgTag")
public class MessageBgTag extends AbstractPageTag{

    @Autowired
    private IMessageBgService messageBgService;

    @Override
    protected Object exec(Map params) throws TemplateModelException {


        Map<String, Object> map = new HashMap<>();

        handlePageCat(map, "56");

        Member member  = UserConext.getCurrentMember();

        if(member == null){
            return map;
        }

        int pageNo = getPage();
        Page<MessageBgVo> webPage =messageBgService.queryMessageInfoByPage(member.getMember_id(),pageNo, getPageSize());
        webPage.setCurrentPageNo(pageNo);

        map.put("webPage", webPage);
        map.put("items", webPage.getResult());
        map.put("blockview", ThreadContextHolder.getHttpRequest().getParameter("blockview"));
        return map;
    }


}
