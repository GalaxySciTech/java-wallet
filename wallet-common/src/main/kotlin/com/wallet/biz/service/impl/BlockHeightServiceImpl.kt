package com.wallet.biz.service.impl

import com.wallet.biz.domain.PageEntity
import com.wallet.biz.service.BlockHeightService
import com.wallet.biz.utils.BasicUtils
import com.wallet.entity.domain.BlockHeight
import com.wallet.entity.domain.QBlockHeight
import com.wallet.repository.BlockHeightRepository
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

@Service
class BlockHeightServiceImpl: BlockHeightService {

    override fun getById(id:Long): BlockHeight? {
        return blockHeightRepository.findOne(id)
    }

    override fun save(blockHeight:BlockHeight): BlockHeight {
        return blockHeightRepository.saveAndFlush(blockHeight)
    }

    override fun findByEntity(find: PageEntity<BlockHeight>): List<BlockHeight> {
        var pre = QBlockHeight.blockHeight.id.isNotNull
        val entity = find.entity
        if (entity.chainType != null)
            pre = pre.and(QBlockHeight.blockHeight.chainType.eq(entity.chainType))
        return blockHeightRepository.findAll(pre).toList()
    }

    override fun findAll(): List<BlockHeight> {
        return blockHeightRepository.findAll()
    }

    override fun update(blockHeight: BlockHeight) {
        val e=blockHeightRepository.findOne(blockHeight.id)
        BasicUtils.copyPropertiesIgnoreNull(blockHeight,e)
        blockHeightRepository.save(e)
    }

    override fun del(id: Long) {
        blockHeightRepository.delete(id)
    }

    @Autowired lateinit var blockHeightRepository: BlockHeightRepository
}
