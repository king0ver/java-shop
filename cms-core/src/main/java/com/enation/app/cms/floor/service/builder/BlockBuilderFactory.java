package com.enation.app.cms.floor.service.builder;

import com.enation.app.cms.floor.model.enumeration.BlockType;
import com.enation.framework.context.spring.SpringContextHolder;

/**
 * 
 * 工厂模式，根据类型不通返回一个特定builder
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0 
 * @date 2017年8月12日 下午1:10:15
 */
public class BlockBuilderFactory {
	private BlockBuilderFactory() {
	}

	public static Builder getBlockBuilder(BlockType blockType) {

		if (BlockType.MANUAL_GOODS.equals(blockType)) {// 手动推荐商品
			ManualGoodsBlockBuilder manualGoodsBlockBuilder = SpringContextHolder.getBean("manualGoodsBlockBuilder");
			return manualGoodsBlockBuilder;
		} else if (BlockType.SINGLE_IMAGE.equals(blockType)) {// 单个图片
			 Builder panelBuilder = SpringContextHolder.getBean("signleImageBuilder");
			return panelBuilder;
		} else if (BlockType.BRAND.equals(blockType)) {// 品牌
			BrandBuilder brandBuilder = SpringContextHolder.getBean("brandBuilder");
			return brandBuilder;
		} else if (BlockType.MULTI_IMAGE.equals(blockType)) {// 多个图片
			MultiImageBuilder multiImageBuilder = SpringContextHolder.getBean("multiImageBuilder");
			return multiImageBuilder;
		}else if (BlockType.TEXT.equals(blockType)) {// 文本
			TextBuilder textBuilder = SpringContextHolder.getBean("textBuilder");
			return textBuilder;
		}else if (BlockType.AUTO_GOODS.equals(blockType)) {// 自动规则商品
			AutoGoodsBlockBuilder autoGoodsBlockBuilder = SpringContextHolder.getBean("autoGoodsBlockBuilder");
			return autoGoodsBlockBuilder;
		}

		return null;

	}

}
