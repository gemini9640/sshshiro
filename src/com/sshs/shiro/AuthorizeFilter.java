package com.sshs.shiro;

import org.apache.log4j.Logger;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

public class AuthorizeFilter extends AuthorizationFilter{
	private static Logger log = Logger.getLogger(AuthorizeFilter.class);
	protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        Subject subject = getSubject(servletRequest, servletResponse);
        String[] rolesOrPerms = (String[]) o;
        //无需权限
        if(rolesOrPerms == null || rolesOrPerms.length == 0)
            return true;
        //角色权限验证，一个通过则放行
        for (String roleOrPerm: rolesOrPerms ) {
        	log.info(subject.getPrincipal()+" has " + roleOrPerm + " "+(subject.hasRole(roleOrPerm) || subject.isPermitted(roleOrPerm)));
            if(subject.hasRole(roleOrPerm) || subject.isPermitted(roleOrPerm))
                return true;
        }
        return false;
    }
}
