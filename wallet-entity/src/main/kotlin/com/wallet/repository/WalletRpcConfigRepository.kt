package com.wallet.repository

import com.wallet.entity.domain.WalletRpcConfig
import org.springframework.data.jpa.repository.JpaRepository

interface WalletRpcConfigRepository: JpaRepository<WalletRpcConfig, Long> {
    fun findByChain(chain: String): WalletRpcConfig?
}
