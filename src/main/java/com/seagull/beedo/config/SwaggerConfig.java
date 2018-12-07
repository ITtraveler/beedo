package com.seagull.beedo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author guosheng.huang
 * @version $Id: SwaggerConfig, v1.0 2018年11月08日 14:16 guosheng.huang Exp $
 */
@Configuration
public class SwaggerConfig {
    /*** swagger ***/
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
            .apis(RequestHandlerSelectors.basePackage("com.seagull.beedo.web.controller"))
            .paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("beedo RESTful APIs").description("beedo api接口文档")
            .version("1.0").build();
    }
}
