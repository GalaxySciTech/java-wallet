package com.wallet.hsm.env


import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration


@ConfigurationProperties(prefix = "keystore")
@Configuration
open class KeyStoreProperties {
    var dir:String?=null
    var password:String="8c79cf3bf0db7ccf54bd75f52b942b9d3154a4d5"

    var eosDepositAddress:String?=null

}