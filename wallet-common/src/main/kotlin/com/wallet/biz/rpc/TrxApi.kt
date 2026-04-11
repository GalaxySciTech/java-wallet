package com.wallet.biz.rpc

import com.wallet.biz.domain.DecodedTransferInput
import com.wallet.biz.domain.exception.BizException
import com.wallet.biz.utils.BasicUtils
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.consenlabs.tokencore.foundation.utils.NumericUtil
import org.slf4j.LoggerFactory
import org.springframework.web.client.RestTemplate

class TrxApi(val url: String, val restTemplate: RestTemplate) {

    private val logger = LoggerFactory.getLogger(TrxApi::class.java)

    val callAddress = "TM1zzNDZD2DPASbKcgdVoTYhfmYgtfwx9R"

    fun easyTransferByPrivate(
        privateKey: String,
        fromAddress: String,
        toAddress: String,
        amount: Long
    ): JsonNode {
        val res = createTransaction(toAddress, fromAddress, amount)
        val signedTransaction = signTransactionLocally(res, privateKey)
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
        val signedTransaction = signTransactionLocally(res["transaction"], privateKey)
        return broadcastTransaction(signedTransaction)
    }

    fun broadcastTransaction(signedTransaction: JsonNode): JsonNode {
        return post("$url/wallet/broadcasttransaction", signedTransaction)
    }

    /**
     * Local offline signing using ECDSA secp256k1 - private key never leaves the process.
     * This replaces the old approach of sending private keys to a remote HTTP endpoint.
     */
    private fun signTransactionLocally(transaction: JsonNode, privateKey: String): JsonNode {
        val txId = transaction["txID"]?.asText()
            ?: throw BizException(-1, "Transaction has no txID field")
        val rawDataHex = transaction["raw_data_hex"]?.asText()
            ?: throw BizException(-1, "Transaction has no raw_data_hex field")

        val txIdBytes = NumericUtil.hexToBytes(txId)
        val privKeyBytes = NumericUtil.hexToBytes(privateKey)

        val ecKey = org.bitcoinj.core.ECKey.fromPrivate(privKeyBytes, false)
        val signature = ecKey.sign(org.bitcoinj.core.Sha256Hash.wrap(txIdBytes))

        val sigBytes = ByteArray(65)
        val rBytes = signature.r.toByteArray()
        val sBytes = signature.s.toByteArray()
        System.arraycopy(rBytes, Math.max(0, rBytes.size - 32), sigBytes, Math.max(0, 32 - rBytes.size), Math.min(32, rBytes.size))
        System.arraycopy(sBytes, Math.max(0, sBytes.size - 32), sigBytes, Math.max(0, 64 - sBytes.size), Math.min(32, sBytes.size))
        var recId = -1
        for (i in 0..3) {
            val recovered = org.bitcoinj.core.ECKey.recoverFromSignature(i, signature, org.bitcoinj.core.Sha256Hash.wrap(txIdBytes), false)
            if (recovered != null && recovered.pubKeyPoint == ecKey.pubKeyPoint) {
                recId = i
                break
            }
        }
        sigBytes[64] = recId.toByte()

        val signatureHex = NumericUtil.bytesToHex(sigBytes)

        val signedTx = obj.createObjectNode()
        signedTx.set<JsonNode>("raw_data", transaction["raw_data"])
        signedTx.put("raw_data_hex", rawDataHex)
        signedTx.put("txID", txId)
        val sigArray = signedTx.putArray("signature")
        sigArray.add(signatureHex)

        return signedTx
    }

    fun easyTransferByPrivate(privateKey: String, toAddress: String, amount: Long): JsonNode {
        val fromAddress = getAddressFromPrivateKey(privateKey)
        return easyTransferByPrivate(privateKey, fromAddress, toAddress, amount)
    }

    private fun getAddressFromPrivateKey(privateKey: String): String {
        val ecKey = org.bitcoinj.core.ECKey.fromPrivate(NumericUtil.hexToBytes(privateKey), false)
        val pubKeyBytes = ecKey.pubKeyPoint.getEncoded(false)
        val addressBytes = ByteArray(21)
        addressBytes[0] = 0x41
        val hash = org.web3j.crypto.Hash.sha3(pubKeyBytes.copyOfRange(1, pubKeyBytes.size))
        System.arraycopy(hash, 12, addressBytes, 1, 20)
        return BasicUtils.encode58Check(addressBytes)
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
        return restTemplate.postForObject(url, obj.writeValueAsString(body), JsonNode::class.java)!!
    }

    private fun get(url: String): JsonNode {
        return restTemplate.getForObject(url, JsonNode::class.java)!!
    }

    fun getTransactionInfo(txid: String): JsonNode {
        return get("https://apilist.tronscan.org/api/transaction-info?hash=$txid")
    }

    private val obj = ObjectMapper()
}
