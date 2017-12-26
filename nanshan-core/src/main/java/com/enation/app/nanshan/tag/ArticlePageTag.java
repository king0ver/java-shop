package com.enation.app.nanshan.tag;

import com.enation.app.nanshan.service.IArticleService;
import com.enation.app.nanshan.vo.ArticleVo;
import com.enation.app.nanshan.vo.NCatVo;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import freemarker.template.TemplateModelException;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yulong on 17/12/18.
 */
@Component("articlePage")
public class ArticlePageTag extends AbstractPageTag {

    @Autowired
    private IArticleService articleService;

    @Override
    protected Object exec(Map params) throws TemplateModelException {

        Map<String, Object> map = new HashMap<>();

        String articleId;

        if(map.containsKey("id")){
            articleId = map.get("id").toString();
        }else{
            HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
            articleId = request.getParameter("id");
        }

        ArticleVo articleVo = articleService.queryArticleInfoById(Integer.parseInt(articleId));


        handlePageCat(map, String.valueOf(articleVo.getCatId()));

        map.put("item", articleVo);


        return map;
    }














}
