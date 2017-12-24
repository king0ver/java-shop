package com.enation.app.shop.goodssearch.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.facet.DrillDownQuery;
import org.apache.lucene.facet.FacetResult;
import org.apache.lucene.facet.Facets;
import org.apache.lucene.facet.FacetsCollector;
import org.apache.lucene.facet.FacetsConfig;
import org.apache.lucene.facet.taxonomy.FastTaxonomyFacetCounts;
import org.apache.lucene.facet.taxonomy.TaxonomyReader;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyReader;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.enation.app.shop.goods.model.po.Category;
import com.enation.app.shop.goods.service.ICategoryManager;
import com.enation.app.shop.goodssearch.model.GoodsSearchLine;
import com.enation.app.shop.goodssearch.model.GoodsWords;
import com.enation.app.shop.goodssearch.service.IGoodsSearchManager;
import com.enation.app.shop.goodssearch.service.ISearchSelectorCreator;
import com.enation.app.shop.goodssearch.util.ParamsUtils;
import com.enation.app.shop.goodssearch.util.Separator;
import com.enation.app.shop.goodssearch.util.SortContainer;
import com.enation.eop.SystemSetting;
import com.enation.eop.processor.core.UrlNotFoundException;
import com.enation.framework.context.spring.SpringContextHolder;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.StringUtil;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * 基于lucene的全文检索
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月14日 上午11:10:46
 */
@Service
@Lazy(true)
public class GoodsSearchLuceneManager implements IGoodsSearchManager {

	@Autowired
	private ICategoryManager categoryManager;
	@Autowired
	private IDaoSupport daoSupport;

	private String[] selectorCreators = {
			"catSelectorCreator",
			"propSelectorCreator", 
			"brandSelectorCreator",
			"sortSelectorCreator",
			"priceSelectorCreator"
	};

	private final FacetsConfig config = new FacetsConfig();

	/* (non-Javadoc)
	 * @see com.enation.app.shop.core.service.IGoodsSearchManager#getSelector(com.enation.app.shop.core.model.Cat)
	 */
	@Override
	public Map<String,Object> getSelector() {

		long start = System.currentTimeMillis();

		try {
			Query query = this.createQuery();
			DrillDownQuery drillDownQuery = new DrillDownQuery(config, query);

			String[] prop_ar = ParamsUtils.getProps();
			for (String p : prop_ar) {
				String[] onprop_ar = p.split(Separator.separator_prop_vlaue);
				drillDownQuery.add(onprop_ar[0], onprop_ar[1]);
			}

			Map<String, Object> map = new HashMap<String, Object>(); // 要返回的结果

			TaxonomyReader taxoReader = this.getTaxoReader();
			FacetsCollector fc = new FacetsCollector();

			IndexSearcher searcher = this.getIndexSearcher();
			FacetsCollector.search(searcher, drillDownQuery, 100, fc);
			Facets facets = new FastTaxonomyFacetCounts(taxoReader, config, fc);

			List<FacetResult> results = facets.getAllDims(100);
			// 处理选择器
			for (String creatorid : selectorCreators) {
				ISearchSelectorCreator creator = SpringContextHolder
						.getBean(creatorid);
				creator.createAndPut(map, results);
			}
			long end = System.currentTimeMillis();
			return map;

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.enation.app.shop.core.service.IGoodsSearchManager#search(int, int)
	 */
	@Override
	public Page search(int pageNo,int pageSize) {
		try {

			IndexSearcher searcher = this.getIndexSearcher();

			int maxResultNum = pageNo * pageSize;

			List list = new ArrayList();
			Query query = this.createQuery();

			Sort sort = this.getSort();

			TopDocs results = searcher.search(query, maxResultNum, sort);

			ScoreDoc[] hits = results.scoreDocs;
			int totalCount = results.totalHits;
			int start = pageSize * (pageNo - 1);
			// System.out.println("共检索出 "+totalCount+" 条记录");
			int end = Math.min(pageNo * pageSize, totalCount);
			for (int i = start; i < end; i++) {
				Document document = searcher.doc(hits[i].doc);
				GoodsSearchLine goodsSearchLine = new GoodsSearchLine();
				goodsSearchLine.setName(document.get("name"));
				goodsSearchLine.setThumbnail(document.get("thumbnail"));
				goodsSearchLine.setSmall(document.get("small"));
				goodsSearchLine.setPrice(StringUtil.toDouble(document.get("price").toString(),0d));
				goodsSearchLine.setQuantity(Integer.parseInt(document.get("quantity").toString()));
				goodsSearchLine.setGoods_id(Integer.parseInt(document.get("goods_id").toString()));
				goodsSearchLine.setComment_num(Integer.parseInt(document.get("comment_num").toString()));
				goodsSearchLine.setBuy_count(Integer.parseInt(document.get("buy_count").toString()));
				list.add(goodsSearchLine);
			}

			Page webPage = new Page(0, totalCount, pageSize, list);
			return webPage;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	/**
	 * 获取lucene的 indexsearcher
	 * 
	 * @return
	 * @throws IOException
	 */
	private IndexSearcher getIndexSearcher() throws IOException {
		String index_path = SystemSetting.getIndex_path() + "/content";

		File indexDir = new File(index_path);
		if(!indexDir.exists()) {  
			indexDir.mkdir();
		}
		FSDirectory directory = FSDirectory.open(indexDir.toPath());
		IndexReader reader = DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);
		return searcher;
	}

	/**
	 * 根据搜索条件创建query
	 * 
	 * @return
	 * @throws ParseException
	 */
	private Query createQuery() throws ParseException {

		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String keyword = request.getParameter("keyword");
		String cat = request.getParameter("cat");
		String brand = request.getParameter("brand");
		String price = request.getParameter("price");

		//		Analyzer analyzer = new MaxWordAnalyzer();
		Analyzer analyzer = new IKAnalyzer();
		//		Analyzer analyzer = new StandardAnalyzer();
		//		Analyzer analyzer = new SimpleAnalyzer();

		BooleanQuery query = new BooleanQuery();
		
		query.add(new TermQuery(new Term("disabled", "0")), BooleanClause.Occur.MUST);
		
		query.add(new TermQuery(new Term("market_enable", "1")), BooleanClause.Occur.MUST);

		// 关键字检索
		if (!StringUtil.isEmpty(keyword)) {
			QueryParser parser = new QueryParser("name", analyzer);
			Query queryname = parser.parse(keyword);
			//			Query queryname = new TermQuery(new Term("name", keyword));

			query.add(queryname, BooleanClause.Occur.MUST);

		}

		// 品牌搜素
		if (!StringUtil.isEmpty(brand)) {
			Query brandquery = new TermQuery(new Term("brand", brand));
			query.add(brandquery, BooleanClause.Occur.MUST);
		}

		// 分类检索
		if (!StringUtil.isEmpty(cat)) {

			String[] catar = cat.split(Separator.separator_prop_vlaue);
			String catid = catar[catar.length - 1];

			Category goodscat = this.categoryManager.get(StringUtil.toInt(catid,0));
			if (goodscat == null)
				throw new UrlNotFoundException();

			PrefixQuery catquery = new PrefixQuery(new Term("catpath",
					goodscat.getCategory_path())); // 只查出最后的分类（最小的子类）
			query.add(catquery, BooleanClause.Occur.MUST);
		}

		// 属性检索
		String[] prop_ar = ParamsUtils.getProps();
		for (String p : prop_ar) {
			String[] onprop_ar = p.split(Separator.separator_prop_vlaue);
			Query propQuery = new TermQuery(
					new Term(onprop_ar[0], onprop_ar[1]));
			query.add(propQuery, BooleanClause.Occur.MUST);
		}

		if (!StringUtil.isEmpty(price)) {

			String[] pricear = price.split(Separator.separator_prop_vlaue);
			int min = StringUtil.toInt(pricear[0], 0);
			int max = Integer.MAX_VALUE;

			if (pricear.length == 2) {
				max = StringUtil.toInt(pricear[1], Integer.MAX_VALUE);
			}

			Query priceQuery = NumericRangeQuery.newDoubleRange("price",
					Double.valueOf(min), Double.valueOf(max), true, true);

			query.add(priceQuery, BooleanClause.Occur.MUST);
		}

		return query;
	}


	/**
	 * 由reuquest的sort参数 生成sort对象
	 * 
	 * @return
	 */
	private Sort getSort() {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();

		String sortfield = request.getParameter("sort");

		Map<String, String> sortMap = SortContainer.getSort(sortfield);

		String sortid = sortMap.get("id");

		// 如果是默认排序
		if ("def".equals(sortid)) {
			sortid = "goods_id";
		}

		boolean desc = "desc".equals(sortMap.get("def_sort"));

		Sort sort = new Sort(new SortField(sortid + "_sort", SortField.Type.LONG, desc));

		return sort;
	}

	/**
	 * 获取 维度的reader
	 * 
	 * @return
	 * @throws IOException
	 */
	private TaxonomyReader getTaxoReader() throws IOException {
		String facet_path = SystemSetting.getIndex_path() + "/facet";

		File taoDir = new File(facet_path);
		if(!taoDir.exists()) {  
			taoDir.mkdir();
		}
		Path taopath = taoDir.toPath();
		Directory taoDirectory = FSDirectory.open(taopath);
		TaxonomyReader taxoReader = new DirectoryTaxonomyReader(taoDirectory);
		return taxoReader;
	}

	@Override
	public List<GoodsWords> getGoodsWords(String keyword) {
		String sql = "select words,goods_num from es_goods_words where words like ? or quanpin like ? or szm like ? order by goods_num desc";
		return (List<GoodsWords>) this.daoSupport
				.queryForPage(sql, 1, 15, GoodsWords.class,"%"+keyword+"%","%"+keyword+"%","%"+keyword+"%").getResult();
	}

}
