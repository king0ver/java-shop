package com.enation.app.shop.goodssearch.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.PackedTokenAttributeImpl;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.facet.FacetField;
import org.apache.lucene.facet.FacetsConfig;
import org.apache.lucene.facet.taxonomy.TaxonomyWriter;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.NumericUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.chenlb.mmseg4j.analysis.TokenUtils;
import com.enation.app.shop.goods.model.po.Category;
import com.enation.app.shop.goods.model.po.GoodsParams;
import com.enation.app.shop.goods.service.ICategoryManager;
import com.enation.app.shop.goodssearch.service.IGoodsIndexManager;
import com.enation.app.shop.goodssearch.util.PinYinUtil;
import com.enation.eop.SystemSetting;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.util.StringUtil;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * lucene商品索引实现
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月14日 上午11:13:18
 */
@Service
@Lazy(true)
public class GoodsIndexLuceneManager implements IGoodsIndexManager {
	@Autowired
	private IDaoSupport daoSupport;


	@Autowired
	private ICategoryManager categoryManager;

	protected final Logger logger = Logger.getLogger(getClass());


	public final static String PRGRESSID = "lucene_create"; // 进度id


	/**
	 * 将商品名称按中文名、英文全拼、首字母进行索引<br>
	 * 同时将名称的分词写入数据库，以便形成搜索建议
	 */
	@Override
	public void addIndex(Map goods) {

		try {

			IndexWriter indexWriter = this.getIndexWriter();
			// 维度
			TaxonomyWriter taxoWriter = getTaxoWirter();

			String goods_name = goods.get("goods_name").toString();
			List<String> wordsList = toWordsList(goods_name);

			this.fillGoodsPinyin(goods);

			Document doc = createDocument(goods);
			indexWriter.addDocument(config.build(taxoWriter, doc));

			this.wordsToDb(wordsList);// 分词入库

			indexWriter.close();

			taxoWriter.close();
		} catch (Exception e) {
			this.logger.error("商品索引错误", e);
			e.printStackTrace();
			throw new RuntimeException("商品索引错误");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.shop.component.goodsindex.service.IGoodsIndexManager#
	 * updateIndex(java.util.Map)
	 */
	@Override
	public void updateIndex(Map goods) {

		try {

			IndexWriter indexWriter = this.getIndexWriter();
			// 维度
			TaxonomyWriter taxoWriter = getTaxoWirter();

			this.fillGoodsPinyin(goods);

			Document doc = this.createDocument(goods);
			Term term = new Term("goods_id", goods.get("goods_id").toString());

			indexWriter.updateDocument(term, config.build(taxoWriter, doc));

			indexWriter.close();
			taxoWriter.close();
		} catch (Exception e) {
			this.logger.error("商品索引错误", e);
			throw new RuntimeException("商品索引错误");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.shop.component.goodsindex.service.IGoodsIndexManager#
	 * deleteIndex(java.util.Map)
	 */
	@Override
	public void deleteIndex(Map goods) {
		try {

			String goods_name = goods.get("goods_name").toString();
			List<String> wordsList = toWordsList(goods_name);

			IndexWriter indexWriter = this.getIndexWriter();
			Term term = new Term("goods_id", goods.get("goods_id").toString());
			indexWriter.deleteDocuments(term);
			indexWriter.close();
			this.deleteWords(wordsList);
		} catch (Exception e) {
			this.logger.error("商品索引错误", e);
			throw new RuntimeException("商品索引错误");
		}
	}

	/**
	 * 将list中的分词减一
	 * 
	 * @param wordsList
	 */
	private void deleteWords(List<String> wordsList) {
		for (String words : wordsList) {
			this.daoSupport.execute("update es_goods_words set goods_num=goods_num-1 where words=?",words);
		}
	}

	private final FacetsConfig config = new FacetsConfig();


	/**
	 * 获取分词结果
	 * 
	 * @param txt
	 * @return 分词list
	 */
	public static List<String> toWordsList(String txt) {

		List<String> list = new ArrayList<String>();
		TokenStream ts = null;
		try {
			ts = getAnalyzer().tokenStream("text", new StringReader(txt));
			ts.reset();
			for (PackedTokenAttributeImpl t = new PackedTokenAttributeImpl(); (t = TokenUtils.nextToken(ts, t)) != null;) {
				// System.out.println(t.toString());
				list.add(t.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ts != null) {
				try {
					ts.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return list;
	}


	private static Analyzer getAnalyzer() {
		return   new IKAnalyzer();
//		return new MaxWordAnalyzer();
	}

	/**
	 * 填充商品
	 * 
	 * @param goods
	 */
	private void fillGoodsPinyin(Map goods) {
		try {
			String goods_name = goods.get("goods_name").toString();
			List<String> wordsList = toWordsList(goods_name);
			String seg_name = StringUtil.listToString(wordsList, " "); // 将分词结果转为空格分格的一串字符

			String name_quanpin = PinYinUtil.getPingYin(seg_name); // 全拼
			String name_header_py = PinYinUtil.getPinYinHeadChar(seg_name);// 首字母

			goods.put("goods_name", goods_name);
			goods.put("name_quanpin", name_quanpin);
			goods.put("name_header_py", name_header_py);
		} catch(Exception e) {

		}
	}

	/**
	 * 获取lucence 的indexwriter
	 * 
	 * @return
	 * @throws IOException
	 */
	private IndexWriter getIndexWriter() throws IOException {
		String index_path = SystemSetting.getIndex_path() + "/content";
		File indexDir = new File(index_path);
		if(!indexDir.exists()) {  
			indexDir.mkdir();
		}
		Path path = indexDir.toPath();
		Directory directory = FSDirectory.open(path);

		Analyzer luceneAnalyzer = getAnalyzer();

		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(
				luceneAnalyzer);

		IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
		return indexWriter;
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
	 * 获取维度的writer
	 * 
	 * @return
	 * @throws IOException
	 */
	private TaxonomyWriter getTaxoWirter() throws IOException {
		String facet_path = SystemSetting.getIndex_path() + "/facet";

		File taoDir = new File(facet_path);
		if(!taoDir.exists()) {  
			taoDir.mkdir();
		}
		Path taopath = taoDir.toPath();
		Directory taoDirectory = FSDirectory.open(taopath);
		TaxonomyWriter taxoWriter = new DirectoryTaxonomyWriter(taoDirectory);
		return taxoWriter;

	}


	/**
	 * 获取商品数量
	 * 
	 * @return
	 */
	private int getGoodsCount() {
		return this.daoSupport.queryForInt("select count(0) from es_goods");
	}

	/**
	 * 将一个商品创建为Document
	 * 
	 * @param goods
	 * @return
	 * @throws IOException
	 */
	private Document createDocument(Map goods) throws IOException {

		Document document = new Document();

		try {

			String goods_name = goods.get("goods_name").toString();
			String name_quanpin = goods.get("name_quanpin").toString();
			String name_header_py = goods.get("name_header_py").toString();

			Field name = new TextField("name", goods_name, Field.Store.YES);
			Field f_name_quanpin = new TextField("name_quanpin", name_quanpin,
					Field.Store.YES);
			Field f_name_header_py = new TextField("name_header_py",
					name_header_py, Field.Store.YES);
			String thumb = goods.get("thumbnail") == null ? "" : goods.get(
					"thumbnail").toString();
			Field thumbnail = new StringField("thumbnail", thumb,
					Field.Store.YES);
			String small = goods.get("small") == null ? "" : goods.get(
					"small").toString();
			Field small_field = new StringField("small", small,
					Field.Store.YES);

			// 价格要排序
			Double p = StringUtil.toDouble(goods.get("price").toString(), 0d);
			Field price_sort = new NumericDocValuesField("price_sort", NumericUtils.doubleToSortableLong(p));
			Field price = new DoubleField("price", p, Field.Store.YES);
			// 销量要排序
			Integer buy_count = (Integer) goods.get("buy_count");
			if (buy_count == null){
				buy_count = 0;
			}
			document.add(new NumericDocValuesField("buynum_sort", buy_count));
			document.add(new IntField("buy_count", buy_count, Field.Store.YES));
			Integer comment_num = (Integer) goods.get("comment_num");
			if(comment_num==null){
				comment_num = 0;
			}
			document.add(new IntField("comment_num", comment_num,Field.Store.YES));
			// 评价要排序
			Integer grade = (Integer) goods.get("grade");
			if (grade == null){
				grade = 0;
			}
			document.add(new NumericDocValuesField("grade_sort", grade));
			document.add(new IntField("grade", grade, Field.Store.YES));

			// 默认排序
			int goodsId = (Integer) goods.get("goods_id");

			document.add(new NumericDocValuesField("goods_id_sort", goodsId));
			Field goodsid_field = new StringField("goods_id", goods.get(
					"goods_id").toString(), Field.Store.YES);

			// 品牌维度
			document.add(new FacetField("brand", goods.get("brand_id")
					.toString()));
			document.add(new StringField("brand", goods.get("brand_id")
					.toString(), Field.Store.YES));

			// 分类维度
			Integer catid = StringUtil.toInt(goods.get("category_id").toString(), 0);
			document.add(new FacetField("category", "" + catid));
			Category cat = categoryManager.get(catid);
			if (cat != null) {
				// System.out.println(goods_name +"->"+cat.getCat_path());
				document.add(new StringField("catpath", cat.getCategory_path(),
						Field.Store.YES));
			}

			//创建参数维度
			List<GoodsParams> params = (List<GoodsParams>) goods.get("params");//已填写参数

			if(params!=null&&params.size()>0){
				for(GoodsParams param : params){
					document.add(new FacetField(param.getParam_name(), param.getParam_value()));
					document.add(new StringField(param.getParam_name(), param.getParam_value(),Field.Store.YES));
				}
			}

			//库存维度
			document.add(new IntField("quantity", Integer.parseInt(goods.get("quantity").toString()),Field.Store.YES));
			//上架：1 下架：0
			document.add(new StringField("market_enable", goods.get("market_enable").toString(), Field.Store.YES));
			//删除： 回收站：1 正常 ：0
			document.add(new StringField("disabled", goods.get("disabled").toString(), Field.Store.YES));

			document.add(goodsid_field);
			document.add(name);
			document.add(f_name_quanpin);
			document.add(f_name_header_py);

			document.add(thumbnail);
			document.add(small_field);
			document.add(price);
			document.add(price_sort);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return document;

	}

	/**
	 * 将分词结果写入数据库
	 * 
	 * @param wordsList
	 */
	private void wordsToDb(List<String> wordsList) {
		for (String words : wordsList) {

			int count = this.daoSupport
					.queryForInt(
							"select count(0)  from es_goods_words where words=?",
							words);
			if (count > 0) {// 已经存在此分词 +1
				this.daoSupport
				.execute(
						"update es_goods_words g set g.goods_num=g.goods_num+1 where g.words=? ",
						words);

			} else {

				Map data = new HashMap();
				data.put("words", words);
				String name_quanpin = PinYinUtil.getPingYin(words); // 全拼
				String name_header_py = PinYinUtil.getPinYinHeadChar(words);// 首字母
				data.put("quanpin", name_quanpin);
				data.put("szm", name_header_py);
				data.put("goods_num", 1);

				this.daoSupport.insert("es_goods_words", data);
			}

		}
	}

}
