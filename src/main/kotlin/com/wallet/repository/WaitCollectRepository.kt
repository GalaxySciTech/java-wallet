package com.wallet.repository

import com.wallet.entity.domain.WaitCollect
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor

interface WaitCollectRepository : JpaRepository<WaitCollect, Long>,
    QuerydslPredicateExecutor<WaitCollect>
