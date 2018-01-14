package com.enation.app.nanshan.tag;

import com.enation.app.nanshan.service.ICatManager;
import com.enation.app.nanshan.vo.NCatVo;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yulong on 17/12/20.
 */
public abstract class AbstractPageTag extends BaseFreeMarkerTag{

    @Autowired
    protected ICatManager iCatManager;



    protected void handlePageCat(Map<String, Object> result, String catId){

        NCatVo nCatVo = iCatManager.getCatTree();

        NCatVo cur;
        if(catId.equals(String.valueOf(nCatVo.getId()))){
            cur = nCatVo;
        }else{
            cur = findNCatVo(nCatVo.getLeafs(), Long.parseLong(catId));
        }

        result.put("curCat", cur);

        if(cur.getParent() != null){

            if(cur.getParent().getParent()!= null
                    && cur.getParent().getParent().getParent() == null){

                result.put("siblings", new ArrayList<>());
                result.put("parentCat",cur);
            }else{
                result.put("siblings", cur.getParent().getLeafs());
                result.put("parentCat",cur.getParent());
            }


            if(cur.getParent().getParent() != null){
            	result.put("grandCat", cur.getParent().getParent());
            	result.put("uncleCats", cur.getParent().getParent().getLeafs());
            }
        }
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
                NCatVo tmp = findNCatVo(nCatVo.getLeafs(), catId);
                if(tmp != null){
                    return tmp;
                }
            }
        }

        return null;
    }


    /**
     * 判断是否是wap访问
     * @return 是否是wap
     */
    public static boolean isWap(){
        String header = ThreadContextHolder.getHttpRequest().getHeader("User-Agent");
        boolean flag = false;
        String[] keywords = { "Android", "iPhone", "iPod", "iPad", "Windows Phone", "MQQBrowser","Mobile" };
        for (String s : keywords) {
            if(header.contains(s)){
                flag = true;
                break;
            }
        }
        return flag;
    }


}
