package com.wallet.api.controller

import com.wallet.biz.domain.dict.TokenResponse
import com.wallet.biz.domain.dict.KeyType
import com.wallet.biz.domain.po.*
import com.wallet.biz.domain.vo.TransactionLogVo
import com.wallet.biz.xservice.WalletXService
import com.wallet.entity.domain.Deposit
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.consenlabs.tokencore.wallet.model.ChainType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RestController
@Tag(name = "Wallet API", description = "本地钱包接口")
@RequestMapping("wallet/v1")
class WalletController {

    @PostMapping("get_address")
    @Operation(summary = "获得地址", description = "chainType(btc:BITCOIN|eth:ETHEREUM|trx:TRON)")
    fun getAddress(@RequestParam chain: String, @RequestParam num: Int): TokenResponse<List<String?>> {
        val getAddressPo = GetAddressPo()
        getAddressPo.chain = chain
        getAddressPo.num = num
        val list = walletXService.getAddress(getAddressPo.chain!!.uppercase(), getAddressPo.num!!)
        return TokenResponse(list)
    }

    @PostMapping("send")
    @Operation(summary = "提现", description = "指定提现from地址,不指定将从热钱包转出")
    fun send(
        @RequestParam amount: BigDecimal,
        @RequestParam chain: String,
        @RequestParam(required = false) from: String?,
        @RequestParam(required = false) symbol: String?,
        @RequestParam to: String,
        @RequestParam(required = false) gas: Int?,
        @RequestParam(required = false) gasLimit: Long?,
        @RequestParam(required = false) data: String?
    ): TokenResponse<Any> {
        val sendPo = SendPo()
        sendPo.amount = amount
        sendPo.chain = chain
        sendPo.from = from
        sendPo.symbol = symbol
        sendPo.to = to
        sendPo.gas = gas
        sendPo.gasLimit = gasLimit
        sendPo.data = data
        val hash = walletXService.send(sendPo)
        return TokenResponse(hash)
    }

    @GetMapping("get_hot_address")
    @Operation(summary = "查询热钱包地址", description = "type 100发送钱包300gas钱包")
    fun getHotAddress(@RequestParam chain: String, @RequestParam type: Int): TokenResponse<List<String>?> {
        val getHotAddressPo = GetHotAddressPo()
        getHotAddressPo.chain = chain
        getHotAddressPo.type = type
        val addr = walletXService.getHotAddress(getHotAddressPo.chain!!, getHotAddressPo.type!!)
        return TokenResponse(addr)
    }

    @PostMapping("create_hot_address")
    @Operation(summary = "生成热钱包地址", description = "type(100发送钱包 300gas钱包)")
    fun createHotAddress(@RequestParam type: Int, @RequestParam chain: String): TokenResponse<String> {
        val createHotAddressPo = CreateHotAddressPo()
        createHotAddressPo.type = type
        createHotAddressPo.chain = chain
        val addr = walletXService.createHotAddress(createHotAddressPo).address
        return TokenResponse(addr)
    }

    @GetMapping("check_address")
    @Operation(summary = "检测地址私钥存在")
    fun checkAddress(@RequestParam address: String, @RequestParam chain: String): TokenResponse<Any> {
        val checkAddressPo = CheckAddressPo()
        checkAddressPo.address = address
        checkAddressPo.chain = chain
        val exists = walletXService.checkAddress(checkAddressPo)
        return TokenResponse(exists)
    }

    @GetMapping("get_new_deposit")
    @Operation(summary = "获得充值交易(备用) 拿一次就消失")
    fun getDepositTransaction(): TokenResponse<List<Deposit>> {
        val list = walletXService.getDepositTransactionAndSave()
        return TokenResponse(list)
    }

    @GetMapping("get_transaction")
    @Operation(summary = "获得记录", description = "type(充值 100 |提现 200| 归集 300|发送gas费 400)")
    fun getLocalTransaction(
        @RequestParam type: Int,
        @RequestParam page: Int,
        @RequestParam size: Int
    ): TokenResponse<Page<TransactionLogVo>> {
        val list = walletXService.getLocalTransaction(type, page, size)
        return TokenResponse(list)
    }

    @PostMapping("export_wallet")
    @Operation(summary = "导出私钥/助记词", description = "比特币只能导入导出助记词 |私钥${KeyType.PRIVATE}|助记词${KeyType.MNEMONIC}")
    fun exportWallet(@RequestParam walletCode: String, @RequestParam type: Int): TokenResponse<String> {
        val exportWalletPo = ExportWalletPo()
        exportWalletPo.walletCode = walletCode
        exportWalletPo.type = type
        val key = walletXService.exportWallet(exportWalletPo)
        return TokenResponse(key)
    }

    @PostMapping("import_wallet")
    @Operation(summary = "导入私钥/助记词", description = "比特币只能导入导出助记词 |私钥${KeyType.PRIVATE}|助记词${KeyType.MNEMONIC}")
    fun importWallet(
        @RequestParam type: Int,
        @RequestParam chain: String,
        @RequestParam key: String
    ): TokenResponse<String> {
        val importWalletPo = ImportWalletPo()
        importWalletPo.type = type
        importWalletPo.chain = chain
        importWalletPo.key = key
        val address = walletXService.importWallet(importWalletPo)
        return TokenResponse(address)
    }

    @PostMapping("remove_useless_wallet")
    @Operation(summary = "清除无用的钱包")
    fun removeUselessWallet(): TokenResponse<Any> {
        walletXService.removeUselessWallet()
        return TokenResponse()
    }

    @GetMapping("check_tx_status")
    @Operation(summary = "检查交易状态")
    fun checkTxStatus(@RequestParam hash: String, @RequestParam chain: String): TokenResponse<Boolean> {
        val status = walletXService.checkTxStatus(hash, chain)
        return TokenResponse(status)
    }

    @Autowired
    lateinit var walletXService: WalletXService
}
