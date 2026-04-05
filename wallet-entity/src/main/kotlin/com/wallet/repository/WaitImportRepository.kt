package com.wallet.repository

import com.wallet.entity.domain.WaitImport
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor

interface WaitImportRepository : JpaRepository<WaitImport, Long>,
    QuerydslPredicateExecutor<WaitImport>
