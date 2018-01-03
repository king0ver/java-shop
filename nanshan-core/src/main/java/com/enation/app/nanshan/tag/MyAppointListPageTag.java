package com.enation.app.nanshan.tag;

import com.enation.app.nanshan.service.IArticleService;
import com.enation.app.nanshan.service.ICatManager;
import com.enation.app.nanshan.vo.ArticleVo;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import freemarker.template.TemplateModelException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yulong on 17/12/21.
 */
@Component("myAppointListPageTag")
public class MyAppointListPageTag extends AbstractPageTag{

    @Autowired
    private ICatManager catManager;

    @Override
    protected Object exec(Map params) throws TemplateModelException {
        Map<String, Object> map = new HashMap<>();

        HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
        String catId  = request.getParameter("catId");
        if(StringUtils.isBlank(catId) && params.containsKey("catId")){
            catId = params.get("catId").toString();
        }

        handlePageCat(map, catId);

        int pageNo = getPage();


/*
        webPage.setCurrentPageNo(pageNo);

        map.put("webPage", webPage);
        map.put("items", webPage.getResult());*/

        map.put("blockview", ThreadContextHolder.getHttpRequest().getParameter("blockview"));

        return map;
    }


}
