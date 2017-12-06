package com.enation.app.core.event;

import java.util.List;

/**
 * 帮助变化
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月29日 下午4:54:12
 */
public interface IHelpChangeEvent {

	/**
	 * 帮助变化
	 * @param articeids
	 */
	public void helpChange(List<Integer> articeids);
}
