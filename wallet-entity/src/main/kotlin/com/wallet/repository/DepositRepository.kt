package com.wallet.repository

import com.wallet.entity.domain.Deposit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface DepositRepository : JpaRepository<Deposit, Long>,
    QueryDslPredicateExecutor<Deposit>
