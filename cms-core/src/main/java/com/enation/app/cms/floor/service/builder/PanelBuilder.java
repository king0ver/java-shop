package com.enation.app.cms.floor.service.builder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enation.app.cms.floor.model.vo.Layout;
import com.enation.app.cms.floor.model.vo.Panel;
import com.enation.app.cms.floor.service.Element;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
 /**
  * 
  * 模版builder 
  * @author yanlin
  * @version v1.0
  * @since v6.4.0
  * @date 2017年8月14日 下午9:24:39
  */
@Service
public class PanelBuilder implements Builder {

	@Autowired
	@Qualifier("layoutBuilder")
	private Builder layoutBuilder;
	
	@Override
	public Element build(Map map) {
		
		Panel panel  = new Panel( );
		panel.buildSelf(map);
		
		String  panel_data = (String)map.get("panel_data");
		
		Gson gson = new Gson();
	    Type type = new TypeToken<ArrayList<Map>>() {}.getType();  
		List<Map> layoutMapList = gson.fromJson(panel_data, type);
		
		for (Map layoutMap : layoutMapList) {
			Layout layout  =(Layout) layoutBuilder.build(layoutMap);
			panel.addLayout(layout);
		}
		
		return panel;
	}

}
