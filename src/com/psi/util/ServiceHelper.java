package com.psi.util;

import com.psi.service.system.menu.impl.MenuService;
import com.psi.service.system.role.impl.RoleService;
import com.psi.service.system.user.UserManager;

/** 
 * 说明：ServiceHelper
 */
public final class ServiceHelper {
	
	public static Object getService(String serviceName){
		//WebApplicationContextUtils.
		return Const.WEB_APP_CONTEXT.getBean(serviceName);
	}
	
	public static UserManager getUserService(){
		return (UserManager) getService("userService");
	}
	
	public static RoleService getRoleService(){
		return (RoleService) getService("roleService");
	}
	
	public static MenuService getMenuService(){
		return (MenuService) getService("menuService");
	}
}
