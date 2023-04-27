package com.bcipriano.pharmacysystem.config;

import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.bcipriano.pharmacysystem.api.controller"))
                .paths(PathSelectors.any())
                .build()
                .securityContexts(Arrays.asList())
                .securitySchemes(Arrays.asList())
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("Pharmacy system")
                .description("Pharmacy sale systema")
                .version("1.0")
                .contact(contact())
                .build();
    }

    private Contact contact(){
        return new Contact("Bruno Cipriano Ribeiro"
                , "http://github.com/BrunoCiprianoDev",
                "cipriano990@gmail.com");
    }


}