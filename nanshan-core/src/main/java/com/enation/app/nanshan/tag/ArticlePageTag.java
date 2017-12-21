package com.enation.app.nanshan.tag;

import com.enation.app.nanshan.vo.NCatVo;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import freemarker.template.TemplateModelException;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yulong on 17/12/18.
 */
@Component("articlePage")
public class ArticlePageTag extends AbstractPageTag {


    @Override
    protected Object exec(Map params) throws TemplateModelException {

        Map<String, Object> map = new HashMap<>();

        String catId = "9";

        handlePageCat(map, catId);

        return map;
    }














}
