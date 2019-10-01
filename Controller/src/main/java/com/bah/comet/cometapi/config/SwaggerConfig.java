package com.bah.comet.cometapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	@Primary
	public Docket apiWorkflow() {
		return new Docket(DocumentationType.SWAGGER_2)
				.globalOperationParameters(
						Arrays.asList(new ParameterBuilder()
								.name("Authorization")
								.description("Security Token")
								.modelRef(new ModelRef("string"))
								.parameterType("header")
								.required(false)
								.build()))
				.groupName("RestAPI").apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.bah.comet.cometapi.controller"))
				.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfo("BAH Comet API Service",
				"<p>Comet Tech Challenge REST API", "V1",
				null, new Contact("vargas_raymond@bah.com", null, "vargas_raymond@bah.com"), null, null,
				Collections.emptyList());
	}
}