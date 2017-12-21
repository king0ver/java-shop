package com.enation.app.nanshan.tag;

import com.enation.app.nanshan.vo.SpecValVo;
import com.enation.app.nanshan.vo.SpecVo;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import freemarker.template.TemplateModelException;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yulong on 17/12/20.
 */
@Component("selectionConditionTag")
public class SelectionConditionTag extends BaseFreeMarkerTag{


    @Override
    protected Object exec(Map params) throws TemplateModelException {

        String catId = (String)params.get("catId");


        List<SpecVo> specVos = new ArrayList<>();


        SpecVo specVo = new SpecVo();
        specVo.setName("游戏类型");
        SpecValVo specValVo = new SpecValVo();
        specValVo.setId(1l);
        specValVo.setName("角色");
        SpecValVo specValVo1 = new SpecValVo();
        specValVo1.setId(2l);
        specValVo1.setName("射击");
        SpecValVo specValVo2 = new SpecValVo();
        specValVo2.setId(3l);
        specValVo2.setName("格斗");
        List<SpecValVo> specValVos = new ArrayList<>();
        specValVos.add(specValVo);
        specValVos.add(specValVo1);
        specValVos.add(specValVo2);

        specVo.setSpecValVos(specValVos);


        SpecVo specVo1 = new SpecVo();
        specVo1.setName("游戏类型");

        SpecValVo specValVo3 = new SpecValVo();
        specValVo3.setId(4l);
        specValVo3.setName("儿童");
        SpecValVo specValVo4 = new SpecValVo();
        specValVo4.setId(5l);
        specValVo4.setName("成人");
        SpecValVo specValVo5 = new SpecValVo();
        specValVo5.setId(6l);
        specValVo5.setName("老人");

        List<SpecValVo> specValVos1 = new ArrayList<>();
        specValVos1.add(specValVo3);
        specValVos1.add(specValVo4);
        specValVos1.add(specValVo5);

        specVo1.setSpecValVos(specValVos1);

        specVos.add(specVo);
        specVos.add(specVo1);


        handleSelectedSpecVo(specVos);

        Map<String, Object> result = new HashMap<>();
        result.put("specVos",specVos);

        return result;
    }

    /**
     * 处理属性值
     * @param specVos
     */
    private void handleSelectedSpecVo(List<SpecVo> specVos){

        String selectedSpecIds = "," + getRequest().getParameter("specs") + ",";

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
