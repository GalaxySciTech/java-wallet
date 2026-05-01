package com.wallet.biz.service

import com.wallet.biz.domain.PageEntity
import com.wallet.entity.domain.Withdraw
import org.springframework.data.domain.Page

interface WithdrawService {
    fun getById(id:Long): Withdraw?

    fun save(withdraw:Withdraw): Withdraw
    fun findByWithdraw(findWith: Withdraw): List<Withdraw>
    fun findAll(): List<Withdraw>
    fun findByEntity(find: PageEntity<Withdraw>): Page<Withdraw>
}
