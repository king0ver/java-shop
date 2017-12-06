package com.enation.app.shop.goods.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.shop.goods.model.po.Brand;
import com.enation.app.shop.goods.model.po.Category;
import com.enation.app.shop.goods.model.vo.BrandVo;
import com.enation.app.shop.goods.model.vo.SelectVo;
import com.enation.app.shop.goods.service.IBrandManager;
import com.enation.app.shop.goods.service.ICategoryManager;
import com.enation.app.shop.goodssearch.model.SearchSelector;
import com.enation.app.shop.goodssearch.util.BrandUrlUtils;
import com.enation.app.shop.goodssearch.util.ParamsUtils;
import com.enation.framework.annotation.Log;
import com.enation.framework.cache.ICache;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.log.LogType;
import com.enation.framework.util.StringUtil;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.UnProccessableServiceException;

/**
 * 
 * 品牌管理实现
 * 
 * @author zh
 * @version v1.0
 * @since v1.0 2017年3月5日 下午4:00:54
 */
@Service
public class BrandManager implements IBrandManager {
	public static final String BRAND_CACHE_KEY = "brand";
	@Autowired
	private IDaoSupport daoSupport;
	@Autowired
	private ICategoryManager categoryManager;
	@Autowired
	private ICache cache;

	@Override
	public Brand get(Integer brand_id) {
		String sql = "select * from es_brand where brand_id=?";
		Brand brand = this.daoSupport.queryForObject(sql, Brand.class, brand_id);
		return brand;
	}

	@Override
	public boolean checkname(String name, Integer brandid) {
		if (name != null) {
			name = name.trim();
		}
		String sql = "select count(0) from es_brand where name=? and brand_id!=?";
		if (brandid == null) {
			brandid = 0;
		}
		int count = this.daoSupport.queryForInt(sql, name, brandid);

		return count > 0;
	}

	@Override
	public List<BrandVo> getBrandsByCategory(Integer category_id) {
		Category category = categoryManager.get(category_id);
		if (category == null) {
			throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR, "该分类不存在");
		}
		String sql = "select b.brand_id,b.`name`,b.logo "
				+ "from es_category_brand cb inner join es_brand b on cb.brand_id=b.brand_id "
				+ "where cb.category_id=?";
		List<BrandVo> list = this.daoSupport.queryForList(sql, BrandVo.class, category_id);
		return list;
	}

	@Override
	public List<Brand> getAllBrands() {
		String sql = "select * from es_brand where disabled = 0";
		List<Brand> list = this.daoSupport.queryForList(sql, Brand.class);
		return list;
	}

	@Override
	public List<SelectVo> getCatBrand(Integer category_id) {
		String sql = "select b.brand_id id ,b.`name` text," + "case category_id" + " when ? " + " then true "
				+ " else false " + "end selected "
				+ "from es_brand b left join (select * from es_category_brand where category_id = ?) cb on b.brand_id=cb.brand_id where b.disabled=0";
		return this.daoSupport.queryForList(sql, SelectVo.class, category_id, category_id);
	}

	@Override
	public void createSelectorList(Map map, Category cat) {
		List<Brand> allbrand = getAllBrands();
		List<SearchSelector> selectorList = new ArrayList<SearchSelector>();

		// 此分类的品牌列表
		List<Brand> brandList = null;

		if (cat != null) {
			brandList = this.brandsByCategory(cat.getCategory_id());
		} else {
			brandList = allbrand;
		}
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String servlet_path = request.getServletPath();

		for (Brand brand : brandList) {
			SearchSelector selector = new SearchSelector();
			selector.setName(brand.getName());
			// 因首页可以进入品牌搜索列表，所以改为固定html页
			String brandurl = "goods_list.html" + "?" + BrandUrlUtils.createBrandUrl("" + brand.getBrand_id());
			selector.setUrl(brandurl);
			selector.setValue(brand.getLogo());
			selectorList.add(selector);
		}
		map.put("brand", selectorList);

		List selectedList = createSelectedBrand(allbrand);
		map.put("selected_brand", selectedList); // 已经选择的品牌

	}

	private List<Brand> brandsByCategory(Integer category_id) {
		String sql = "select b.brand_id,b.`name`,b.logo "
				+ "from es_category_brand cb inner join es_brand b on cb.brand_id=b.brand_id "
				+ "where cb.category_id=?";
		List<Brand> list = this.daoSupport.queryForList(sql, Brand.class, category_id);
		return list;
	}

	/**
	 * 生成已经选择的品牌
	 * 
	 * @param brandList
	 * @return
	 */
	public static List<SearchSelector> createSelectedBrand(List<Brand> brandList) {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String servlet_path = request.getServletPath();
		List<SearchSelector> selectorList = new ArrayList();
		String brandid = request.getParameter("brand");
		if (StringUtil.isEmpty(brandid)) {
			return selectorList;
		}
		int bid = StringUtil.toInt(brandid, 0);
		String brandname = "";
		Brand findbrand = findBrand(brandList, bid);
		if (findbrand != null) {
			brandname = findbrand.getName();
		}

		SearchSelector selector = new SearchSelector();
		selector.setName(brandname);
		String url = servlet_path + "?" + createUrlWithOutBrand();
		selector.setUrl(url);
		selectorList.add(selector);
		return selectorList;
	}

	/**
	 * 根据id查找brand
	 * 
	 * @param brandList
	 * @param brandid
	 * @return
	 */
	private static Brand findBrand(List<Brand> brandList, int brandid) {

		for (Brand brand : brandList) {
			if (brand.getBrand_id() == brandid)
				return brand;
		}
		return null;
	}

	/**
	 * 生成没有品牌的url
	 * 
	 * @return
	 */
	private static String createUrlWithOutBrand() {
		Map<String, String> params = ParamsUtils.getReqParams();

		params.remove("brand");

		return ParamsUtils.paramsToUrlString(params);
	}

	@Override
	@Log(type = LogType.GOODS, detail = "添加了一个品牌名为${brand.name}的品牌")
	public void add(Brand brand) {
		this.daoSupport.insert("es_brand", brand);
		this.cache.remove(BRAND_CACHE_KEY);
	}

	@Override
	@Log(type = LogType.GOODS, detail = "修改品牌名为${brand.name}的品牌信息")
	public void update(Brand brand) {
		this.daoSupport.update("es_brand", brand, "brand_id=" + brand.getBrand_id());
		this.cache.remove(BRAND_CACHE_KEY);
	}

	@Override
	@Log(type = LogType.GOODS, detail = "删除品牌")
	public void delete(Integer[] bid) {
		if (bid == null) {
			return;
		}
		List<Object> term = new ArrayList<>();
		String[] bids = new String[bid.length];
		for (int i = 0; i < bid.length; i++) {
			bids[i] = "?";
			term.add(bid[i]);
		}
		String id_str = StringUtil.arrayToString(bids, ",");
		// 检测是否有商品关联
		String checksql = "select count(0) from es_goods where brand_id in (" + id_str + ")";
		int has_rel = this.daoSupport.queryForInt(checksql, term.toArray());
		if (has_rel > 0) {
			throw new RuntimeException("要删除的品牌已经关联商品，不能删除。");
		}
		String sql = "update es_brand set disabled=1  where brand_id in (" + id_str + ")";
		this.daoSupport.execute(sql, term.toArray());
		this.cache.remove(BRAND_CACHE_KEY);
	}

	@Override
	public List<Brand> list() {
		List<Brand> list = (List<Brand>) cache.get(BRAND_CACHE_KEY);
		if (list == null) {
			list = getAllBrands();
			cache.put(BRAND_CACHE_KEY, list);
		}
		return list;
	}

	@Override
	public List<Brand> listBrands(Integer tag_id) {
		String sql = "select b.* from es_brand b inner join es_tag_relb r on b.brand_id = r.rel_id where r.tag_id = ? order by r.ordernum desc ";
		List<Brand> brands = this.daoSupport.queryForList(sql, Brand.class, tag_id);
		return brands;
	}

	@Override
	public Page searchBrand(Map brandMap, int page, int pageSize) {
		String keyword = (String) brandMap.get("keyword");
		String sql = "select * from es_brand where disabled=0";
		if (keyword != null && !StringUtil.isEmpty(keyword)) {
			sql += " and name like '%" + keyword + "%'";
		}
		sql += " order by brand_id desc";
		Page webpage = this.daoSupport.queryForPage(sql, page, pageSize);
		return webpage;
	}


}
