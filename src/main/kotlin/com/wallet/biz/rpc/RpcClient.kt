package com.wallet.biz.rpc

import com.wallet.biz.dict.SysConfigKey
import com.wallet.biz.rpc.BitcoinFork.*
import com.fasterxml.jackson.databind.JsonNode
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import wf.bitcoin.javabitcoindrpcclient.BitcoinJSONRPCClient
import java.math.BigInteger

@Component
class RpcClient {

    private val logger = LoggerFactory.getLogger(RpcClient::class.java)

    @Volatile
    private var lastGasPrice = 90

    @Volatile
    private var lastSatPerByte = 35L

    fun getGasPrice(): Int {
        return try {
            val rpc = ethRpc()
            val gasPrice = rpc.ethGasPrice().send().gasPrice.divide(BigInteger.TEN.pow(9)).toInt()
            lastGasPrice = gasPrice
            gasPrice
        } catch (e: Exception) {
            logger.warn("Failed to get gas price from node, falling back to external API: ${e.message}")
            try {
                val gasLevel = cacheService.getSysConfig(SysConfigKey.ETH_GAS_LEVEL)
                val gasProp = cacheService.getSysConfig(SysConfigKey.GAS_PROP).toBigDecimal()
                val node =
                    restTemplate.getForObject("https://ethgasstation.info/json/ethgasAPI.json", JsonNode::class.java)
                val gasPrice = (node!![gasLevel].decimalValue() * gasProp).toInt() / 10
                lastGasPrice = gasPrice
                gasPrice
            } catch (e2: Exception) {
                logger.warn("Failed to get gas price from external API, using cached value: $lastGasPrice")
                lastGasPrice
            }
        }
    }

    fun getSatPerByte(): Long {
        return try {
            val gasLevel = cacheService.getSysConfig(SysConfigKey.BTC_GAS_LEVEL)
            val gasProp = cacheService.getSysConfig(SysConfigKey.GAS_PROP).toBigDecimal()
            val node = restTemplate.getForObject(
                "https://bitcoinfees.earn.com/api/v1/fees/recommended",
                JsonNode::class.java
            )
            val satPerByte = (node!![gasLevel].decimalValue() * gasProp).toLong()
            lastSatPerByte = satPerByte
            satPerByte
        } catch (e: Exception) {
            logger.warn("Failed to get BTC fee estimate, using cached value: $lastSatPerByte")
            lastSatPerByte
        }
    }

    fun omniRpc(): OmniRpc {
        val url = getBTCOrForkRpcUrl(SysConfigKey.OMNI_RPC_URL)
        return OmniRpc(url)
    }

    fun ethRpc(): EthRpc {
        val url = getETHRpcUrl()
        return EthRpc(url)
    }

    fun bchRpc(): BchRpc {
        val url = getBTCOrForkRpcUrl(SysConfigKey.BCH_RPC_URL)
        return BchRpc(url)
    }

    fun ltcRpc(): LtcRpc {
        val url = getBTCOrForkRpcUrl(SysConfigKey.LTC_RPC_URL)
        return LtcRpc(url)
    }

    fun bsvRpc(): BsvRpc {
        val url = getBTCOrForkRpcUrl(SysConfigKey.BSV_RPC_URL)
        return BsvRpc(url)
    }

    fun dashRpc(): DashRpc {
        val url = getBTCOrForkRpcUrl(SysConfigKey.DASH_RPC_URL)
        return DashRpc(url)
    }

    fun dogeRpc(): DogeRpc {
        val url = getBTCOrForkRpcUrl(SysConfigKey.DOGE_RPC_URL)
        return DogeRpc(url)
    }

    private fun getBTCOrForkRpcUrl(key: SysConfigKey): String {
        val url = cacheService.getSysConfig(key)
        if (url.isBlank()) {
            logger.error("RPC URL not configured for $key")
            throw IllegalStateException("RPC URL not configured for $key")
        }
        return url
    }

    private fun getETHRpcUrl(): String {
        val url = cacheService.getSysConfig(SysConfigKey.ETH_RPC_URL)
        if (url.isBlank()) {
            logger.error("ETH RPC URL not configured")
            throw IllegalStateException("ETH RPC URL not configured")
        }
        return url
    }

    fun trxApi(): TrxApi {
        val url = cacheService.getSysConfig(SysConfigKey.TRX_API_URL)
        return TrxApi(url, restTemplate)
    }

    @Autowired
    lateinit var cacheService: com.wallet.biz.cache.CacheService

    @Autowired
    lateinit var restTemplate: RestTemplate
}
