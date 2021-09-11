package com.hiberus.challenge.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

import static java.util.function.Predicate.not;

@Configuration
@Profile("sync")
public class SwaggerConfig {

    @Value("${swagger.title}")
    private String title;

    @Value("${swagger.description}")
    private String description;

    @Value("${swagger.version}")
    private String version;

    @Value("${swagger.termsOfService}")
    private String termsOfService;

    @Value("${swagger.contact.name}")
    private String teamName;

    @Value("${swagger.contact.url}")
    private String contactUrl;

    @Value("${swagger.contact.email}")
    private String contactEmail;

    /**
     * Documentation auto configuration.
     *
     * @return Docket object
     */
    @Bean
    public Docket documentation() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
                .apis(RequestHandlerSelectors.basePackage("com.hiberus.challenge"))
                .build()
                .pathMapping("/")
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                title,
                description,
                version,
                termsOfService,
                new Contact(teamName, contactUrl, contactEmail),
                null,
                null,
                Collections.emptyList());
    }
}
