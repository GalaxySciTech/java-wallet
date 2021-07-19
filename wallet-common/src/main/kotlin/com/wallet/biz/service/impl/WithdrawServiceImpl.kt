package com.wallet.biz.service.impl

import com.wallet.biz.domain.PageEntity
import com.wallet.biz.service.WithdrawService
import com.wallet.entity.domain.QWithdraw
import com.wallet.entity.domain.Withdraw
import com.wallet.repository.WithdrawRepository
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.querydsl.QSort

@Service
class WithdrawServiceImpl : WithdrawService {

    override fun findByEntity(find: PageEntity<Withdraw>): Page<Withdraw> {
        var pre = QWithdraw.withdraw.id.isNotNull
        val entity = find.entity
        if (entity.hash != null)
            pre = pre.and(QWithdraw.withdraw.hash.eq(entity.hash))
        if (entity.withdrawType != null)
            pre = pre.and(QWithdraw.withdraw.withdrawType.eq(entity.withdrawType))
        val sort = QSort(QWithdraw.withdraw.createdAt.desc())

        return withdrawRepository.findAll(pre, PageRequest(find.page, find.size, sort))
    }

    override fun findAll(): List<Withdraw> {
        return withdrawRepository.findAll()
    }

    override fun findByWithdraw(findWith: Withdraw): List<Withdraw> {
        var pre = QWithdraw.withdraw.id.isNotNull
        if (findWith.hash != null)
            pre = pre.and(QWithdraw.withdraw.hash.eq(findWith.hash))
        if (findWith.withdrawType != null)
            pre = pre.and(QWithdraw.withdraw.withdrawType.eq(findWith.withdrawType))
        return withdrawRepository.findAll(pre).toList()
    }

    override fun getById(id: Long): Withdraw? {
        return withdrawRepository.findOne(id)
    }

    override fun save(withdraw: Withdraw): Withdraw {
        return withdrawRepository.saveAndFlush(withdraw)
    }

    @Autowired
    lateinit var withdrawRepository: WithdrawRepository
}
