package com.enation.app.shop.snapshot.service;

import com.enation.app.shop.snapshot.model.po.Snapshot;
import com.enation.app.shop.trade.model.po.OrderPo;

/**
 * 
 * (商品快照业务类) 
 * @author zjp
 * @version v1.0
 * @since v6.4.0
 * 2017年10月27日 下午5:18:33
 */
public interface ISnapshotManager {
	/**
	 * 添加快照
	 * @param snapshot
	 */
	public void add(OrderPo orderPo);
	
	/**
	 * 获取商品快照
	 * @param snapshot_id
	 * @return
	 */
	public Snapshot get(Integer snapshot_id);
}
