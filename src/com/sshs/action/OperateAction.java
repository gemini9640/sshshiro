package com.sshs.action;


import com.sshs.service.OperatorService;
import com.sshs.vo.ReturnBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;


@Controller
@Scope("prototype")
public class OperateAction extends SubActionSupport {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(OperateAction.class);
	@Autowired
	private OperatorService operatorService;

	public String modifyOwnPwd() {
		ReturnBean returnBean = operatorService.modifyOwnPassword(getOperatorLoginname(), oldPassword, newPassword);
		if (returnBean.getMsg() == null)
			setErrormsg("修改成功");
		else
			setErrormsg(returnBean.getMsg());
		return INPUT;
	}

	public String createSubOperator() {
		ReturnBean returnBean = operatorService.createSubOperator(newOperator, password, authority, getOperatorLoginname());
		if (returnBean.getMsg() == null)
			setErrormsg("添加成功");
		else
			setErrormsg(returnBean.getMsg());
		return INPUT;
	}

	private String errormsg;
	private String oldPassword;
	private String newPassword;
	private String newOperator;
	private String password;
	private String authority;

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getNewOperator() {
		return newOperator;
	}

	public void setNewOperator(String newOperator) {
		this.newOperator = newOperator;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}
}
