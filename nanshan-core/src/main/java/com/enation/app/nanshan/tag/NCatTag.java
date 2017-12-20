package com.enation.app.nanshan.tag;

import com.enation.app.nanshan.core.service.ICatManager;
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
	
	private ICatManager catManager;


    @Override
    protected Object exec(Map params) throws TemplateModelException {
        List<NCatVo> catVos = new ArrayList<NCatVo>();   
        catVos=catManager.getCatList();
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
