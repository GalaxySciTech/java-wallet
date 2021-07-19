package com.wallet.biz.service.impl

import com.wallet.biz.domain.PageEntity
import com.wallet.biz.service.ConfigService
import com.wallet.biz.utils.BasicUtils
import com.wallet.entity.domain.Config
import com.wallet.entity.domain.QConfig
import com.wallet.repository.ConfigRepository
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

@Service
class ConfigServiceImpl: ConfigService {

    override fun getById(id:Long): Config? {
        return configRepository.findOne(id)
    }

    override fun save(config:Config): Config {
        return configRepository.saveAndFlush(config)
    }

    override fun findAll(): List<Config> {
        return configRepository.findAll()
    }

    override fun findByEntity(find: PageEntity<Config>): List<Config> {
        var pre = QConfig.config.id.isNotNull
        val entity = find.entity
        if (entity.configKey != null)
            pre = pre.and(QConfig.config.configKey.eq(entity.configKey))
        return configRepository.findAll(pre).toList()
    }

    override fun update(config: Config) {
        val e=configRepository.findOne(config.id)
        BasicUtils.copyPropertiesIgnoreNull(config,e)
        configRepository.save(e)
    }

    override fun del(id: Long) {
        configRepository.delete(id)
    }

    @Autowired lateinit var configRepository: ConfigRepository
}
