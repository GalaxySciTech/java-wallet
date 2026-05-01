package com.wallet.repository

import com.wallet.entity.domain.WalletAdminAuditLog
import org.springframework.data.jpa.repository.JpaRepository

interface WalletAdminAuditLogRepository: JpaRepository<WalletAdminAuditLog, Long>
