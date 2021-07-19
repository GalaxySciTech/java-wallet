package com.wallet.biz.service.impl

import com.wallet.biz.dict.TokenSymbolKey
import com.wallet.biz.service.WaitCollectService
import com.wallet.entity.domain.QWaitCollect
import com.wallet.entity.domain.WaitCollect
import com.wallet.repository.WaitCollectRepository
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

@Service
class WaitCollectServiceImpl: WaitCollectService {

    override fun clearByBean(waitCollect: WaitCollect) {
        if (waitCollect.address == null) return
        if (waitCollect.chainType == null) return
        val list = getByBean(waitCollect)

        waitCollectRepository.delete(list)
    }

    override fun getByBean(waitCollect: WaitCollect): List<WaitCollect> {
        var pre = QWaitCollect.waitCollect.id.isNotNull
        if (waitCollect.address != null)
            pre = pre.and(QWaitCollect.waitCollect.address.eq(waitCollect.address))
        if (waitCollect.chainType != null)
            pre = pre.and(QWaitCollect.waitCollect.chainType.eq(waitCollect.chainType))
        if (waitCollect.tokenSymbol == TokenSymbolKey.MUST_NULL) {
            pre = pre.and(QWaitCollect.waitCollect.tokenSymbol.isNull)
        } else if (waitCollect.tokenSymbol != null) {
            pre = pre.and(QWaitCollect.waitCollect.tokenSymbol.eq(waitCollect.tokenSymbol))
        }
        return waitCollectRepository.findAll(pre).toList()
    }

    override fun getById(id:Long): WaitCollect? {
        return waitCollectRepository.findOne(id)
    }

    override fun save(waitCollect:WaitCollect): WaitCollect {
        return waitCollectRepository.saveAndFlush(waitCollect)
    }

    @Autowired lateinit var waitCollectRepository: WaitCollectRepository
}
