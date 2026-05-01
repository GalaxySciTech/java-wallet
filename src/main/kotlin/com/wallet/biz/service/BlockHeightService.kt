package com.wallet.biz.service

import com.wallet.biz.domain.PageEntity
import com.wallet.entity.domain.BlockHeight

interface BlockHeightService {
    fun getById(id:Long): BlockHeight?

    fun save(blockHeight:BlockHeight): BlockHeight
    fun findByEntity(find: PageEntity<BlockHeight>): List<BlockHeight>
    fun findAll(): List<BlockHeight>
    fun update(blockHeight: BlockHeight)
    fun del(id: Long)
}
