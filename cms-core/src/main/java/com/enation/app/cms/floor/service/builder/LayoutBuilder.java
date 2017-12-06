package com.enation.app.cms.floor.service.builder;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.enation.app.cms.floor.model.enumeration.BlockType;
import com.enation.app.cms.floor.model.vo.Block;
import com.enation.app.cms.floor.model.vo.Layout;
import com.enation.app.cms.floor.service.Element;
import com.enation.framework.util.StringUtil;

/**
 * 
 * 通过不的builder返回不同的楼层布局
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0 
 * @date 2017年8月12日 下午1:12:27
 */
@Service
public class LayoutBuilder implements Builder {
	protected final Logger logger = Logger.getLogger(getClass());
	@Override
	public Element build(Map map) {
		Layout layout = new Layout();
		layout.buildSelf(map);
		List<Map> blocklist = (List<Map>) map.get("block_list");
		for (Map blockMap : blocklist) {
			if (blockMap == null) {
				continue;
			}
			String block_type_str = blockMap.get("block_type").toString();
			if(StringUtil.isEmpty(block_type_str)) {
				logger.error("构建区块失败：block type为空");
				continue;
			}
			BlockType block_type = BlockType.valueOf( block_type_str );
			if(StringUtil.isEmpty(block_type_str)) {
				logger.error("构建区块失败：BlockType为空");
				continue;
			}
			Builder builder = BlockBuilderFactory.getBlockBuilder(block_type);
			Block block = (Block) builder.build(blockMap);
			layout.addBlock(block);
		}
		return layout;

	}
}
