
package com.enation.app.base.core.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.base.core.service.IExampleDataCleanManager;
import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.UnProccessableServiceException;

/**
 * 
 * 示例数据清除
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月19日 下午1:26:03
 */
@RestController
@RequestMapping("/core/admin/example-data-clean")
public class ExampleDataCleanController {

	@Autowired
	private IExampleDataCleanManager exampleDataCleanManager;

	/**
	 * 跳转至清除演示数据页面
	 * 
	 * @return 清除演示数据页面
	 */
	@RequestMapping("/info")
	public ModelAndView execute() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/core/admin/data/clean");
		return view;
	}

	/**
	 * 清除演示数据
	 * 
	 * @param switchs
	 *            是否清除
	 */
	@GetMapping(value = "/clean")
	public void clean(String switchs) {
		// 判断是否为演示站点
		AdminUser user  = UserConext.getCurrentAdminUser();
		if(EopSetting.IS_DEMO_SITE && user!= null && user.getFounder() != 1){
			throw new UnProccessableServiceException(ErrorCode.INVALID_REQUEST_PARAMETER, EopSetting.DEMO_SITE_TIP);
		}
		// 清除演示数据
		if (switchs != null) {
			this.exampleDataCleanManager.clean();
		}else {
			throw new UnProccessableServiceException(ErrorCode.INVALID_REQUEST_PARAMETER, "开关未打开");
		}
	}
}
