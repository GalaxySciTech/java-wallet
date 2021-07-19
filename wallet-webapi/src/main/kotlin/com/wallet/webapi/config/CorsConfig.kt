package com.wallet.webapi.config

import com.wallet.biz.rpc.RpcClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import java.math.BigInteger
import javax.annotation.PostConstruct


/** 
 * Created by pie on 2020/12/7 19: 48. 
 */
@Configuration
open class CorsConfig {

    private fun corsConfig(): CorsConfiguration {
        val corsConfiguration = CorsConfiguration()
        corsConfiguration.addAllowedOrigin("*") //允许所有域名访问
        corsConfiguration.addAllowedHeader("*") //允许所有请求头
        corsConfiguration.addAllowedMethod("*") //允许所有的请求类型
        corsConfiguration.maxAge = 3600L
        corsConfiguration.allowCredentials = true //允许请求携带验证信息（cookie）
        return corsConfiguration
    }

    @Bean
    open fun corsFilter(): CorsFilter { //存储request与跨域配置信息的容器，基于url的映射
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", corsConfig())
        return CorsFilter(source)
    }
}
