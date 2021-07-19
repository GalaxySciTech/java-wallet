package com.wallet.biz.xservice.impl

import com.wallet.biz.dict.TxLogDict
import com.wallet.biz.dict.AddressAdminKey
import com.wallet.biz.dict.WithType
import com.wallet.biz.domain.*
import com.wallet.biz.domain.dict.ErrorCode
import com.wallet.biz.domain.dict.KeyType
import com.wallet.biz.domain.exception.BizException
import com.wallet.biz.domain.po.*
import com.wallet.biz.domain.vo.BTCScript
import com.wallet.biz.domain.vo.TransactionLogVo
import com.wallet.biz.domain.vo.TransactionVo
import com.wallet.biz.request.HsmRequest
import com.wallet.biz.rpc.RpcClient
import com.wallet.biz.service.*
import com.wallet.biz.utils.ETHUtils
import com.wallet.biz.utils.OMNIUtils
import com.wallet.biz.xservice.WalletXService
import com.wallet.entity.domain.*
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.kiulian.converter.AddressConverter
import com.wallet.biz.cache.CacheService
import com.wallet.biz.log.impl.LogService
import org.bitcoinj.core.ECKey
import org.consenlabs.tokencore.foundation.utils.NumericUtil
import org.consenlabs.tokencore.wallet.model.ChainType
import org.consenlabs.tokencore.wallet.network.BitcoinCashMainNetParams
import org.consenlabs.tokencore.wallet.transaction.BitcoinTransaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Bool
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.protocol.core.DefaultBlockParameterName
import wf.bitcoin.javabitcoindrpcclient.BitcoinJSONRPCClient
import wf.bitcoin.javabitcoindrpcclient.BitcoindRpcClient
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*


/** 
 * Created by pie on 2019-04-13 14: 22. 
 */
@Service
open class WalletXServiceImpl : WalletXService, LogService() {

    override fun getLocalTransaction(type: Int?, page: Int, size: Int): Page<TransactionLogVo> {
        return when (type) {
            TxLogDict.DEPOSIT -> {
                val find = PageEntity(Deposit())
                find.page = page
                find.size = size
                val pageList = depositService.findByEntity(find).map {
                    val txLogVo = TransactionLogVo()
                    txLogVo.to = it.address
                    txLogVo.hash = it.hash
                    txLogVo.amount = it.amount
                    txLogVo.time = it.createdAt
                    txLogVo.chainType = it.chainType
                    txLogVo.tokenSymbol = it.tokenSymbol
                    txLogVo.type = TxLogDict.DEPOSIT
                    txLogVo
                }
                PageImpl(pageList.toList(), PageRequest(page, size), pageList.totalElements)
            }
            TxLogDict.WITHDRAW -> {
                val find = PageEntity(Withdraw())
                find.page = page
                find.size = size
                find.entity.withdrawType = WithType.SEND
                val pageList = walletAddressWithdrawService.findByEntity(find).map {
                    val txLogVo = TransactionLogVo()
                    txLogVo.from = it.fromAddress
                    txLogVo.to = it.toAddress
                    txLogVo.hash = it.hash
                    txLogVo.amount = it.amount
                    txLogVo.time = it.createdAt
                    txLogVo.chainType = it.chainType
                    txLogVo.tokenSymbol = it.tokenSymbol
                    txLogVo.type = TxLogDict.WITHDRAW
                    txLogVo
                }
                PageImpl(pageList.toList(), PageRequest(page, size), pageList.totalElements)
            }
            TxLogDict.COLLECT -> {
                val find = PageEntity(Withdraw())
                find.page = page
                find.size = size
                find.entity.withdrawType = WithType.COLLECT
                val pageList = walletAddressWithdrawService.findByEntity(find).map {
                    val txLogVo = TransactionLogVo()
                    txLogVo.from = it.fromAddress
                    txLogVo.to = it.toAddress
                    txLogVo.hash = it.hash
                    txLogVo.amount = it.amount
                    txLogVo.time = it.createdAt
                    txLogVo.chainType = it.chainType
                    txLogVo.tokenSymbol = it.tokenSymbol
                    txLogVo.type = TxLogDict.WITHDRAW
                    txLogVo
                }
                PageImpl(pageList.toList(), PageRequest(page, size), pageList.totalElements)
            }
            TxLogDict.SEND_FEE -> {
                val find = PageEntity(Withdraw())
                find.page = page
                find.size = size
                find.entity.withdrawType = WithType.SEND_FEE
                val pageList = walletAddressWithdrawService.findByEntity(find).map {
                    val txLogVo = TransactionLogVo()
                    txLogVo.from = it.fromAddress
                    txLogVo.to = it.toAddress
                    txLogVo.hash = it.hash
                    txLogVo.amount = it.amount
                    txLogVo.time = it.createdAt
                    txLogVo.chainType = it.chainType
                    txLogVo.tokenSymbol = it.tokenSymbol
                    txLogVo.type = TxLogDict.WITHDRAW
                    txLogVo
                }
                PageImpl(pageList.toList(), PageRequest(page, size), pageList.totalElements)
            }
            else -> throw BizException(ErrorCode.ERROR_PARAM)
        }
    }

    override fun getDepositTransactionAndSave(): List<Deposit> {
        val list = getDepositTransaction()
        saveUploadedDepositTransaction(list)
        return list
    }

    override fun getDepositTransaction(): List<Deposit> {
        log("拿取未上传的交易")
        val list = depositService.getByIsUpload(0)
        return list
    }

    override fun saveUploadedDepositTransaction(saveList: List<Deposit>) {
        saveList.forEach {
            it.isUpload = 1
        }
        depositService.saveAll(saveList)
        log("更改未上传交易的交易状态并且保存")
    }

    override fun checkAddress(checkAddressPo: CheckAddressPo): String {
        val walletAddress = Address()
        walletAddress.address = checkAddressPo.address
        walletAddress.chainType = checkAddressPo.chain
        val addr = addressService.findByBean(walletAddress).first()
        log("从数据库查询该地址${checkAddressPo.address}参数")
        log("从hsm检查钱包是否存在")
        return hsmRequest.checkWallet(addr.walletCode)
    }


    override fun getHotAddress(chainType: String, type: Int): List<String>? {
        val adminMap = cacheService.findAllAdminAddress()
        return adminMap["$chainType$type"]?.map { it.address }
    }

    override fun saveWithReocord(
        amount: BigDecimal,
        hash: String,
        toAddress: String,
        fromAddress: String,
        withType: Int,
        chainType: String,
        tokenSymbol: String
    ) {
        val walletAddressWithdraw = Withdraw()
        walletAddressWithdraw.amount = amount
        walletAddressWithdraw.hash = hash
        walletAddressWithdraw.toAddress = toAddress
        walletAddressWithdraw.fromAddress = fromAddress
        walletAddressWithdraw.withdrawType = withType
        walletAddressWithdraw.chainType = chainType
        walletAddressWithdraw.tokenSymbol = tokenSymbol
        walletAddressWithdrawService.save(walletAddressWithdraw)
    }

    override fun getTransaction(getTransactionByHashPo: GetTransactionPo): List<TransactionVo> {
        when (getTransactionByHashPo.type) {
            100 -> {
                val vo = TransactionVo()
                return when (getTransactionByHashPo.chain) {
                    ChainType.BITCOIN -> {
                        val rpc = rpcClient.omniRpc()
                        if (getTransactionByHashPo.tokenAddress == null) {
                            val transaction = rpc.getRawTransaction(getTransactionByHashPo.hash)
                            var fromValue = BigDecimal.ZERO
                            var toValue = BigDecimal.ZERO
                            vo.from = transaction.vIn().map {
                                if (it.txid() != null) {
                                    val sendTransaction = rpc.getRawTransaction(it.txid())
                                    val vin = sendTransaction.vOut()[it.vout()]
                                    fromValue += vin.value()
                                    val btcScript = BTCScript()
                                    btcScript.address = vin.scriptPubKey().addresses()?.firstOrNull()
                                    btcScript.value = vin.value()
                                    btcScript
                                } else {
                                    null
                                }
                            }
                            vo.to = transaction.vOut().map {
                                toValue += it.value()
                                val btcScript = BTCScript()
                                btcScript.address = it.scriptPubKey().addresses()?.firstOrNull()
                                btcScript.value = it.value()
                                btcScript
                            }
                            val fee = fromValue - toValue
                            if (fee > BigDecimal.ZERO)
                                vo.fee = fee
                            vo.txid = transaction.txId()
                        } else {
                            val transaction = rpc.omniGetTransaction(getTransactionByHashPo.hash!!)
                            if (transaction.propertyid == getTransactionByHashPo.tokenAddress!!.toLong()) {
                                vo.from = transaction.sendingaddress
                                vo.to = transaction.referenceaddress
                                vo.value = transaction.amount
                                vo.fee = transaction.fee
                                vo.txid = transaction.txid
                            }
                        }
                        listOf(vo)
                    }
                    ChainType.ETHEREUM -> {
                        val rpc = rpcClient.ethRpc()
                        val transaction = rpc.ethGetTransactionByHash(getTransactionByHashPo.hash).send().result
                        if (getTransactionByHashPo.tokenAddress == null) {
                            vo.txid = transaction.hash
                            vo.from = transaction.from
                            vo.to = transaction.to
                            vo.value = transaction.value.toBigDecimal().divide(BigDecimal.TEN.pow(18))
                            vo.fee =
                                (transaction.gas * transaction.gasPrice).toBigDecimal().divide(BigDecimal.TEN.pow(18))
                        } else {
                            if (transaction.to == getTransactionByHashPo.tokenAddress) {
                                val decimals = ethUtils.getETHContractDecimals(transaction.to)
                                vo.from = transaction.from
                                val input = decodeTransferInput(transaction.input)
                                vo.pass = input.pass
                                vo.to = input.to
                                vo.value = input.value!!.toBigDecimal().divide(BigDecimal.TEN.pow(decimals))
                                vo.txid = transaction.hash
                                vo.fee = (transaction.gas * transaction.gasPrice).toBigDecimal()
                                    .divide(BigDecimal.TEN.pow(18))
                            }
                        }
                        listOf(vo)
                    }
                    else -> throw BizException(ErrorCode.NO_THIS_CHAIN_TYPE)
                }
            }
            200 -> {
                return listOf()
            }
            else -> throw BizException(ErrorCode.NO_THIS_TYPE)
        }
    }

    override fun getAddress(chainType: String, num: Int): List<String?> {
        val list = ArrayList<String>()
        for (i in 1..num) {
            list.add(chainType)
        }
        log("从Hsm获取${chainType}地址 ${num}个")
        val re = hsmRequest.deriveWallets(list).map {
            when (chainType) {
                ChainType.ETHEREUM -> it.address = "0x${it.address}"
                ChainType.BITCOINCASH -> it.address = AddressConverter.toCashAddress(it.address)
            }
            it
        }
        val mapList = re.map {
            val walletAddress = Address()
            walletAddress.address = it.address
            walletAddress.chainType = chainType
            walletAddress.walletCode = it.walletCode
            walletAddress.autoCollect = 1
            walletAddress
        }
        log("正在把地址保存到数据库")
        addressService.saveAll(mapList)
        val addressList = re.map { it.address!! }

        if (chainType == ChainType.BITCOIN ||
            chainType == ChainType.LITECOIN ||
            chainType == ChainType.BITCOINSV ||
            chainType == ChainType.DOGECOIN ||
            chainType == ChainType.BITCOINCASH ||
            chainType == ChainType.DASH
        ) {
            log("把地址名导入到节点，维护地址的utxo")
            val waitImportList = addressList.map {
                val waitImport = WaitImport()
                waitImport.address = it
                waitImport.chainType = chainType
                waitImport
            }
            walletWaitImportService.saveAll(waitImportList)
        }

        return addressList
    }

    override fun send(sendPo: SendPo): String {
        if (sendPo.from == null) {
            log("from参数为空，自动设置from地址为热钱包地址")
            val sendAddressAdmin = when (sendPo.chain) {
                ChainType.ETHEREUM -> {
                    cacheService.getAdminAddress(AddressAdminKey.ETH_SEND).firstOrNull()
                        ?: throw BizException(ErrorCode.SEND_ADDRESS_NOT_FOUND)
                }
                ChainType.BITCOIN -> {
                    cacheService.getAdminAddress(AddressAdminKey.BTC_SEND).firstOrNull()
                        ?: throw BizException(ErrorCode.SEND_ADDRESS_NOT_FOUND)
                }
                ChainType.DASH -> {
                    cacheService.getAdminAddress(AddressAdminKey.DASH_SEND).firstOrNull()
                        ?: throw BizException(ErrorCode.SEND_ADDRESS_NOT_FOUND)
                }
                ChainType.BITCOINCASH -> {
                    cacheService.getAdminAddress(AddressAdminKey.BCH_SEND).firstOrNull()
                        ?: throw BizException(ErrorCode.SEND_ADDRESS_NOT_FOUND)
                }
                ChainType.BITCOINSV -> {
                    cacheService.getAdminAddress(AddressAdminKey.BSV_SEND).firstOrNull()
                        ?: throw BizException(ErrorCode.SEND_ADDRESS_NOT_FOUND)
                }
                ChainType.LITECOIN -> {
                    cacheService.getAdminAddress(AddressAdminKey.LTC_SEND).firstOrNull()
                        ?: throw BizException(ErrorCode.SEND_ADDRESS_NOT_FOUND)
                }
                ChainType.DOGECOIN -> {
                    cacheService.getAdminAddress(AddressAdminKey.DOGE_SEND).firstOrNull()
                        ?: throw BizException(ErrorCode.SEND_ADDRESS_NOT_FOUND)
                }
                ChainType.TRON -> {
                    cacheService.getAdminAddress(AddressAdminKey.TRON_SEND).firstOrNull()
                        ?: throw BizException(ErrorCode.SEND_ADDRESS_NOT_FOUND)
                }
                else -> throw BizException(ErrorCode.NO_THIS_CHAIN_TYPE)
            }
            sendPo.from = sendAddressAdmin.address
            sendPo.fromWalletCode = sendAddressAdmin.walletCode
        } else {
            val walletAddress = Address()
            walletAddress.address = sendPo.from
            walletAddress.chainType = sendPo.chain
            log("正在从钱包数据库获取from地址的参数")
            val sendAddress = addressService.findByBean(walletAddress).firstOrNull()
                ?: throw BizException(ErrorCode.SEND_ADDRESS_NOT_FOUND)
            sendPo.from = sendAddress.address
            sendPo.fromWalletCode = sendAddress.walletCode
        }

        return when (sendPo.chain) {
            ChainType.ETHEREUM -> {
                when (sendPo.symbol) {
                    null -> sendETH(sendPo)
                    else -> sendERC20(sendPo)
                }
            }
            ChainType.TRON -> {
                when (sendPo.symbol) {
                    null -> sendTRX(sendPo)
                    else -> sendTRC20(sendPo)
                }
            }
            ChainType.BITCOIN -> {
                when (sendPo.symbol) {
                    null -> sendBTCOrFork(sendPo, ChainType.BITCOIN, rpcClient.omniRpc())
                    else -> sendOMNI(sendPo)
                }
            }
            ChainType.DASH -> {
                sendBTCOrFork(sendPo, ChainType.DASH, rpcClient.dashRpc())
            }
            ChainType.LITECOIN -> {
                sendBTCOrFork(sendPo, ChainType.LITECOIN, rpcClient.ltcRpc())
            }
            ChainType.BITCOINCASH -> {
                sendBCHOrFork(sendPo, ChainType.BITCOINCASH, rpcClient.bchRpc())
            }
            ChainType.BITCOINSV -> {
                sendBCHOrFork(sendPo, ChainType.BITCOINSV, rpcClient.bsvRpc())
            }
            ChainType.DOGECOIN -> {
                sendBTCOrFork(sendPo, ChainType.DOGECOIN, rpcClient.dogeRpc())
            }
            else -> throw BizException(ErrorCode.NO_THIS_CHAIN_TYPE)
        }
    }

    private fun sendBCHOrFork(
        sendPo: SendPo,
        chainType: String,
        rpc: BitcoinJSONRPCClient
    ): String {
        log("开始${chainType}转币 转出地址:${sendPo.from} 转入地址:${sendPo.to} 数量:${sendPo.amount}")
        log("获得${chainType} RPC")
        val sendAddr = sendPo.from!!
        log("开始${chainType}转币 获取转出地址${sendAddr}的utxo")
        var utxos = getUtxos(chainType, sendAddr)
        var balance = BigDecimal.ZERO
        utxos.forEach {
            balance += it.amount.toBigDecimal().divide(BigDecimal.TEN.pow(8))
        }
        log("${chainType} 转出地址${sendAddr}的余额为${balance}")
        if (utxos.isEmpty()) throw BizException(ErrorCode.UTXO_NOT_FOUND)
        val satPerByte = sendPo.gas?.toLong() ?: rpcClient.getSatPerByte()
        log("开始${chainType}转币 设置satPerByte${satPerByte}")
        val fee = omniUtils.calculateFee(utxos.size, 2, satPerByte)
        val totalAmount = sendPo.amount!!.multiply(BigDecimal.TEN.pow(8)).toLong() + fee
        utxos = checkAmount(utxos, totalAmount)
        log("正在签名中")
        val privateKey = hsmRequest.exportWallet(sendPo.fromWalletCode!!, KeyType.PRIVATE)
        val txid = try {
            log("${chainType} 推送已签名交易到节点")
            val inputs = utxos.map {
                BitcoindRpcClient.BasicTxInput(it.txHash, it.vout, it.address)
            }
            val outputs = listOf(BitcoindRpcClient.BasicTxOutput(sendPo.to, sendPo.amount!!))
            val unsignedTx = rpc.createRawTransaction(inputs, outputs)
            val eckey = ECKey.fromPrivate(NumericUtil.hexToBigInteger(privateKey))
            val wifiKey = eckey.getPrivateKeyAsWiF(BitcoinCashMainNetParams.get())
            val signedTx = rpc.signRawTransaction(unsignedTx, null, listOf(wifiKey))
            rpc.sendRawTransaction(signedTx)
        } catch (e: Exception) {
            log("${ChainType.BITCOINCASH}转币失败，失败原因:${e.message}")
            throw e
        }
        saveWithReocord(
            sendPo.amount!!,
            txid,
            sendPo.to!!,
            sendAddr,
            WithType.SEND,
            ChainType.BITCOINCASH,
            ""
        )
        log("${ChainType.BITCOINCASH}转币成功 保存转币记录到数据库")
        return txid
    }

    private fun sendTRX(sendPo: SendPo): String {
        log("开始${ChainType.TRON}转币 转出地址:${sendPo.from} 转入地址:${sendPo.to} 数量:${sendPo.amount}")
        log("获取${ChainType.TRON} api")
        val trxApi = rpcClient.trxApi()
        log("获取${ChainType.TRON} 地址${sendPo.from}的私钥 ，私钥文件为${sendPo.fromWalletCode}")
        val privateKey = hsmRequest.exportWallet(sendPo.fromWalletCode!!, KeyType.PRIVATE)
        val amount = (sendPo.amount!! * BigDecimal("1000000")).toLong()
        val node = trxApi.easyTransferByPrivate(privateKey, sendPo.to!!, amount)
        val txid = node["transaction"]["txID"].textValue()
        saveWithReocord(
            sendPo.amount!!,
            txid,
            sendPo.to!!,
            sendPo.from!!,
            WithType.SEND,
            ChainType.TRON,
            ""
        )
        return txid
    }

    private fun sendTRC20(sendPo: SendPo): String {
        log("开始${ChainType.TRON}代币${sendPo.symbol}转币 转出地址:${sendPo.from} 转入地址:${sendPo.to} 数量:${sendPo.amount}")
        log("获取${ChainType.TRON} api")
        val api = rpcClient.trxApi()
        log("获取${ChainType.TRON} 地址${sendPo.from}的私钥 ，私钥文件为${sendPo.fromWalletCode}")
        val privateKey = hsmRequest.exportWallet(sendPo.fromWalletCode!!, KeyType.PRIVATE)
        val walletToken = Token()
        walletToken.tokenSymbol = sendPo.symbol
        walletToken.chainType = ChainType.TRON
        val token =
            walletTokenService.getByBean(walletToken).firstOrNull() ?: throw BizException(ErrorCode.NO_THIS_SYMBOL)
        val decimals = api.contractDecimals(token.tokenAddress)
        api.contractBalanceOf(token.tokenAddress, sendPo.from!!)

        val balance = (sendPo.amount!! * BigDecimal.TEN.pow(decimals)).toLong()
        val node = api.easyTransferContractByPrivate(
            privateKey,
            token.tokenAddress,
            sendPo.from!!,
            sendPo.to!!,
            balance,
            token.gasLimit
        )
        val txid = node["txid"].textValue()
        saveWithReocord(
            sendPo.amount!!,
            txid,
            sendPo.to!!,
            sendPo.from!!,
            WithType.SEND,
            ChainType.TRON,
            token.tokenSymbol
        )
        return txid
    }

    override fun exportWallet(exportWalletPo: ExportWalletPo): String {
        return hsmRequest.exportWallet(exportWalletPo.walletCode!!, exportWalletPo.type!!)
    }

    override fun importWallet(importWalletPo: ImportWalletPo): String {
        log("导入钱包到hsm 类型:${importWalletPo.type} key:${importWalletPo.key} 链类型:${importWalletPo.chain}")
        val addressVo = hsmRequest.importWallet(importWalletPo)
        if (importWalletPo.chain == ChainType.ETHEREUM) addressVo.address = "0x${addressVo.address}"
        val find = Address()
        find.address = addressVo.address
        val re = addressService.findByBean(find).firstOrNull()
        if (re == null) {
            val walletAddress = Address()
            walletAddress.walletCode = addressVo.walletCode
            walletAddress.address = addressVo.address
            walletAddress.chainType = importWalletPo.chain
            walletAddress.autoCollect = 0
            addressService.save(walletAddress)
            log("保存地址${walletAddress.address}参数到数据库")
        }
        return addressVo.address!!
    }

    private fun sendOMNI(sendPo: SendPo): String {
        log(
            "开始${ChainType.BITCOIN} 代币USDT 转币 转出地址:${sendPo.from} 转入地址:${sendPo.to} 数量:${sendPo.amount}"
        )
        log(
            "获得OMNI RPC"
        )
        val rpc = rpcClient.omniRpc()
        val walletToken = Token()
        walletToken.tokenSymbol = sendPo.symbol
        walletToken.chainType = ChainType.BITCOIN
        val token =
            walletTokenService.getByBean(walletToken).firstOrNull() ?: throw BizException(ErrorCode.NO_THIS_SYMBOL)

        val sendAddr = sendPo.from!!
        val balance = omniUtils.getOMNIBalance(sendAddr, token.tokenAddress)
        log("转出地址${sendAddr} USDT余额${balance}")
        if (balance < sendPo.amount) throw BizException(ErrorCode.INSUFFICIENT_BALANCE)
        var utxos = getUtxos(ChainType.BITCOIN, sendAddr)
        if (utxos.isEmpty()) throw BizException(ErrorCode.UTXO_NOT_FOUND)
        val satPerByte = sendPo.gas?.toLong() ?: rpcClient.getSatPerByte()
        val fee = omniUtils.calculateFee(utxos.size, 2, satPerByte)
        //usdt转btc为546+手续费
        val totalAmount = 546L + fee
        utxos = checkAmount(utxos, totalAmount)
        log("正在进行交易签名")
        val txSignResult = hsmRequest.signUsdtTransaction(
            utxos,
            sendPo.amount!!,
            fee.toBigDecimal().divide(BigDecimal.TEN.pow(8)),
            sendPo.to!!,
            sendPo.fromWalletCode!!
        )
        try {
            log("正在推送交易签名")
            rpc.sendRawTransaction(txSignResult.signedTx)
        } catch (e: Exception) {
            log("${ChainType.BITCOIN}转币失败 ${sendPo.symbol}，失败原因:${e.message}")
            throw e
        }

        saveWithReocord(
            sendPo.amount!!,
            txSignResult.txHash,
            sendPo.to!!,
            sendAddr,
            WithType.SEND,
            ChainType.BITCOIN,
            sendPo.symbol!!
        )
        log("${ChainType.ETHEREUM} 代币${sendPo.symbol} 转币成功 保存转币记录到数据库")
        return txSignResult.txHash
    }

    fun importAddress(address: String) {
        rpcClient.omniRpc().importAddress(address, "wallet", false)
    }

    fun importDashAddress(address: String) {
        rpcClient.dashRpc().importAddress(address, "wallet", false)
    }

    private fun sendERC20(sendPo: SendPo): String {
        log(
            "开始${ChainType.ETHEREUM} 代币${sendPo.symbol} 转币 转出地址:${sendPo.from} 转入地址:${sendPo.to} 数量:${sendPo.amount}"
        )
        log(
            "获得${ChainType.ETHEREUM} RPC"
        )
        val rpc = rpcClient.ethRpc()
        val walletToken = Token()
        walletToken.tokenSymbol = sendPo.symbol
        walletToken.chainType = ChainType.ETHEREUM
        val token =
            walletTokenService.getByBean(walletToken).firstOrNull() ?: throw BizException(ErrorCode.NO_THIS_SYMBOL)
        log(
            "获得${ChainType.ETHEREUM} 代币${sendPo.symbol}配置"
        )
        val sendAddr = sendPo.from!!
        val ethBalance = ethUtils.getBalance(sendAddr)
        val balance = ethUtils.getContractBalance(token.tokenAddress, sendAddr)
        log(
            "${ChainType.ETHEREUM} 代币${sendPo.symbol}转出地址${sendAddr} 余额${balance}"
        )
        val ethNonce = cacheService.getETHNonce(sendAddr)
        val decimals = ethUtils.getETHContractDecimals(token.tokenAddress)
        log("代币${sendPo.symbol}的精度为${decimals}")
        val data = ethUtils.createContractTransferData(sendPo.amount!!, sendPo.to!!, decimals)

        val gasPrice = sendPo.gas ?: rpcClient.getGasPrice()
        val gasLimit =
            sendPo.gasLimit ?: token.gasLimit
        if (ethBalance < BigDecimal(gasLimit * gasPrice).divide(BigDecimal.TEN.pow(9))) throw BizException(
            -1,
            "gas费用不足"
        )

        log("开始${ChainType.ETHEREUM} 代币${sendPo.symbol} 转币 gasPrice为${gasPrice} gasLimit为${gasLimit}")

        log("进行${ChainType.ETHEREUM} 代币${sendPo.symbol} 交易签名")
        val txSignResult = hsmRequest.signEthtransaction(
            ethNonce.nonce,
            BigDecimal.ZERO,
            BigDecimal(gasPrice),
            gasLimit,
            token.tokenAddress,
            sendPo.fromWalletCode!!,
            data
        )

        try {
            log("进行${ChainType.ETHEREUM} 代币${sendPo.symbol} 交易签名")
            rpc.handleError(rpc.ethSendRawTransaction("0x" + txSignResult.signedTx).send())
        } catch (e: Exception) {
            ethNonce.nonce = rpc.ethGetTransactionCount(sendAddr, DefaultBlockParameterName.PENDING).send()
                .transactionCount.toInt()
            log("${ChainType.ETHEREUM}转币失败，将转出地址${sendAddr}nonce修正为${ethNonce.nonce}")
            log("${ChainType.ETHEREUM}转币失败，失败原因:${e.message}")
            throw e
        }
        saveWithReocord(
            sendPo.amount!!,
            txSignResult.txHash,
            sendPo.to!!,
            sendAddr,
            WithType.SEND,
            ChainType.ETHEREUM,
            sendPo.symbol!!
        )
        log("${ChainType.ETHEREUM} 代币${sendPo.symbol} 转币成功 保存转币记录到数据库")
        ethNonce.nonce++
        return txSignResult.txHash
    }

    private fun sendETH(
        sendPo: SendPo
    ): String {
        log(
            "开始${ChainType.ETHEREUM}转币 转出地址:${sendPo.from} 转入地址:${sendPo.to} 数量:${sendPo.amount}"
        )
        log(
            "获得${ChainType.ETHEREUM} RPC"
        )
        val rpc = rpcClient.ethRpc()

        val sendAddr = sendPo.from!!

        val balance = ethUtils.getBalance(sendAddr)
        log("转出地址${sendAddr} 余额${balance}")
        val ethNonce = cacheService.getETHNonce(sendAddr)
        val gasPrice = sendPo.gas ?: rpcClient.getGasPrice()
        val gasLimit = sendPo.gasLimit ?: 21000L
        if (balance < BigDecimal(gasLimit * gasPrice).divide(BigDecimal.TEN.pow(9))) throw BizException(-1, "gas费用不足")
        log("开始${ChainType.ETHEREUM}转币 设置gasPrice为${gasPrice} gasLimit为${gasLimit}")
        log("开始${ChainType.ETHEREUM}转币 转出nonce为${ethNonce.nonce}")
        log("正在签名中")
        val txSignResult = hsmRequest.signEthtransaction(
            ethNonce.nonce,
            sendPo.amount!!,
            BigDecimal(gasPrice),
            gasLimit,
            sendPo.to!!,
            sendPo.fromWalletCode!!,
            sendPo.data
        )

        try {
            log("${ChainType.ETHEREUM} 推送已签名交易到节点")
            rpc.handleError(rpc.ethSendRawTransaction("0x" + txSignResult.signedTx).send())
        } catch (e: Exception) {
            ethNonce.nonce = rpc.ethGetTransactionCount(sendAddr, DefaultBlockParameterName.PENDING).send()
                .transactionCount.toInt()
            log("${ChainType.ETHEREUM}转币失败，将转出地址${sendAddr}nonce修正为${ethNonce.nonce}")
            log("${ChainType.ETHEREUM}转币失败，失败原因:${e.message}")
            throw e
        }

        saveWithReocord(
            sendPo.amount!!,
            txSignResult.txHash,
            sendPo.to!!,
            sendAddr,
            WithType.SEND,
            ChainType.ETHEREUM,
            ""
        )
        log("${ChainType.ETHEREUM}转币成功 保存转币记录到数据库")
        ethNonce.nonce++
        return txSignResult.txHash
    }

    private fun sendBTCOrFork(
        sendPo: SendPo,
        chainType: String,
        rpc: BitcoinJSONRPCClient
    ): String {
        log(
            "开始${chainType}转币 转出地址:${sendPo.from} 转入地址:${sendPo.to} 数量:${sendPo.amount}"
        )
        log(
            "获得${chainType} RPC"
        )
        val sendAddr = sendPo.from!!
        log("开始${chainType}转币 获取转出地址${sendAddr}的utxo")
        var utxos = getUtxos(chainType, sendAddr)
        var balance = BigDecimal.ZERO
        utxos.forEach {
            balance += it.amount.toBigDecimal().divide(BigDecimal.TEN.pow(8))
        }
        log("${chainType} 转出地址${sendAddr}的余额为${balance}")
        if (utxos.isEmpty()) throw BizException(ErrorCode.UTXO_NOT_FOUND)
        val satPerByte = sendPo.gas?.toLong() ?: rpcClient.getSatPerByte()
        log("开始${chainType}转币 设置satPerByte${satPerByte}")
        val fee = omniUtils.calculateFee(utxos.size, 2, satPerByte)
        val totalAmount = sendPo.amount!!.multiply(BigDecimal.TEN.pow(8)).toLong() + fee
        utxos = checkAmount(utxos, totalAmount)
        log("正在签名中")
        val txSignResult = hsmRequest.signBtcTransaction(
            sendPo.amount!!,
            fee.toBigDecimal().divide(BigDecimal.TEN.pow(8)),
            sendPo.to!!,
            utxos,
            sendPo.fromWalletCode!!
        )
        try {
            log("${chainType} 推送已签名交易到节点")
            rpc.sendRawTransaction(txSignResult.signedTx)
        } catch (e: Exception) {
            log("${chainType}转币失败，失败原因:${e.message}")
            throw e
        }
        saveWithReocord(
            sendPo.amount!!,
            txSignResult.txHash,
            sendPo.to!!,
            sendAddr,
            WithType.SEND,
            chainType,
            ""
        )
        log("${chainType}转币成功 保存转币记录到数据库")
        return txSignResult.txHash
    }


    override fun getUtxos(chainType: String, address: String): ArrayList<BitcoinTransaction.UTXO> {
        val symbol: String
        val rpc = when (chainType) {
            ChainType.BITCOIN -> {
                symbol = "btc"
                rpcClient.omniRpc()
            }
            ChainType.LITECOIN -> {
                symbol = "ltc"
                rpcClient.ltcRpc()
            }
            ChainType.BITCOINCASH -> {
                symbol = "bch"
                rpcClient.bchRpc()
            }
            ChainType.BITCOINSV -> {
                symbol = "bsv"
                rpcClient.bsvRpc()
            }
            ChainType.DASH -> {
                symbol = "dash"
                rpcClient.dashRpc()
            }
            ChainType.DOGECOIN -> {
                symbol = "doge"
                rpcClient.dogeRpc()
            }
            else -> throw BizException(ErrorCode.NO_THIS_CHAIN_TYPE)
        }
        val utxos = ArrayList<BitcoinTransaction.UTXO>()
        rpc.listUnspent(0, Int.MAX_VALUE, address).forEach {
            if (it.confirmations() > -1) {
                val utxo = BitcoinTransaction.UTXO(
                    it.txid(),
                    it.vout(),
                    it.amount().multiply(BigDecimal.TEN.pow(8)).toLong(),
                    it.address(),
                    it.scriptPubKey(),
                    "0/0"
                )
                utxos.add(utxo)
            }
        }
        if (utxos.isEmpty() && chainType != ChainType.BITCOINSV && chainType != ChainType.BITCOINCASH) {
            utxos.addAll(getUtxosFromBlockCypherApi(symbol, address))
        }
        return utxos
    }


    private fun getUtxosFromTokenViewApi(symbol: String, address: String): ArrayList<BitcoinTransaction.UTXO> {
        val utxos = ArrayList<BitcoinTransaction.UTXO>()
        val url = "http://www.tokenview.com:8088/unspent/${symbol}/${address}/1/${Int.MAX_VALUE}"
        val node = restTemplate.getForObject(url, JsonNode::class.java)
        node["data"]?.forEach {
            val utxo = BitcoinTransaction.UTXO(
                it["txid"].textValue(),
                it["index"].intValue(),
                (it["value"].decimalValue() * BigDecimal.TEN.pow(8)).toLong(),
                address,
                it[""].textValue(),
                "0/0"
            )
            utxos.add(utxo)
        }
        return utxos
    }

    private fun getUtxosFromBlockCypherApi(symbol: String, address: String): ArrayList<BitcoinTransaction.UTXO> {
        val utxos = ArrayList<BitcoinTransaction.UTXO>()
        val url = "https://api.blockcypher.com/v1/$symbol/main/addrs/$address?unspentOnly=true&includeScript=true"
        val re = restTemplate.getForObject(url, String::class.java)
        val apiDomain = obj.readValue(re, ApiDomain::class.java)
        apiDomain.txrefs?.forEach {
            val utxo = BitcoinTransaction.UTXO(
                it.tx_hash,
                it.tx_output_n!!,
                it.value!!,
                address,
                it.script,
                "0/0"
            )
            utxos.add(utxo)
        }
        apiDomain.unconfirmed_txrefs?.forEach {
            val utxo = BitcoinTransaction.UTXO(
                it.tx_hash,
                it.tx_output_n!!,
                it.value!!,
                address,
                it.script,
                "0/0"
            )
            utxos.add(utxo)
        }
        return utxos
    }

    fun checkAmount(utxos: List<BitcoinTransaction.UTXO>, totalAmount: Long): ArrayList<BitcoinTransaction.UTXO> {
        var actualAmount = 0L
        val list = ArrayList<BitcoinTransaction.UTXO>()
        utxos.forEach {
            actualAmount += it.amount
            list.add(it)
            if (actualAmount >= totalAmount)
                return list
        }
        throw BizException(ErrorCode.INSUFFICIENT_BALANCE)
    }

    override fun ethContractTransferData(amount: BigDecimal, toAddress: String, decimals: Int): String {
        val function = Function(
            "transfer",
            listOf(
                Address(toAddress),
                Uint256(amount.multiply(BigDecimal.TEN.pow(decimals)).toBigInteger())
            ),
            listOf<TypeReference<*>>(object : TypeReference<Bool>() {

            })
        )
        return FunctionEncoder.encode(function)
    }

    override fun ethConractBalanceData(address: String): String {
        val function = Function(
            "balanceOf",
            listOf(Address(address)),
            listOf<TypeReference<*>>(object : TypeReference<Bool>() {

            })
        )
        return FunctionEncoder.encode(function)
    }

    override fun rescanTransaction(addTransactionPo: RescanTransactionPo) {
        depositService.getByHash(addTransactionPo.hash!!)?.let {
            throw BizException(ErrorCode.DOUBLE_ENTRY_HASH)
        }
        when (addTransactionPo.chainType) {
            ChainType.ETHEREUM -> {
                val ethRpc = rpcClient.ethRpc()
                val map = addressService.findByType(ChainType.ETHEREUM).associateBy { it.address }
                val walletToken = Token()
                walletToken.chainType = ChainType.ETHEREUM
                val contractMap = walletTokenService.getByBean(walletToken).associateBy { it.tokenAddress }
                val transaction = ethRpc.ethGetTransactionByHash(addTransactionPo.hash!!).send().result
                //-100 :manual
                synEthByTransaction(transaction, map, contractMap, -100)
            }
            else -> throw BizException(ErrorCode.NOT_SUPPORT)
        }
    }

    override fun createHotAddress(createSendAddressPo: CreateHotAddressPo): AddressAdmin {
        when (createSendAddressPo.type) {
            100, 300 -> {
            }
            else -> throw BizException(ErrorCode.NO_THIS_TYPE)
        }
        log("获得地址${createSendAddressPo.chain} 数量 1")
        val addr = getAddress(createSendAddressPo.chain!!, 1).firstOrNull()
            ?: throw BizException(ErrorCode.GET_ADDRESS_FAILURE_FROM_HSM)
        val findAddr = Address()
        findAddr.address = addr
        findAddr.chainType = createSendAddressPo.chain
        val walletAddr = addressService.findByBean(findAddr).first()
        log("删除数据库wallet_address地址 地址${addr}")
        addressService.delete(walletAddr.id)
        val walletAddressAdmin = AddressAdmin()
        walletAddressAdmin.address = addr
        walletAddressAdmin.addressType = createSendAddressPo.type
        walletAddressAdmin.chainType = createSendAddressPo.chain
        walletAddressAdmin.walletCode = walletAddr.walletCode
        log("保存到数据库wallet_address_admin 地址${addr}")
        return walletAddressAdminService.save(walletAddressAdmin)
    }

    override fun synEthByTransaction(
        it: org.web3j.protocol.core.methods.response.Transaction,
        map: Map<String, com.wallet.entity.domain.Address>,
        contractMap: Map<String, Token>,
        diffHeight: Long
    ) {

        val addr = it.to ?: return
        val txidIsSendFee = checkTxidIsSendFee(it.hash)
        if (txidIsSendFee) {
            log("${ChainType.ETHEREUM} 交易hash${it.hash}是本钱包发送手续费交易，跳过入账操作")
            return
        }
        val addressInDb = checkAddressInDb(map, addr)
        if (addressInDb) {
            if (!checkTxStatus(it.hash, ChainType.ETHEREUM)) return
            log(" ${ChainType.ETHEREUM} 接收地址${addr}是钱包数据库地址")
            depositService.getByHash(it.hash)?.let { tx ->
                log("${ChainType.ETHEREUM} 交易hash${it.hash}已经入账数据库，只更新确认数，跳过入账操作")
                tx.confirmations = diffHeight
                depositService.save(tx)
                return
            }
            val transaction = Deposit()
            transaction.hash = it.hash
            transaction.address = addr
            transaction.amount = BigDecimal(it.value).divide(BigDecimal.TEN.pow(18))
            transaction.isUpload = 0
            transaction.chainType = ChainType.ETHEREUM
            transaction.confirmTime = Date()
            transaction.confirmations = diffHeight
            depositService.save(transaction)
            log(
                "${ChainType.ETHEREUM} 交易hash${it.hash}入账数据库， 地址${transaction.address} 收到数量${transaction.amount}"
            )
            map[addr]?.let {
                if (it.autoCollect == 1) {
                    log("${ChainType.ETHEREUM} 地址${it.address}配置自动归集，将入归集队列")
                    val walletWaitCollect = WaitCollect()
                    walletWaitCollect.address = transaction.address
                    walletWaitCollect.chainType = transaction.chainType
                    walletWaitCollect.sendFee = 0
                    walletWaitCollectService.save(walletWaitCollect)
                } else {
                    log("${ChainType.ETHEREUM} 地址${it.address}未配置自动归集，将不会入归集队列")
                }
            }
        }
        val addressInContract = checkAddressInContract(contractMap, addr)
        if (addressInContract) {
            val contract = contractMap.getValue(addr)
            val input = decodeTransferInput(it.input)
            // method id  transfer
            if (input.pass && input.methodID == "0xa9059cbb") {
                input.to ?: return
                input.value ?: return
                val addressInDbContract = checkAddressInDb(map, input.to)
                if (addressInDbContract) {
                    log("${ChainType.ETHEREUM} 交易hash${it.hash}为合约交易，判断接收地址${input.to}为本数据库地址")
                    depositService.getByHash(it.hash)?.let { tx ->
                        log("${ChainType.ETHEREUM} 交易hash${it.hash}已经入账数据库，只更新确认数，跳过入账操作")
                        tx.confirmations = diffHeight
                        depositService.save(tx)
                        return
                    }
                    val decimals = ethUtils.getETHContractDecimals(contract.tokenAddress)
                    log("${ChainType.ETHEREUM} 合约${contract.tokenAddress}精度为${decimals}")
                    val amount = BigDecimal(input.value!!).divide(BigDecimal.TEN.pow(decimals))
                    val transaction = Deposit()
                    transaction.chainType = ChainType.ETHEREUM
                    transaction.address = input.to!!
                    transaction.amount = amount
                    transaction.confirmTime = Date()
                    transaction.tokenSymbol = contract.tokenSymbol
                    transaction.isUpload = 0
                    transaction.hash = it.hash
                    transaction.confirmations = diffHeight
                    depositService.save(transaction)
                    log(
                        "${ChainType.ETHEREUM} 交易hash${it.hash}入账数据库，代币${transaction.tokenSymbol} 地址${transaction.address} 收到数量${transaction.amount}"
                    )
                    map[input.to!!]?.let {
                        if (it.autoCollect == 1) {
                            log("${ChainType.ETHEREUM} 地址${it.address}配置自动归集，将入归集队列")
                            val walletWaitCollect = WaitCollect()
                            walletWaitCollect.address = transaction.address
                            walletWaitCollect.chainType = transaction.chainType
                            walletWaitCollect.tokenSymbol = transaction.tokenSymbol
                            walletWaitCollect.sendFee = 0
                            walletWaitCollectService.save(walletWaitCollect)
                        } else {
                            log("${ChainType.ETHEREUM} 地址${it.address}未配置自动归集，将不会入归集队列")
                        }
                    }
                }
            }
        }
    }

    override fun rescanBlock(rescanBlockPo: RescanBlockPo) {
        when (rescanBlockPo.chainType) {
            ChainType.BITCOIN -> {

            }
            ChainType.ETHEREUM -> {
            }
            else -> throw BizException(ErrorCode.NO_THIS_CHAIN_TYPE)
        }
    }

    override fun checkAddressInDb(map: Map<String, com.wallet.entity.domain.Address>, address: String?): Boolean {
        if (map[address] is com.wallet.entity.domain.Address) return true
        return false
    }

    override fun checkAddressInContract(map: Map<String, Token>, address: String?): Boolean {
        if (map[address] is Token) return true
        return false
    }

    override fun putAllAddressToWaitImport() {
        val addressList = addressService.findAllBitcoinFork().map {
            val walletWaitImport = WaitImport()
            walletWaitImport.address = it.address
            walletWaitImport.chainType = it.chainType
            walletWaitImport
        }
        val addressAdminList = walletAddressAdminService.findAllBitcoinFork().map {
            val walletWaitImport = WaitImport()
            walletWaitImport.address = it.address
            walletWaitImport.chainType = it.chainType
            walletWaitImport
        }
        val list = addressList.plus(addressAdminList)
        walletWaitImportService.saveAll(list)
    }

    override fun removeUselessWallet() {
        val addressMap = addressService.findAll().filter { it.walletCode != null }.associate { it.walletCode to "1" }
        val adminMap =
            walletAddressAdminService.findAll().filter { it.walletCode != null }.associate { it.walletCode to "1" }
        val map = addressMap.plus(adminMap)
        hsmRequest.removeUselessWallet(map)
    }

    override fun checkTxidIsSendFee(txid: String): Boolean {
        val findWith = Withdraw()
        findWith.hash = txid
        findWith.withdrawType = WithType.SEND_FEE
        val list = walletAddressWithdrawService.findByWithdraw(findWith)
        if (list.isNotEmpty()) return true
        return false
    }

    override fun checkTxStatus(hash: String, chain: String): Boolean {
        return when (chain) {
            ChainType.ETHEREUM -> {
                val rpc = rpcClient.ethRpc()
                val res = rpc.handleError(rpc.ethGetTransactionReceipt(hash).send())
                val receipt = res.transactionReceipt.get()
                receipt.isStatusOK
            }
            else -> true
        }
    }

    private fun decodeTransferInput(inputData: String): DecodedTransferInput {
        val i = DecodedTransferInput()
        try {
            i.methodID = inputData.substring(0, 10)
            i.to = hexToAddress(inputData.substring(10, 74))
            i.value = hexToBigInteger(inputData.substring(74))
            i.pass = true
        } catch (e: Exception) {
            i.pass = false
        }
        return i
    }


    /**
     * <p>功能描述：16进制转10进制整数。</p>
     * <p>jl</p>
     * @param strHex
     * @since JDK1.8
     * <p>创建日期：2018/10/19 15:55。</p>
     * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
     */
    private fun hexToBigInteger(hex: String): BigInteger? {
        var strHex = hex
        if (strHex.length > 2) {
            if (strHex[0] == '0' && (strHex[1] == 'X' || strHex[1] == 'x')) {
                strHex = strHex.substring(2);
            }
            val bigInteger = BigInteger(strHex, 16);
            return bigInteger;
        }
        return null;
    }

    /**
     * <p>功能描述：hex地址转地址。</p>
     * <p>jl</p>
     * @param strHex
     * @since JDK1.8
     * <p>创建日期：2018/10/19 16:24。</p>
     * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
     */
    private fun hexToAddress(hex: String): String? {
        var strHex = hex
        if (strHex.length > 42) {
            if (strHex[0] == '0' && (strHex[1] == 'X' || strHex[1] == 'x')) {
                strHex = strHex.substring(2);
            }
            strHex = strHex.substring(24);
            return "0x$strHex";
        }
        return null;
    }


    val obj = ObjectMapper()

    @Autowired
    lateinit var restTemplate: RestTemplate

    @Autowired
    lateinit var hsmRequest: HsmRequest

    @Autowired
    lateinit var addressService: AddressService

    @Autowired
    lateinit var rpcClient: RpcClient

    @Autowired
    lateinit var walletTokenService: TokenService

    @Autowired
    lateinit var walletAddressWithdrawService: WithdrawService

    @Autowired
    lateinit var walletAddressAdminService: AddressAdminService

    @Autowired
    lateinit var depositService: DepositService

    @Autowired
    lateinit var walletWaitCollectService: WaitCollectService

    @Autowired
    lateinit var ethUtils: ETHUtils

    @Autowired
    lateinit var omniUtils: OMNIUtils

    @Autowired
    lateinit var cacheService: CacheService

    @Autowired
    lateinit var walletWaitImportService: WaitImportService

    @Autowired
    lateinit var configService: ConfigService

}

