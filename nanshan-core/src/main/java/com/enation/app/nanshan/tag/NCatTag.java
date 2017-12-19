package com.enation.app.nanshan.tag;

import com.enation.app.nanshan.vo.NCatVo;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import freemarker.template.TemplateModelException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yulong on 17/12/18.
 */
@Component("nCatTag")
public class NCatTag extends BaseFreeMarkerTag {



    @Override
    protected Object exec(Map params) throws TemplateModelException {

        List<NCatVo> catVos = new ArrayList<NCatVo>();

        catVos.add(build(1,"首页","http://www.baidu.com","",null));

        NCatVo nCatVo1 = build(2,"测试1","http://www.baidu.com","",null);
        NCatVo nCatVo2 = build(3,"测试1","http://www.baidu.com","",null);
        NCatVo nCatVo3 = build(4,"测试3","http://www.baidu.com","",null);

        List<NCatVo> nCatVos = new ArrayList<>();
        nCatVos.add(nCatVo1);
        nCatVos.add(nCatVo2);
        nCatVos.add(nCatVo3);

        NCatVo nCatVo4 = build(5,"展示展览","http://www.baidu.com","",nCatVos);
        catVos.add(nCatVo4);

        NCatVo nCatVo6 = build(6,"线上体验","http://www.baidu.com","",null);
        NCatVo nCatVo7 = build(7,"活动资讯","http://www.baidu.com","",null);
        NCatVo nCatVo8 = build(8,"服务导览","http://www.baidu.com","",null);
        catVos.add(nCatVo6);
        catVos.add(nCatVo7);
        catVos.add(nCatVo8);

        return catVos;
    }

    private  NCatVo build(long id, String name, String pcUrl, String wapUrl, List<NCatVo> leafs){

        NCatVo nCatVo = new NCatVo();

        nCatVo.setId(id);
        nCatVo.setName(name);
        nCatVo.setPcUrl(pcUrl);
        nCatVo.setWapUrl(wapUrl);
        nCatVo.setLeafs(leafs);

        return nCatVo;
    }
}
