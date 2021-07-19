package com.wallet.biz.service.impl

import com.wallet.biz.service.WaitImportService
import com.wallet.entity.domain.WaitImport
import com.wallet.repository.WaitImportRepository
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

@Service
class WaitImportServiceImpl: WaitImportService {

    override fun getById(id:Long): WaitImport? {
        return waitImportRepository.findOne(id)
    }

    override fun save(waitImport:WaitImport): WaitImport {
        return waitImportRepository.saveAndFlush(waitImport)
    }

    override fun saveAll(waitImportList: List<WaitImport>): List<WaitImport> {
        return waitImportRepository.save(waitImportList)
    }

    override fun findAll(): List<WaitImport> {
        return waitImportRepository.findAll()
    }

    override fun deleteById(id: Long) {
        waitImportRepository.delete(id)
    }
    
    @Autowired lateinit var waitImportRepository: WaitImportRepository
}
