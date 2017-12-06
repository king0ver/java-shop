package com.enation.app.cms.floor.model.vo;
import java.io.Serializable;

import org.jsoup.nodes.Element;

import com.enation.app.cms.floor.model.enumeration.BlockType;
import com.enation.framework.util.StringUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 
 * 手动规则商品 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月13日 下午9:31:04
 */
@ApiModel( description = "手动规则商品")
public class ManualGoodsBlock extends Block  implements Serializable  {
	 
	private static final long serialVersionUID = -6142425010332454061L;
	
	

	@Override
	public void convertToElemnet(Element blockEl) {
		if(goods!=null){
 			blockEl.select(".goods .g-img img").attr("src" ,goods.getThumbnail()==null?"":goods.getThumbnail());
 			blockEl.select(".goods .g-img a").attr("href" ,getContextPath()+"/goods-"+goods.getGoods_id()+".html");
			blockEl.select(".goods .g-name").html(goods.getGoods_name()==null?"":goods.getGoods_name());
			blockEl.select(".g-price span").html( StringUtil.toString( goods.getGoods_price()));
		}else{
			blockEl.select(".goods .g-img img").attr("src" ,"themes/b2b2cv4/images/no_goods.jpg");
 			blockEl.select(".goods .g-img a").attr("href" ,"javascript:void(0);");
			blockEl.select(".goods .g-name").html("该商品已被删除");
			blockEl.select(".g-price span").html( "0.00");
		}
	}

	
	
	public  ManualGoodsBlock(){
		super();
	} 
 
	
	
	@Override
	protected BlockType definitionType() {
		return BlockType.MANUAL_GOODS;
	}

	private Goods goods;
	
	@ApiModelProperty(value = "商品id" )
	private int goods_id;

	public int getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(int goods_id) {
		this.goods_id = goods_id;
	}

	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}





	
	
}
