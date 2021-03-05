package com.example.storage.modules.sys.token;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenConfig implements WebMvcConfigurer {
	@Bean
	public HandlerInterceptor getTokenInterceptor() {
		return new TokenInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		log.info("---------进入addInterceptors--------");
		InterceptorRegistration ir = registry.addInterceptor(getTokenInterceptor());
		// 拦截项
		List<String> addPathPatternsList = new ArrayList<String>();
		addPathPatternsList.add("/tsp/app/**");
		// 排除拦截项
		List<String> excludeList = new ArrayList<String>();
		excludeList.add("/tsp/app/sendCode");
		excludeList.add("/tsp/app/telLogin");
		excludeList.add("/tsp/app/getKssCertChain");

		//设置
		ir.addPathPatterns(addPathPatternsList);
		ir.excludePathPatterns(excludeList);		
	}
    /**
     * 支持跨域配置
     */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		log.info("---------进入addCorsMappings--------");
		registry.addMapping("/**").allowedOrigins("*").allowedHeaders("*").allowCredentials(true)
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS").maxAge(3600);
	}
}
