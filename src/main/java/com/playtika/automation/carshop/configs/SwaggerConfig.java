package com.playtika.automation.carshop.configs;

import com.google.common.collect.ImmutableList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .globalResponseMessage(RequestMethod.GET, ImmutableList.of(new ResponseMessage(200, "OK",null, null, null)))
                .globalResponseMessage(RequestMethod.POST, ImmutableList.of(new ResponseMessage(200, "OK",null, null, null)))
                .globalResponseMessage(RequestMethod.DELETE, ImmutableList.of(new ResponseMessage(200, "OK",null, null, null)))
                .globalResponseMessage(RequestMethod.PUT, ImmutableList.of(new ResponseMessage(200, "OK",null, null, null)))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.playtika.automation.carshop.web"))
//                .paths(regex("/cars.*"))
//                .paths(regex("/deal.*"))
                .build()
                .apiInfo(metaData());
    }

    private ApiInfo metaData() {
        ApiInfo apiInfo = new ApiInfo(
                "Spring Boot REST API",
                "Spring Boot REST API for Online Store",
                "1.0",
                "Terms of service",
                "Evgenija Muravjova",
                "",
                "");
        return apiInfo;
    }

}
