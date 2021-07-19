package com.wallet.biz.rpc

import com.wallet.biz.dict.SysConfigKey
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import com.wallet.biz.rpc.BitcoinFork.*
import com.fasterxml.jackson.databind.JsonNode
import com.wallet.biz.service.ConfigService
import org.springframework.web.client.RestTemplate
import wf.bitcoin.javabitcoindrpcclient.BitcoinJSONRPCClient
import java.math.BigInteger

/** 
 * Created by pie on 2019-04-14 10: 32. 
 */
@Component
class RpcClient {

    var DEFAULT_GAS_PRICE = 90

    var DEFAULT_SAT_PER_BYTE = 35L

    fun getGasPrice(): Int {
        return try {
            val rpc=ethRpc()
            val gasPrice = rpc.ethGasPrice().send().gasPrice.divide(BigInteger.TEN.pow(9)).toInt()
            gasPrice
        } catch (e: Exception) {
            val gasLevel = cacheService.getSysConfig(SysConfigKey.ETH_GAS_LEVEL)
            val gasProp = cacheService.getSysConfig(SysConfigKey.GAS_PROP).toBigDecimal()

            val node =
                restTemplate.getForObject("https://ethgasstation.info/json/ethgasAPI.json", JsonNode::class.java)
            val gasPrice = (node[gasLevel].decimalValue() * gasProp).toInt() / 10
            DEFAULT_GAS_PRICE = gasPrice
            gasPrice
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
            val satPetByte = (node[gasLevel].decimalValue() * gasProp).toLong()
            DEFAULT_SAT_PER_BYTE = satPetByte
            satPetByte
        } catch (e: Exception) {
            DEFAULT_SAT_PER_BYTE
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
        val rpc = BitcoinJSONRPCClient(url)
        return try {
            rpc.blockChainInfo
            url
        } catch (e: Exception) {
            "http://127.0.0.1:8332"
        }
    }

    private fun getETHRpcUrl(): String {
        val url = cacheService.getSysConfig(SysConfigKey.ETH_RPC_URL)
        val rpc=EthRpc(url)
        return try {
            rpc.ethBlockNumber()
            url
        } catch (e: Exception) {
            return "http://127.0.0.1:8545"
        }
    }

//    fun eosRpc(): EosApiRestClient {
//        return EosApiClientFactory.newInstance(SysConfigKey.DOGE_RPC_URL.defaultValue).newRestClient()
//    }

    fun trxApi(): TrxApi {
        val url = cacheService.getSysConfig(SysConfigKey.TRX_API_URL)
        return TrxApi(url, restTemplate)
    }

    @Autowired
    lateinit var cacheService: com.wallet.biz.cache.CacheService

    @Autowired
    lateinit var configService: ConfigService

    @Autowired
    lateinit var restTemplate: RestTemplate

}
