package com.wallet.biz.utils

import com.wallet.biz.domain.exception.BizException
import com.wallet.biz.rpc.RpcClient
import org.consenlabs.tokencore.foundation.utils.NumericUtil
import org.consenlabs.tokencore.wallet.model.ChainType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class TRONUtils {

    fun getBalance(address: String): BigDecimal {
        val api = rpcClient.trxApi()
        val accounts = try {
            api.accounts(address)
        } catch (e: Exception) {
            return BigDecimal.ZERO
        }

        if (accounts["Error"] != null) return BigDecimal.ZERO

        return accounts["balance"]?.decimalValue()?.divide(BigDecimal.TEN.pow(6)) ?: BigDecimal.ZERO
    }

    fun getContractBalance(tokenAddress: String, address: String): BigDecimal {
        val api = rpcClient.trxApi()
        val balance = try {
            val accounts = api.contractBalanceOf(tokenAddress, address)
            val hexBalance = accounts["constant_result"].firstOrNull()?.textValue() ?: return BigDecimal.ZERO
            BigDecimal(NumericUtil.hexToBigInteger(hexBalance))
        } catch (e: Exception) {
            return BigDecimal.ZERO
        }

        val decimals = api.contractDecimals(tokenAddress)
        return balance.divide(BigDecimal.TEN.pow(decimals))
    }

    @Autowired
    lateinit var rpcClient: RpcClient

}