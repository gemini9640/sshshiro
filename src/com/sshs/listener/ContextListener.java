package com.sshs.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.sshs.service.AuthorizeService;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


public class ContextListener implements ServletContextListener {

	private Logger log;
	private WebApplicationContext springContext; 
	
	public ContextListener() {
		log = Logger.getLogger(ContextListener.class);
	}

	public void contextDestroyed(ServletContextEvent arg0) {
		log.debug("LoadConstantListener Destroyed");
	}

	public void contextInitialized(ServletContextEvent ctx) {
		log.debug("contextInitialized");
		log.info("load springContext...");
		springContext = WebApplicationContextUtils.getWebApplicationContext(ctx.getServletContext());
		AuthorizeService authorizeService = (AuthorizeService) springContext.getBean("authorizeService");
		ctx.getServletContext().setAttribute("OperatorRole", authorizeService.queryRoles());
	}
}
