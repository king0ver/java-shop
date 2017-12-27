package com.enation.app.nanshan.tag;

import com.enation.app.nanshan.service.IArticleService;
import com.enation.app.nanshan.vo.ArticleVo;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.database.data.IDataOperation;
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
@Component("articleStaticPage")
public class ArticleStaticPageTag extends AbstractPageTag{


    @Autowired
    private IDataOperation dataOperation;

    @Override
    protected Object exec(Map params) throws TemplateModelException {
        Map<String, Object> map = new HashMap<>();

        String catId  = params.get("catId").toString();

        handlePageCat(map, catId);

        //dataOperation.imported("file:com/enation/app/nanshan/nanshan_cat_data.xml");

        return map;
    }


}
