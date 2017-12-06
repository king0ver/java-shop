package com.enation.framework.plugin;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 
 * 插件注解
 * @author zh
 * @version v1.0
 * @since v6.4.0
 * 2017年9月7日 下午8:28:19
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Bundle {
	/**
	 * 注解
	 * @return	类
	 */
	public Class value();


}
