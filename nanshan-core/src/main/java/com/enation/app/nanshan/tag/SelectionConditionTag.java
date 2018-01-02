package com.enation.app.nanshan.tag;

import com.enation.app.nanshan.service.ISpecService;
import com.enation.app.nanshan.vo.SpecValVo;
import com.enation.app.nanshan.vo.SpecVo;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import freemarker.template.TemplateModelException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yulong on 17/12/20.
 */
@Component("selectionConditionTag")
public class SelectionConditionTag extends BaseFreeMarkerTag{

    @Autowired
    private ISpecService specService;

    @Override
    protected Object exec(Map params) throws TemplateModelException {

        String catId = ThreadContextHolder.getHttpRequest().getParameter("catId");

        Map<String, Object> result = new HashMap<>();

        if(StringUtils.isNotBlank(catId)){

            List<SpecVo> specVos = specService.querySpecInfoByCatId(Integer.parseInt(catId));
            handleSelectedSpecVo(specVos);
            result.put("specVos", specVos);
        }

        return result;
    }

    /**
     * 处理属性值
     * @param specVos
     */
    private void handleSelectedSpecVo(List<SpecVo> specVos){

        String selectedSpecIds = "," + getRequest().getParameter("specs") + ",";

        if(CollectionUtils.isEmpty(specVos)){
            return;
        }

        for(SpecVo specVo : specVos){

            boolean flag = false;

            if(CollectionUtils.isNotEmpty(specVo.getSpecValVos())){
                for(SpecValVo specValVo : specVo.getSpecValVos()){
                    if(selectedSpecIds.indexOf("," + specValVo.getId() + ",") != -1){
                        specValVo.setSelected(true);
                        flag = true;
                    }else{
                        specValVo.setSelected(false);
                    }
                }
            }

            if(flag){
                specVo.getSpecValVos().add(0,new SpecValVo(0,"全部", false));
            }else{
                specVo.getSpecValVos().add(0,new SpecValVo(0,"全部", true));
            }

        }
    }




}
