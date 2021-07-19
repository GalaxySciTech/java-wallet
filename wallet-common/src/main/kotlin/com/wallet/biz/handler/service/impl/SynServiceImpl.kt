package com.wallet.biz.handler.service.impl


import com.wallet.biz.dict.SysConfigKey
import com.wallet.biz.domain.PageEntity
import com.wallet.biz.domain.dict.ErrorCode
import com.wallet.biz.domain.exception.BizException
import com.wallet.biz.rpc.RpcClient
import com.wallet.biz.service.*
import com.wallet.biz.xservice.WalletXService
import com.wallet.entity.domain.*
import com.wallet.biz.handler.service.SynService
import com.wallet.biz.mq.PushComponent
import com.fasterxml.jackson.databind.ObjectMapper
import com.wallet.biz.cache.CacheService
import org.consenlabs.tokencore.wallet.model.ChainType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.web3j.protocol.core.DefaultBlockParameterNumber
import org.web3j.protocol.core.methods.response.EthBlock
import wf.bitcoin.javabitcoindrpcclient.BitcoinJSONRPCClient
import java.math.BigDecimal
import java.util.*
import java.util.concurrent.Executors
import javax.annotation.PostConstruct

/** 
 * Created by pie on 2019-04-12 14: 03. 
 */
@Service
class SynServiceImpl : SynService {

    override fun synOMNI() {


        val omniRpc = rpcClient.omniRpc()

        val find = PageEntity(BlockHeight())
        find.entity.chainType = "OMNI"
        val walletHeight = walletBlockHeightService.findByEntity(find).firstOrNull()
            ?: throw  BizException(ErrorCode.DB_WALLET_HEIGHT_MUST_HAVE)
        val dbHeight = walletHeight.height

        val nodeHeight = omniRpc.blockCount.toLong()

        val scanBackNum = cacheService.getSysConfig(SysConfigKey.BTC_SCAN_BACK).toLongOrNull() ?: 6L

        val startScanHeight: Long
        startScanHeight = if (nodeHeight - dbHeight < scanBackNum)
            nodeHeight - scanBackNum
        else dbHeight

        if (startScanHeight < nodeHeight)
            for (scanHeight in (startScanHeight + 1)..nodeHeight) {


                val map = walletAddressService.findByType(ChainType.BITCOIN)
                    .map { it.address to it }.toMap()
                walletHeight.height = scanHeight
                if (map.isEmpty()) {

                    walletBlockHeightService.save(walletHeight)
                    continue
                }
                val walletToken = Token()
                walletToken.chainType = ChainType.BITCOIN
                val contractMap = walletTokenService.getByBean(walletToken).associateBy { it.tokenAddress }


                omniRpc.omniListBlockTransactions(scanHeight).forEach {

                    val tx = omniRpc.omniGetTransaction(it)

                    if (tx.valid != "true") return@forEach
                    val addressInContract =
                        walletXService.checkAddressInContract(contractMap, tx.propertyid.toString())
                    if (addressInContract) {
                        val contract = contractMap.getValue(tx.propertyid.toString())
                        val addressInDb = walletXService.checkAddressInDb(map, tx.referenceaddress)
                        if (addressInDb) {

                            walletAddressTransactionService.getByHash(tx.txid!!)?.let { tx ->

                                tx.confirmations = tx.confirmations!!.toLong()
                                walletAddressTransactionService.save(tx)
                                return@forEach
                            }
                            val transaction = Deposit()
                            transaction.address = tx.referenceaddress
                            transaction.chainType = contract.chainType
                            transaction.tokenSymbol = contract.tokenSymbol
                            transaction.hash = tx.txid
                            transaction.amount = tx.amount
                            transaction.isUpload = 0
                            transaction.confirmTime = Date()
                            transaction.confirmations = tx.confirmations!!.toLong()
                            walletAddressTransactionService.save(transaction)

                            map[tx.referenceaddress]?.let {
                                if (it.autoCollect == 1) {

                                    val walletWaitCollect = WaitCollect()
                                    walletWaitCollect.address = transaction.address
                                    walletWaitCollect.chainType = transaction.chainType
                                    walletWaitCollect.tokenSymbol = transaction.tokenSymbol
                                    walletWaitCollect.sendFee = 0
                                    walletWaitCollectService.save(walletWaitCollect)
                                } else {
                                }
                            }
                        }
                    }
                }
                walletBlockHeightService.save(walletHeight)

            }

    }

    override fun synETH() {


        val ethRpc = rpcClient.ethRpc()
        val find = PageEntity(BlockHeight())
        find.entity.chainType = ChainType.ETHEREUM
        val walletHeight = walletBlockHeightService.findByEntity(find).firstOrNull()
            ?: throw  BizException(ErrorCode.DB_WALLET_HEIGHT_MUST_HAVE)
        val dbHeight = walletHeight.height

        val nodeHeight = ethRpc.ethBlockNumber().send().blockNumber.toLong()

        val scanBackNum = cacheService.getSysConfig(SysConfigKey.ETH_SCAN_BACK).toLongOrNull() ?: 12L

        val startScanHeight: Long
        if (dbHeight == nodeHeight) return
        startScanHeight = if (nodeHeight - dbHeight < scanBackNum)
            nodeHeight - scanBackNum
        else dbHeight
        if (startScanHeight < nodeHeight)
            for (scanHeight in (startScanHeight + 1)..nodeHeight) {

                val confirmations = nodeHeight - scanHeight

                val map = walletAddressService.findByType(ChainType.ETHEREUM).associateBy { it.address }
                val walletToken = Token()
                walletToken.chainType = ChainType.ETHEREUM

                val contractMap = walletTokenService.getByBean(walletToken).associateBy { it.tokenAddress }
                walletHeight.height = scanHeight
                if (map.isEmpty()) {

                    walletBlockHeightService.save(walletHeight)
                    return
                }


                val ethBlock = ethRpc.ethGetBlockByNumber(
                    DefaultBlockParameterNumber(scanHeight), true
                ).send()

                ethBlock.block.transactions.forEach {
                    if (it is EthBlock.TransactionObject) {
                        walletXService.synEthByTransaction(it, map, contractMap, confirmations)

                    }
                }

                walletBlockHeightService.save(walletHeight)
            }

    }

    override fun synBTCOrFork(rpc: BitcoinJSONRPCClient, chainType: String) {


        val find = PageEntity(BlockHeight())
        find.entity.chainType = chainType
        val walletHeight = walletBlockHeightService.findByEntity(find).firstOrNull()
            ?: throw  BizException(ErrorCode.DB_WALLET_HEIGHT_MUST_HAVE)
        val dbHeight = walletHeight.height

        val nodeHeight = rpc.blockCount.toLong()

        val scanBackNum = cacheService.getSysConfig(SysConfigKey.BTC_SCAN_BACK).toLongOrNull() ?: 6L

        val startScanHeight: Long
        startScanHeight = if (nodeHeight - dbHeight < scanBackNum)
            nodeHeight - scanBackNum
        else dbHeight
        if (startScanHeight < nodeHeight)
            for (scanHeight in (startScanHeight + 1)..nodeHeight) {


                val map = walletAddressService.findByType(chainType)
                    .map { it.address to it }.toMap()
                walletHeight.height = scanHeight
                if (map.isEmpty()) {

                    walletBlockHeightService.save(walletHeight)
                    continue
                }

                val block = rpc.getBlock(scanHeight.toInt())
                block.tx()!!.forEach {

                    val tx = rpc.getRawTransaction(it)
                    if (tx.vOut() == null) {
//                        
                        return@forEach
                    }
                    val txidIsSendFee = walletXService.checkTxidIsSendFee(it)
                    if (txidIsSendFee) {
                        //是本钱包发送手续费交易，跳过入账操作
                        return@forEach
                    }
                    val amountMap = HashMap<String, BigDecimal>()
                    tx.vOut()!!.forEach loop@{ vout ->
                        if (vout.scriptPubKey() == null) return@loop
                        if (vout.scriptPubKey().addresses() == null) return@loop
                        val addr = vout.scriptPubKey().addresses().first()

                        val addressInDb = walletXService.checkAddressInDb(map, addr)
                        if (addressInDb) {

                            walletAddressTransactionService.getByHash(it)?.let { tx ->

                                tx.confirmations = block.confirmations().toLong()
                                walletAddressTransactionService.save(tx)
                                return@forEach
                            }
                            amountMap[addr] = (amountMap[addr] ?: BigDecimal.ZERO) + vout.value()

                        }
                    }

                    amountMap.forEach amountFor@{ (addr, value) ->
                        if (value.compareTo(BigDecimal("0.00000546")) == 0) {

                            return@amountFor
                        }
                        val transaction = Deposit()
                        transaction.amount = value
                        transaction.isUpload = 0
                        transaction.hash = it
                        transaction.address = addr
                        transaction.chainType = chainType
                        transaction.confirmations = block.confirmations().toLong()
                        transaction.confirmTime = Date()
                        walletAddressTransactionService.save(transaction)

                        map[addr]?.let {
                            if (it.autoCollect == 1) {
                                val walletWaitCollect = WaitCollect()
                                walletWaitCollect.address = transaction.address
                                walletWaitCollect.chainType = transaction.chainType
                                walletWaitCollect.sendFee = 0
                                walletWaitCollectService.save(walletWaitCollect)
                            } else {

                            }
                        }
                    }
                }
                walletBlockHeightService.save(walletHeight)
            }
    }

    override fun synEOS() {
//        
//        
//        val rpc = rpcClient.eosRpc()
//        
//        val waitCollectAddr = cacheService.getAdminAddress(WalletAddressAdminKey.EOS_COLLECT).random().address
//        
//        val res = rpc.getActions(waitCollectAddr, 0, 100)
//        res.actions.forEach {
//
//        }
    }

    override fun synTRX() {
        val api = rpcClient.trxApi()
        val find = PageEntity(BlockHeight())
        find.entity.chainType = ChainType.TRON
        val walletHeight = walletBlockHeightService.findByEntity(find).firstOrNull()
            ?: throw  BizException(ErrorCode.DB_WALLET_HEIGHT_MUST_HAVE)
        val dbHeight = walletHeight.height

        val nodeHeight = api.getNowBlock()["block_header"]["raw_data"]["number"].longValue()

        val scanBackNum = cacheService.getSysConfig(SysConfigKey.TRX_SCAN_BACK).toLongOrNull() ?: 6L

        val startScanHeight = if (nodeHeight - dbHeight < scanBackNum)
            nodeHeight - scanBackNum
        else dbHeight
        if (startScanHeight < nodeHeight)
            for (scanHeight in (startScanHeight + 1)..nodeHeight) {
                val diffHeight = nodeHeight - scanHeight
                walletHeight.height = scanHeight
                val map = walletAddressService.findByType(ChainType.TRON)
                    .map { it.address to it }.toMap()

                val walletToken = Token()
                walletToken.chainType = ChainType.TRON
                val contractMap = walletTokenService.getByBean(walletToken).associateBy { it.tokenAddress }
                val block = api.getBlockByNum(scanHeight)
                val transactions = block["transactions"]
                transactions?.let {
                    it.forEach forTransaction@{
                        val txid = it["txID"].textValue()
                        val contractRet = it["ret"]?.get(0)?.get("contractRet")?.asText() ?: "FAIL"
                        if (contractRet != "SUCCESS") return@forTransaction
                        val txidIsSendFee = walletXService.checkTxidIsSendFee(txid)
                        if (txidIsSendFee) {
                            //是本钱包发送手续费交易，跳过入账操作
                            return@forTransaction
                        }

                        //使用节点源数据解析
                        it["raw_data"]["contract"].forEach forContract@{
                            when (it["type"].textValue()) {
                                "TransferContract" -> {
                                    val amount = it["parameter"]["value"]["amount"].longValue()
//                                val from = it["parameter"]["owner_address"].textValue()
                                    val to = it["parameter"]["value"]["to_address"].textValue()
                                    val addressInDb = walletXService.checkAddressInDb(map, to)
                                    if (addressInDb) {

                                        walletAddressTransactionService.getByHash(txid)?.let { tx ->

                                            tx.confirmations = diffHeight
                                            walletAddressTransactionService.save(tx)
                                            return@forContract
                                        }
                                        val transaction = Deposit()
                                        transaction.amount =
                                            BigDecimal(amount).divide(BigDecimal("1000000"))
                                        transaction.isUpload = 0
                                        transaction.hash = txid
                                        transaction.address = to
                                        transaction.chainType = ChainType.TRON
                                        transaction.confirmations = diffHeight
                                        transaction.confirmTime = Date()
                                        walletAddressTransactionService.save(transaction)
                                    }
                                    map[to]?.let {
                                        if (it.autoCollect == 1) {

                                            val walletWaitCollect = WaitCollect()
                                            walletWaitCollect.address = to
                                            walletWaitCollect.chainType = ChainType.TRON
                                            walletWaitCollect.sendFee = 0
                                            walletWaitCollectService.save(walletWaitCollect)
                                        } else {

                                        }
                                    }
                                }
                                "TransferAssetContract" -> {
                                    //not to do
                                }
                                "TriggerSmartContract" -> {
                                    val contractAddress = it["parameter"]["value"]["contract_address"].textValue()
                                    val contractInDb =
                                        walletXService.checkAddressInContract(contractMap, contractAddress)
                                    if (contractInDb) {
                                        val data = it["parameter"]["value"]["data"].textValue()
                                        val contract = contractMap.getValue(contractAddress)
                                        val input = api.decodeTransferInput(data)
                                        if (input.pass && input.methodID == "a9059cbb") {
                                            val addressInDb = walletXService.checkAddressInDb(map, input.to)
                                            if (addressInDb) {

                                                walletAddressTransactionService.getByHash(txid)?.let { tx ->

                                                    tx.confirmations = diffHeight
                                                    walletAddressTransactionService.save(tx)
                                                    return@forContract
                                                }
                                                val decimals = api.contractDecimals(contractAddress)
                                                val amount =
                                                    input.value!!.toBigDecimal().divide(BigDecimal.TEN.pow(decimals))
                                                val transaction = Deposit()
                                                transaction.chainType = ChainType.TRON
                                                transaction.address = input.to!!
                                                transaction.amount = amount
                                                transaction.confirmTime = Date()
                                                transaction.tokenSymbol = contract.tokenSymbol
                                                transaction.isUpload = 0
                                                transaction.hash = txid
                                                transaction.confirmations = diffHeight
                                                walletAddressTransactionService.save(transaction)


                                                map[input.to!!]?.let {
                                                    if (it.autoCollect == 1) {

                                                        val walletWaitCollect = WaitCollect()
                                                        walletWaitCollect.address = transaction.address
                                                        walletWaitCollect.chainType = transaction.chainType
                                                        walletWaitCollect.tokenSymbol = transaction.tokenSymbol
                                                        walletWaitCollect.sendFee = 0
                                                        walletWaitCollectService.save(walletWaitCollect)
                                                    } else {

                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                walletBlockHeightService.save(walletHeight)
            }
    }

    override fun synImportAddress() {
        val executor = Executors.newFixedThreadPool(10)
        val list = walletWaitImportService.findAll()
        list.forEach {
            executor.execute {
                try {
                    when (it.chainType) {
                        ChainType.BITCOIN -> importAddress(rpcClient.omniRpc(), it.address, false)
                        ChainType.LITECOIN -> importAddress(rpcClient.ltcRpc(), it.address, false)
                        ChainType.BITCOINSV -> importAddress(rpcClient.bsvRpc(), it.address, false)
                        ChainType.DOGECOIN -> importAddress(rpcClient.dogeRpc(), it.address, false)
                        ChainType.BITCOINCASH -> importAddress(rpcClient.bchRpc(), it.address, false)
                        ChainType.DASH -> importAddress(rpcClient.dashRpc(), it.address, false)
                    }
                    walletWaitImportService.deleteById(it.id)
                } catch (e: Exception) {

                }
            }
        }
    }

    fun importAddress(rpc: BitcoinJSONRPCClient, address: String, rescan: Boolean) {
        rpc.importAddress(address, "wallet", rescan)
    }

    override fun synDeposit() {
        val list = walletXService.getDepositTransaction()
        if(list.isNotEmpty()){
            val json = obj.writeValueAsString(list)

            val modes = cacheService.getSysConfig(SysConfigKey.DEPOSIT_NOTIFY_MODE).split(",").toSet()
            modes.forEach {
                when (it) {
                    "post" -> pushComponent.sendMsgToService(json)
                    "rabbitmq" -> pushComponent.sendMsgToMq(json)
                    else -> throw BizException(ErrorCode.NOT_SUPPORT_MODE)
                }
            }
            walletXService.saveUploadedDepositTransaction(list)
        }
    }

    @Autowired
    lateinit var rpcClient: RpcClient

    @Autowired
    lateinit var pushComponent: PushComponent

    @Autowired
    lateinit var walletBlockHeightService: BlockHeightService

    @Autowired
    lateinit var walletAddressService: AddressService

    @Autowired
    lateinit var walletAddressTransactionService: DepositService

    @Autowired
    lateinit var walletWaitCollectService: WaitCollectService

    @Autowired
    lateinit var walletXService: WalletXService

    @Autowired
    lateinit var walletTokenService: TokenService

    @Autowired
    lateinit var cacheService: CacheService

    @Autowired
    lateinit var walletWaitImportService: WaitImportService
    private val obj = ObjectMapper()
}
