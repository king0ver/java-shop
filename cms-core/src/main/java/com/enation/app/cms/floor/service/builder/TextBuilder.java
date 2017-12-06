package com.enation.app.cms.floor.service.builder;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.enation.app.cms.floor.model.vo.Text;
import com.enation.app.cms.floor.model.vo.TextBlock;
import com.enation.app.cms.floor.service.Element;
import com.enation.framework.util.StringUtil;

/**
 * 文本构建
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月11日 下午8:40:13
 */
@Service
public class TextBuilder implements Builder {

	@Override
	public Element build(Map map) {

		TextBlock textBlock = new TextBlock();
		textBlock.buildSelf(map);
		
		Text text = new Text();
		
		Map textMap = (Map) map.get("text");
		
		String value ="";
		String text_type ="";
		String title_text ="";
		
		if(  textMap.get("value")!=null ){
			value= (String)textMap.get("value");
		}
		
		if(  textMap.get("text_type")!=null ){
			text_type= (String)textMap.get("text_type");
		}
		
		if(  textMap.get("title_text")!=null ){
			title_text= (String)textMap.get("title_text");
		}
		
		
		text.setValue(value);
		text.setText_type(text_type);
		text.setTitle_text(title_text);
		
		textBlock.setText(text);

		return textBlock;
	}

	
	
}
