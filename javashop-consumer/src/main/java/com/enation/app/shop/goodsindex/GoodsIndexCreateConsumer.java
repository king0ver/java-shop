package com.enation.app.shop.goodsindex;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.progress.model.TaskProgress;
import com.enation.app.base.progress.service.IProgressManager;
import com.enation.app.core.event.IGoodsIndexInitEvent;
import com.enation.app.shop.goods.model.po.GoodsParams;
import com.enation.app.shop.goodssearch.service.IGoodsIndexManager;
import com.enation.app.shop.goodssearch.service.IndexTypeFactory;
import com.enation.app.shop.goodssearch.service.impl.GoodsIndexSendManager;
import com.enation.eop.SystemSetting;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.util.StringUtil;


/**
 * 消费者
 * @author zh
 * @version v1.0
 * @since v6.4.0
 * 2017年4月12日 下午4:33:14
 */
@Component
public class GoodsIndexCreateConsumer implements IGoodsIndexInitEvent{
	@Autowired
	private IDaoSupport daoSupport;
	@Autowired
	private IProgressManager progressManager;

	protected final Logger logger = Logger.getLogger(getClass());



	/**
	 * 获取商品总数
	 * @return	商品总数
	 */
	private int getGoodsCount() {
		return this.daoSupport.queryForInt("select count(0) from es_goods");
	}
	/**
	 * 生成消息
	 * @param goods_name	 商品名称
	 */
	private void showMessage(String goods_name) {
		try {
			TaskProgress tk= progressManager.getProgress(GoodsIndexSendManager.INDEXID);
			tk.step("正在生成[" + goods_name + "]");
			progressManager.putProgress(GoodsIndexSendManager.INDEXID, tk);
		} catch (Exception e) {
			logger.error(e);
		}
	}
	/**
	 * 订阅生成商品索引消息
	 */
	@Override
	public void createGoodsIndex() {
		System.out.println("开始执行任务");
		/** 获取商品数 */
		int goods_count = this.getGoodsCount();
		/** 生成任务进度 */
		TaskProgress taskProgress = new TaskProgress(goods_count);
		progressManager.putProgress(GoodsIndexSendManager.INDEXID, taskProgress);
		int page_size = 100;
		int page_count = 0;
		page_count = goods_count / page_size;
		page_count = goods_count % page_size > 0 ? page_count + 1 : page_count;
		for (int i = 1; i <= page_count; i++) {
			StringBuffer sqlBuffer = new StringBuffer("select g.* from es_goods  g  where 1=1 ");
			sqlBuffer.append(" order by goods_id desc ");
			List<Map> goodsList = this.daoSupport.queryForListPage(sqlBuffer.toString() ,i, page_size);
			if(goodsList!=null){
				for(Map<String, Object> map : goodsList){
					//查询该商品关联的可检索的参数集合
					String sql = "select gp.* from es_goods_params gp inner join es_parameters p on gp.param_id=p.param_id "
							+ "where goods_id = ? and p.param_type = 2 and is_index = 1";
					List<GoodsParams> listParams = this.daoSupport.queryForList(sql,GoodsParams.class,map.get("goods_id"));
					map.put("params",listParams);
				}
			}

			SystemSetting.load();//加载下系统设置
			
			IGoodsIndexManager goodsIndexManager = IndexTypeFactory.getIndexType();

			for (Map goods : goodsList) {
				/** 生成索引消息 */
				this.showMessage(StringUtil.toString(goods.get("goods_name")));
				/** 生成索引之前删除索引 */
				goodsIndexManager.deleteIndex(goods);
				/** 生成索引之 */
				goodsIndexManager.addIndex(goods);
			}
		}
		TaskProgress task=(TaskProgress)progressManager.getProgress(GoodsIndexSendManager.INDEXID);
		task.step("索引生成完成");
		task.success();
		/** 更新进度 重新放入缓存 */
		progressManager.putProgress(GoodsIndexSendManager.INDEXID,task);

	}
}
