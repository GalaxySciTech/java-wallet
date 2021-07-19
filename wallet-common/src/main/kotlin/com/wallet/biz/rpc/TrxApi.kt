package com.wallet.biz.rpc

import com.wallet.biz.domain.DecodedTransferInput
import com.wallet.biz.domain.exception.BizException
import com.wallet.biz.utils.BasicUtils
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.consenlabs.tokencore.foundation.utils.NumericUtil
import org.springframework.web.client.RestTemplate
import org.tron.trident.core.ApiWrapper
import java.lang.Exception


/** 
 * Created by pie on 2020/9/7 18: 01. 
 */
class TrxApi(val url: String, val restTemplate: RestTemplate) {

    val tronClient=ApiWrapper.ofMainnet("")

    val callAddress = "TM1zzNDZD2DPASbKcgdVoTYhfmYgtfwx9R"

    val sendTxUrl="http://13.127.47.162:8090"

    fun easyTransferByPrivate(
        privateKey: String,
        fromAddress: String,
        toAddress: String,
        amount: Long
    ): JsonNode {

        val res = createTransaction(toAddress, fromAddress, amount)
        val signedTransaction = getTransactionSign(res, privateKey)
        return broadcastTransaction(signedTransaction)
    }

    fun easyTransferContractByPrivate(
        privateKey: String,
        contractAddress: String,
        fromAddress: String,
        toAddress: String,
        amount: Long,
        feeLimit: Long
    ): JsonNode {
        val res = contractTransfer(contractAddress, fromAddress, toAddress, amount, feeLimit)
        val signedTransaction = getTransactionSign(res["transaction"], privateKey)
        return broadcastTransaction(signedTransaction)
    }

    fun broadcastTransaction(signedTransaction: JsonNode): JsonNode {
        return post("$url/wallet/broadcasttransaction", signedTransaction)
    }

    fun getTransactionSign(transaction: JsonNode, privateKey: String): JsonNode {
        val json=obj.writeValueAsString(transaction)
        val map = hashMapOf(
            "transaction" to json,
            "privateKey" to privateKey
        )
        return post("$sendTxUrl/wallet/gettransactionsign", map)
    }

    fun accounts(address: String): JsonNode {
        val map = hashMapOf(
            "address" to address,
            "visible" to true
        )
        return try {
            post("$url/wallet/getaccount", map)
        } catch (e: Exception) {
            throw BizException(-1, "地址有误")
        }
    }

    fun decodeTransferInput(inputData: String): DecodedTransferInput {
        val i = DecodedTransferInput()
        try {
            i.methodID = inputData.substring(0, 8)
            var addressHex = inputData.substring(30, 72)
            if (addressHex.substring(0, 2) == "00") addressHex = addressHex.replaceRange(0, 2, "41")
            i.to = BasicUtils.hexToBase58(addressHex)
            i.value = NumericUtil.hexToBigInteger(inputData.substring(72))
            i.pass = true
        } catch (e: Exception) {
            i.pass = false
        }
        return i
    }

    fun getBlockByNum(num: Long): JsonNode {
        val map = hashMapOf(
            "num" to num,
            "visible" to true
        )
        return post("$url/wallet/getblockbynum", map)
    }

    fun getNowBlock(): JsonNode {
        return get("$url/wallet/getnowblock")
    }

    fun easyTransferByPrivate(privateKey: String, toAddress: String, amount: Long): JsonNode {
        val map = hashMapOf(
            "privateKey" to privateKey,
            "toAddress" to toAddress,
            "amount" to amount,
            "visible" to true
        )
        val node = post("$sendTxUrl/wallet/easytransferbyprivate", map)
        if (node["result"]["code"] != null) throw BizException(-1, node["result"]["message"].textValue())
        return node
    }

    fun easyTransferAssetByPrivate(privateKey: String, toAddress: String, assetId: String, amount: Long): JsonNode {
        val map = hashMapOf(
            "privateKey" to privateKey,
            "toAddress" to toAddress,
            "assetId" to assetId,
            "amount" to amount,
            "visible" to true
        )
        return post("$sendTxUrl/wallet/easytransferassetbyprivate", map)
    }


    fun createTransaction(toAddress: String, ownerAddress: String, amount: Long): JsonNode {
        val map =
            hashMapOf("to_address" to toAddress, "owner_address" to ownerAddress, "amount" to amount, "visible" to true)
        return post("$url/wallet/createtransaction", map)
    }

    fun contractTransfer(
        contractAddress: String,
        fromAddress: String,
        toAddress: String,
        amount: Long,
        feeLimit: Long
    ): JsonNode {
        val parameter =
            "0000000000000000000000" + BasicUtils.base58CheckToHexString(toAddress) + NumericUtil.bigIntegerToHexWithZeroPadded(
                amount.toBigInteger(),
                64
            )
        return triggerConstantContract(contractAddress, "transfer(address,uint256)", fromAddress, parameter, feeLimit)
    }

    fun contractBalanceOf(contractAddress: String, address: String): JsonNode {
        val parameter = "0000000000000000000000" + BasicUtils.base58CheckToHexString(address)
        return triggerConstantContract(contractAddress, "balanceOf(address)", address, parameter)
    }

    fun contractDecimals(contractAddress: String): Int {
        val node = triggerConstantContract(contractAddress, "decimals()", callAddress)
        return NumericUtil.hexToBigInteger(node["constant_result"].first().textValue()).toInt()
    }

    fun triggerConstantContract(contractAddress: String, functionSelector: String, ownerAddress: String): JsonNode {
        val map = hashMapOf(
            "contract_address" to contractAddress,
            "function_selector" to functionSelector,
            "owner_address" to ownerAddress,
            "visible" to true
        )
        return post("$url/wallet/triggerconstantcontract", map)
    }

    fun triggerConstantContract(
        contractAddress: String,
        functionSelector: String,
        ownerAddress: String,
        parameter: String
    ): JsonNode {
        val map = hashMapOf(
            "contract_address" to contractAddress,
            "function_selector" to functionSelector,
            "owner_address" to ownerAddress,
            "parameter" to parameter,
            "visible" to true
        )
        return post("$url/wallet/triggerconstantcontract", map)
    }

    fun triggerConstantContract(
        contractAddress: String,
        functionSelector: String,
        ownerAddress: String,
        parameter: String,
        feeLimit: Long
    ): JsonNode {
        val map = hashMapOf(
            "contract_address" to contractAddress,
            "function_selector" to functionSelector,
            "owner_address" to ownerAddress,
            "parameter" to parameter,
            "fee_limit" to feeLimit,
            "visible" to true
        )
        return post("$url/wallet/triggerconstantcontract", map)
    }

    private fun post(url: String, body: Any): JsonNode {
        return restTemplate.postForObject(url, obj.writeValueAsString(body), JsonNode::class.java)
    }

    private fun get(url: String): JsonNode {
        return restTemplate.getForObject(url, JsonNode::class.java)
    }

    fun getTransactionInfo(txid: String): JsonNode {
        return get("https://apilist.tronscan.org/api/transaction-info?hash=$txid")
    }


    private val obj = ObjectMapper()
}
