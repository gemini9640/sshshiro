package com.sshs.shiro;

import java.util.List;

import com.sshs.pojo.AuthrPermmission;
import com.sshs.pojo.AuthrRolePermmission;
import com.sshs.service.AuthorizeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.config.Ini;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.config.IniFilterChainResolverFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;


public class ShiroFilterFactory extends ShiroFilterFactoryBean {
	private static Logger log = Logger.getLogger(ShiroFilterFactory.class);	
    @Autowired
    private AuthorizeService authorizeService;

	@Override
    public void setFilterChainDefinitions(String definitions) {
    	definitions = ShiroUtil.getInitDefinitions();
        List<AuthrPermmission> aPermmissions = authorizeService.queryPermissions(null, null, null);
        List<AuthrRolePermmission> aRolePermmissions = authorizeService.queryRolePermissionSelective(null, null);
        StringBuilder sb = new StringBuilder();
       for (AuthrPermmission p : aPermmissions) {
			StringBuilder sbRole = new StringBuilder();
			for (AuthrRolePermmission rp : aRolePermmissions) {
				if(StringUtils.equals(rp.getId().getPermmission(), p.getPermission()))
					sbRole.append(rp.getId().getRole()+",");
			}
			sb.append(p.getUrl() + " = authorizeFilter["+sbRole.toString()+p.getPermission()+"]\n");
		}
        definitions += sb.append("/office/** = authc").toString();
        log.info(definitions);


        //从配置文件加载权限配置
        Ini ini = new Ini();
        ini.load(definitions);
        Ini.Section section = ini.getSection(IniFilterChainResolverFactory.URLS);
        if (CollectionUtils.isEmpty(section)) {
            section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
        }
        setFilterChainDefinitionMap(section);
    }
}
