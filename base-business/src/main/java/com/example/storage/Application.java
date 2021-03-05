package com.example.storage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.web.WebApplicationInitializer;

import com.example.sso.client.SSOConfiguration;
import com.example.storage.common.config.MybatisPlusConfig;
import com.example.storage.common.config.RedisConfig;
import com.example.storage.modules.sys.token.TokenConfig;

@MapperScan({ "com.example.storage.modules.*.dao" })
@SpringBootApplication
@Import({ SSOConfiguration.class, MybatisPlusConfig.class, RedisConfig.class, TokenConfig.class })
public class Application   extends SpringBootServletInitializer implements WebApplicationInitializer{

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(Application.class, args);
	}

	@Override
	 protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
	  }
}
