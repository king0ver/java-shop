package com.enation.app.base.data;

import org.junit.Test;

import com.enation.app.base.core.service.dbsolution.DBSolutionFactory;
import com.enation.framework.test.SpringTestSupport;

/**
 * 示例数据输出工具
 * @author kingapex
 * @verison 1.0
 * @since 6.4
 * 2017年9月11日下午5:52:16
 */
public class ExampleDataExport extends SpringTestSupport {
		
	
	/**
	 * 导出一个或多个表的示例数据
	 * 首先要保证src/test/reources/中config下的数据库配置正确
	 */
	@Test
	public void export() {
		/** 如果是多个表，可以加在数组中 **/
		String[] tables = {"es_goods"}; 
		String xmlData = DBSolutionFactory.dbExport(tables, true, "");
		System.out.println(xmlData);
	}
}
