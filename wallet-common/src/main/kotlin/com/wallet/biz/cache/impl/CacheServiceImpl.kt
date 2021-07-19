package com.wallet.biz.cache.impl

import com.wallet.biz.dict.SysConfigKey
import com.wallet.biz.dict.AddressAdminKey
import com.wallet.biz.dict.TokenKey
import com.wallet.biz.domain.PageEntity
import com.wallet.biz.domain.dict.ErrorCode
import com.wallet.biz.domain.exception.BizException
import com.wallet.biz.domain.po.CreateHotAddressPo
import com.wallet.biz.rpc.RpcClient
import com.wallet.biz.service.*
import com.wallet.biz.xservice.WalletXService
import com.wallet.entity.domain.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.web3j.protocol.core.DefaultBlockParameterName
import javax.annotation.PostConstruct
import kotlin.collections.HashMap

/** 
 * Created by pie on 2020/7/24 18: 27. 
 */
@Service
open class CacheServiceImpl : com.wallet.biz.cache.CacheService {


    @PostConstruct
    fun initConfig() {
        findAllSysConfig()
        findAllBlockHeight()
        findAllAdminAddress()
        findAllWalletToken()
    }


    val map = HashMap<String, EthNonce>()

    override fun getETHNonce(address: String): EthNonce {
        val rpc = rpcClient.ethRpc()
        var value = map[address]
        if (value == null) {
            val ethNonce = EthNonce()
            ethNonce.nonce = rpc.handleError(
                    rpc.ethGetTransactionCount(
                            address,
                            DefaultBlockParameterName.PENDING
                    ).send()
            ).transactionCount.toInt()
            map[address] = ethNonce
            value = ethNonce
        }
        return value
    }

    class EthNonce(var nonce: Int = 0)

    override fun getSysConfig(key: SysConfigKey): String {
        return cacheService.findAllSysConfig()[key.name] ?: ""
    }

    override fun getAdminAddress(key: AddressAdminKey): List<AddressAdmin> {
        val code = when (key.addressType) {
            100 -> ErrorCode.SEND_ADDRESS_NOT_FOUND
            300 -> ErrorCode.GAS_ADDRESS_NOT_FOUND
            else -> ErrorCode.COLLECT_ADDRESS_NOT_FOUND
        }
        return cacheService.findAllAdminAddress()["${key.chainType}${key.addressType}"] ?: throw BizException(code)
    }

    @Cacheable("sys_config")
    override fun findAllSysConfig(): Map<String, String> {
        val map = configService.findAll().associate { it.configKey to it.configValue }
        SysConfigKey.values().forEach {
            val value = map[it.name]
            if (value == null) {
                val sysConfig = Config()
                sysConfig.configKey = it.name
                sysConfig.configValue = it.defaultValue
                sysConfig.description = it.description
                sysConfig.configGroup = it.group
                configService.save(sysConfig)
            }
        }
        return map
    }

    @Cacheable("admin_address")
    override fun findAllAdminAddress(): Map<String, List<AddressAdmin>> {
        val map = walletAddressAdminService.findAll().groupBy { "${it.chainType}${it.addressType}" }.toMutableMap()
        AddressAdminKey.values().forEach {
            if (it.addressType != 200) {
                val value = map["${it.chainType}${it.addressType}"]
                if (value == null) {
                    val po = CreateHotAddressPo()
                    po.chain = it.chainType
                    po.type = it.addressType
                    try {
                        val addr = walletXService.createHotAddress(po)
                        map["${it.chainType}${it.addressType}"] = listOf(addr)
                    } catch (e: Exception) {
                        //nothing to do
                    }
                }
            }
        }
        return map
    }

    @Cacheable("wallet_token")
    override fun findAllWalletToken(): Map<String, Token> {
        val map = walletTokenService.findAll().associateBy { "${it.tokenSymbol}_${it.chainType}" }
        TokenKey.values().forEach {
            val value = map["${it.tokenSymbol}_${it.chainType}"]
            if (value == null) {
                val walletToken = Token()
                walletToken.chainType = it.chainType
                walletToken.tokenSymbol = it.tokenSymbol
                walletToken.gasLimit = it.gasLimit
                walletToken.tokenAddress = it.tokenAddress
                walletToken.minCollect=it.minCollect
                walletTokenService.save(walletToken)
            }

        }
        return map
    }

    override fun getWalletToken(chainType: String, tokenSymbol: String): Token {
        return findAllWalletToken()["${tokenSymbol}_${chainType}"] ?: throw BizException(-1, "没找到wallettoken")
    }

    @Cacheable("block_height")
    override fun findAllBlockHeight(): Map<String, Long> {
        val find = PageEntity(BlockHeight())
        val map = walletBlockHeightService.findByEntity(find).associate { it.chainType to it.height }.toMutableMap()
        AddressAdminKey.values().forEach {
            val value = map[it.chainType]
            if (value == null) {
                val blockHeight = BlockHeight()
                blockHeight.chainType = it.chainType
                blockHeight.height = 0
                walletBlockHeightService.save(blockHeight)
                map[it.chainType] = 0
            }
        }
        val value = map["OMNI"]
        if (value == null) {
            val blockHeight = BlockHeight()
            blockHeight.chainType = "OMNI"
            blockHeight.height = 0
            walletBlockHeightService.save(blockHeight)
            map["OMNI"] = 0
        }
        return map
    }


    @Autowired
    lateinit var configService: ConfigService

    @Autowired
    lateinit var walletAddressAdminService: AddressAdminService

    @Autowired
    lateinit var walletBlockHeightService: BlockHeightService

    @Autowired
    lateinit var cacheService: com.wallet.biz.cache.CacheService

    @Autowired
    lateinit var walletXService: WalletXService

    @Autowired
    lateinit var rpcClient: RpcClient

    @Autowired
    lateinit var walletTokenService: TokenService

}
