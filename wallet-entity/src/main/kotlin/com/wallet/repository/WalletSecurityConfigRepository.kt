package com.wallet.repository

import com.wallet.entity.domain.WalletSecurityConfig
import org.springframework.data.jpa.repository.JpaRepository

interface WalletSecurityConfigRepository: JpaRepository<WalletSecurityConfig, Long>
