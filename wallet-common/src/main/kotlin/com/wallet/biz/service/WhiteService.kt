package com.wallet.biz.service

import com.wallet.biz.domain.PageEntity
import com.wallet.entity.domain.White
import org.springframework.data.domain.Page

interface WhiteService {
    fun getById(id:Long): White?

    fun save(white:White): White
    fun findByEntity(find: PageEntity<White>): Page<White>
    fun findAll(): List<White>
    fun update(white: White)
    fun del(id: Long)
}
