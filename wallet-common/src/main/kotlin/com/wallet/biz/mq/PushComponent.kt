package com.wallet.biz.mq

import com.fasterxml.jackson.databind.JsonNode
import com.wallet.biz.dict.MqKey
import com.wallet.biz.dict.SysConfigKey
import com.wallet.biz.domain.dict.ErrorCode
import com.wallet.biz.domain.exception.BizException
import com.wallet.biz.log.impl.LogService
import com.wallet.biz.utils.Crypto
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class PushComponent : LogService() {

    @Value("\${wallet.crypto.push-key:#{null}}")
    private var pushEncryptionKey: String? = null

    private fun getEncryptionKey(): String {
        return pushEncryptionKey
            ?: throw BizException(ErrorCode.ERROR_PARAM, "Push encryption key not configured (wallet.crypto.push-key)")
    }

    fun sendMsgToMq(msg: String) {
        amqpTemplate.convertAndSend(MqKey.TOPIC_EXCHANGE, MqKey.DEPOSIT_KEY, msg)
    }

    fun sendMsgToService(msg: String) {
        log("推送充值记录到post")
        val enMsg = Crypto.aesEncrypt(msg, getEncryptionKey())
        val url = cacheService.getSysConfig(SysConfigKey.DEPOSIT_POST_NOTIFY_URL)
        url.split(",").forEach {
            val res = restTemplate.postForObject(it, enMsg, JsonNode::class.java)
            val code = res?.get("code")?.asInt() ?: -1
            if (code != 200) throw BizException(ErrorCode.UPLOAD_FAILURE)
        }
    }

    @Autowired
    lateinit var amqpTemplate: AmqpTemplate

    @Autowired
    lateinit var restTemplate: RestTemplate

    @Autowired
    lateinit var cacheService: com.wallet.biz.cache.CacheService
}
