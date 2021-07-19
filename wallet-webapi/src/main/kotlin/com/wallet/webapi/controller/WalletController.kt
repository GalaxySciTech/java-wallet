package com.wallet.webapi.controller

import com.wallet.biz.domain.dict.TokenResponse
import com.wallet.biz.domain.dict.KeyType
import com.wallet.biz.domain.po.*
import com.wallet.biz.domain.vo.TransactionLogVo
import com.wallet.biz.xservice.WalletXService
import com.wallet.entity.domain.Deposit
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.consenlabs.tokencore.wallet.model.ChainType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

/** 
 * Created by pie on 2019-04-11 16: 36. 
 */
@RestController
@Api(description = "本地钱包接口")
@RequestMapping("wallet/v1")
class WalletController {

    @GetMapping("get_address")
    @ApiOperation("获得地址 例如/getaddr/BITCOIN  chainType(btc:${ChainType.BITCOIN}|eth:${ChainType.ETHEREUM}|trx:${ChainType.TRON})")
    fun getAddress(chain: String, num: Int): TokenResponse<List<String?>> {
        val getAddressPo = GetAddressPo()
        getAddressPo.chain = chain
        getAddressPo.num = num
        val list = walletXService.getAddress(getAddressPo.chain!!.toUpperCase(), getAddressPo.num!!)
//        val list= arrayListOf("18fiBrC2Gnnih53S9Aq5hiuNRD21xm5o7p","1LrtBDVdxDnq7WVoLmSLJT7iw6GLKZhSGt")
        return TokenResponse(list)
    }

    @GetMapping("send")
    @ApiOperation("提现(指定提现from地址,不指定将从热钱包转出)")
    fun send(
        amount: BigDecimal,
        chain: String,
        from: String?,
        symbol: String?,
        to: String,
        gas: Int?,
        gasLimit: Long?,
        data: String?
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
    @ApiOperation("查询热钱包地址 type 100发送钱包300gas钱包")
    fun getHotAddress(chain: String, type: Int): TokenResponse<List<String>?> {
        val getHotAddressPo = GetHotAddressPo()
        getHotAddressPo.chain = chain
        getHotAddressPo.type = type
        val addr = walletXService.getHotAddress(getHotAddressPo.chain!!, getHotAddressPo.type!!)
        return TokenResponse(addr)
    }

    @GetMapping("create_hot_address")
    @ApiOperation("生成热钱包地址 type(100发送钱包 300gas钱包)")
    fun createHotAddress(type: Int, chain: String): TokenResponse<String> {
        val createHotAddressPo = CreateHotAddressPo()
        createHotAddressPo.type = type
        createHotAddressPo.chain = chain
        val addr = walletXService.createHotAddress(createHotAddressPo).address
        return TokenResponse(addr)
    }

    @GetMapping("check_address")
    @ApiOperation("检测地址私钥存在")
    fun checkAddress(address: String, chain: String): TokenResponse<Any> {
        val checkAddressPo = CheckAddressPo()
        checkAddressPo.address = address
        checkAddressPo.chain = chain
        val exists = walletXService.checkAddress(checkAddressPo)
        return TokenResponse(exists)
    }

    @GetMapping("get_new_deposit")
    @ApiOperation("获得充值交易(备用) 拿一次就消失")
    fun getDepositTransaction(): TokenResponse<List<Deposit>> {
        val list = walletXService.getDepositTransactionAndSave()
        return TokenResponse(list)
    }

    @GetMapping("get_transaction")
    @ApiOperation("获得记录 type(充值 100 |提现 200| 归集 300|发送gas费 400  )")
    fun getLocalTransaction(type: Int, page: Int, size: Int): TokenResponse<Page<TransactionLogVo>> {
        val list = walletXService.getLocalTransaction(
            type,
            page,
            size
        )
        return TokenResponse(list)
    }

    @GetMapping("export_wallet")
    @ApiOperation("导出私钥/助记词(比特币只能导入导出助记词) |私钥${KeyType.PRIVATE}|助记词${KeyType.MNEMONIC}")
    fun exportWallet(walletCode: String, type: Int): TokenResponse<String> {
        val exportWalletPo = ExportWalletPo()
        exportWalletPo.walletCode = walletCode
        exportWalletPo.type = type
        val key = walletXService.exportWallet(exportWalletPo)
        return TokenResponse(key)
    }

    @GetMapping("import_wallet")
    @ApiOperation("导入私钥/助记词(比特币只能导入导出助记词) |私钥${KeyType.PRIVATE}|助记词${KeyType.MNEMONIC}")
    fun importWallet(type: Int, chain: String, key: String): TokenResponse<String> {
        val importWalletPo = ImportWalletPo()
        importWalletPo.type = type
        importWalletPo.chain = chain
        importWalletPo.key = key
        val address = walletXService.importWallet(importWalletPo)
        return TokenResponse(address)
    }

    @GetMapping("remove_useless_wallet")
    @ApiOperation("清除无用的钱包")
    fun removeUselessWallet(): TokenResponse<Any> {
        walletXService.removeUselessWallet()
        return TokenResponse()
    }

    @GetMapping("check_tx_status")
    @ApiOperation("检查交易状态")
    fun checkTxStatus(hash: String, chain: String): TokenResponse<Boolean> {
        val status = walletXService.checkTxStatus(hash, chain)
        return TokenResponse(status)
    }


//    @GetMapping("put_all_address_to_wait_import")
//    @ApiOperation("把所有地址导入到节点")
//    fun putAllAddressToWaitImport(): TokenResponse<Any>{
//        walletXService.putAllAddressToWaitImport()
//        return TokenResponse()
//    }

//    @GetMapping("add_wait_collect")
//    @ApiOperation("添加待归集地址")
//    fun addWaitCollect(): TokenResponse<Any> {
//        walletXService.addWaitCollect()
//        return TokenResponse()
//    }

//    @PostMapping("rescan_transaction")
//    @ApiOperation("重扫充值交易")
//    fun rescanTransaction(@RequestBody rescanTransactionPo:RescanTransactionPo): TokenResponse<Any>{
//        walletXService.rescanTransaction(rescanTransactionPo)
//        return TokenResponse()
//    }
//
//    @PostMapping("rescan_block")
//    @ApiOperation("重扫区块高度")
//    fun rescanBlock(@RequestBody rescanBlockPo:RescanBlockPo): TokenResponse<Any>{
//        walletXService.rescanBlock(rescanBlockPo)
//        return TokenResponse()
//    }


    @Autowired
    lateinit var walletXService: WalletXService


}
