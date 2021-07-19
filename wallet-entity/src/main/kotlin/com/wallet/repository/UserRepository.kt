package com.wallet.repository

import com.wallet.entity.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface UserRepository : JpaRepository<User, Long>,
    QueryDslPredicateExecutor<User>
