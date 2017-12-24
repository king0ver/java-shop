package com.enation.app.nanshan.tag;

import com.enation.app.nanshan.service.IArticleService;
import com.enation.app.nanshan.vo.ArticleVo;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.util.StringUtil;
import freemarker.template.TemplateModelException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yulong on 17/12/21.
 */
@Component("articleListPageTag")
public class ArticleListPageTag extends AbstractPageTag{

    @Autowired
    private IArticleService articleService;

    @Override
    protected Object exec(Map params) throws TemplateModelException {
        Map<String, Object> map = new HashMap<>();

        HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
        String catId  = request.getParameter("catId");
        String specs  = request.getParameter("specs");

        handlePageCat(map, catId);

        int pageNo = getPage();

        Page<ArticleVo> webPage =articleService.querySpecInfoByCatId(Integer.parseInt(catId),
                specs, pageNo, getPageSize());


        webPage.setCurrentPageNo(pageNo);

        map.put("webPage", webPage);
        map.put("items", webPage.getResult());

        return map;
    }


}
