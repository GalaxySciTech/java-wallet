package com.wallet.hsm.controller

import com.wallet.biz.domain.dict.TokenResponse
import com.wallet.biz.domain.po.*
import com.wallet.biz.domain.vo.AddressVo
import com.wallet.hsm.env.KeyStoreProperties
import com.wallet.hsm.xservice.HsmXService
import org.consenlabs.tokencore.wallet.WalletManager
import org.consenlabs.tokencore.wallet.transaction.TxSignResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


/**
 * Created by pie on 2019/2/15 15: 42.
 */
@RestController
@RequestMapping("hsm")
class HsmController {

    @PostMapping("deriveWallets")
    fun deriveWallets(@RequestBody chainTypes: List<String>): TokenResponse<List<AddressVo>> {
        return TokenResponse(hsmXService.deriveWallets(chainTypes))
    }

    @PostMapping("sign_bitcoin_transaction")
    fun signBitcoinTransaction(@RequestBody signBitcoinPo: SignBitcoinPo): TokenResponse<TxSignResult> {
        return TokenResponse(hsmXService.signBitcoinTransaction(signBitcoinPo))
    }

    @PostMapping("sign_ethereum_transaction")
    fun signEthereumTransaction(@RequestBody signEthereumPo: SignEthereumPo): TokenResponse<TxSignResult> {
        return TokenResponse(hsmXService.signEthereumTransaction(signEthereumPo))
    }

    @PostMapping("sign_eos_transaction")
    fun signEosTransaction(@RequestBody signEosPo: SignEosPo): TokenResponse<TxSignResult> {
        return TokenResponse(hsmXService.signEosTransaction(signEosPo))
    }

    @PostMapping("sign_usdt_transaction")
    fun signUsdtTransaction(@RequestBody signUsdtPo: SignUsdtPo): TokenResponse<TxSignResult> {
        return TokenResponse(hsmXService.signUsdtTransaction(signUsdtPo))
    }

    @PostMapping("sign_usdt_collect_transaction")
    fun signUsdtTransaction(@RequestBody signUsdtCollectPo: SignUsdtCollectPo): TokenResponse<TxSignResult> {
        return TokenResponse(hsmXService.signUsdtCollectTransaction(signUsdtCollectPo))
    }

    @GetMapping("eos_deposit_address")
    fun eosDepositAddress(): TokenResponse<String> {
        return TokenResponse(keyStoreProperties.eosDepositAddress!!)
    }

    @GetMapping("check_wallet/{walletId}")
    fun checkWallet(@PathVariable walletId: String): TokenResponse<Boolean> {
        WalletManager.mustFindWalletById(walletId)
        return TokenResponse(true)
    }

    @GetMapping("export_wallet/{walletId}/{type}")
    fun exportWallet(@PathVariable walletId: String, @PathVariable type: Int): TokenResponse<String> {
        return TokenResponse(hsmXService.exportWallet(walletId, type))
    }

    @PostMapping("import_wallet")
    fun importWallet(@RequestBody importWalletPo: ImportWalletPo): TokenResponse<AddressVo> {
        return TokenResponse(hsmXService.importWallet(importWalletPo))
    }

    @GetMapping("get_wallet_by_address")
    fun getWalletByAddress(type:String,address:String): TokenResponse<Any> {
        val res=hsmXService.getWalletByAddress(type,address)
        return TokenResponse(res)
    }

    @GetMapping("flush_wallet_to_identity")
    fun flushWalletToIdentity(): TokenResponse<Any> {
        hsmXService.flushWalletToIdentity()
        return TokenResponse()
    }

    @PostMapping("remove_useless_wallet")
    fun removeUselessWallet(@RequestBody useWalletMap:Map<String,String>):TokenResponse<Any>{
        hsmXService.removeUselessWallet(useWalletMap)
        return TokenResponse()
    }

    @GetMapping("get_all_wallets")
    fun getAllWallets():TokenResponse<List<AddressVo>>{
        val list=hsmXService.getAllWallets()
        return TokenResponse(list)
    }

    @Autowired
    lateinit var hsmXService: HsmXService
    @Autowired
    lateinit var keyStoreProperties: KeyStoreProperties
}
