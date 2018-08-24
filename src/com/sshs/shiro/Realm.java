package com.sshs.shiro;

import com.sshs.pojo.Operator;
import com.sshs.service.OperatorService;
import com.sshs.util.AuthrUtil;
import com.sshs.vo.AuthrAuthorizationInfo;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;


public class Realm extends AuthorizingRealm {
	@Autowired
	private OperatorService operatorService;

    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        AuthrAuthorizationInfo authrAuthorizationInfo = AuthrUtil.AuthrConts.AUTHORIZATION_INFO.get(username);
        info.setRoles(authrAuthorizationInfo.getRoles());
        info.setStringPermissions(authrAuthorizationInfo.getPermission());
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //1.从主体传过来的认证信息获得用户名
        String username = (String) authenticationToken.getPrincipal();
        //2.通过用户名到数据库中获取凭证
        Operator operator = (Operator) operatorService.getOperator(username);
        if (operator == null)
        	return null;
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(username, operator.getPassword(), getName());
        return info;
    }
}
