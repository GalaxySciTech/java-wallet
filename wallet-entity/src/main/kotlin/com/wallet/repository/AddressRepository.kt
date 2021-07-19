package com.wallet.repository

import com.wallet.entity.domain.Address
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface AddressRepository : JpaRepository<Address, Long>,
    QueryDslPredicateExecutor<Address>
