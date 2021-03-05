package com.example.storage.modules;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicates;

import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config {

    com.google.common.base.Predicate<RequestHandler> selector4 = RequestHandlerSelectors.basePackage("com.example.storage.modules.sys.controller");
    com.google.common.base.Predicate<RequestHandler> selector5 = RequestHandlerSelectors.basePackage("com.example.storage.modules.user.controller");
    com.google.common.base.Predicate<RequestHandler> selector6 = RequestHandlerSelectors.basePackage("com.example.storage.modules.wxuser.controller");
    com.google.common.base.Predicate<RequestHandler> selector7 = RequestHandlerSelectors.basePackage("com.example.storage.modules.expresstake.controller");
    //配置swagger的docket的bean实例
    @Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2).securitySchemes(Arrays.asList(new BasicAuth("test")))
				.apiInfo(apiInfo()).select()
				.apis(Predicates.or(selector4,selector5,selector6,selector7))
				.paths(PathSelectors.any()).build();
	}

	ApiKey apiKey() {
		return new ApiKey("sessionId", "sessionId", "header");
	}
    //配置swagger信息
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("base系统接口").description("base系统接口").version("1.0").build();
	}
}
