package com.enation.app.cms.pagecreate.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.enation.eop.SystemSetting;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.enation.app.base.progress.model.TaskProgress;
import com.enation.app.base.progress.service.IProgressManager;
import com.enation.app.cms.pagecreate.service.IPageCreator;
import com.enation.app.shop.payment.model.enums.ClientType;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.util.StringUtil;

/**
 * 
 * 静态页向redis生成
 * 
 * @author zh
 * @version v1.0
 * @since v6.4.0 2017年8月29日 上午11:45:20
 */
@Component
public class RedisPageCreator implements IPageCreator {

	protected final Logger logger = Logger.getLogger(getClass());

	// @Autowired
	// private ICache cache;

	/**
	 * 因生成的html为stinrg 型，因此采用 stringRedisTemplate，而非通用cache by kingapex 2017-10-11
	 */
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private IDaoSupport daoSupport;

	@Autowired
	private IProgressManager progressManager;

	@Override
	public void createOne(String path, String type) {
		SystemSetting.load();
		String url = SystemSetting.getStatic_page_address() + "/" + path;
		try {
			/** 生成消息 */
			this.showMessage(path);
			/** 通过http 来获取html存储redis */
			String html = this.getHTML(url, type);
			stringRedisTemplate.opsForValue().set("/" + type + SystemSetting.getContext_path() + path, html);
		} catch (ClientProtocolException e) {
			logger.error("生成静态页出错", e);
		} catch (IOException e) {
			logger.error("生成静态页出错", e);
		}
	}

	@Override
	public void createAll() {
		/** 生成商品信息页面 */
		this.createGoods();
		/** 生成商城首页 */
		this.createIndex();
		/** 生成帮助中心页面 */
		this.createHelp();
	}

	@Override
	public void createGoods() {
		/** 为了防止首页有相关商品，所以需要生成首页一次 */
		this.createIndex();
		/** 商品总数 */
		int goods_count = this.getGoodsCount();
		/** 100条查一次 */
		int page_size = 100;
		int page_count = 0;
		page_count = goods_count / page_size;
		page_count = goods_count % page_size > 0 ? page_count + 1 : page_count;
		for (int i = 1; i <= page_count; i++) {
			/** 查询商品信息 */
			List<Map> goodsList = this.daoSupport
					.queryForListPage("select goods_id,goods_name from es_goods order by goods_id desc ", i, page_size);
			for (Map goods : goodsList) {
				int goodsid = Integer.valueOf(goods.get("goods_id").toString());
				/** 商品页面名称 */
				String pagename = ("/goods-" + goodsid + ".html");
				/** 生成静态页 */
				this.createOne(pagename, ClientType.PC.name());
				this.createOne(pagename, ClientType.WAP.name());
			}
		}
	}

	@Override
	public void createIndex() {
		String pagename = "/index.html";
		/** 生成静态页 */
		this.createOne("/", ClientType.PC.name());
		this.createOne("/", ClientType.WAP.name());
		this.createOne(pagename, ClientType.PC.name());
		this.createOne(pagename, ClientType.WAP.name());
	}

	@Override
	public void createHelp() {
		/** 帮助中心页面 */
		int help_count = this.getHelpCount();
		int page_size = 100; // 100条查一次
		int page_count = 0;
		page_count = help_count / page_size;
		page_count = help_count % page_size > 0 ? page_count + 1 : page_count;
		String sql = "select cat_id,id,title from  es_helpcenter ";
		for (int i = 1; i <= page_count; i++) {
			/** 获取数据 */
			List<Map> list = this.daoSupport.queryForListPage(sql, i, page_size);
			for (Map map : list) {
				int catid = (Integer) map.get("cat_id");
				int articleid = StringUtil.toInt(map.get("id"), false);
				String pagename = ("/help-" + catid + "-" + articleid + ".html");
				/** 生成静态页 */
				this.createOne(pagename, ClientType.PC.name());
			}
		}
	}

	/**
	 * 查询商品总数
	 * 
	 * @return 商品总数
	 */
	private int getGoodsCount() {
		return this.daoSupport.queryForInt("select count(0) from es_goods");
	}

	/**
	 * 查询帮助中心总数
	 * 
	 * @return 帮助中心总数
	 */
	private int getHelpCount() {
		return this.daoSupport.queryForInt("select count(0) from  es_helpcenter ");
	}

	/**
	 * 传入url 返回对应页面的html
	 * 
	 * @param url  页面的url
	 * @param type  客户端类型
	 * @return 返回对应页面的html
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private String getHTML(String url, String type) throws ClientProtocolException, IOException {
		String html = "null";
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(50000) // socket超时
				.setConnectTimeout(50000) // connect超时
				.build();
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("Client-Type", type);
		CloseableHttpResponse response = httpClient.execute(httpGet);
		html = EntityUtils.toString(response.getEntity(), "utf-8");
		return html;
	}

	/**
	 * 生成消息
	 * 
	 * @param path
	 *            路径
	 */
	private void showMessage(String path) {
		try {
			TaskProgress tk = progressManager.getProgress(PageCreateManager.PAGEID);
			tk.step("正在生成[" + path + "]");
			progressManager.putProgress(PageCreateManager.PAGEID, tk);
		} catch (Exception e) {
			logger.error(e);
		}
	}
}
