package com.wallet.task.env

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

/** 
 * Created by pie on 2019-03-12 15: 34. 
 */
@ConfigurationProperties(prefix = "xxl.job")
@Configuration
open class XxlJobProperties{

     var adminAddresses: String? = null

     var appName: String? = null

     var ip: String? = null

     var port: Int ?= null
    
     var accessToken: String? = null

     var logPath: String? = null

     var logRetentionDays: Int ?= null

}