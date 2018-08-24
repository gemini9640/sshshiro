package com.sshs.action;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import com.sshs.common.Constants;
import com.sshs.pojo.Operator;
import com.sshs.service.AuthorizeService;
import com.sshs.service.OperatorService;
import com.sshs.util.AuthrUtil;
import com.sshs.vo.AuthrAuthorizationInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;



@Controller
@Scope("prototype")
public class LoginAction extends SubActionSupport {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(LoginAction.class);
	@Autowired
	private OperatorService operatorService;
	@Autowired
	private AuthorizeService authorizeService;

	public void validateLogin() {
		if (StringUtils.isEmpty(loginname))
			addFieldError("loginname", "请填写登录帐号");
		if (StringUtils.isEmpty(password))
			addFieldError("password", "请填写登录密码");
	}

	public String login() {
		if (getHttpSession().getAttribute(Constants.SESSION_OPERATORID) != null)
			return SUCCESS;
		Operator operator = (Operator) operatorService.getOperator(loginname);
		if (operator == null)
			setErrormsg("帐号不存在");
		else {
	        try {
	        	UsernamePasswordToken token = new UsernamePasswordToken(loginname, password);
				Subject subject = SecurityUtils.getSubject();
	            subject.login(token);
				getHttpSession().setAttribute(Constants.SESSION_OPERATORID, operator);
			    List<String> permissions = authorizeService.queryPermissionsByOperators(AuthrUtil.string2List(operator.getUsername()));
			    AuthrAuthorizationInfo authrAuthorizationInfo = new AuthrAuthorizationInfo();
			    authrAuthorizationInfo.setRoles(AuthrUtil.string2Set(operator.getRole()));
			    authrAuthorizationInfo.setPermission(new HashSet<String>(permissions));
			    AuthrUtil.AuthrConts.AUTHORIZATION_INFO.put(loginname, authrAuthorizationInfo);
			    AuthrUtil.AuthrConts.HOMEMENU.put(operator, authorizeService.organizeHomeMenu(operator));
				log.info("login operator: "+getOperatorLoginname());
				log.info("permissions: "+authrAuthorizationInfo);
	            return SUCCESS;
	        } catch (AuthenticationException e) {
	            log.error(e.getMessage());
	            setErrormsg("密码不正确");
	        }
			
		}
		return INPUT;
	}
	
	public void logout() {
		try {
			AuthrUtil.AuthrConts.HOMEMENU.remove(getOperatorLoginname());
			AuthrUtil.AuthrConts.AUTHORIZATION_INFO.remove(getHttpOperator());
			getHttpSession().invalidate();
			getResponse().sendRedirect(getRequest().getScheme()+"://"+getRequest().getServerName()+":"+getRequest().getServerPort()+getRequest().getContextPath()+"/index.jsp");
		} catch (IOException e) {
			log.error(e);
			responseWriter("登出错误。。。");
		}
	}

	private String loginname;
	private String password;
	private String errormsg;

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
