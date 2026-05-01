package com.wallet.biz.service

import com.wallet.entity.domain.Token

interface TokenService {
    fun getById(id:Long): Token?

    fun save(token:Token): Token
    fun getByBean(token: Token): List<Token>
    fun findAll(): List<Token>
    fun update(token: Token)
    fun del(id: Long)
}
