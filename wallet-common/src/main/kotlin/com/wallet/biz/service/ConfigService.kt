package com.wallet.biz.service

import com.wallet.biz.domain.PageEntity
import com.wallet.entity.domain.Config

interface ConfigService {
    fun getById(id:Long): Config?

    fun save(config:Config): Config

    fun findAll(): List<Config>

    fun findByEntity(find: PageEntity<Config>): List<Config>

    fun update(config: Config)

    fun del(id: Long)
}
