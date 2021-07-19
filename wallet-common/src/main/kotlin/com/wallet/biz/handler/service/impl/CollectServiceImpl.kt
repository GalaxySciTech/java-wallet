package com.wallet.biz.handler.service.impl

import com.wallet.biz.cache.CacheService
import com.wallet.biz.dict.TokenSymbolKey
import com.wallet.biz.dict.AddressAdminKey
import com.wallet.biz.dict.WithType
import com.wallet.biz.domain.dict.ErrorCode
import com.wallet.biz.domain.dict.KeyType
import com.wallet.biz.domain.exception.BizException
import com.wallet.biz.handler.service.CollectService
import com.wallet.biz.request.HsmRequest
import com.wallet.biz.rpc.RpcClient
import com.wallet.biz.service.AddressAdminService
import com.wallet.biz.service.AddressService
import com.wallet.biz.service.TokenService
import com.wallet.biz.service.WaitCollectService
import com.wallet.biz.utils.ETHUtils
import com.wallet.biz.utils.OMNIUtils
import com.wallet.biz.utils.TRONUtils
import com.wallet.biz.xservice.WalletXService
import com.wallet.entity.domain.Address
import com.wallet.entity.domain.Token
import com.wallet.entity.domain.WaitCollect
import org.bitcoinj.core.ECKey
import org.consenlabs.tokencore.foundation.utils.NumericUtil
import org.consenlabs.tokencore.wallet.model.ChainType
import org.consenlabs.tokencore.wallet.network.BitcoinCashMainNetParams
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.web3j.protocol.core.DefaultBlockParameterName
import wf.bitcoin.javabitcoindrpcclient.BitcoinJSONRPCClient
import wf.bitcoin.javabitcoindrpcclient.BitcoindRpcClient
import java.math.BigDecimal
import java.math.BigInteger

/** 
 * Created by pie on 2019-04-16 11: 34. 
 */
@Service
class CollectServiceImpl : CollectService{

    override fun collectOMNI() {
        
        
        val rpc = rpcClient.omniRpc()
        val walletToken = Token()
        walletToken.chainType = ChainType.BITCOIN
        
        
        walletTokenService.getByBean(walletToken).forEach { token ->
            val walletWaitCollect = WaitCollect()
            walletWaitCollect.chainType = token.chainType
            walletWaitCollect.tokenSymbol = token.tokenSymbol
            
            val list = waitCollectService.getByBean(walletWaitCollect)
            list.forEach loop@{
                
                val amount = omniUtils.getOMNIBalance(it.address, token.tokenAddress)
                
                var minCollect = token.minCollect
                
                if (minCollect <= BigDecimal.ZERO) {
                    
                    minCollect = BigDecimal("0.01")
                }
                if (amount < minCollect) {

                    waitCollectService.clearByBean(it)
                    return@loop
                }
                
                val utxos = walletXservice.getUtxos(token.chainType, it.address)
                
                
                val feeProviderAddrAdmin = cacheService.getAdminAddress(AddressAdminKey.BTC_GAS).firstOrNull()
                    ?: throw BizException(ErrorCode.GAS_ADDRESS_NOT_FOUND)
                
                
                val waitCollectAddr = cacheService.getAdminAddress(AddressAdminKey.BTC_COLLECT).random().address
                
                
                val feeProviderUtxos = walletXservice.getUtxos(token.chainType, feeProviderAddrAdmin.address)
                
                val sat = rpcClient.getSatPerByte()
                
                val fee = omniUtils.calculateFee(utxos.size + feeProviderUtxos.size, 2, sat)
                
                val walletAddress = Address()
                walletAddress.address = it.address
                walletAddress.chainType = token.chainType
                val walletId = addressService.findByBean(walletAddress).first().walletCode
                
                
                val txSignResult =
                    hsmRequest.signUsdtCollectTransaction(
                        waitCollectAddr,
                        amount,
                        fee.toBigDecimal().divide(BigDecimal.TEN.pow(8)),
                        utxos,
                        feeProviderUtxos,
                        walletId,
                        feeProviderAddrAdmin.walletCode
                    )
                
                
                try {
                    rpc.sendRawTransaction(txSignResult.signedTx)
                } catch (e: Exception) {
                    
                    return@loop
                }
                
                waitCollectService.clearByBean(it)
                
                walletXservice.saveWithReocord(
                    amount,
                    txSignResult.txHash,
                    waitCollectAddr,
                    it.address,
                    WithType.COLLECT,
                    token.chainType,
                    token.tokenSymbol
                )
                
            }
        }
        

    }

    override fun collectBTCOrFork(rpc: BitcoinJSONRPCClient, chainType: String) {
        
        
        val walletWaitCollect = WaitCollect()
        walletWaitCollect.chainType = chainType
        walletWaitCollect.tokenSymbol = TokenSymbolKey.MUST_NULL
        
        val list = waitCollectService.getByBean(walletWaitCollect)
        val sat = rpcClient.getSatPerByte()
        list.forEach loop@{
            
            
            val utxos = walletXservice.getUtxos(chainType, it.address)
            var amount = 0L
            utxos.forEach { utxo ->
                amount += utxo.amount
            }
            
            val token = cacheService.getWalletToken(chainType, chainType)
            var minCollect = token.minCollect
            
            
            val fee = omniUtils.calculateFee(utxos.size, 2, sat)
            
            if (minCollect <= BigDecimal.ZERO) {
                
                minCollect = fee.toBigDecimal().divide(BigDecimal.TEN.pow(8))
            }
            if (amount < minCollect.multiply(BigDecimal.TEN.pow(8)).toLong()) {
                waitCollectService.clearByBean(it)
                return@loop
            }

            val waitCollectAddr =
                cacheService.getAdminAddress(AddressAdminKey.getCollect(chainType)).random().address
            

            val walletAddress = Address()
            walletAddress.address = it.address
            walletAddress.chainType = chainType
            val walletId = addressService.findByBean(walletAddress).first().walletCode
            
            val realAmount = (amount - fee).toBigDecimal().divide(BigDecimal.TEN.pow(8))
            
            val txSignResult = hsmRequest.signBtcTransaction(
                realAmount,
                fee.toBigDecimal().divide(BigDecimal.TEN.pow(8)),
                waitCollectAddr,
                utxos,
                walletId
            )

            
            
            try {
                rpc.sendRawTransaction(txSignResult.signedTx)
            } catch (e: Exception) {
                
                return@loop
            }
            
            waitCollectService.clearByBean(it)
            
            walletXservice.saveWithReocord(
                realAmount,
                txSignResult.txHash,
                waitCollectAddr,
                it.address,
                WithType.COLLECT,
                chainType,
                ""
            )
            
        }
        
    }

    override fun collectETH() {
        
        
        val rpc = rpcClient.ethRpc()
        val gasPrice = rpcClient.getGasPrice()
        
        val gasLimit = 21000L
        
        val fee = BigDecimal(gasPrice).multiply(BigDecimal.TEN.pow(9)).multiply(BigDecimal(gasLimit))
        
        val walletWaitCollect = WaitCollect()
        walletWaitCollect.chainType = ChainType.ETHEREUM
        walletWaitCollect.tokenSymbol = TokenSymbolKey.MUST_NULL
        
        val list = waitCollectService.getByBean(walletWaitCollect)
        list.forEach loop@{
            
            val balance = rpc.handleError(rpc.ethGetBalance(it.address, DefaultBlockParameterName.PENDING).send())
                .balance
            
            val token = cacheService.getWalletToken(ChainType.ETHEREUM, ChainType.ETHEREUM)
            var minCollect = token.minCollect
            

            if (minCollect <= BigDecimal.ZERO) {
                
                minCollect = fee.divide(BigDecimal.TEN.pow(18))
            }
            if (balance < minCollect.multiply(BigDecimal.TEN.pow(18)).toBigInteger()) {
                waitCollectService.clearByBean(it)
                return@loop
            }
            val walletAddress = Address()
            walletAddress.chainType = ChainType.ETHEREUM
            walletAddress.address = it.address
            val walletCode = addressService.findByBean(walletAddress).first().walletCode
            
            val waitCollectAddr = cacheService.getAdminAddress(AddressAdminKey.ETH_COLLECT).random().address
            
            val nonce =
                rpc.ethGetTransactionCount(it.address, DefaultBlockParameterName.PENDING).send()
                    .transactionCount.toInt()
            
            val realAmount = BigDecimal(balance).subtract(fee).divide(BigDecimal.TEN.pow(18))
            
            val txSignResult = hsmRequest.signEthtransaction(
                nonce,
                realAmount,
                BigDecimal(gasPrice),
                gasLimit,
                waitCollectAddr,
                walletCode,
                ""
            )
            
            try {
                
                rpc.handleError(rpc.ethSendRawTransaction("0x" + txSignResult.signedTx).send())
            } catch (e: Exception) {
                
                return@loop
            }
            
            waitCollectService.clearByBean(it)
            
            walletXservice.saveWithReocord(
                realAmount,
                txSignResult.txHash,
                waitCollectAddr,
                it.address,
                WithType.COLLECT,
                ChainType.ETHEREUM,
                ""
            )
            
        }
        
    }

    override fun collectERC() {
        
        
        val rpc = rpcClient.ethRpc()
        
        
        val walletToken = Token()
        walletToken.chainType = ChainType.ETHEREUM
        walletTokenService.getByBean(walletToken).forEach { token ->
            if(token.tokenAddress==null)return@forEach
            val decimals = ethUtils.getETHContractDecimals(token.tokenAddress)
            val walletWaitCollect = WaitCollect()
            walletWaitCollect.chainType = ChainType.ETHEREUM
            walletWaitCollect.tokenSymbol = token.tokenSymbol
            
            val list = waitCollectService.getByBean(walletWaitCollect)
            list.forEach loop@{
                
                if (it.sendFee == 1) {
                    val waitCollectAddr =
                        cacheService.getAdminAddress(AddressAdminKey.ETH_COLLECT).random().address
                    
                    
                    val gasPrice = it.sendFeeGasPrice ?: rpcClient.getGasPrice()
                    
                    val gasLimit = token.gasLimit
                    
                    val walletAddress = Address()
                    walletAddress.chainType = ChainType.ETHEREUM
                    walletAddress.address = it.address
                    
                    val walletCode = addressService.findByBean(walletAddress).first().walletCode
                    

                    val nonce = rpc.ethGetTransactionCount(it.address, DefaultBlockParameterName.PENDING).send()
                        .transactionCount.toInt()
                    
                    val realAmount = ethUtils.getContractBalance(token.tokenAddress, it.address)
                    
                    var minCollect = token.minCollect
                    
                    if (minCollect <= BigDecimal.ZERO) {
                        
                        minCollect = BigDecimal("0.01")
                    }
                    if (realAmount < minCollect) {
                        
                        waitCollectService.clearByBean(it)
                        return@loop
                    }

                    val data = walletXservice.ethContractTransferData(realAmount, waitCollectAddr, decimals)
                    

                    val txSignResult = hsmRequest.signEthtransaction(
                        nonce,
                        BigDecimal.ZERO,
                        BigDecimal(gasPrice),
                        gasLimit,
                        token.tokenAddress,
                        walletCode,
                        data
                    )
                    

                    try {
                        rpc.handleError(rpc.ethSendRawTransaction("0x" + txSignResult.signedTx).send())
                    } catch (e: BizException) {
                        
                        return@loop
                    }

                    
                    waitCollectService.clearByBean(it)
                    
                    walletXservice.saveWithReocord(
                        realAmount,
                        txSignResult.txHash,
                        waitCollectAddr,
                        it.address,
                        WithType.COLLECT,
                        ChainType.ETHEREUM,
                        token.tokenSymbol
                    )
                    
                }
            }
        }
        
    }


    override fun collectTRX() {
        
        val api = rpcClient.trxApi()
        val walletWaitCollect = WaitCollect()
        walletWaitCollect.chainType = ChainType.TRON
        walletWaitCollect.tokenSymbol = TokenSymbolKey.MUST_NULL
        val list = waitCollectService.getByBean(walletWaitCollect)
        list.forEach loop@{
            val realAmount = tronUtils.getBalance(it.address)
            val balance = realAmount.multiply(BigDecimal.TEN.pow(6)).toLong()
            
            val token = cacheService.getWalletToken(ChainType.TRON, ChainType.TRON)
            var minCollect = token.minCollect
            
            if (minCollect <= BigDecimal.ZERO) {
                
                minCollect = BigDecimal("0.01")
            }
            if (realAmount < minCollect) {
                
                waitCollectService.clearByBean(it)
                return@loop
            }

            val walletAddress = Address()
            walletAddress.chainType = ChainType.TRON
            walletAddress.address = it.address
            val walletCode = addressService.findByBean(walletAddress).first().walletCode
            
            
            val privateKey = hsmRequest.exportWallet(walletCode, KeyType.PRIVATE)
            val waitCollectAddr = cacheService.getAdminAddress(AddressAdminKey.TRON_COLLECT).random().address
            val node = api.easyTransferByPrivate(privateKey, waitCollectAddr, balance)
            val txid = node["transaction"]["txID"].textValue()
            
            waitCollectService.clearByBean(it)
            
            walletXservice.saveWithReocord(
                realAmount,
                txid,
                waitCollectAddr,
                it.address,
                WithType.COLLECT,
                ChainType.TRON,
                ""
            )
        }
        
    }

    override fun collectTRC() {
        
        val api = rpcClient.trxApi()
        val walletToken = Token()
        walletToken.chainType = ChainType.TRON
        walletTokenService.getByBean(walletToken).forEach { token ->
            if(token.tokenAddress==null)return@forEach
            val decimals = api.contractDecimals(token.tokenAddress)
            
            val walletWaitCollect = WaitCollect()
            walletWaitCollect.chainType = ChainType.TRON
            walletWaitCollect.tokenSymbol = token.tokenSymbol
            val list = waitCollectService.getByBean(walletWaitCollect)
            list.forEach loop@{
                if (it.sendFee == 1) {
                    val realAmount = tronUtils.getContractBalance(token.tokenAddress, it.address)
                    val balance = realAmount.multiply(BigDecimal.TEN.pow(decimals)).toLong()
                    val walletAddress = Address()
                    walletAddress.chainType = ChainType.TRON
                    walletAddress.address = it.address
                    val walletCode = addressService.findByBean(walletAddress).first().walletCode
                    
                    val privateKey = hsmRequest.exportWallet(walletCode, KeyType.PRIVATE)
                    val waitCollectAddr =
                        cacheService.getAdminAddress(AddressAdminKey.TRON_COLLECT).random().address

                    val res = api.easyTransferContractByPrivate(
                        privateKey,
                        token.tokenAddress,
                        it.address,
                        waitCollectAddr,
                        balance,
                        token.gasLimit
                    )
                    val txid = res["txid"].textValue()
                    waitCollectService.clearByBean(it)
                    
                    walletXservice.saveWithReocord(
                        realAmount,
                        txid,
                        waitCollectAddr,
                        it.address,
                        WithType.COLLECT,
                        ChainType.TRON,
                        ""
                    )
                }
            }
        }
        
    }

    override fun collectBCHOrFork(rpc: BitcoinJSONRPCClient, chainType: String) {
        
        
        val walletWaitCollect = WaitCollect()
        walletWaitCollect.chainType = chainType
        walletWaitCollect.tokenSymbol = TokenSymbolKey.MUST_NULL
        
        val list = waitCollectService.getByBean(walletWaitCollect)
        list.forEach loop@{
            
            
            val utxos = walletXservice.getUtxos(chainType, it.address)
            var amount = 0L
            utxos.forEach { utxo ->
                amount += utxo.amount
            }
            
            val token = cacheService.getWalletToken(chainType, chainType)
            var minCollect = token.minCollect
            
            val sat = rpcClient.getSatPerByte()
            
            val fee = omniUtils.calculateFee(utxos.size, 2, sat)
            
            if (minCollect <= BigDecimal.ZERO) {
                minCollect = fee.toBigDecimal().divide(BigDecimal.TEN.pow(8))
            }
            if (amount < minCollect.multiply(BigDecimal.TEN.pow(8)).toLong()) {

                waitCollectService.clearByBean(it)
                return@loop
            }

            val waitCollectAddr =
                cacheService.getAdminAddress(AddressAdminKey.getCollect(chainType)).random().address
            
            val walletAddress = Address()
            walletAddress.address = it.address
            walletAddress.chainType = chainType
            val walletId = addressService.findByBean(walletAddress).first().walletCode
            
            val realAmount = (amount - fee).toBigDecimal().divide(BigDecimal.TEN.pow(8))
            

            val privateKey = hsmRequest.exportWallet(walletId, KeyType.PRIVATE)

            val txHash = try {

                val inputs = utxos.map {
                    BitcoindRpcClient.BasicTxInput(it.txHash, it.vout, it.address)
                }
                val outputs = listOf(BitcoindRpcClient.BasicTxOutput(waitCollectAddr, realAmount))
                val unsignedTx = rpc.createRawTransaction(inputs, outputs)
                val eckey = ECKey.fromPrivate(NumericUtil.hexToBigInteger(privateKey))
                val wifiKey = eckey.getPrivateKeyAsWiF(BitcoinCashMainNetParams.get())
                val signedTx = rpc.signRawTransaction(unsignedTx, null, listOf(wifiKey))
                rpc.sendRawTransaction(signedTx)
            } catch (e: Exception) {
                
                return@loop
            }
            
            waitCollectService.clearByBean(it)
            
            walletXservice.saveWithReocord(
                realAmount,
                txHash,
                waitCollectAddr,
                it.address,
                WithType.COLLECT,
                chainType,
                ""
            )
            
        }
        
    }

    override fun scanAllCoin() {
        val list = addressService.findAll()
        val addrAdminMap = addressAdminService.findAll().associateBy { it.address }
        val addrMap = list.associateBy { "${it.chainType}_${it.address}" } as HashMap
        hsmRequest.getAllWallets().forEach {
            if (addrMap["${it.chainType}_${it.address}"] == null && addrAdminMap[it.address] == null) {
                val address = Address()
                address.autoCollect = 1
                address.chainType = it.chainType
                address.address = it.address
                address.walletCode = it.walletCode
                addressService.save(address)
                addrMap["${it.chainType}_${it.address}"] = address
            }
        }

        val tokenMap = walletTokenService.findAll().filter { it.tokenAddress.isNotBlank() }.groupBy { it.chainType }
        addrMap.values.forEach { addr ->
            when (addr.chainType) {
                ChainType.BITCOIN -> {
                    val balance = omniUtils.getBTCBalance(addr.address)
                    if (balance > BigDecimal.ZERO) {
                        //无视autoCollect字段
                        val walletWaitCollect = WaitCollect()
                        walletWaitCollect.address = addr.address
                        walletWaitCollect.chainType = addr.chainType
                        walletWaitCollect.sendFee = 0
                        waitCollectService.save(walletWaitCollect)
                    }
                    val tokenList = tokenMap[ChainType.BITCOIN]
                    tokenList?.forEach {
                        val tokenBalance = omniUtils.getOMNIBalance(addr.address, it.tokenAddress)
                        if (tokenBalance > BigDecimal.ZERO) {
                            //无视autoCollect字段
                            val walletWaitCollect = WaitCollect()
                            walletWaitCollect.address = addr.address
                            walletWaitCollect.chainType = addr.chainType
                            walletWaitCollect.tokenSymbol = it.tokenSymbol
                            walletWaitCollect.sendFee = 0
                            waitCollectService.save(walletWaitCollect)
                        }
                    }
                }
                ChainType.ETHEREUM -> {
                    val balance = ethUtils.getBalance(addr.address)
                    if (balance > BigDecimal.ZERO) {
                        //无视autoCollect字段
                        val walletWaitCollect = WaitCollect()
                        walletWaitCollect.address = addr.address
                        walletWaitCollect.chainType = addr.chainType
                        walletWaitCollect.sendFee = 0
                        waitCollectService.save(walletWaitCollect)
                    }
                    val tokenList = tokenMap[ChainType.ETHEREUM]
                    tokenList?.forEach {
                        val tokenBalance = ethUtils.getContractBalance(it.tokenAddress, addr.address)
                        if (tokenBalance > BigDecimal.ZERO) {
                            //无视autoCollect字段
                            val walletWaitCollect = WaitCollect()
                            walletWaitCollect.address = addr.address
                            walletWaitCollect.chainType = addr.chainType
                            walletWaitCollect.tokenSymbol = it.tokenSymbol
                            walletWaitCollect.sendFee = 0
                            waitCollectService.save(walletWaitCollect)
                        }
                    }
                }
                ChainType.TRON -> {
                    val balance = tronUtils.getBalance(addr.address)
                    if (balance > BigDecimal.ZERO) {
                        //无视autoCollect字段
                        val walletWaitCollect = WaitCollect()
                        walletWaitCollect.address = addr.address
                        walletWaitCollect.chainType = addr.chainType
                        walletWaitCollect.sendFee = 0
                        waitCollectService.save(walletWaitCollect)
                    }
                    val tokenList = tokenMap[ChainType.TRON]
                    tokenList?.forEach {
                        val tokenBalance = tronUtils.getContractBalance(it.tokenAddress, addr.address)
                        if (tokenBalance > BigDecimal.ZERO) {
                            //无视autoCollect字段
                            val walletWaitCollect = WaitCollect()
                            walletWaitCollect.address = addr.address
                            walletWaitCollect.chainType = addr.chainType
                            walletWaitCollect.tokenSymbol = it.tokenSymbol
                            walletWaitCollect.sendFee = 0
                            waitCollectService.save(walletWaitCollect)
                        }
                    }
                }
            }

        }
    }

    @Autowired
    lateinit var waitCollectService: WaitCollectService

    @Autowired
    lateinit var addressService: AddressService

    @Autowired
    lateinit var hsmRequest: HsmRequest

    @Autowired
    lateinit var walletXservice: WalletXService

    @Autowired
    lateinit var addressAdminService: AddressAdminService

    @Autowired
    lateinit var rpcClient: RpcClient

    @Autowired
    lateinit var walletTokenService: TokenService

    @Autowired
    lateinit var ethUtils: ETHUtils

    @Autowired
    lateinit var omniUtils: OMNIUtils

    @Autowired
    lateinit var cacheService: CacheService

    @Autowired
    lateinit var tronUtils: TRONUtils

}
