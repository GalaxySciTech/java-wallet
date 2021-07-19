package com.wallet.repository

import com.wallet.entity.domain.White
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface WhiteRepository : JpaRepository<White, Long>,
    QueryDslPredicateExecutor<White>
