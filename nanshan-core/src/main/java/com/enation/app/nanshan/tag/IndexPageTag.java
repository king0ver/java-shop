package com.enation.app.nanshan.tag;

import com.enation.app.nanshan.service.IArticleService;
import com.enation.app.nanshan.vo.ArticleVo;
import freemarker.template.TemplateModelException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
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

        ArticleVo news = articleService.queryArticleInfoByCatId(40);
        ArticleVo activeBack = articleService.queryArticleInfoByCatId(39);
        ArticleVo activeNew = articleService.queryArticleInfoByCatId(38);

        news.setImgUrl(getNewImageUrl(news));

        map.put("item", articleVo);
        map.put("news", news);
        map.put("activeBack", activeBack);
        map.put("activeNew", activeNew);

        return map;
    }

    /**
     * 获取新图片URL
     * @param articleVo
     * @return
     */
    private String getNewImageUrl(ArticleVo articleVo){

       JSONObject content = articleVo.getContent();

       if(content != null){
           JSONArray array =  content.getJSONArray("content");

           if(array != null && array.size() > 0){

               for(int i = 0; i < array.size(); i++){
                   JSONObject object =  array.getJSONObject(0);

                   String type = object.getString("type");
                   if("img".equals(type)){
                       return object.getString("content");
                   }
               }
           }
       }

       return "/nanshan/css/images/news_default.jpg";

    }














}
