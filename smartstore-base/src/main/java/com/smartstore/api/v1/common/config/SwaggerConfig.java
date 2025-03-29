package com.smartstore.api.v1.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Primary
@Profile({ "dev", "local" })
@Configuration
public class SwaggerConfig {
  @Bean
  public OpenAPI customOpenAPI() {
    var info = new Info().title("smart-store API Docs").version("v1").description("스마트스토어(가칭)의 API 명세");
    var accessTokenName = "accessToken";
    var securityRequirement = new SecurityRequirement().addList(accessTokenName);
    var components = new Components()
        .addSecuritySchemes(accessTokenName, new SecurityScheme()
            .name(accessTokenName)
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT"));

    return new OpenAPI()
        .info(info)
        .addSecurityItem(securityRequirement)
        .components(components);

  }

}