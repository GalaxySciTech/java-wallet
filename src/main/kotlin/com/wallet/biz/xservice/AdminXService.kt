package com.wallet.biz.xservice

import com.wallet.entity.domain.*

/** 
 * Created by pie on 2020/12/7 13: 06. 
 */
interface AdminXService {
    fun getAddrList(): List<Address>

    fun getSysConfigList(): List<Config>

    fun login(name: String, password: String): String

    fun getWhiteList(): List<White>

    fun getAddrAdminList(): List<AddressAdmin>

    fun getTransactionList(): List<Deposit>

    fun getWithdrawList(): List<Withdraw>

    fun getBlockHeightList(): List<BlockHeight>

    fun getTokenList(): List<Token>

    fun getDashboard(): Map<String,Any>

    fun addAddrAdmin(type: Int, address: String, chainType: String)

    fun delAddrAdmin(id: Long)

    fun editAddrAdmin(walletAddressAdmin: AddressAdmin)

    fun editToken(walletToken: Token)

    fun delToken(id: Long)

    fun editBlockHeight(walletBlockHeight: BlockHeight)

    fun delBlockHeight(id: Long)

    fun editConfig(config: Config)

    fun delConfig(id: Long)

    fun editWhite(white: White)

    fun delWhite(id: Long)
}
