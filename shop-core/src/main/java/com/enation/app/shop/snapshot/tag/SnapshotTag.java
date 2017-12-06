package com.enation.app.shop.snapshot.tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.goods.model.po.GoodsGallery;
import com.enation.app.shop.goods.model.po.SpecValue;
import com.enation.app.shop.goods.model.vo.GoodsParamsList;
import com.enation.app.shop.snapshot.model.po.Snapshot;
import com.enation.app.shop.snapshot.model.vo.SnapshotVo;
import com.enation.app.shop.snapshot.service.ISnapshotManager;
import com.enation.app.shop.trade.model.po.OrderItem;
import com.enation.app.shop.trade.service.IOrderItemManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import freemarker.template.TemplateModelException;
/**
 * 
 * 商品快照标签
 * @author zjp
 * @version v1.0
 * @since v6.4.0
 * 2017年10月31日 上午11:05:15
 */
@Component
@Scope("prototype")
public class SnapshotTag extends BaseFreeMarkerTag{
	
	@Autowired
	ISnapshotManager snapshotManager;
	
	@Autowired
	IOrderItemManager orderItemQueryManager;

	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		
		int snapshot_id = StringUtil.toInt(params.get("snapshot_id"), false);
		//获取快照信息
		Snapshot snapshot = snapshotManager.get(snapshot_id);
		
		//存入po
		SnapshotVo snapshotVo = new SnapshotVo(snapshot);
		
		if(snapshot.getHave_spec()!=null&&snapshot.getHave_spec()!=0) {
			//获取商品规格信息
			OrderItem orderItem = orderItemQueryManager.queryOrderItemBySnapshot(snapshot_id);
			String spec_json = orderItem.getSpec_json();
			
			//将json数据转成相应的list
			Gson gson = new Gson();
			List<SpecValue> specLists = gson.fromJson(spec_json, new TypeToken<List<SpecValue>>() {}.getType());
			
			snapshotVo.setSpec_list(specLists);
		}
		
		return snapshotVo;
	}
	
}
