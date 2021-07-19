package com.wallet.biz.cache

import com.wallet.biz.dict.SysConfigKey
import com.wallet.biz.dict.AddressAdminKey
import com.wallet.entity.domain.*

/** 
 * Created by pie on 2020/7/24 18: 27. 
 */
interface CacheService {
    fun findAllSysConfig(): Map<String, String>

    fun findAllAdminAddress(): Map<String, List<AddressAdmin>>

    fun getSysConfig(key: SysConfigKey): String

    fun getAdminAddress(key: AddressAdminKey): List<AddressAdmin>

    fun findAllBlockHeight(): Map<String, Long>

    fun getETHNonce(address: String): com.wallet.biz.cache.impl.CacheServiceImpl.EthNonce

    fun findAllWalletToken(): Map<String, Token>

    fun getWalletToken(chainType: String,tokenSymbol:String): Token
}
