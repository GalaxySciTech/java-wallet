package com.wallet.hsm.config.globalhandler

import com.wallet.biz.dict.SysConfigKey
import com.wallet.biz.domain.dict.TokenResponse
import com.wallet.biz.domain.exception.BizException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import wf.bitcoin.javabitcoindrpcclient.BitcoinRPCException


@RestControllerAdvice
class GlobalHandler {

    private val logger = LoggerFactory.getLogger(GlobalHandler::class.java)

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): TokenResponse<Any?> {
        logger.error("Unhandled exception: ${e.message}", e)
        val message = when (cacheService.getSysConfig(SysConfigKey.ERROR_LEVEL).toIntOrNull() ?: 0) {
            0 -> e.message ?: "Internal server error"
            else -> e.message ?: "Internal server error"
        }
        return TokenResponse(-1, message, "")
    }

    @ExceptionHandler(BizException::class)
    fun handleException(e: BizException): TokenResponse<Any?> {
        logger.warn("Business exception: code=${e.code}, message=${e.message}")
        return TokenResponse(e.code, e.message ?: "Business error", "")
    }

    @ExceptionHandler(BitcoinRPCException::class)
    fun handleBizException(e: BitcoinRPCException): TokenResponse<Any?> {
        logger.error("Bitcoin RPC exception: ${e.rpcError?.message}", e)
        return TokenResponse(e.rpcError?.code ?: -1, e.rpcError?.message ?: "RPC error", "")
    }

    @Autowired
    lateinit var cacheService: com.wallet.biz.cache.CacheService
}
