package com.enation.app.shop.goods.service;

import java.util.List;

import com.enation.app.shop.goods.model.po.SpecValue;
import com.enation.app.shop.goods.model.po.Specification;
import com.enation.app.shop.goods.model.vo.SelectVo;
import com.enation.app.shop.goods.model.vo.SpecValueVo;
import com.enation.app.shop.goods.model.vo.SpecificationVo;
import com.enation.framework.database.Page;

/**
 * 
 * 规格
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年9月28日 下午1:54:12
 */
public interface ISpecManager {

	/**
	 * 添加规格
	 * 
	 * @param spec
	 * @param valueList
	 * @return
	 */
	public Specification add(Specification spec);

	/**
	 * 修改规格
	 * 
	 * @param spec
	 * @param valueList
	 */
	public void edit(Specification spec);

	/**
	 * 保存某规格的规格值
	 * 
	 * @param spec
	 *            规格实体
	 * @param specValueVo
	 *            组合后的规格值
	 */
	public void saveSpecValue(Integer spec_id, List<SpecValue> valueList);

	/**
	 * 删除规格
	 * 
	 * @param spec_id
	 *            规格id
	 */
	public void delete(Integer[] spec_id) throws Exception;

	/**
	 * 规格列表
	 * 
	 * @param page_no
	 * @param page_size
	 * @return
	 */
	public Page getSpecList(Integer page_no, Integer page_size);

	/**
	 * 规格详情
	 * 
	 * @param spec_id
	 *            规格id
	 * @return Specification
	 */
	public Specification get(Integer spec_id) throws Exception;

	/**
	 * 查询某规格的规格值
	 * 
	 * @param spec_id
	 *            规格id
	 * @param page_no
	 * @param page_size
	 * @return
	 */
	public List<SpecValueVo> getSpecValueList(Integer spec_id);

	/**
	 * 检测规格是否被使用
	 * 
	 * @param spec_id
	 *            规格id
	 * @return boolean
	 */
	public boolean checkUsed(Integer[] spec_id);

	/**
	 * 查询分类管理的规格，包括规格值
	 * 
	 * @param category_id
	 *            分类id
	 * @param goods_id
	 *            商品id
	 * @return List<SpecificationVo>
	 */
	public List<SpecificationVo> getSpecByCategory(Integer category_id, Integer goods_id);

	/**
	 * 查村分类绑定的规格（包括所有规格）
	 * 
	 * @param category_id
	 *            分类id
	 * @return List<SelectVo>
	 */
	public List<SelectVo> getCatSpec(Integer category_id);

	/**
	 * 检测某规格值是否被使用
	 * 
	 * @param spec_value_id
	 *            规格值id
	 * @return boolean
	 */
	public boolean checkUsed(Integer spec_value_id);

	/**
	 * 商家添加商品时 显示分类的关联的规格
	 * 
	 * @param catid
	 *            分类id
	 * @param seller_id
	 *            商家id
	 * @return List<Specification>
	 */
	public List<Specification> querySellerSpec(Integer catid, Integer seller_id);

	/**
	 * 读取规格列表
	 * 
	 * @return List
	 */
	public List list();

	/**
	 * 取出商品类型所绑定的规格列表
	 * 
	 * @param goodsTypeId
	 *            商品类型id
	 * @return List<Specification>
	 */
	public List<Specification> listSpecAndValueByType(int goodsTypeId, int goodsId);

	/**
	 * 查询某分类关联的规格
	 * 
	 * @param category_id
	 * @param goods_id
	 * @return
	 */
	public List<SpecificationVo> getCategorySpec(Integer category_id, Integer goods_id);

	/**
	 * 商家手动自定义添加规格项
	 * 
	 * @param specificationVo
	 * @return Specification
	 */
	public Specification addSellerSpec(SpecificationVo specificationVo);
}
