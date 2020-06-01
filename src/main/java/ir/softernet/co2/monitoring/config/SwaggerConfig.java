package ir.softernet.co2.monitoring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * Configuration for enabling Swagger-ui.html API documentation
 *
 * @author saman
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

  @Bean
  public Docket swaggerApiV1() {
    return new Docket(DocumentationType.SWAGGER_2)
      .groupName("ver. 1")
      .select()
      .apis(RequestHandlerSelectors.basePackage("ir.softernet.co2.monitoring.controller.v1"))
      .paths(regex("/api/v1/.*"))
      .build()
      .apiInfo(new ApiInfoBuilder().version("1").title("Version 1 APIs").description("Documentation of APIs (v1)").build());
  }

}
