package com.wallet.hsm.config

import com.wallet.hsm.env.KeyStoreProperties
import org.consenlabs.tokencore.wallet.Identity
import org.consenlabs.tokencore.wallet.KeystoreStorage
import org.consenlabs.tokencore.wallet.WalletManager
import org.consenlabs.tokencore.wallet.model.Metadata
import org.consenlabs.tokencore.wallet.model.Network
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import javax.annotation.PostConstruct

/**
 * Created by pie on 2019/2/15 15: 36.
 */
@Configuration
@EnableConfigurationProperties(KeyStoreProperties::class)
open class ScanKeyStoreConfig:KeystoreStorage{

    override fun getKeystoreDir(): File {
        return File(keyStoreProperties.dir)
    }

    @PostConstruct
    fun init() {
        try {
            Files.createDirectories(Paths.get("${keyStoreProperties.dir}/wallets"))
        } catch (ignored: Throwable) {
        }
        WalletManager.storage = this
        WalletManager.scanWallets()
        val identity = Identity.getCurrentIdentity()
        if (identity == null) {
            Identity.createIdentity(
                "token",
                keyStoreProperties.password,
                "",
                Network.MAINNET,
                Metadata.P2WPKH
            )
        }
    }

    @Autowired
    lateinit var keyStoreProperties: KeyStoreProperties
}