package com.wallet.hsm.config.globalhandler

import com.wallet.biz.domain.dict.TokenResponse
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import wf.bitcoin.javabitcoindrpcclient.BitcoinRPCException

@RestControllerAdvice
class GlobalHandler {

    private val log = LoggerFactory.getLogger(GlobalHandler::class.java)

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): TokenResponse<Any?> {
        log.error("HSM unhandled exception: ${e.message}", e)
        return TokenResponse(-1, e.message ?: "Internal server error", "")
    }

    @ExceptionHandler(BitcoinRPCException::class)
    fun handleBizException(e: BitcoinRPCException): TokenResponse<Any?> {
        log.error("HSM Bitcoin RPC exception: ${e.rpcError?.message}", e)
        return TokenResponse(e.rpcError?.code ?: -1, e.rpcError?.message ?: "RPC error", "")
    }
}
