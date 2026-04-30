package com.wallet.repository

import com.wallet.entity.domain.WalletWithdrawConfig
import org.springframework.data.jpa.repository.JpaRepository

interface WalletWithdrawConfigRepository: JpaRepository<WalletWithdrawConfig, Long> { fun findByChain(chain: String): WalletWithdrawConfig? }
