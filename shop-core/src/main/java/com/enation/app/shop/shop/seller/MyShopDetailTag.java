package com.enation.app.shop.shop.seller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.shop.apply.model.po.Shop;
import com.enation.app.shop.shop.apply.model.vo.ShopVo;
import com.enation.app.shop.shop.apply.service.IShopManager;
import com.enation.app.shop.shop.rdesign.service.IStoreThemesManager;
import com.enation.eop.processor.core.UrlNotFoundException;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;
@Component
/**
 * 店铺信息Tag
 * @author LiFenLong
 *
 */
public class MyShopDetailTag extends BaseFreeMarkerTag{
	@Autowired
	private IShopManager shopManager;
	@Autowired
	private ISellerManager sellerManager;
	@Autowired
	private IStoreThemesManager storeThemesManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		String ctx = this.getRequest().getContextPath();
		HttpServletResponse response= ThreadContextHolder.getHttpResponse();
		ShopVo storeVo = new ShopVo();
		Shop store=new Shop();
		try {
			//判断是浏览商家店铺页面还是访问商家中心,type不为0则为访问商家店铺页面
			if(params.get("type")!=null&&params.get("store_id")==null){
				//通过链接获取店铺Id
				store=storeThemesManager.getStoreByUrl(this.getRequest().getServletPath());
			}else if(params.get("type")!=null&&params.get("store_id")!=null){
				//通过链接获取店铺Id 
				store=shopManager.getShop(StringUtil.toInt(params.get("store_id").toString(),true));
			}else{
				//session中获取会员信息,判断用户是否登陆
				Seller member=sellerManager.getSeller();
				if(member==null){
					response.sendRedirect(ctx+"/store/login.html");
				}
				//重新申请店铺时使用
				else if(member.getStore_id()==null){
					store=shopManager.getShopByMember(member.getMember_id());
				}else{
					 store=shopManager.getShop(member.getStore_id());
				}
			}
			if(store!=null&&store.getShop_id()!=null) {
				return shopManager.getShopVo(store.getShop_id());
			}
			//if(store.getDisabled()==2){
				//response.sendRedirect(ctx+"/store/dis_store.html");
			//}
		} catch (IOException e) {
			throw new UrlNotFoundException();
		}
		storeVo.setShop_id(store.getShop_id());
		return storeVo;
	}
	
}
