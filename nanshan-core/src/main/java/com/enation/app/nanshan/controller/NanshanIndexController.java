package com.enation.app.nanshan.controller;

import com.enation.app.nanshan.service.IArticleService;
import com.enation.app.nanshan.service.ICatManager;
import com.enation.app.nanshan.vo.ArticleVo;
import com.enation.app.nanshan.vo.NCatVo;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yulong on 18/1/15.
 */
@Api(description="预约活动")
@RestController
@RequestMapping("/nanshan-index")
@Validated
public class NanshanIndexController {

    @Autowired
    private ICatManager catManager;
    @Autowired
    private IArticleService articleService;


    @ApiOperation(value="首页数据", notes="首页数据")
    @ResponseBody
    @PostMapping(value="/index")
    public JsonResult index(){

        try {
            NCatVo nCatVo = catManager.getCatTree();

            ArticleVo articleVo = articleService.queryArticleInfoByCatId(0);

            ArticleVo news = articleService.queryArticleInfoByCatId(40);
            ArticleVo activeBack = articleService.queryArticleInfoByCatId(39);
            ArticleVo activeNew = articleService.queryArticleInfoByCatId(38);

            news.setImgUrl(getNewImageUrl(news));

            Map<String, Object> result = new HashMap<>();

            result.put("item", articleVo);
            result.put("news", news);
            result.put("activeNew", news);
            result.put("activeBack", news);
            result.put("cats", handleCatVo(nCatVo));


            return JsonResultUtil.getObjectJson(result);
        } catch (Exception e) {
            if (!StringUtil.isEmpty(e.getMessage())) {
                return JsonResultUtil.getErrorJson(e.getMessage());
            }
            return JsonResultUtil.getErrorJson("接口异常!");
        }

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

    /**
     * 处理分类对象
     * @param nCatVo
     * @return
     */
    private List<NCatVo> handleCatVo(NCatVo nCatVo){

        List<NCatVo> nCatVos = new ArrayList<>();

        removeParentCat(nCatVo);

        nCatVos.add(nCatVo);
        nCatVos.addAll(nCatVo.getLeafs());
        nCatVo.setLeafs(null);

        return  nCatVos;

    }

    /**
     * 删除父类引用,以免输出JSON死循环
     * @param catVo
     */
    private void removeParentCat(NCatVo catVo){

       if(catVo.getLeafs() != null){
           for(NCatVo nCatVo : catVo.getLeafs()){
               removeParentCat(nCatVo);
           }
       }

        catVo.setParent(null);
    }



}
