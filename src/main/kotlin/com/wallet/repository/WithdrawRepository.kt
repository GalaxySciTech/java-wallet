package com.wallet.repository

import com.wallet.entity.domain.Withdraw
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor

interface WithdrawRepository : JpaRepository<Withdraw, Long>,
    QuerydslPredicateExecutor<Withdraw>
