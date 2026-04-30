package com.wallet.biz.config

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
@EnableCaching
open class CacheConfig {

    @Bean
    open fun cacheManager(): CacheManager {
        val caffeine = Caffeine.newBuilder()
            .initialCapacity(100)
            .maximumSize(5000)
            .expireAfterWrite(30, TimeUnit.SECONDS)
            .recordStats()
        val cacheManager = CaffeineCacheManager()
        cacheManager.setCaffeine(caffeine)
        return cacheManager
    }
}
