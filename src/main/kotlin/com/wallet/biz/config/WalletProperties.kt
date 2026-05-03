package com.wallet.biz.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "wallet")
data class WalletProperties(
    var signing: SigningProps = SigningProps(),
    var externalSigner: ExternalSignerProps = ExternalSignerProps(),
    var tokencore: TokencoreProps = TokencoreProps()
) {
    data class SigningProps(
        /** Default when wallet_chain_config.signing_backend is null */
        var defaultBackend: String = "TOKENCORE"
    )

    data class ExternalSignerProps(
        var baseUrl: String = "",
        var connectTimeoutMs: Int = 5000,
        var readTimeoutMs: Int = 30000
    )

    /** Chain id strings passed to tokencore signTransaction (mainnet vs testnet). */
    data class TokencoreProps(
        var ethereumChainId: String = "1",
        var bitcoinChainId: String = "0"
    )
}
