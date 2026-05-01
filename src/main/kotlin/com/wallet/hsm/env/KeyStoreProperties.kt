package com.wallet.hsm.env

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@ConfigurationProperties(prefix = "keystore")
@Configuration
open class KeyStoreProperties {
    var dir: String? = null
    var password: String = ""
    var eosDepositAddress: String? = null
}
