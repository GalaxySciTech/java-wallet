package com.wallet.repository

import com.wallet.entity.domain.AddressAdmin
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface AddressAdminRepository : JpaRepository<AddressAdmin, Long>,
    QueryDslPredicateExecutor<AddressAdmin>
