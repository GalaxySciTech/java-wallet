package com.wallet.biz.utils

import com.wallet.biz.domain.dict.ErrorCode
import com.wallet.biz.domain.exception.BizException
import com.wallet.biz.rpc.RpcClient
import com.wallet.biz.xservice.WalletXService
import org.consenlabs.tokencore.wallet.model.ChainType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class OMNIUtils {

    fun getOMNIBalance(address: String, propertyId: String): BigDecimal {
        val rpc = rpcClient.omniRpc()
        return rpc.omniGetBalance(address, propertyId).balance
            ?: throw BizException(ErrorCode.API_NETWORK_BUSY)
    }

    fun getBTCBalance(address: String): BigDecimal {
        val utxos = walletXService.getUtxos(ChainType.BITCOIN, address)
        var amount = 0L
        utxos.forEach { utxo ->
            amount += utxo.amount
        }
        return amount.toBigDecimal().divide(BigDecimal.TEN.pow(8))
    }

    fun calculateFee(inputNum: Int, outputNum: Int, satPerByte: Long): Long {
        return (148L * inputNum + 34L * outputNum + 10L) * satPerByte
    }

    @Autowired lateinit var rpcClient: RpcClient
    @Autowired lateinit var walletXService: WalletXService
}
