package com.bcipriano.pharmacysystem.config;

import com.bcipriano.pharmacysystem.api.dto.saleDTO.SaleReadDTO;
import com.bcipriano.pharmacysystem.api.dto.saleItemDTO.SaleItemReadDTO;
import com.bcipriano.pharmacysystem.api.dto.saleItemDTO.SaleItemWriteDTO;
import com.bcipriano.pharmacysystem.model.entity.*;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger.web.OperationsSorter;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;

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
                .apiInfo(apiInfo())
                .ignoredParameterTypes(
                        Address.class,
                        Client.class,
                        DiscountGroup.class,
                        Employee.class,
                        Loss.class,
                        Lot.class,
                        Merchandise.class,
                        Purchase.class,
                        Return.class,
                        Sale.class,
                        SaleItem.class,
                        Supplier.class,
                        SaleItemReadDTO.class,
                        SaleReadDTO.class
                );
    }

    @Bean
    public UiConfiguration uiConfig() {
        return UiConfigurationBuilder
                .builder()
                .operationsSorter(OperationsSorter.METHOD)
            .build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("Pharmacy system")
                .description("Pharmacy management system")
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
