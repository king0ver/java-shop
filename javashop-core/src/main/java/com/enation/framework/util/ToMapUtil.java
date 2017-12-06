package com.enation.framework.util;

import java.util.HashMap;
import java.util.Iterator;

import net.sf.json.JSONObject;

/**
 * 将对象转换为map 
 * @author Chopper
 * @version v1.0
 * @since pangu1.0
 * 2017年5月8日 下午4:03:39
 */
public class ToMapUtil {

	/**
	 * to map
	 * s
	 * @param object
	 * @return
	 */
	public static HashMap toHashMap(Object object) {
		HashMap  data = new HashMap();
		// 将json字符串转换成jsonObject
		JSONObject jsonObject = JSONObject.fromObject(object);
		@SuppressWarnings("rawtypes")
		Iterator it = jsonObject.keys();
		// 遍历jsonObject数据，添加到Map对象
		while (it.hasNext()) {
			String key = String.valueOf(it.next());
			Object value = jsonObject.get(key);
			data.put(key, value);
		}
		return data;
	}
}
