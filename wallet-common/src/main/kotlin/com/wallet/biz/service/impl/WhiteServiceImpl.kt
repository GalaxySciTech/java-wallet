package com.wallet.biz.service.impl

import com.wallet.biz.domain.PageEntity
import com.wallet.biz.service.WhiteService
import com.wallet.biz.utils.BasicUtils
import com.wallet.entity.domain.QWhite
import com.wallet.entity.domain.White
import com.wallet.repository.WhiteRepository
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

@Service
class WhiteServiceImpl: WhiteService {

    override fun getById(id:Long): White? {
        return whiteRepository.findOne(id)
    }

    override fun save(white:White): White {
        return whiteRepository.saveAndFlush(white)
    }

    override fun findByEntity(find: PageEntity<White>): Page<White> {
        var pre = QWhite.white.id.isNotNull
        val entity = find.entity
        if (entity.ip != null)
            pre = pre.and(QWhite.white.ip.eq(entity.ip).or(QWhite.white.ip.eq("0.0.0.0")))
        return whiteRepository.findAll(pre, PageRequest(find.page, find.size))
    }

    override fun findAll(): List<White> {
        return whiteRepository.findAll()
    }

    override fun update(white: White) {
        val e=whiteRepository.findOne(white.id)
        BasicUtils.copyPropertiesIgnoreNull(white,e)
        whiteRepository.save(e)
    }

    override fun del(id: Long) {
        whiteRepository.delete(id)
    }

    @Autowired lateinit var whiteRepository: WhiteRepository
}
