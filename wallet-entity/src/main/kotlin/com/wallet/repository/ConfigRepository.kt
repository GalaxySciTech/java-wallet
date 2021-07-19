package com.wallet.repository

import com.wallet.entity.domain.Config
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface ConfigRepository : JpaRepository<Config, Long>,
    QueryDslPredicateExecutor<Config>
