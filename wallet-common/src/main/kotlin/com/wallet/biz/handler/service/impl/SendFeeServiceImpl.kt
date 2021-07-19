package com.wallet.biz.handler.service.impl

import com.wallet.biz.cache.CacheService
import com.wallet.biz.dict.AddressAdminKey
import com.wallet.biz.dict.WithType
import com.wallet.biz.domain.dict.ErrorCode
import com.wallet.biz.domain.dict.KeyType
import com.wallet.biz.domain.exception.BizException
import com.wallet.biz.request.HsmRequest
import com.wallet.biz.rpc.RpcClient
import com.wallet.biz.service.TokenService
import com.wallet.biz.xservice.WalletXService
import com.wallet.biz.handler.service.SendFeeService
import com.wallet.biz.service.WaitCollectService
import com.wallet.biz.utils.ETHUtils
import com.wallet.biz.utils.TRONUtils
import com.wallet.entity.domain.Token
import com.wallet.entity.domain.WaitCollect
import org.consenlabs.tokencore.wallet.model.ChainType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.web3j.protocol.core.DefaultBlockParameterName
import java.lang.Exception
import java.math.BigDecimal

/** 
 * Created by pie on 2020/6/29 16: 20. 
 */
@Service
class SendFeeServiceImpl : SendFeeService{

    override fun sendFeeETH() {
        
        
        val rpc = rpcClient.ethRpc()
        val walletToken = Token()
        walletToken.chainType = ChainType.ETHEREUM
        
        tokenService.getByBean(walletToken).forEach { token ->
            val walletWaitCollect = WaitCollect()
            walletWaitCollect.chainType = ChainType.ETHEREUM
            walletWaitCollect.tokenSymbol = token.tokenSymbol
            
            val list = waitCollectService.getByBean(walletWaitCollect)
            list.forEach loop@{
                if (it.sendFee == 0) {
                    
                    val gasPrice = rpcClient.getGasPrice()
                    
                    val waitCollectAddr = cacheService.getAdminAddress(AddressAdminKey.ETH_COLLECT).random().address
                    val gasLimit = token.gasLimit?:0L
                    
                    val f = WaitCollect()
                    f.address=it.address
                    f.chainType=it.chainType
                    //解决多个待归集的列表只发一笔手续费的bug
                    val needFeeTimes=waitCollectService.getByBean(f).size
                    val fee = BigDecimal(gasPrice).multiply(BigDecimal.TEN.pow(9)).multiply(
                        BigDecimal(gasLimit).multiply(BigDecimal(needFeeTimes))
                    )

                    
                    val balance =
                        rpc.handleError(rpc.ethGetBalance(it.address, DefaultBlockParameterName.PENDING).send())
                            .balance
                    if (balance < fee.toBigInteger()) {

                        val reduceAmount = fee.subtract(BigDecimal(balance)).divide(BigDecimal.TEN.pow(18))
                        val sendAddrAdmin = cacheService.getAdminAddress(AddressAdminKey.ETH_GAS).firstOrNull()
                            ?: throw BizException(
                                ErrorCode.ADDRESS_NOT_FOUND
                            )
                        
                        val walletCode = sendAddrAdmin.walletCode
                        
                        val sendNonce =
                            rpc.ethGetTransactionCount(sendAddrAdmin.address, DefaultBlockParameterName.PENDING).send()
                                .transactionCount.toInt()
                        
                        
                        val txSignResult = hsmRequest.signEthtransaction(
                            sendNonce,
                            reduceAmount,
                            BigDecimal(rpcClient.getGasPrice()),
                            21000L,
                            it.address,
                            walletCode,
                            ""
                        )
                        
                        try {
                            
                            rpc.handleError(rpc.ethSendRawTransaction("0x" + txSignResult.signedTx).send())
                        } catch (e: Exception) {
                            
                            return@loop
                        }
                        
                        walletXService.saveWithReocord(
                            reduceAmount,
                            txSignResult.txHash,
                            it.address,
                            sendAddrAdmin.address,
                            WithType.SEND_FEE,
                            ChainType.ETHEREUM,
                            ""
                        )
                    } else {

                    }
                    it.sendFee = 1
                    it.sendFeeGasPrice = gasPrice
                    
                    waitCollectService.save(it)
                }
            }
        }
        
    }

    override fun sendFeeTRX() {
        
        
        val api = rpcClient.trxApi()
        val walletToken = Token()
        walletToken.chainType = ChainType.TRON
        
        tokenService.getByBean(walletToken).forEach { token ->
            val walletWaitCollect = WaitCollect()
            walletWaitCollect.chainType = ChainType.TRON
            walletWaitCollect.tokenSymbol = token.tokenSymbol
            
            val list = waitCollectService.getByBean(walletWaitCollect)
            list.forEach loop@{
                if (it.sendFee == 0) {
                    val sendAddrAdmin =
                        cacheService.getAdminAddress(AddressAdminKey.TRON_GAS).firstOrNull()
                            ?: throw BizException(
                                ErrorCode.ADDRESS_NOT_FOUND
                            )
                    val balance=tronUtils.getBalance(it.address).multiply(BigDecimal.TEN.pow(6)).toLong()
                    val fee = token.gasLimit?:0L
                    if (balance < fee) {

                        val privateKey = hsmRequest.exportWallet(sendAddrAdmin.walletCode, KeyType.PRIVATE)
                        val reduceBalance = fee - balance

                        val node = api.easyTransferByPrivate(privateKey, it.address, reduceBalance)

                        val txid = node["transaction"]["txID"].textValue()
                        val realAmount = BigDecimal(reduceBalance).divide(BigDecimal.TEN.pow(6))
                        walletXService.saveWithReocord(
                            realAmount,
                            txid,
                            it.address,
                            sendAddrAdmin.address,
                            WithType.SEND_FEE,
                            ChainType.TRON,
                            ""
                        )

                    } else {
                    }
                    it.sendFee = 1
                    it.sendFeeGasPrice = fee.toInt()
                    
                    waitCollectService.save(it)
                }
            }
        }
    }

    @Autowired
    lateinit var walletXService: WalletXService
    @Autowired
    lateinit var hsmRequest: HsmRequest
    @Autowired
    lateinit var rpcClient: RpcClient
    @Autowired
    lateinit var tokenService: TokenService
    @Autowired
    lateinit var waitCollectService: WaitCollectService
    @Autowired
    lateinit var cacheService: CacheService
    @Autowired
    lateinit var ethUtils: ETHUtils
    @Autowired
    lateinit var tronUtils: TRONUtils

}
