package com.enation.app.nanshan.tag;

import com.enation.app.nanshan.service.ICatManager;
import com.enation.app.nanshan.vo.NCatVo;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yulong on 17/12/18.
 */
@Component("nCatTag")
public class NCatTag extends BaseFreeMarkerTag {

    @Autowired
	private ICatManager catManager;


    @Override
    protected Object exec(Map params) throws TemplateModelException {

        NCatVo nCatVo = catManager.getCatTree();

        List<NCatVo> nCatVos = new ArrayList<>();

        nCatVos.add(nCatVo);
        nCatVos.addAll(nCatVo.getLeafs());
        nCatVo.setLeafs(null);

        return nCatVos;
    }

}
