package com.wallet.repository

import com.wallet.entity.domain.White
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor

interface WhiteRepository : JpaRepository<White, Long>,
    QuerydslPredicateExecutor<White>
