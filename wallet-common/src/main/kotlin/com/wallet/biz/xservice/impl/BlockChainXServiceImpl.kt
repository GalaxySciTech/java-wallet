package com.wallet.biz.xservice.impl

import com.wallet.biz.domain.dict.ErrorCode
import com.wallet.biz.domain.po.GetTransactionPo
import com.wallet.biz.domain.vo.BTCScript
import com.wallet.biz.domain.vo.TransactionVo
import com.wallet.biz.domain.exception.BizException
import com.wallet.biz.domain.po.CalculationFeePo
import com.wallet.biz.domain.vo.GetRecommendFeeVo
import com.wallet.biz.rpc.RpcClient
import com.wallet.biz.utils.ETHUtils
import com.wallet.biz.utils.OMNIUtils
import com.wallet.biz.xservice.BlockChainXService
import com.wallet.biz.xservice.WalletXService
import com.fasterxml.jackson.databind.JsonNode
import com.wallet.biz.log.impl.LogService
import org.consenlabs.tokencore.wallet.model.ChainType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal

/** 
 * Created by pie on 2020/7/11 16: 10. 
 */
@Service
open class BlockChainXServiceImpl : BlockChainXService, LogService() {

    override fun getTransaction(getTransactionByHashPo: GetTransactionPo): List<TransactionVo> {
        when (getTransactionByHashPo.type) {
            100 -> {
                log("根据hash查询交易详情")
                val vo = TransactionVo()
                return when (getTransactionByHashPo.chain) {
                    ChainType.BITCOIN -> {
                        log("获得OMNI RPC")
                        val rpc = rpcClient.omniRpc()
                        if (getTransactionByHashPo.tokenAddress == null) {
                            log("tokenAddress参数为空，将查询比特币的交易")
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
                            log("tokenAddress参数有值，将查询OMNI的交易")
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
                        log("获得${ChainType.ETHEREUM} RPC")
                        val rpc = rpcClient.ethRpc()
                        val transaction = rpc.ethGetTransactionByHash(getTransactionByHashPo.hash).send().result
                        if (getTransactionByHashPo.tokenAddress == null) {
                            log("tokenAddress参数为空，将查询以太坊的交易")
                            vo.txid = transaction.hash
                            vo.from = transaction.from
                            vo.to = transaction.to
                            vo.value = transaction.value.toBigDecimal().divide(BigDecimal.TEN.pow(18))
                            vo.fee =
                                (transaction.gas * transaction.gasPrice).toBigDecimal().divide(BigDecimal.TEN.pow(18))
                        } else {
                            if (transaction.to == getTransactionByHashPo.tokenAddress) {
                                log("tokenAddress参数有值，将查询以太坊代币的交易")
                                val decimals = ethUtils.getETHContractDecimals(transaction.to)
                                vo.from = transaction.from
                                val input = ethUtils.decodeTransferInput(transaction.input)
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
                log("根据address查询交易列表详情")
                val symbol = when (getTransactionByHashPo.chain) {
                    ChainType.BITCOIN -> "btc"
                    ChainType.ETHEREUM -> "eth"
                    else -> throw BizException(ErrorCode.NOT_SUPPORT)
                }
                val limit = getTransactionByHashPo.limit ?: 10
                val url = "https://api.blockcypher.com/v1/$symbol/main/addrs/${getTransactionByHashPo.address}"
                val node = restTemplate.getForObject(url, JsonNode::class.java)
                if (node["error"] != null) throw BizException(404, node["error"].toString())
                log("根据adress查询交易列表hash，根据hash再依次查询交易详情")
                val txs = node["txrefs"].take(limit).map {
                    val txid = it["tx_hash"].asText()
                    getTransactionByHashPo.type = 100
                    getTransactionByHashPo.hash = txid
                    getTransaction(getTransactionByHashPo).first()
                }
                return txs
            }
            else -> throw BizException(ErrorCode.NO_THIS_TYPE)
        }
    }

    override fun getAddressBalance(chainType: String, address: String, tokenAddress: String?): BigDecimal {
        return when (chainType) {
            ChainType.BITCOIN -> {
                if (tokenAddress != null) {
                    log("获得OMNI余额")
                    omniUtils.getOMNIBalance(address, tokenAddress)
                } else {
                    log("获得比特币余额")
                    omniUtils.getBTCBalance(address)
                }
            }
            ChainType.ETHEREUM -> {
                if (tokenAddress != null) {
                    log("获得以太坊代币余额")
                    ethUtils.getContractBalance(tokenAddress, address)
                } else {
                    log("获得以太坊余额")
                    ethUtils.getBalance(address)
                }
            }
            else -> throw BizException(ErrorCode.NO_THIS_CHAIN_TYPE)
        }
    }

    @Cacheable("recommendFee")
    override fun getRecommendFee(chain: String): GetRecommendFeeVo {
        log("获得推荐手续费")
        val vo = GetRecommendFeeVo()
        when (chain) {
            ChainType.BITCOIN -> {
                val node = restTemplate.getForObject(
                    "https://bitcoinfees.earn.com/api/v1/fees/recommended",
                    JsonNode::class.java
                )
                vo.slow = node["hourFee"].longValue()
                vo.medium = node["halfHourFee"].longValue()
                vo.fast = node["fastestFee"].longValue()
            }
            ChainType.ETHEREUM -> {
                val node =
                    restTemplate.getForObject("https://ethgasstation.info/json/ethgasAPI.json", JsonNode::class.java)
                vo.slow = node["safeLow"].longValue() / 10
                vo.medium = node["average"].longValue() / 10
                vo.fast = node["fast"].longValue() / 10
            }
        }

        return vo
    }

    override fun calculationFee(po: CalculationFeePo): BigDecimal {
        log("开始计算手续费")
        return when (po.chain) {
            ChainType.BITCOIN -> {
                log("获得地址${po.from}的utxo")
                val utxos = walletXService.getUtxos(ChainType.BITCOIN, po.from!!)
                if (utxos.isEmpty()) throw BizException(ErrorCode.INSUFFICIENT_BALANCE)
                val satPerByte = po.gas?.toLong() ?: rpcClient.getSatPerByte()
                val fee = omniUtils.calculateFee(utxos.size, 2, satPerByte)
                fee.toBigDecimal().divide(BigDecimal.TEN.pow(8))
            }
            ChainType.ETHEREUM -> {
                BigDecimal(po.gas!! * po.gasLimit!!) * BigDecimal.TEN.pow(9)
            }
            else -> throw BizException(ErrorCode.NOT_SUPPORT)
        }
    }

    @Autowired
    lateinit var ethUtils: ETHUtils
    @Autowired
    lateinit var omniUtils: OMNIUtils
    @Autowired
    lateinit var rpcClient: RpcClient
    @Autowired
    lateinit var restTemplate: RestTemplate
    @Autowired
    lateinit var walletXService: WalletXService
}
