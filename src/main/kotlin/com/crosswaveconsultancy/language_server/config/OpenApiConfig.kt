package com.crosswaveconsultancy.language_server.config

import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityRequirement
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI().info(
            Info()
                .title("Language Learning App CMS API")
                .version("1.0.0")
                .description("API documentation for the Language Learning App by Cross Wave Consultancy")
        ).addSecurityItem(SecurityRequirement().addList("jwt", listOf("read", "write")))
    }
}
