package com.sshs.action;

import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sshs.common.Constants;
import com.sshs.pojo.Operator;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
public class SubActionSupport extends ActionSupport {
	private static Logger log = Logger.getLogger(SubActionSupport.class);
	private static final long serialVersionUID = 1L;

	public Operator getHttpOperator() {
		return (Operator) getHttpSession().getAttribute(Constants.SESSION_OPERATORID);
	}
	
	public String getOperatorLoginname() {
		Operator operator = (Operator) getHttpOperator();
		log.info("get operator: "+operator);
		if (operator != null)
			return operator.getUsername();
		else
			return null;
	}

	public void responseWriter(String content) {
		try {
			getResponse().setContentType("text/plain;charset=" + Constants.ENCODING + "");
			PrintWriter writer = getResponse().getWriter();
			writer.write(content);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getStackTrace());
		}
	}
	
	public Object json2Bean(String json, Class<?> cls) {
		return JSONObject.toBean(JSONObject.fromObject(json), cls);
	}
	
	public Object json2BeanList(String json, Class<?> cls) {
		return JSONArray.toCollection(JSONArray.fromObject(json), cls);
	}
	
	public HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	public HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	public HttpSession getHttpSession() {
		return getRequest().getSession();
	}

	public ServletContext getServltContext() {
		return ServletActionContext.getServletContext();
	}

}
