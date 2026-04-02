package com.wallet.biz.config

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate
import java.time.Duration

@Configuration
open class RestTemplateConfig {

    @Bean
    open fun rest(restTemplateBuilder: RestTemplateBuilder): RestTemplate {
        val restTemplate = restTemplateBuilder
            .connectTimeout(Duration.ofSeconds(10))
            .readTimeout(Duration.ofSeconds(30))
            .build()
        restTemplate.messageConverters.add(OctetStreamJsonConverter())
        return restTemplate
    }

    private class OctetStreamJsonConverter : MappingJackson2HttpMessageConverter() {
        init {
            supportedMediaTypes = listOf(MediaType.APPLICATION_OCTET_STREAM)
        }
    }
}
