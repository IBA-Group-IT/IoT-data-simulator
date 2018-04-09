package com.iba.iot.datasimulator.common.config;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * Created by Alex on 28.08.2017.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /** **/
    public static String PROJECT_TITLE = "IoT Data Generator";

    /** **/
    public static String PROJECT_DESCRIPTION = "IoT Data Generator REST API";

    /** **/
    public static String BASE_PACKAGE = "com.iba.iot.datasimulator";

    /** **/
    public static String ENDPOINT_PATHS = "/*.*";

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
            .paths(getPaths())
            .build()
            .apiInfo(getMetaData());
    }

    private Predicate<String> getPaths() {
        return regex(ENDPOINT_PATHS);
    }

    private ApiInfo getMetaData() {
        return new ApiInfoBuilder()
            .title(PROJECT_TITLE)
            .description(PROJECT_DESCRIPTION)
            .build();
    }
}