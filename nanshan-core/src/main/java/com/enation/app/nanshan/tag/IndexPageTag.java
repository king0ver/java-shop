package com.enation.app.nanshan.tag;

import com.enation.app.nanshan.service.IArticleService;
import com.enation.app.nanshan.vo.ArticleVo;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import freemarker.template.TemplateModelException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yulong on 17/12/18.
 */
@Component("indexPageTag")
public class IndexPageTag extends AbstractPageTag {

    @Autowired
    private IArticleService articleService;

    @Override
    protected Object exec(Map params) throws TemplateModelException {

        Map<String, Object> map = new HashMap<>();

        ArticleVo articleVo = articleService.queryArticleInfoByCatId(0);

        ArticleVo news = articleService.queryArticleInfoByCatId(37);
        ArticleVo activeBack = articleService.queryArticleInfoByCatId(39);
        ArticleVo activeNew = articleService.queryArticleInfoByCatId(38);


        map.put("item", articleVo);
        map.put("news", news);
        map.put("activeBack", activeBack);
        map.put("activeNew", activeNew);

        return map;
    }














}
