package com.wallet.biz.config

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate


/** 
 * Created by pie on 2019-03-05 13: 41. 
 */
@Configuration
open class RestTemplateConfig {

    @Bean
    open fun rest(restTemplateBuilder: RestTemplateBuilder): RestTemplate {
        val restTemplate= restTemplateBuilder.build()
        restTemplate.messageConverters.add(WxMappingJackson2HttpMessageConverter())
        return restTemplate
    }

    class WxMappingJackson2HttpMessageConverter : MappingJackson2HttpMessageConverter() {
        init {
            val mediaTypes: MutableList<MediaType> = ArrayList<MediaType>()
            mediaTypes.add(MediaType.APPLICATION_OCTET_STREAM)
            supportedMediaTypes = mediaTypes // tag6
        }
    }

}
