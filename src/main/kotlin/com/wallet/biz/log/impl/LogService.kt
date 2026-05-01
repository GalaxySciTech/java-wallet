package com.wallet.biz.log.impl

import org.slf4j.LoggerFactory

open class LogService {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    fun log(str: String?) {
        if (str != null) {
            logger.info(str)
        }
    }

    fun log(str: String?, level: Int) {
        if (str == null) return
        when (level) {
            0 -> logger.debug(str)
            1 -> logger.info(str)
            2 -> logger.warn(str)
            3 -> logger.error(str)
            else -> logger.info(str)
        }
    }

    fun log(map: HashMap<String, Any?>) {
        logger.info("Operation: {}", map)
    }
}
