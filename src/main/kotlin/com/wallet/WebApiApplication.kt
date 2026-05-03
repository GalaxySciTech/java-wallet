package com.wallet

import com.wallet.biz.config.WalletProperties
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties

/** 
 * Created by pie on 2019-04-11 15: 53. 
 */
@SpringBootApplication
@EnableConfigurationProperties(WalletProperties::class)
open class WebApiApplication

fun main() {
    SpringApplication.run(WebApiApplication::class.java)
}
