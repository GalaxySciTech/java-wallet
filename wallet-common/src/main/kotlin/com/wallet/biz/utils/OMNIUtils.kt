package com.wallet.biz.utils

import com.wallet.biz.domain.ApiDomain
import com.wallet.biz.domain.dict.ErrorCode
import com.wallet.biz.domain.exception.BizException
import com.wallet.biz.rpc.RpcClient
import com.wallet.biz.xservice.WalletXService
import com.fasterxml.jackson.databind.ObjectMapper
import org.consenlabs.tokencore.wallet.model.ChainType
import org.consenlabs.tokencore.wallet.model.TokenException
import org.consenlabs.tokencore.wallet.transaction.BitcoinTransaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal
import java.util.ArrayList

/** 
 * Created by pie on 2020/7/11 16: 15. 
 */
@Component
class OMNIUtils {


    fun getOMNIBalance(address: String, propertyId: String): BigDecimal {
        val rpc = rpcClient.omniRpc()
        return rpc.omniGetBalance(address, propertyId).balance ?: throw BizException(ErrorCode.API_NETWORK_BUSY)
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
        return (148 * inputNum + 34 * outputNum + 10) * satPerByte
    }


    @Autowired
    lateinit var rpcClient: RpcClient
    @Autowired
    lateinit var restTemplate: RestTemplate
    @Autowired
    lateinit var walletXService: WalletXService
    val obj=ObjectMapper()
}
