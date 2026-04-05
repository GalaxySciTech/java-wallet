package com.wallet.repository

import com.wallet.entity.domain.BlockHeight
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor

interface BlockHeightRepository : JpaRepository<BlockHeight, Long>,
    QuerydslPredicateExecutor<BlockHeight>
