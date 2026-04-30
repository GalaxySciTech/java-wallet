package com.wallet.biz.service

import com.wallet.entity.domain.User

interface UserService {
    fun getById(id:Long): User?

    fun save(user: User): User

    fun getByName(name: String): User?

    fun getByToken(token: String): User?
}
