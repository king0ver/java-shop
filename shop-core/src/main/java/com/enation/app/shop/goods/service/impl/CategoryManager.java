package com.enation.app.shop.goods.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.base.AmqpExchange;
import com.enation.app.base.core.model.Member;
import com.enation.app.shop.goods.model.po.Category;
import com.enation.app.shop.goods.model.vo.CategoryPluginVo;
import com.enation.app.shop.goods.model.vo.CategoryVo;
import com.enation.app.shop.goods.service.ICategoryManager;
import com.enation.app.shop.shop.apply.model.po.Shop;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.annotation.Log;
import com.enation.framework.cache.ICache;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.jms.support.goods.CategoryChangeMsg;
import com.enation.framework.log.LogType;
import com.enation.framework.util.StringUtil;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.UnProccessableServiceException;

/**
 * 
 * 商品分类
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0 2017年8月15日 上午11:03:51
 */
@Service
public class CategoryManager implements ICategoryManager {

	@Autowired
	private IDaoSupport daoSupport;
	@Autowired
	private ICache cache;
	@Autowired
	private AmqpTemplate amqpTemplate;

	/**
	 * 查询某个商品分类
	 */
	@Override
	public Category get(Integer id) {

		Category cat = (Category) cache.get("category_" + id);
		if (cat == null) {
			String sql = "select * from es_goods_category where category_id = ?";
			cat = this.daoSupport.queryForObject(sql, Category.class, id);
			cache.put("category_" + id, cat);
			return cat;
		}
		return cat;
	}

	/**
	 * 查询分类列表
	 */
	@Override
	public List list(Integer parentId, String format) {
		if (parentId == null) {
			return null;
		}
		String sql = "select c.* from es_goods_category c  where c.parent_id = ? order by c.category_order asc";
		if (format != null) {
			List<CategoryPluginVo> catlist = this.daoSupport.queryForList(sql, CategoryPluginVo.class, parentId);
			return catlist;
		} else {
			List<CategoryVo> catlist = this.daoSupport.queryForList(sql, CategoryVo.class, parentId);
			return catlist;
		}
	}

	/**
	 * 修改分类
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@Log(type = LogType.GOODS, detail = "更新商品类别名为${category.category_name}的商品类别")
	public void update(Category category) throws Exception {
		Category parent = null;

		if (category.getParent_id() != null && category.getParent_id().intValue() != 0) {

			parent = this.get(category.getParent_id());
			category.setCategory_path(parent.getCategory_path() + category.getCategory_id() + "|");

		} else {
			// 顶级类别，直接更新为parentid|catid
			category.setCategory_path(category.getParent_id() + "|" + category.getCategory_id() + "|");
		}
		category.setImage(StringUtil.isEmpty(category.getImage()) ? null : category.getImage());
		this.daoSupport.update("es_goods_category", category, "category_id=" + category.getCategory_id());
		// 缓存
		cache.put("category_" + category.getCategory_id(), category);
		List<Category> list = (List<Category>) cache.get("all_category");
		for (Category cat : list) {
			if (cat.getCategory_id().equals(category.getCategory_id())) {
				cat = category;
				cache.put("all_category", list);
				break;
			}
		}

		// 修改子分类的cat_path
		List<CategoryVo> childList = this.listAllChildren(category.getCategory_id());
		if (childList != null && childList.size() > 0) {
			for (Category cat : childList) {
				Integer cat_id = cat.getCategory_id();
				Map<String, Object> childmap = new HashMap<String, Object>();
				childmap.put("category_path", category.getCategory_path() + cat_id + "|");
				// 如果是父分类，且是关闭状态，则子分类变为关闭状态
				if (category.getList_show().equals(0)) {
					childmap.put("list_show", 0);
				}
				daoSupport.update("es_goods_category", childmap, " category_id =  " + cat_id);
				// 缓存
				cache.put("category_" + cat_id, cat);
				List<Category> childlist = (List<Category>) cache.get("all_category");
				for (Category catchild : childlist) {
					if (catchild.getCategory_id().equals(cat.getCategory_id())) {
						catchild = cat;
						cache.put("all_category", childlist);
						break;
					}
				}
			}
		}
		// 发送消息
		CategoryChangeMsg categoryChangeMsg = new CategoryChangeMsg(category.getCategory_id(),
				CategoryChangeMsg.UPDATE_OPERATION);
		this.amqpTemplate.convertAndSend(AmqpExchange.GOODS_CATEGORY_CHANGE.name(), "category-change-routingKey",
				categoryChangeMsg);
	}

	/**
	 * 删除分类
	 */
	@Override
	@Log(type = LogType.GOODS, detail = "删除ID为${catId}的商品类别")
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Integer id) throws Exception {

		List<CategoryVo> list = this.listAllChildren(id);
		if (list != null && list.size() > 0) {
			throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR, "此类别下存在子类别不能删除");
		}
		// 查询某商品分类的商品
		String goodsSql = "select count(0) from es_goods where category_id = ?";
		Integer count = this.daoSupport.queryForInt(goodsSql, id);
		if (count > 0) {
			throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR, "此类别下存在商品不能删除");
		}
		String sql = "delete from  es_goods_category where category_id=?";
		this.daoSupport.execute(sql, id);

		// 缓存
		cache.remove("category_" + id);
		List<Category> cachelist = (List<Category>) cache.get("all_category");
		List<Category> new_list = new ArrayList<>();
		if (cachelist.size() > 0) {
			for (Category cat : cachelist) {
				if (!cat.getCategory_id().equals(id)) {
					new_list.add(cat);
				}
			}
			cache.put("all_category", new_list);
		}
		// 发送消息
		CategoryChangeMsg categoryChangeMsg = new CategoryChangeMsg(id, CategoryChangeMsg.DEL_OPERATION);
		this.amqpTemplate.convertAndSend(AmqpExchange.GOODS_CATEGORY_CHANGE.name(), "category-change-routingKey",
				categoryChangeMsg);
	}

	/**
	 * 后台添加商品类别
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@Log(type = LogType.GOODS, detail = "添加了一个商品类别名为${Category.name}的商品类别")
	public void saveAdd(Category category) {

		Category parent = this.get(category.getParent_id());
		if (parent != null) {
			// 替换cat_path 根据cat_path规则来匹配级别
			String cat_path = parent.getCategory_path().replace("|", ",");
			String[] str = cat_path.split(",");
			// 如果当前的cat_path length 大于4 证明当前分类级别大于五级 提示
			if (str.length >= 4) {
				throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR, "最多为三级分类,添加失败");
			}
		}
		daoSupport.insert("es_goods_category", category);
		int category_id = daoSupport.getLastId("es_goods_category");
		String sql = "";
		// 判断是否是顶级类似别，如果parentid为空或为0则为顶级类似别
		// 注意末尾都要加|，以防止查询子孙时出错
		if (category.getParent_id() != null && category.getParent_id().intValue() != 0) { // 不是顶级类别，有父
			category.setCategory_path(parent.getCategory_path() + category_id + "|");
		} else {// 是顶级类别
			category.setCategory_path(category.getParent_id() + "|" + category_id + "|");
			category.setParent_id(0);
		}

		sql = "update es_goods_category set  category_path=?,parent_id=?  where  category_id=?";
		this.daoSupport.execute(sql, category.getCategory_path(), category.getParent_id(), category_id);
		category.setCategory_id(category_id);
		cache.put("category_" + category_id, category);
		List<Category> list = (List<Category>) cache.get("all_category");
		if (list == null) {
			List<Category> listCat = new ArrayList<Category>();
			listCat.add(category);
			cache.put("all_category", listCat);
		} else {
			list.add(category);
			cache.put("all_category", list);
		}
		// 发送消息
		CategoryChangeMsg categoryChangeMsg = new CategoryChangeMsg(category_id, CategoryChangeMsg.ADD_OPERATION);
		System.out.println("准备发送消息了");
		this.amqpTemplate.convertAndSend(AmqpExchange.GOODS_CATEGORY_CHANGE.name(), "category-change-routingKey",
				categoryChangeMsg);
	}

	/**
	 * 修改商品类别的排序
	 */
	@Override
	@Log(type = LogType.GOODS, detail = "修改商品类别的排序")
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveSort(Integer[] category_ids, Integer[] category_sorts) {
		String sql = "";
		if (category_ids != null) {
			for (int i = 0; i < category_ids.length; i++) {
				sql = "update  es_goods_cat  set cat_order = ? where cat_id = ?";
				daoSupport.execute(sql, category_sorts[i], category_ids[i]);
			}
		}
	}

	/**
	 * 保存分类关联的规格
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Category saveSpec(Integer categoryId, Integer[] choose_specs) {

		Category category = this.get(categoryId);
		if (category == null) {
			throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR, "该分类不存在");
		}
		String sql = "delete from es_category_spec where category_id = ?";
		this.daoSupport.execute(sql, categoryId);
		if (choose_specs != null && choose_specs.length > 0) {
			for (Integer spec_id : choose_specs) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("category_id", categoryId);
				map.put("spec_id", spec_id);
				daoSupport.insert("es_category_spec", map);
			}
		}
		return category;
	}

	/**
	 * 保存分类关联的品牌
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Category saveBrand(Integer categoryId, Integer[] choose_brands) {
		Category category = this.get(categoryId);
		if (category == null) {
			throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR, "该分类不存在");
		}
		String sql = "delete from es_category_brand where category_id = ?";
		this.daoSupport.execute(sql, categoryId);
		if (choose_brands != null) {
			for (int i = 0; i < choose_brands.length; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("category_id", categoryId);
				map.put("brand_id", choose_brands[i]);
				daoSupport.insert("es_category_brand", map);
			}
		}
		return category;
	}

	/**
	 * 初始化缓存
	 */
	@Override
	public List<Category> initCategory() {
		List<Category> list = this.getCategoryList();
		if (list != null && list.size() > 0) {
			for (Category cat : list) {
				cache.put("category_" + cat.getCategory_id(), cat);
			}
			cache.put("all_category", list);
		}
		return list;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public List<CategoryVo> getParents(int catid) {
		Category cat = this.get(catid);
		String path = cat.getCategory_path();
		path = path.substring(0, path.length() - 1);
		path = path.replace("|", ",");
		List lists = new ArrayList();
		this.getParent(catid, lists);

		List list = new ArrayList();
		for (int i = (lists.size() - 1); i >= 0; i--) {
			CategoryVo c = (CategoryVo) lists.get(i);
			list.add(c);
		}
		return list;
	}

	private List getParent(Integer catid, List ls) {
		if (catid != null) {
			String sql = "select category_id,name,parent_id from es_goods_category where category_id=" + catid;
			List<CategoryVo> list = this.daoSupport.queryForList(sql, CategoryVo.class);
			if (!list.isEmpty()) {
				for (CategoryVo cat : list) {
					ls.add(cat);
					this.getParent(cat.getParent_id(), ls);
				}
			}
		}
		return ls;
	}

	@Override
	public List<Category> getGoodsParentsType(Integer category_id) {
		String sql = "";
		Member m = UserConext.getCurrentMember();
		sql = "select * from es_shop where member_id = ? ";
		Shop shop = this.daoSupport.queryForObject(sql, Shop.class, m.getMember_id());
		sql = "select goods_management_category from es_shop_detail where shop_id = " + shop.getShop_id(); // 获取会员所有经营类目
		String goods_management_category = this.daoSupport.queryForString(sql);
		if (category_id == 0 && goods_management_category != null) {
			sql = "select c.*,'' type_name from es_goods_category c where category_id in (" + goods_management_category
					+ ") order by category_order";
			return this.daoSupport.queryForList(sql, Category.class);
		} else {
			String parentSql = "select * from es_goods_category  where parent_id=?";
			return this.daoSupport.queryForList(parentSql, Category.class, category_id);
		}
	}

	/**
	 * 获取某个类别的所有子类，所有的子孙
	 */
	@Override
	public List<CategoryVo> listAllChildren(Integer parentid) {
		// 从缓存取所有的分类
		List<Category> list = (List<Category>) cache.get("all_category");
		if (list == null) {

			// 调用初始化分类缓存方法
			list = initCategory();
		}
		List<CategoryVo> topCatList = new ArrayList<CategoryVo>();

		for (Category cat : list) {
			CategoryVo categoryVo = new CategoryVo(cat);
			if (cat.getParent_id().compareTo(parentid) == 0) {
				List<CategoryVo> children = this.getChildren(list, cat.getCategory_id());
				categoryVo.setChildren(children);
				topCatList.add(categoryVo);
			}
		}
		return topCatList;
	}

	/**
	 * 得到当前分类的子孙
	 * 
	 * @param catList
	 *            分类集合
	 * @param parentid
	 *            父分类id
	 * @return 带子分类的集合
	 */
	private List<CategoryVo> getChildren(List<Category> catList, Integer parentid) {
		List<CategoryVo> children = new ArrayList<CategoryVo>();
		for (Category cat : catList) {
			CategoryVo categoryVo = new CategoryVo(cat);
			if (cat.getParent_id().compareTo(parentid) == 0) {
				categoryVo.setChildren(this.getChildren(catList, cat.getCategory_id()));
				children.add(categoryVo);
			}
		}
		return children;
	}

	/**
	 * 查询所有分类列表
	 */
	private List<Category> getCategoryList() {
		// 不能修改为缓存读取
		String sql = "select * from es_goods_category where list_show=1 order by category_order asc";
		List<Category> list = this.daoSupport.queryForList(sql, Category.class);
		return list;
	}

	@Override
	public void saveSellerSpec(Integer categoryId, Integer spec) {
		Category category = this.get(categoryId);
		if (category == null) {
			throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR, "该分类不存在");
		}
		if (spec != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("category_id", categoryId);
			map.put("spec_id", spec);
			daoSupport.insert("es_category_spec", map);
		}

	}

}
