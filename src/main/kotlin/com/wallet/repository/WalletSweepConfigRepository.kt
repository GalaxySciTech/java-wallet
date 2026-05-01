package com.wallet.repository

import com.wallet.entity.domain.WalletSweepConfig
import org.springframework.data.jpa.repository.JpaRepository

interface WalletSweepConfigRepository: JpaRepository<WalletSweepConfig, Long> { fun findByChain(chain: String): WalletSweepConfig? }
