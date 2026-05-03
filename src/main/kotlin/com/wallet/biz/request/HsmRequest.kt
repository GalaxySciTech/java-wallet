package com.wallet.biz.request

import com.wallet.biz.domain.po.*
import com.wallet.biz.domain.vo.AddressVo
import com.wallet.biz.signing.ChainSigningService
import org.consenlabs.tokencore.wallet.model.ChainType
import org.consenlabs.tokencore.wallet.transaction.BitcoinTransaction
import org.consenlabs.tokencore.wallet.transaction.TxSignResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.math.BigDecimal

/** 
 * Created by pie on 2019-04-13 16: 01. 
 */
@Component
class HsmRequest {


    fun getAllWallets():List<AddressVo>{
        return hsmXService.getAllWallets()
    }

    fun deriveWallets(chainTypes: List<String>): List<AddressVo> {
        return hsmXService.deriveWallets(chainTypes)
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
        return chainSigningService.signUsdt(ChainType.BITCOIN, signUsdtPo)
    }

    fun signBtcTransaction(
        chainType: String,
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
        return chainSigningService.signBitcoin(chainType, signBitcoinPo)
    }

    fun signEthtransaction(
        chainType: String,
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
        return chainSigningService.signEthereum(chainType, signEthereumPo)
    }

    fun signUsdtCollectTransaction(
        chainType: String,
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
        return chainSigningService.signUsdtCollect(chainType, signUsdtCollectPo)
    }

    fun checkWallet(walletCode: String): String = hsmXService.getAllWallets().firstOrNull { it.walletCode == walletCode }?.address ?: ""

    fun exportWallet(walletCode: String, type: Int): String = hsmXService.exportWallet(walletCode, type)

    fun removeUselessWallet(map: Map<String, String>) { hsmXService.removeUselessWallet(map) }

    fun importWallet(importWalletPo: ImportWalletPo): AddressVo = hsmXService.importWallet(importWalletPo)

    @Autowired
    lateinit var hsmXService: com.wallet.hsm.xservice.HsmXService

    @Autowired
    lateinit var chainSigningService: ChainSigningService
}
