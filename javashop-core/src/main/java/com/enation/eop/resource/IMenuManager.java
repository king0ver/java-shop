package com.enation.eop.resource;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.resource.model.Menu;

/**
 * 
 * 菜单管理
 * 
 * @author lzf
 *         <p>
 *         2009-12-16 上午11:05:28
 *         </p>
 * @version 1.0
 */
public interface IMenuManager {

	/**
	 * 添加菜单项
	 * 
	 * @param menu 菜单
	 * @return
	 */
	public Integer add(Menu menu);

	/**
	 * 修改一个菜单项
	 * 
	 * @param menu 菜单
	 */
	public void edit(Menu menu);

	/**
	 * 读取菜单列表
	 * @return
	 */
	public List<Menu> getMenuList();

	/**
	 * 获取某个菜单的详细信息
	 * 
	 * @param id 菜单id
	 * @return
	 */
	public Menu get(Integer id);

	/**
	 * 根据名字获取菜单
	 * 
	 * @param title 名字
	 * @return
	 */
	public Menu get(String title);

	/**
	 * 读取某菜单列表并形成Tree格式
	 * 
	 * @param menuid 
	 *            要读取的顶级菜单id ,0为读取所有菜单
	 * @return
	 * @since 2.1.3
	 * @author kingapex
	 */
	public List<Menu> getMenuTree(Integer menuid);

	/**
	 * 根据id删除一个菜单
	 * 
	 * @param id 菜单id
	 * @throws RuntimeException如果存在子菜单则抛出此异常
	 */
	public void delete(Integer id) throws RuntimeException;

	/**
	 * 根据title删除一个菜单
	 * 
	 * @param title 菜单title
	 */
	public void delete(String title);

	/**
	 * 更新菜单排序
	 * 
	 * @param ids 菜单id
	 * @param sorts 排序方式
	 */
	public void updateSort(Integer[] ids, Integer[] sorts);

	/**
	 * 清除 一般用于站点安装时
	 */
	public void clean();
	/**
	 * 移动某个菜单 
	 * @param menuid 菜单id
	 * @param targetid 父id
	 * @param type <br> 移动次序
	 * inner:移入某个父
	 * prev:在某个之上
	 * next:在某个之后
	 */
	public void move(int menuid,int targetid,String type);
	
	/**
	 * 根据权限读取菜单列表
	 */
	public List<Menu> newMenutree(Integer menuid,AdminUser user);
	
	
	/**
	 * 根据管理员获取该管理员具有的所有菜单权限
	 * @param user 管理员实体
	 * @return
	 */
	public List<Menu> getMenuByUser(AdminUser user);

}
