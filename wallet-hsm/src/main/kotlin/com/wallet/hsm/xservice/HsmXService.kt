package com.wallet.hsm.xservice

import com.wallet.biz.domain.po.*
import com.wallet.biz.domain.vo.AddressVo
import org.consenlabs.tokencore.wallet.transaction.TxSignResult

/** 
 * Created by pie on 2019-02-16 14: 19. 
 */
interface HsmXService {
    fun deriveWallets(chainTypes: List<String>): List<AddressVo>

    fun signBitcoinTransaction(signBitcoinPo: SignBitcoinPo): TxSignResult

    fun signEthereumTransaction(signEthereumPo: SignEthereumPo): TxSignResult

    fun signEosTransaction(signEosPo: SignEosPo): TxSignResult

    fun signUsdtTransaction(signUsdtPo: SignUsdtPo): TxSignResult

    fun signUsdtCollectTransaction(signUsdtCollectPo: SignUsdtCollectPo): TxSignResult

    fun importWallet(importWalletPo: ImportWalletPo): AddressVo

    fun exportWallet(walletId: String, type: Int): String

    fun flushWalletToIdentity()

    fun removeUselessWallet(useWalletMap: Map<String, String>)

    fun getAllWallets(): List<AddressVo>

    fun getWalletByAddress(type:String,address:String): String
}
