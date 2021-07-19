package com.wallet.biz.service

import com.wallet.biz.domain.PageEntity
import com.wallet.entity.domain.Deposit
import org.springframework.data.domain.Page

interface DepositService {
    fun getById(id:Long): Deposit?

    fun save(deposit:Deposit): Deposit
    fun getByHash(hash: String, address: String): Deposit?
    fun findByEntity(find: PageEntity<Deposit>): Page<Deposit>
    fun getByHash(hash: String): Deposit?
    fun getByIsUpload(isUpload: Int): List<Deposit>
    fun saveAll(list: List<Deposit>): List<Deposit>
    fun findAll(): List<Deposit>
}
