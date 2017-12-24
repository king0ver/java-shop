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
        String catId  = request.getParameter("cat");

        handlePageCat(map, catId);

        int pageNo = getPage();

        List<ArticleVo> items = buildArticleVos();

        Page webPage = new Page(0, 100, 16, items);
        webPage.setCurrentPageNo(pageNo);

        map.put("webPage", webPage);
        map.put("items", items);

        return map;
    }

    /**
     * 构建文章列表
     * @return
     */
    private List<ArticleVo> buildArticleVos(){

        List<ArticleVo> items = new ArrayList<>();

        for(int i = 0; i < 16; i++){

            ArticleVo articleVo = new ArticleVo();

            articleVo.setImgUrl("http://localhost:8180/images/drill5.jpg");
            articleVo.setTitle("地震应急演练");
            articleVo.setSummary("硬件设备电动6自由度地震平台、厨房场景家具、显示器、观众席座椅、控制中心。");
            articleVo.setUrl("http://www.baidu.com");

            items.add(articleVo);
        }

        return items;
    }


}
