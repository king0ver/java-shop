package com.enation.app.shop.goods.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.goods.model.po.Goods;
import com.enation.app.shop.goods.model.vo.GoodsVo;
import com.enation.app.shop.goods.service.IGoodsManager;
import com.enation.app.shop.member.model.Favorite;
import com.enation.app.shop.member.service.IFavoriteManager;
import com.enation.eop.processor.core.UrlNotFoundException;
import com.enation.eop.sdk.context.UserConext;
import com.enation.eop.sdk.utils.StaticResourcesUtil;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.ObjectNotFoundException;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.RequestUtil;
import com.enation.framework.util.StringUtil;
import com.enation.framework.util.ToMapUtil;

import freemarker.template.TemplateModelException;

/**
 * 
 * 商品详细标签 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年9月16日 下午1:04:11
 */
@Service
public class GoodsBaseDataTag extends BaseFreeMarkerTag {

	@Autowired
	private IGoodsManager goodsManager;
	
	@Autowired
	private IFavoriteManager favoriteManager;

	/**
	 *  商品详细标签,获取商品基本信息的,如果找不到商品，或商品下架了，会跳转至404页面。
	 * @param goodsid:商品id ,int型，如果不指定此参数，则试图由地址栏获取商品id,如：goods-1.html则会得到商品id为1.或phone-1.html也会得到商品id为1.
	 * @return 商品基本信息
	 * {@link Goods} 注：这个goods里有small和big的属性，分别用于输出商品的大图和小图
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		try{ 
			Integer goods_id = StringUtil.toInt(params.get("goodsid"), false);
			if(goods_id==null||goods_id==0){
				goods_id= this.getGoodsId(); 
			}
			GoodsVo goods = goodsManager.getFromCache(goods_id);
			Map goodsMap=ToMapUtil.toHashMap(goods);
			/**
			 * 如果商品不存在抛出页面找不到异常 
			 */
			if(goodsMap==null||goodsMap.size()==0){
				//标记下架
				goodsMap.put("goods_off", -1);
				return goodsMap;
			}

			goodsMap.put("goods_off", 0);//默认商品没有下架
			/**
			 * 如果已下架抛出页面找不到异常 
			 */
			if(StringUtil.toString(goodsMap.get("market_enable"),false).equals("0")){

				//标记下架
				goodsMap.put("goods_off", 1);
				return goodsMap;     
				//throw new UrlNotFoundException();
			}
			/**
			 * @author xulipeng
			 * 如果是草稿商品
			 */
			if(StringUtil.toString(goodsMap.get("market_enable"),false).equals("2")){
				//标记预览
				goodsMap.put("goods_off", 2);
			}
			/**
			 * 如果已删除（到回收站）抛出页面找不到异常 
			 */
			if(goodsMap.get("disabled").toString().equals("1")){
				//标记下架
				goodsMap.put("goods_off", 1);
				return goodsMap;
				//					throw new UrlNotFoundException();
			}
			String intro  =(String)goodsMap.get("intro");
			if(!StringUtil.isEmpty(intro)){
				intro = StaticResourcesUtil.convertToUrl(intro);
				goodsMap.put("intro", intro);
			}
			
			//如果会员登录 判断 会员是否收藏了此商品
			Member currentMember = UserConext.getCurrentMember();
			if(currentMember!= null) {
				Favorite favorite = favoriteManager.get(goods_id, currentMember.getMember_id());
				if(favorite != null) {
					goodsMap.put("is_collect", 1);
				}else {
					goodsMap.put("is_collect", 0);
				}
			}else {
				goodsMap.put("is_collect", 0);
			}
			
			List<Map> goodsList  = new ArrayList<Map>();
			goodsList.add(goodsMap);
			this.getRequest().setAttribute("goods", goodsMap);
			return goodsMap;
		}catch(ObjectNotFoundException e){
			throw new UrlNotFoundException();
		}
	}



	private Integer getGoodsId(){
		HttpServletRequest httpRequest = ThreadContextHolder.getHttpRequest();
		String url = RequestUtil.getRequestUrl(httpRequest);
		String goods_id = this.paseGoodsId(url);

		return StringUtil.toInt(goods_id,false);
	}

	private  static String  paseGoodsId(String url){
		String pattern = "(-)(\\d+)";
		String value = null;
		Pattern p = Pattern.compile(pattern, 2 | Pattern.DOTALL);
		Matcher m = p.matcher(url);
		if (m.find()) {
			value=m.group(2);
		}
		return value;
	}



}
