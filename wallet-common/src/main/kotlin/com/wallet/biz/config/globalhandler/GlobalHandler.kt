package com.wallet.hsm.config.globalhandler

import com.wallet.biz.dict.SysConfigKey
import com.wallet.biz.domain.dict.TokenResponse
import com.wallet.biz.domain.exception.BizException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import wf.bitcoin.javabitcoindrpcclient.BitcoinRPCException
import javax.servlet.http.HttpServletRequest


@RestControllerAdvice
class GlobalHandler {

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): TokenResponse<Any?> {
        val message = when (cacheService.getSysConfig(SysConfigKey.ERROR_LEVEL).toIntOrNull()?:0) {
            0 -> e.message.toString()
            else -> e.stackTrace.joinToString("  ")
        }

        return TokenResponse(-1, message, "")
    }

    @ExceptionHandler(BizException::class)
    fun handleException(e: BizException): TokenResponse<Any?> {
        val message = when (cacheService.getSysConfig(SysConfigKey.ERROR_LEVEL).toIntOrNull()?:0) {
            0 -> e.message.toString()
            else -> e.stackTrace.joinToString("  ")
        }

        return TokenResponse(e.code, message, "")
    }

    @ExceptionHandler(BitcoinRPCException::class)
    fun handleBizException(e: BitcoinRPCException): TokenResponse<Any?> {

        return TokenResponse(e.rpcError.code, e.rpcError.message, "")
    }

    @Autowired
    lateinit var request: HttpServletRequest
    @Autowired
    lateinit var cacheService: com.wallet.biz.cache.CacheService


}
