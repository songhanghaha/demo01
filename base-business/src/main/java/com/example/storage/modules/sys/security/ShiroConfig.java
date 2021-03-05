package com.example.storage.modules.sys.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
//import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;

@Configuration
public class ShiroConfig {

	@Bean
	public SSORealm ssoRealm() {
		return new SSORealm();
	}
	
//	@Bean
//	public EhCacheManager cacheManager() {
//		EhCacheManager cacheManager = new EhCacheManager();
//		//cacheManager.setCacheManagerConfigFile("classpath:cache/ehcache-local.xml");
//		return cacheManager;
//	}
//	
//	@Bean
//	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
//		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
//		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
//		return authorizationAttributeSourceAdvisor;
//	}
//
//	@Bean
//	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
//		DefaultAdvisorAutoProxyCreator proxy = new DefaultAdvisorAutoProxyCreator();
//		proxy.setProxyTargetClass(true);
//		return proxy;
//	}
	
}
