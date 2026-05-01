package com.wallet.repository

import com.wallet.entity.domain.WalletChainConfig
import org.springframework.data.jpa.repository.JpaRepository

interface WalletChainConfigRepository: JpaRepository<WalletChainConfig, Long> {
    fun findByChain(chain: String): WalletChainConfig?
}
