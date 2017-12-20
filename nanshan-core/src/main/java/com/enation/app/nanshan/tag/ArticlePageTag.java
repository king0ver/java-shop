package com.enation.app.nanshan.tag;

import com.enation.app.nanshan.vo.NCatVo;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import freemarker.template.TemplateModelException;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yulong on 17/12/18.
 */
public class ArticlePageTag extends BaseFreeMarkerTag {
    @Override
    protected Object exec(Map params) throws TemplateModelException {


        Map<String, Object> map = new HashMap<>();


        NCatVo nCatVo = findNCatVo(null, 1);

      
        map.put("secondCat", new NCatVo());
        map.put("thirdCats", new ArrayList<>());
        map.put("thirdCats", new ArrayList<>());


        return null;
    }

    /**
     * 遍历到三级分类
     * @param catVos
     * @param catId
     * @return
     */
    private NCatVo findNCatVo(List<NCatVo> catVos, long catId){

        if(CollectionUtils.isEmpty(catVos)){
            return null;
        }

        for(NCatVo nCatVo : catVos){

            if(nCatVo.getId() == catId){
                return nCatVo;
            }else{
                NCatVo tmp = findNCatVo(catVos, catId);
                if(tmp != null){
                    return tmp;
                }
            }
        }

        return null;
    }












}
