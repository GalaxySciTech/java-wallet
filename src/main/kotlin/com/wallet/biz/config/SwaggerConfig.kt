package com.wallet.biz.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.security.SecurityRequirement
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class SwaggerConfig {

    @Bean
    open fun openAPI(): OpenAPI {
        val securitySchemeName = "access_token"
        return OpenAPI()
            .info(
                Info()
                    .title("Java-Wallet API")
                    .description("Multi-chain cryptocurrency wallet REST API")
                    .version("3.0.0")
                    .license(License().name("Apache 2.0").url("http://www.apache.org/licenses/LICENSE-2.0.html"))
            )
            .addSecurityItem(SecurityRequirement().addList(securitySchemeName))
            .components(
                Components()
                    .addSecuritySchemes(
                        securitySchemeName,
                        SecurityScheme()
                            .name(securitySchemeName)
                            .type(SecurityScheme.Type.APIKEY)
                            .`in`(SecurityScheme.In.HEADER)
                    )
            )
    }
}
