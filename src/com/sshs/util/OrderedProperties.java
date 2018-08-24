package com.sshs.util;

import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

public class OrderedProperties extends Properties{
	private static final long serialVersionUID = -2767430512711139453L;
	private static Logger log = Logger.getLogger(OrderedProperties.class);
	private final LinkedHashSet<Object> keys = new LinkedHashSet<>();  
    @Override  
    public Object put(Object key, Object value) {  
        keys.add(key);  
        return super.put(key, value);  
    } 
    
    @Override  
    public Set<Object> keySet() {  
        return keys;  
    }  
    
    public static OrderedProperties getProperties(InputStream ins) {  
		OrderedProperties prop = new OrderedProperties();
		if(ins == null)
			return prop;
		try {
			prop.load(ins);  
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e);
		} 
		return prop;
	}
}
