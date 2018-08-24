package com.sshs.shiro;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import com.sshs.util.AuthrUtil;
import com.sshs.util.OrderedProperties;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;


public class ShiroUtil {
	
	public static String getInitDefinitions() {
		InputStream in = AuthrUtil.class.getResourceAsStream("/conf/properties/authr/init.properties");
		Properties prop = OrderedProperties.getProperties(in);
		StringBuilder sb = new StringBuilder();
		for(Object o : prop.keySet()) {
			String key = o.toString();
			sb.append(key).append(" = ").append(prop.get(key)).append("\n");
		}
		return sb.toString();
	}
	
	public static void reloadFilterChainDefinitions(ShiroFilterFactory shiroFilterFactory) throws Exception {
	   	AbstractShiroFilter shiroFilter = (AbstractShiroFilter) shiroFilterFactory.getObject();
        PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter.getFilterChainResolver();
        DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();
	    manager.getFilterChains().clear();
	    shiroFilterFactory.getFilterChainDefinitionMap().clear();
	    shiroFilterFactory.setFilterChainDefinitions("");
	    Map<String, String> chains = shiroFilterFactory.getFilterChainDefinitionMap();
	    for(Map.Entry<String, String> entry :chains.entrySet()) {
            String url = entry.getKey();
            String chainDefinition = entry.getValue().trim().replace(" ", "");
            manager.createChain(url, chainDefinition);
        }
	}
}
