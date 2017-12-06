package com.enation.app.shop.promotion.exchange.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.promotion.exchange.model.po.ExchangeGoodsCategory;
import com.enation.app.shop.promotion.exchange.model.vo.ExchangeGoodsCategoryVo;
import com.enation.app.shop.promotion.exchange.service.IExchangeCategoryManager;
import com.enation.framework.annotation.Log;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.log.LogType;
import com.enation.framework.util.StringUtil;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.UnProccessableServiceException;

/**
 * 
 * 积分商品分类管理实现类 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月26日 上午10:09:39
 */
@Service
public class ExchangeCategoryManager implements IExchangeCategoryManager {
	protected final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private IDaoSupport daoSupport;

	/**
	 * 添加积分商品分类
	 * 
	 * @param exchangeGoodsCat
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@Log(type = LogType.GOODS, detail = "添加了一个商品类别名为${Category.name}的商品类别")
	public void saveAdd(ExchangeGoodsCategory category) {
		ExchangeGoodsCategory parent = this.getById(category.getParent_id());
		if (parent != null) {
			// 替换cat_path 根据cat_path规则来匹配级别
			String cat_path = parent.getCategory_path().replace("|", ",");
			String[] str = cat_path.split(",");
			// 如果当前的cat_path length 大于4 证明当前分类级别大于五级 提示
			if (str.length >= 4) {
				throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR, "最多为三级分类,添加失败");
			}
		}
		daoSupport.insert("es_exchange_goods_cat", category);
		int category_id = daoSupport.getLastId("es_exchange_goods_cat");
		String sql = "";
		// 判断是否是顶级类似别，如果parentid为空或为0则为顶级类似别
		// 注意末尾都要加|，以防止查询子孙时出错
		if (category.getParent_id() != null && category.getParent_id().intValue() != 0) { // 不是顶级类别，有父
			category.setCategory_path(parent.getCategory_path() + category_id + "|");
		} else {// 是顶级类别
			category.setCategory_path(category.getParent_id() + "|" + category_id + "|");
			category.setParent_id(0);
		}
		sql = "update es_exchange_goods_cat set  category_path=?,parent_id=?  where  category_id=?";
		this.daoSupport.execute(sql, category.getCategory_path(), category.getParent_id(), category_id);
	}

	/**
	 * 编辑积分商品分类
	 * 
	 * @param exchangeGoodsCat
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@Log(type = LogType.GOODS, detail = "更新商品类别名为${category.category_name}的商品类别")
	public void update(ExchangeGoodsCategory category) throws Exception {
		ExchangeGoodsCategory parent = null;
		if (category.getParent_id() != null && category.getParent_id().intValue() != 0) {
			parent = this.getById(category.getParent_id());
			category.setCategory_path(parent.getCategory_path() + category.getCategory_id() + "|");
		} else {
			// 顶级类别，直接更新为parentid|catid
			category.setCategory_path(category.getParent_id() + "|" + category.getCategory_id() + "|");
		}
		category.setImage(StringUtil.isEmpty(category.getImage()) ? null : category.getImage());
		this.daoSupport.update("es_exchange_goods_cat", category, "category_id=" + category.getCategory_id());
		// 修改子分类的cat_path
		// List<ExchangeGoodsCategory> childList =
		// this.getChildren(category.getCategory_id());
		// if (childList != null && childList.size() > 0) {
		// for (ExchangeGoodsCategory cat : childList) {
		// Integer cat_id = cat.getCategory_id();
		// Map<String, Object> childmap = new HashMap<String, Object>();
		// childmap.put("category_path", category.getCategory_path() + cat_id + "|");
		// // 如果是父分类，且是关闭状态，则子分类变为关闭状态
		// if (category.getList_show().equals(0)) {
		// childmap.put("list_show", 0);
		// }
		// daoSupport.update("es_exchange_goods_cat", childmap, " category_id = " +
		// cat_id);
		// }
		// }

	}

	@Override
	@Log(type = LogType.GOODS, detail = "删除ID为${catId}的商品类别")
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Integer id) throws Exception {
		List<ExchangeGoodsCategoryVo> list = this.listAllChildren(id);
		if (list != null && list.size() > 0) {
			throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR, "此类别下存在子类别不能删除");
		}
		// 查询某商品分类的商品
		String goodsSql = "select count(0) from es_exchange_setting where category_id = ?";
		Integer count = this.daoSupport.queryForInt(goodsSql, id);
		if (count > 0) {
			throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR, "此类别下存在商品不能删除");
		}
		String sql = "delete from  es_exchange_goods_cat where category_id=?";
		this.daoSupport.execute(sql, id);

	}

	/**
	 * 根据分类id 获取积分商品分类
	 * 
	 * @param id
	 *            分类ID
	 * @return
	 */
	@Override
	public ExchangeGoodsCategoryVo getById(int id) {
		String sql = "SELECT * FROM es_exchange_goods_cat WHERE category_id=?";
		ExchangeGoodsCategoryVo exchangeGoodsCat = this.daoSupport.queryForObject(sql, ExchangeGoodsCategoryVo.class, id);
		return exchangeGoodsCat;
	}

	/**
	 * 通过商品ID获取分类
	 * 
	 * @param goods_id
	 * @return
	 */
	public ExchangeGoodsCategory getByGoodId(Integer goods_id) {
		String sql = "SELECT * FROM es_exhange_goods_cat WHERE goods_id=?";
		return this.daoSupport.queryForObject(sql, ExchangeGoodsCategory.class, goods_id);
	}

	@Override
	public List getListChildren(Integer parentid) {
		if(parentid==null) {
			return null;
		}
		String sql = "select c.* from es_exchange_goods_cat c  where c.parent_id = ? order by c.category_order asc";
		List<ExchangeGoodsCategoryVo> catlist = this.daoSupport.queryForList(sql, ExchangeGoodsCategoryVo.class, parentid);
		return catlist;
	}

	@Override
	public List<ExchangeGoodsCategoryVo> listAllChildren(Integer parentid) {
		String sql = "select c.* from  es_exchange_goods_cat c   where list_show=1  order by parent_id";
		List<ExchangeGoodsCategoryVo> allCatList = daoSupport.queryForList(sql, ExchangeGoodsCategoryVo.class);
		List<ExchangeGoodsCategoryVo> topCatList = new ArrayList<ExchangeGoodsCategoryVo>();
		for (ExchangeGoodsCategoryVo cat : allCatList) {
			if (cat.getParent_id().compareTo(parentid) == 0) {
				List<ExchangeGoodsCategoryVo> children = this.getChildren(allCatList, cat.getCategory_id());
				cat.setChildren(children);
				topCatList.add(cat);
			}
		}
		return topCatList;
	}

	private List<ExchangeGoodsCategoryVo> getChildren(List<ExchangeGoodsCategoryVo> catList, Integer parentid) {
		List<ExchangeGoodsCategoryVo> children = new ArrayList<ExchangeGoodsCategoryVo>();
		for (ExchangeGoodsCategoryVo cat : catList) {
			if (cat.getParent_id().compareTo(parentid) == 0) {
				cat.setChildren(this.getChildren(catList, cat.getCategory_id()));
				children.add(cat);
			}
		}
		return children;
	}

	/**
	 * 保存分类排序
	 * 
	 * @param cat_ids
	 * @param cat_sorts
	 */
	public void saveSort(int[] cat_ids, int[] cat_sorts) {
		String sql = "";
		if (cat_ids != null) {
			for (int i = 0; i < cat_ids.length; i++) {
				sql = "update  es_exchange_goods_cat  set cat_order=? where cat_id=?";
				daoSupport.execute(sql, cat_sorts[i], cat_ids[i]);
			}
		}
	}

}
