package com.enation.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 
 * 页数
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * 2017年8月15日 上午10:19:02
 */
@Target({ElementType.FIELD, ElementType.PARAMETER}) 
@Retention(RetentionPolicy.RUNTIME)  
public @interface PageNo {

}
