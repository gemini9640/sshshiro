package com.sshs.vo;

import java.io.Serializable;

public class ReturnBean implements Serializable{
	private static final long serialVersionUID = -1958193542446741587L;

	public ReturnBean(String msg, Object bean) {
		super();
		this.msg = msg;
		this.bean = bean;
	}

	public ReturnBean() {
	}

	private String msg;

	private Object bean;


	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getBean() {
		return bean;
	}

	public void setBean(Object bean) {
		this.bean = bean;
	}

}
