package com.enation.app.cms.floor.model.vo;

import java.io.Serializable;

import org.jsoup.nodes.Element;

import com.enation.app.cms.floor.model.enumeration.BlockType;

import io.swagger.annotations.ApiModel;

/**
 * 文本
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月11日 下午5:10:35
 */
@ApiModel( description = "文本")
public class TextBlock  extends Block implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 4132664510294761657L;

	public TextBlock(){
		super();
	}
	
	
	@Override
	protected BlockType definitionType() {
		return BlockType.TEXT;
	}
	
	private Text text;


	public Text getText() {
		return text;
	}


	public void setText(Text text) {
		this.text = text;
	}


	@Override
	public void convertToElemnet(Element blockEle) {
		if(text!=null){
			blockEle.select("a").html( text.getTitle_text()+"" );
			blockEle.select("a").attr("href",buildLink(text.getText_type(), text.getValue()));
		}
	}

	
}
