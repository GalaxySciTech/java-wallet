package com.wallet.hsm.config.globalhandler

import com.wallet.biz.domain.dict.TokenResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import wf.bitcoin.javabitcoindrpcclient.BitcoinRPCException
import javax.servlet.http.HttpServletRequest

@RestControllerAdvice
class GlobalHandler {

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): TokenResponse<Any?> {

        val message = when (0) {
            0 -> e.message.toString()
            else -> e.stackTrace.joinToString("  ")
        }
        val requestPath = request.requestURI + "?" + request.queryString
        log.error(e.message)
        return TokenResponse(-1, "$requestPath   $message", "")
    }

    @ExceptionHandler(BitcoinRPCException::class)
    fun handleBizException(e: BitcoinRPCException): TokenResponse<Any?> {
        log.error(e.message)
        val requestPath = request.requestURI + "?" + request.queryString
        return TokenResponse(e.rpcError.code, "$requestPath    ${e.rpcError.message}", "")
    }

    @Autowired
    lateinit var request: HttpServletRequest
    val log = LoggerFactory.getLogger(this.javaClass)

}
