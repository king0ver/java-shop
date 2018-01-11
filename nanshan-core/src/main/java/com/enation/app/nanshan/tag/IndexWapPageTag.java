package com.enation.app.nanshan.tag;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.nanshan.service.IArticleService;
import com.enation.app.nanshan.vo.ArticleVo;
import com.enation.framework.database.data.IDataOperation;

import freemarker.template.TemplateModelException;

/**
 * Created by yulong on 17/12/18.
 */
@Component("indexWapPageTag")
public class IndexWapPageTag extends AbstractPageTag {

    @Autowired
    private IDataOperation dataOperation;

    @Autowired
    private IArticleService articleService;

    @Override
    protected Object exec(Map params) throws TemplateModelException {

        Map<String, Object> map = new HashMap<>();

        ArticleVo articleVo = articleService.queryArticleInfoByCatId(0);

        ArticleVo tech = articleService.queryArticleInfoByCatId(42);
        ArticleVo dynamic = articleService.queryArticleInfoByCatId(41);
        ArticleVo media = articleService.queryArticleInfoByCatId(43);

        tech.setImgUrl(getNewImageUrl(tech));
        dynamic.setImgUrl(getNewImageUrl(dynamic));
        media.setImgUrl(getNewImageUrl(media));
        
        
        map.put("item", articleVo);
        map.put("tech", tech);
        map.put("dynamic", dynamic);
        map.put("media", media);

        return map;
    }

    /**
     * 获取新图片URL
     * @param articleVo
     * @return
     */
    private String getNewImageUrl(ArticleVo articleVo){

       if(articleVo != null){

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
       }
       return "/nanshan/css/images/news_default.jpg";

    }














}
