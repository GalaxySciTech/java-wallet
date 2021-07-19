package com.wallet.biz.service.impl

import com.wallet.biz.dict.TokenSymbolKey
import com.wallet.biz.domain.PageEntity
import com.wallet.biz.service.DepositService
import com.wallet.entity.domain.Deposit
import com.wallet.entity.domain.QDeposit
import com.wallet.repository.DepositRepository
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

@Service
class DepositServiceImpl: DepositService {

    override fun findAll(): List<Deposit> {
        return depositRepository.findAll()
    }

    override fun saveAll(list: List<Deposit>): List<Deposit> {
        return depositRepository.save(list)
    }

    override fun getByIsUpload(isUpload: Int): List<Deposit> {
        val pre = QDeposit.deposit.isUpload.eq(isUpload)
        return depositRepository.findAll(pre).toList()
    }

    override fun getByHash(hash: String): Deposit? {
        val pre = QDeposit.deposit.hash.eq(hash)
        val list = depositRepository.findAll(pre).toList()
        return list.firstOrNull()
    }

    override fun getByHash(hash: String,address:String): Deposit? {
        var pre = QDeposit.deposit.hash.eq(hash)
        pre=pre.and(QDeposit.deposit.address.eq(address))
        val list = depositRepository.findAll(pre).toList()
        return list.firstOrNull()
    }

    override fun findByEntity(find: PageEntity<Deposit>): Page<Deposit> {
        var pre = QDeposit.deposit.id.isNotNull
        val entity = find.entity
        if (entity.hash != null)
            pre = pre.and(QDeposit.deposit.hash.eq(entity.hash))
        if (entity.address != null)
            pre = pre.and(QDeposit.deposit.address.eq(entity.address))
        if(entity.tokenSymbol== TokenSymbolKey.MUST_NULL)
            pre=pre.and(QDeposit.deposit.tokenSymbol.isNull)
        else if(entity.tokenSymbol!=null)
            pre=pre.and(QDeposit.deposit.tokenSymbol.eq(entity.tokenSymbol))
        if(entity.chainType!=null)
            pre=pre.and(QDeposit.deposit.chainType.eq(entity.chainType))
        return depositRepository.findAll(pre, PageRequest(find.page, find.size))
    }

    override fun getById(id:Long): Deposit? {
        return depositRepository.findOne(id)
    }

    override fun save(deposit:Deposit): Deposit {
        return depositRepository.saveAndFlush(deposit)
    }

    @Autowired lateinit var depositRepository: DepositRepository
}
