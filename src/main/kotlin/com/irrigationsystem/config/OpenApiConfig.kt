package com.irrigationsystem.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean

class OpenApiConfig {

    @Bean
    fun usersMicroserviceOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info().title("Automatic Land Irrigation Application")
                    .description("This documentation showcases the API's used to build an Automatic Land Irrigation Application.")
                    .version("1.0")
            )
    }
}