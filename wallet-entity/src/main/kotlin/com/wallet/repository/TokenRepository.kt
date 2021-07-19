package com.wallet.repository

import com.wallet.entity.domain.Token
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface TokenRepository : JpaRepository<Token, Long>,
    QueryDslPredicateExecutor<Token>
