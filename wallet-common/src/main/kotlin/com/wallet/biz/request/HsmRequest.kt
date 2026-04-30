package com.wallet.biz.request

import com.wallet.biz.domain.po.*
import com.wallet.biz.domain.vo.AddressVo
import org.consenlabs.tokencore.wallet.transaction.BitcoinTransaction
import org.consenlabs.tokencore.wallet.transaction.TxSignResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.math.BigDecimal

        return hsmXService.getAllWallets()
    }

        return hsmXService.deriveWallets(chainTypes)
        return hsmXService.signUsdtTransaction(signUsdtPo)
    fun signBtcTransaction(amount: BigDecimal, fee: BigDecimal, toAddress: String, utxos: ArrayList<BitcoinTransaction.UTXO>, walletId: String): TxSignResult {
        return hsmXService.signBitcoinTransaction(signBitcoinPo)
    fun signEthtransaction(nonce: Int, amount: BigDecimal, gasPrice: BigDecimal, gasLimit: Long, toAddress: String, walletId: String, data: String?): TxSignResult {
        return hsmXService.signEthereumTransaction(signEthereumPo)
    fun signUsdtCollectTransaction(toAddress: String, amount: BigDecimal, fee: BigDecimal, utxos: ArrayList<BitcoinTransaction.UTXO>, feeProviderUtxos: ArrayList<BitcoinTransaction.UTXO>, walletId: String, feeProviderWalletId: String): TxSignResult {
        return hsmXService.signUsdtCollectTransaction(signUsdtCollectPo)
    fun checkWallet(walletCode: String): String = hsmXService.getAllWallets().firstOrNull { it.walletCode == walletCode }?.address ?: ""
    fun exportWallet(walletCode: String, type: Int): String = hsmXService.exportWallet(walletCode, type)
    fun removeUselessWallet(map: Map<String, String>) { hsmXService.removeUselessWallet(map) }
    fun importWallet(importWalletPo: ImportWalletPo): AddressVo = hsmXService.importWallet(importWalletPo)
    lateinit var hsmXService: com.wallet.hsm.xservice.HsmXService
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
        val response = try {
            restTemplate.exchange(
                url,
                method,
                packRequest(body),
                object : ParameterizedTypeReference<TokenResponse<T>>() {}
            ).body
        } catch (e: Exception) {
            logger.error("HSM connection failed for $url: ${e.message}", e)
            throw BizException(ErrorCode.HSM_CONNECT_FAILURE)
        }
        if (response == null) throw BizException(ErrorCode.HSM_CONNECT_FAILURE)
        if (response.code != 200) throw BizException(response.code, response.msg)
        return obj.readValue(obj.writeValueAsString(response.data ?: ""), responseClass)
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
