package com.wallet.repository

import com.wallet.entity.domain.WalletFeeSupplyConfig
import org.springframework.data.jpa.repository.JpaRepository

interface WalletFeeSupplyConfigRepository: JpaRepository<WalletFeeSupplyConfig, Long> { fun findByChain(chain: String): WalletFeeSupplyConfig? }
