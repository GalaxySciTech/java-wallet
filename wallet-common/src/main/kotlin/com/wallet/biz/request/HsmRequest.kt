package com.wallet.biz.request

import com.wallet.biz.dict.HsmReuqestType
import com.wallet.biz.dict.SysConfigKey
import com.wallet.biz.domain.dict.ErrorCode
import com.wallet.biz.domain.dict.TokenResponse
import com.wallet.biz.domain.exception.BizException
import com.wallet.biz.domain.po.*
import com.wallet.biz.domain.vo.AddressVo
import com.fasterxml.jackson.databind.ObjectMapper
import org.consenlabs.tokencore.wallet.transaction.BitcoinTransaction
import org.consenlabs.tokencore.wallet.transaction.TxSignResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal
import java.util.*

/** 
 * Created by pie on 2019-04-13 16: 01. 
 */
@Component
class HsmRequest {

    private fun getHsmUrl():String {
        return cacheService.getSysConfig(SysConfigKey.HSM_URL)
    }

    fun getAllWallets():List<AddressVo>{
        return exchange(
            "${getHsmUrl()}${HsmReuqestType.GET_ALL_WALLETS}",
            HttpMethod.GET,
            null,
            List::class.java
        )
            .map { obj.readValue(obj.writeValueAsString(it), AddressVo::class.java) }
    }

    fun deriveWallets(chainTypes: List<String>): List<AddressVo> {
        return exchange(
            "${getHsmUrl()}${HsmReuqestType.DERIVE_WALLETS}",
            HttpMethod.POST,
            chainTypes,
            List::class.java
        )
            .map { obj.readValue(obj.writeValueAsString(it), AddressVo::class.java) }
    }

    fun signUsdtTransaction(
        utxos: ArrayList<BitcoinTransaction.UTXO>,
        amount: BigDecimal,
        fee: BigDecimal,
        toAddress: String,
        walletId: String
    ): TxSignResult {
        val signUsdtPo = SignUsdtPo()
        signUsdtPo.utxos = utxos
        signUsdtPo.amount = amount
        signUsdtPo.fee = fee
        signUsdtPo.toAddress = toAddress
        signUsdtPo.walletId = walletId
        return exchange(
            "${getHsmUrl()}${HsmReuqestType.SIGN_USDT_TRANSACTION}",
            HttpMethod.POST,
            signUsdtPo,
            TxSignResult::class.java
        )
    }

    fun signBtcTransaction(
        amount: BigDecimal,
        fee: BigDecimal,
        toAddress: String,
        utxos: ArrayList<BitcoinTransaction.UTXO>,
        walletId: String
    ): TxSignResult {
        val signBitcoinPo = SignBitcoinPo()
        signBitcoinPo.utxos = utxos
        signBitcoinPo.amount = amount
        signBitcoinPo.fee = fee
        signBitcoinPo.toAddress = toAddress
        signBitcoinPo.walletId = walletId
        return exchange(
            "${getHsmUrl()}${HsmReuqestType.SIGN_BTC_TRANSACTION}",
            HttpMethod.POST,
            signBitcoinPo,
            TxSignResult::class.java
        )
    }

    fun signEthtransaction(
        nonce: Int,
        amount: BigDecimal,
        gasPrice: BigDecimal,
        gasLimit: Long,
        toAddress: String,
        walletId: String,
        data: String?
    ): TxSignResult {
        val signEthereumPo = SignEthereumPo()
        signEthereumPo.walletId = walletId
        signEthereumPo.amount = amount
        signEthereumPo.toAddress = toAddress
        signEthereumPo.nonce = nonce
        signEthereumPo.gasPrice = gasPrice
        signEthereumPo.data = data
        signEthereumPo.gasLimit = gasLimit
        return exchange(
            "${getHsmUrl()}${HsmReuqestType.SIGN_ETH_TRANSACTION}",
            HttpMethod.POST,
            signEthereumPo,
            TxSignResult::class.java
        )
    }

    fun signUsdtCollectTransaction(
        toAddress: String,
        amount: BigDecimal,
        fee: BigDecimal,
        utxos: ArrayList<BitcoinTransaction.UTXO>,
        feeProviderUtxos: ArrayList<BitcoinTransaction.UTXO>,
        walletId: String,
        feeProviderWalletId: String
    ): TxSignResult {
        val signUsdtCollectPo = SignUsdtCollectPo()
        signUsdtCollectPo.feeProviderUtxos = feeProviderUtxos
        signUsdtCollectPo.fee = fee
        signUsdtCollectPo.feeProviderWalletId = feeProviderWalletId
        signUsdtCollectPo.walletId = walletId
        signUsdtCollectPo.toAddress = toAddress
        signUsdtCollectPo.amount = amount
        signUsdtCollectPo.utxos = utxos
        return exchange(
            "${getHsmUrl()}${HsmReuqestType.SIGN_USDT_COLLECT_TRANSACTION}",
            HttpMethod.POST,
            signUsdtCollectPo,
            TxSignResult::class.java
        )
    }


    fun checkWallet(walletCode: String): String {

        return exchange(
            "${getHsmUrl()}${HsmReuqestType.CHECK_WALLET}/$walletCode",
            HttpMethod.GET,
            null,
            String::class.java
        )
    }

    fun exportWallet(walletCode: String, type: Int): String {
        return exchange(
            "${getHsmUrl()}${HsmReuqestType.EXPORT_WALLET}/$walletCode/$type",
            HttpMethod.GET,
            null,
            String::class.java
        )

    }

    fun removeUselessWallet(map: Map<String, String>) {
        exchange(
            "${getHsmUrl()}${HsmReuqestType.REMOVE_USELESS_WALLET}",
            HttpMethod.POST,
            map,
            String::class.java
        )
    }

    fun importWallet(importWalletPo: ImportWalletPo): AddressVo {

        return exchange(
            "${getHsmUrl()}${HsmReuqestType.IMPORT_WALLET}",
            HttpMethod.POST,
            importWalletPo,
            AddressVo::class.java
        )
    }

    private fun <T> exchange(url: String, method: HttpMethod, body: Any?, responseClass: Class<T>): T {
        val response =try{
             restTemplate.exchange(
                url,
                method,
                packRequest(body),
                object : ParameterizedTypeReference<TokenResponse<T>>() {}
            ).body
        }catch (e:Exception){
            throw BizException(ErrorCode.HSM_CONNECT_FAILURE)
        }
        if (response.code != 200) throw BizException(response.code, response.msg)
        return obj.readValue(obj.writeValueAsString(response.data?:""), responseClass)
    }


    private fun packRequest(any: Any?): HttpEntity<String> {
        val json = obj.writeValueAsString(any)
        val headers = HttpHeaders()
        headers.add("Content-Type", "application/json;charset=UTF-8")
        return HttpEntity(json, headers)
    }



    @Autowired
    lateinit var restTemplate: RestTemplate
    @Autowired
    lateinit var cacheService: com.wallet.biz.cache.CacheService


    private val obj = ObjectMapper()
}
