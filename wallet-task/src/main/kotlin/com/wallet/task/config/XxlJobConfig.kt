package com.wallet.task.config

import com.wallet.task.env.XxlJobProperties
import org.springframework.context.annotation.Configuration
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean


/** 
 * Created by pie on 2019-03-12 15: 32. 
 */
@Configuration
@EnableConfigurationProperties(XxlJobProperties::class)
open class XxlJobConfig {

    @Bean
    open fun xxlJobExecutor(): XxlJobSpringExecutor {
        val xxlJobSpringExecutor = XxlJobSpringExecutor()
        xxlJobProperties.adminAddresses?.let {
            xxlJobSpringExecutor.setAdminAddresses(it)
        }
        xxlJobProperties.appName?.let {
            xxlJobSpringExecutor.setAppname(it)
        }
        xxlJobProperties.ip?.let {
            xxlJobSpringExecutor.setIp(it)
        }
        xxlJobProperties.port?.let {
            xxlJobSpringExecutor.setPort(it)
        }
        xxlJobProperties.accessToken?.let {
            xxlJobSpringExecutor.setAccessToken(it)
        }
        xxlJobProperties.logPath?.let {
            xxlJobSpringExecutor.setLogPath(it)
        }
        xxlJobProperties.logRetentionDays?.let {
            xxlJobSpringExecutor.setLogRetentionDays(it)
        }
        return xxlJobSpringExecutor
    }

    @Autowired
    lateinit var xxlJobProperties: XxlJobProperties
}
