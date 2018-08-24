package com.sshs.service;

import com.sshs.pojo.Operator;
import com.sshs.vo.ReturnBean;

public interface OperatorService {
	
	public Operator getOperator(String loginname);
	
	public ReturnBean modifyOwnPassword(String operator, String oldPassword, String newPassword);

	public ReturnBean createSubOperator(String newOperator, String password, String authority, String operator);
}
