package com.enation.app.cms.floor.model.vo;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.context.request.RequestContextHolder;

import com.enation.app.cms.floor.model.enumeration.BlockType;
import com.enation.framework.context.spring.SpringContextHolder;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.StringUtil;

import io.swagger.annotations.ApiModel;

/**
 * 自动规则商品区块
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月12日 上午11:36:14
 */
@ApiModel( description = "自动规则商品")
public class AutoGoodsBlock extends Block  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4587204429417088509L;

	@Override
	protected BlockType definitionType() {
		
		return BlockType.AUTO_GOODS;
	}

	private AutoRule rule;
	
	private List<Goods> goodsList;
	
	public AutoRule getRule() {
		return rule;
	}

	public void setRule(AutoRule rule) {
		this.rule = rule;
	}

	public List<Goods> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<Goods> goodsList) {
		this.goodsList = goodsList;
	}

	@Override
	public void convertToElemnet(Element blockEle) {
		blockEle.select("dl").remove();
		if(goodsList!=null && goodsList.size()>0){
			Elements ul = blockEle.select("ul").attr("style","display:block;");
			Elements li = blockEle.select("ul>li");
			for(Goods goods : goodsList){
				Elements cloneLi = li.clone();
				cloneLi.select(".goods .g-img a").attr("href" ,getContextPath()+"/goods-"+goods.getGoods_id()+".html");
				cloneLi.select(".goods .g-img img").attr("src" ,goods.getThumbnail()==null?"":goods.getThumbnail());
				cloneLi.select(".goods .g-name").html(goods.getGoods_name()==null?"":goods.getGoods_name());
				cloneLi.select(".g-price span").html( StringUtil.toString( goods.getGoods_price()));
				ul.add(cloneLi.get(0));
			}
			li.remove();
			blockEle.append(ul.html());
		}
	}

}
