package com.wallet.hsm.xservice.impl

import com.wallet.biz.config.WalletProperties
import com.wallet.biz.domain.dict.ErrorCode
import com.wallet.biz.domain.dict.KeyType
import com.wallet.biz.domain.exception.BizException
import com.wallet.biz.domain.po.*
import com.wallet.biz.domain.vo.AddressVo
import com.wallet.hsm.env.KeyStoreProperties
import com.wallet.hsm.xservice.HsmXService
import org.consenlabs.tokencore.foundation.utils.MnemonicUtil
import org.consenlabs.tokencore.wallet.Identity
import org.consenlabs.tokencore.wallet.Wallet
import org.consenlabs.tokencore.wallet.WalletManager
import org.consenlabs.tokencore.wallet.model.*
import org.consenlabs.tokencore.wallet.transaction.BitcoinTransaction
import org.consenlabs.tokencore.wallet.transaction.EOSTransaction
import org.consenlabs.tokencore.wallet.transaction.EthereumTransaction
import org.consenlabs.tokencore.wallet.transaction.TxSignResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.BigInteger

/** 
 * Created by pie on 2019-02-16 14: 19. 
 */
@Service
class HsmXServiceImpl : HsmXService {


    override fun deriveWallets(chainTypes: List<String>): List<AddressVo> {
        return chainTypes.map {
            val addressVo = AddressVo()
            val wallet = deriveWallet(it)
            addressVo.address = wallet.address
            addressVo.walletCode = wallet.id
            addressVo
        }
    }

    private fun deriveWallet(chainType: String): Wallet {
        val chainTypes = ArrayList<String>()
        chainTypes.add(chainType)
        return identity.deriveWalletsByMnemonics(
            chainTypes,
            keyStoreProperties.password,
            MnemonicUtil.randomMnemonicCodes()
        ).first()
    }

    override fun signBitcoinTransaction(signBitcoinPo: SignBitcoinPo): TxSignResult {
        val wallet = WalletManager.mustFindWalletById(signBitcoinPo.walletId)
        val bitcoinTransaction = BitcoinTransaction(
            signBitcoinPo.toAddress,
            0,
            signBitcoinPo.amount!!.multiply(BigDecimal.TEN.pow(8)).toLong(),
            signBitcoinPo.fee!!.multiply(BigDecimal.TEN.pow(8)).toLong(),
            signBitcoinPo.utxos
        )
        val chainIdStr = signBitcoinPo.chainId ?: walletProperties.tokencore.bitcoinChainId
        return bitcoinTransaction.signTransaction(
            chainIdStr,
            keyStoreProperties.password,
            wallet
        )
    }

    override fun signEthereumTransaction(signEthereumPo: SignEthereumPo): TxSignResult {
        val wallet = WalletManager.mustFindWalletById(signEthereumPo.walletId)
        val ethereumTransaction = EthereumTransaction(
            signEthereumPo.nonce!!.toBigInteger(),
            signEthereumPo.gasPrice!!.multiply(BigDecimal.TEN.pow(9)).toBigInteger(),
            BigInteger.valueOf(signEthereumPo.gasLimit!!),
            signEthereumPo.toAddress,
            signEthereumPo.amount!!.multiply(BigDecimal.TEN.pow(18)).toBigInteger(),
            signEthereumPo.data
        )
        val chainIdStr = signEthereumPo.chainId ?: walletProperties.tokencore.ethereumChainId
        return ethereumTransaction.signTransaction(
            chainIdStr,
            keyStoreProperties.password,
            wallet
        )
    }

    override fun signEosTransaction(signEosPo: SignEosPo): TxSignResult {
        val wallet = WalletManager.mustFindWalletById(signEosPo.walletId)
        val eosTransaction = EOSTransaction(
            signEosPo.signParam,
            EOSContract.TRANSFER,
            signEosPo.fromAddress,
            signEosPo.toAddress,
            "${signEosPo.amount} EOS",
            ""
        )
        return eosTransaction.signTransaction(keyStoreProperties.password, wallet)
    }

    override fun signUsdtTransaction(signUsdtPo: SignUsdtPo): TxSignResult {
        val wallet = WalletManager.mustFindWalletById(signUsdtPo.walletId)
        val bitcoinTransaction = BitcoinTransaction(
            signUsdtPo.toAddress,
            0,
            signUsdtPo.amount!!.multiply(BigDecimal.TEN.pow(8)).toLong(),
            signUsdtPo.fee!!.multiply(BigDecimal.TEN.pow(8)).toLong(),
            signUsdtPo.utxos
        )
        val chainIdStr = signUsdtPo.chainId ?: walletProperties.tokencore.bitcoinChainId
        return bitcoinTransaction.signUsdtTransaction(
            chainIdStr,
            keyStoreProperties.password,
            wallet
        )
    }

    override fun signUsdtCollectTransaction(signUsdtCollectPo: SignUsdtCollectPo): TxSignResult {
        val wallet = WalletManager.mustFindWalletById(signUsdtCollectPo.walletId)
        val feeProviderWallet = WalletManager.mustFindWalletById(signUsdtCollectPo.feeProviderWalletId)
        val bitcoinTransaction = BitcoinTransaction(
            signUsdtCollectPo.toAddress,
            0,
            signUsdtCollectPo.amount!!.multiply(BigDecimal.TEN.pow(8)).toLong(),
            signUsdtCollectPo.fee!!.multiply(BigDecimal.TEN.pow(8)).toLong(),
            signUsdtCollectPo.utxos
        )
        val chainIdStr = signUsdtCollectPo.chainId ?: walletProperties.tokencore.bitcoinChainId
        return bitcoinTransaction.signUsdtCollectTransaction(
            chainIdStr,
            keyStoreProperties.password,
            wallet, feeProviderWallet, signUsdtCollectPo.feeProviderUtxos
        )
    }

    override fun importWallet(importWalletPo: ImportWalletPo): AddressVo {
        ChainType.validate(importWalletPo.chain)
        val name: String
        val path: String
        when (importWalletPo.chain) {
            ChainType.BITCOIN -> {
                name = "BTC"
                path = BIP44Util.BITCOIN_MAINNET_PATH
            }
            ChainType.BITCOINCASH -> {
                name = "BCH"
                path = BIP44Util.BITCOINCASH_MAINNET_PATH
            }
            ChainType.BITCOINSV -> {
                name = "BSV"
                path = BIP44Util.BITCOINSV_MAINNET_PATH
            }
            ChainType.ETHEREUM -> {
                name = "ETH"
                path = BIP44Util.ETHEREUM_PATH
            }
            ChainType.LITECOIN -> {
                name = "LTC"
                path = BIP44Util.LITECOIN_MAINNET_PATH
            }
            ChainType.DASH -> {
                name = "DASH"
                path = BIP44Util.DASH_MAINNET_PATH
            }
            ChainType.TRON -> {
                name = "TRON"
                path = BIP44Util.TRON_PATH
            }
            ChainType.EOS -> {
                name = "EOS"
                path = BIP44Util.EOS_PATH
            }
            ChainType.DOGECOIN -> {
                name = "DOGE"
                path = BIP44Util.DOGECOIN_MAINNET_PATH
            }
            ChainType.FILECOIN -> {
                name = "FIL"
                path = BIP44Util.FILECOIN_PATH
            }
            else -> throw BizException(ErrorCode.NO_THIS_TYPE)
        }
        val metadata = Metadata(
            importWalletPo.chain,
            Network.MAINNET,
            name,
            ""
        )
        val wallet = when (importWalletPo.type) {
            KeyType.PRIVATE -> {
                metadata.source = Metadata.FROM_PRIVATE
                WalletManager.importWalletFromPrivateKey(
                    metadata,
                    importWalletPo.key,
                    keyStoreProperties.password,
                    true
                )
            }
            KeyType.MNEMONIC -> {
                metadata.source = Metadata.FROM_MNEMONIC
                WalletManager.importWalletFromMnemonic(
                    metadata,
                    importWalletPo.key,
                    path,
                    keyStoreProperties.password,
                    true
                )
            }
            else -> throw BizException(ErrorCode.NO_THIS_TYPE)
        }
        val addressVo = AddressVo()
        addressVo.address = wallet.address
        addressVo.walletCode = wallet.id
        addressVo.chainType=wallet.metadata.chainType
        return addressVo
    }

    override fun exportWallet(walletId: String, type: Int): String {
        return when (type) {
            KeyType.PRIVATE -> WalletManager.exportPrivateKey(walletId, keyStoreProperties.password)
            KeyType.MNEMONIC -> WalletManager.exportMnemonic(walletId, keyStoreProperties.password).mnemonic
            else -> throw BizException(ErrorCode.NO_THIS_TYPE)
        }
    }

    override fun flushWalletToIdentity() {
        val keymap = WalletManager.getKeyMap()
        val identityWallets = identity.wallets.associateBy { it.id }
        keymap.forEach { (key, value) ->
            val v = identityWallets[key]
            if (v == null) {
                identity.addWallet(Wallet(value))
            }
        }
    }

    override fun removeUselessWallet(useWalletMap: Map<String, String>) {
        WalletManager.scanWallets()
        flushWalletToIdentity()
        val walletIds = WalletManager.getKeyMap().map {
            it.key
        }
        walletIds.forEach {
            val exist = useWalletMap[it]
            if (exist == null) {
                WalletManager.removeWallet(it, keyStoreProperties.password)
            }
        }
    }

    override fun getAllWallets(): List<AddressVo> {
        return WalletManager.getKeyMap().map {
            val vo = AddressVo()
            vo.chainType=it.value.metadata.chainType
            vo.address=it.value.address
            vo.walletCode=it.key
            vo
        }
    }

    override fun getWalletByAddress(type:String,address:String): String {
        val wallet=WalletManager.findWalletByAddress(type,address)
        return wallet.id
    }

    companion object {
        val identity: Identity
            get() = Identity.currentIdentity
                ?: throw IllegalStateException("Identity not initialized. Ensure ScanKeyStoreConfig has run.")
    }

    @Autowired
    lateinit var keyStoreProperties: KeyStoreProperties

    @Autowired
    lateinit var walletProperties: WalletProperties
}
