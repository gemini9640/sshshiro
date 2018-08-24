package com.sshs.service.impl;

import com.sshs.common.Constants;
import com.sshs.dao.AuthorizeDao;
import com.sshs.dao.OperatorDao;
import com.sshs.pojo.Operator;
import com.sshs.service.OperatorService;
import com.sshs.vo.ReturnBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.LockMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("operatorService")
public class OperatorServiceImpl implements OperatorService {
	private static Logger log = Logger.getLogger(OperatorServiceImpl.class);
	@Autowired
	private AuthorizeDao authorizeDao;
	@Autowired
	private OperatorDao operatorDao;

	
	public Operator getOperator(String loginname) {
		return (Operator) operatorDao.get(Operator.class, loginname);
	}

	public ReturnBean modifyOwnPassword(String operator, String oldPassword, String newPassword) {
		String msg = null;
		Operator currentOperator = (Operator) operatorDao.get(Operator.class, operator, LockMode.UPGRADE);
		if (currentOperator == null) {
			msg = "帐号不存在";
		} else if (!currentOperator.getPassword().equals(oldPassword)) {
			msg = "旧密码不正确";
		} else if (oldPassword.equals(newPassword)) {
			msg = "新密码与旧密码相同";
		} else {
			currentOperator.setPassword(newPassword);
			operatorDao.update(currentOperator);
			msg = null;
		}
		return new ReturnBean(msg, currentOperator);
	}

	public ReturnBean createSubOperator(String newOperator, String password, String authority, String operator) {
		String msg = null;
		Operator currentOperator = (Operator) operatorDao.get(Operator.class, operator);
		if (StringUtils.isEmpty(authority))
			msg = "请选择用户等级";
		else if (currentOperator == null) {
			msg = "帐号不存在";
		} else if (operatorDao.get(Operator.class, newOperator) != null) {
			msg = "帐号已存在";
		} else {
			Operator newOp = new Operator(newOperator, password, authority);
			operatorDao.save(newOp);
			authorizeDao.saveOperatorRole(newOp.getUsername(), authority);
			msg = null;
			return new ReturnBean(msg, newOp);
		}
		return new ReturnBean(msg, currentOperator);
	}
}
