package com.wallet.biz.service

import com.wallet.entity.domain.WaitImport

interface WaitImportService {
    fun getById(id:Long): WaitImport?

    fun save(waitImport:WaitImport): WaitImport
    fun saveAll(waitImportList: List<WaitImport>): List<WaitImport>
    fun findAll(): List<WaitImport>
    fun deleteById(id: Long)
}
