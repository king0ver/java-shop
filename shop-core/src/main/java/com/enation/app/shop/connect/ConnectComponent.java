package com.enation.app.shop.connect;

import com.enation.app.base.core.service.ISettingService;
import com.enation.app.base.core.service.auth.IAuthActionManager;
import com.enation.app.base.core.service.auth.impl.PermissionConfig;
import com.enation.eop.SystemSetting;
import com.enation.eop.resource.IMenuManager;
import com.enation.eop.resource.model.Menu;
import com.enation.framework.component.IComponent;
import com.enation.framework.component.IComponentStartAble;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.IDaoSupport;

import org.springframework.stereotype.Component;

/**
 * 联合登录组件
 *
 * @author Dawei
 *         2016-4-16
 */
@Component
public class ConnectComponent implements IComponent, IComponentStartAble {
	
	public static final String CONNECT_USER_SESSION_KEY = "connectuser";
	
    private ISettingService settingService;
    private IDaoSupport daoSupport;
    private IMenuManager menuManager;
    private IAuthActionManager authActionManager;

    /**
     * 安装lucene组件时写入lucene的设置
     */
    @Override
    public void install() {
//        daoSupport.execute("insert into es_settings (cfg_group) values ( '" + ConnectSetting.SETTING_KEY + "')");
//
//        //给es_member表增加字段
//        try {
//            daoSupport.execute("alter table es_member add qq_id varchar(255) default ''");
//        }catch(Exception ex){}
//        try {
//            daoSupport.execute("alter table es_member add wechat_id varchar(255) default ''");
//        }catch(Exception ex){}
//        try {
//            daoSupport.execute("alter table es_member add weibo_id varchar(255) default ''");
//        }catch(Exception ex){}
//
//        this.addMenu();
    }
    
    @Override
	public void start() {}

    private static final String parentMenuName = "网店设置";

    /**
     * 为组件添加菜单
     */
    private void addMenu() {
        int superAdminAuthId = PermissionConfig.getAuthId("super_admin"); // 超级管理员权限id

        Menu parentMenu = menuManager.get(parentMenuName);
        Menu menu = new Menu();
        menu.setTitle("信任登录");
        menu.setPid(parentMenu.getId());
        menu.setUrl("/shop/admin/connect-setting.do");
        menu.setSorder(105);
        menu.setMenutype(Menu.MENU_TYPE_APP);
        this.menuManager.add(menu);
      //将session中的菜单列表数据移除
		ThreadContextHolder.getSession().removeAttribute(SystemSetting.menuListKey.toString());
    }


    /**
     * 删除组件相应的菜单
     */
    private void deleteMenu() {
        int superAdminAuthId = PermissionConfig.getAuthId("super_admin"); // 超级管理员权限id
        Menu menu = menuManager.get("信任登录");
        if (menu != null) {
            int addmenuid = menu.getId();
            this.authActionManager.deleteMenu(superAdminAuthId, new Integer[]{addmenuid});
            this.menuManager.delete("信任登录");
        }
      //将session中的菜单列表数据移除
		ThreadContextHolder.getSession().removeAttribute(SystemSetting.menuListKey.toString());
    }

    /**
     * 卸载lucene组件时删除lucene的设置
     */
    @Override
    public void unInstall() {
//        settingService.delete("'" + ConnectSetting.SETTING_KEY + "'");
//        deleteMenu();
    }

    public ISettingService getSettingService() {
        return settingService;
    }

    public void setSettingService(ISettingService settingService) {
        this.settingService = settingService;
    }

    public IDaoSupport getDaoSupport() {
        return daoSupport;
    }

    public void setDaoSupport(IDaoSupport daoSupport) {
        this.daoSupport = daoSupport;
    }

    public IMenuManager getMenuManager() {
        return menuManager;
    }

    public void setMenuManager(IMenuManager menuManager) {
        this.menuManager = menuManager;
    }

    public IAuthActionManager getAuthActionManager() {
        return authActionManager;
    }

    public void setAuthActionManager(IAuthActionManager authActionManager) {
        this.authActionManager = authActionManager;
    }

}
