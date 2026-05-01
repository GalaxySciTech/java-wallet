package com.wallet.repository

import com.wallet.entity.domain.WalletSchedulerConfig
import org.springframework.data.jpa.repository.JpaRepository

interface WalletSchedulerConfigRepository: JpaRepository<WalletSchedulerConfig, Long> {
    fun findByChain(chain: String): WalletSchedulerConfig?
}
