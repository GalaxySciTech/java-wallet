package com.wallet.biz.rpc

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import wf.bitcoin.javabitcoindrpcclient.BitcoinJSONRPCClient
import wf.bitcoin.javabitcoindrpcclient.BitcoindRpcClient.TxInput
import wf.bitcoin.javabitcoindrpcclient.GenericRpcException
import java.math.BigDecimal
import java.util.*

/** 
 * Created by pie on 2020/7/24 19: 50. 
 */
class BitcoinFork {

    class LtcRpc(url: String) : BitcoinJSONRPCClient(url)

    class BchRpc(url: String) : BitcoinJSONRPCClient(url) {

        @Throws(GenericRpcException::class)
        override fun signRawTransaction(
            hex: String?,
            inputs: List<TxInput?>?,
            privateKeys: List<String?>?
        ): String {
            return signRawTransaction(hex, privateKeys)
        }

        fun signRawTransaction(
            hex: String?,
            privateKeys: List<String?>?
        ): String {

            val result = query(
                "signrawtransactionwithkey",
                hex,
                privateKeys
            ) as Map<String, *> //if sigHashType is null it will return the default "ALL"
            return if ((result["complete"] as Boolean?)!!) result["hex"] as String else throw GenericRpcException(
                "Incomplete"
            )
        }

    }

    class BsvRpc(url: String) : BitcoinJSONRPCClient(url){

        @Throws(GenericRpcException::class)
        override fun signRawTransaction(
            hex: String?,
            inputs: List<TxInput?>?,
            privateKeys: List<String?>?
        ): String {
            return signRawTransaction(hex, privateKeys)
        }

        fun signRawTransaction(
            hex: String?,
            privateKeys: List<String?>?
        ): String {

            val result = query(
                "signrawtransaction",
                hex,
                null,
                privateKeys
            ) as Map<String, *> //if sigHashType is null it will return the default "ALL"
            return if ((result["complete"] as Boolean?)!!) result["hex"] as String else throw GenericRpcException(
                "Incomplete"
            )
        }

    }

    class DashRpc(url: String) : BitcoinJSONRPCClient(url)

    class DogeRpc(url: String) : BitcoinJSONRPCClient(url)

    class OmniRpc(url: String) : BitcoinJSONRPCClient(url) {

        fun getBlockWithTx(height: Int): BlockWithTx {
            val hash = getBlockHash(height)
            val block = objectMapper.writeValueAsString(query("getblock", hash, 2))
            return objectMapper.readValue(block, BlockWithTx::class.java)
        }

        fun omniListBlockTransactions(height: Long): List<String> {
            return query("omni_listblocktransactions", height) as List<String>
        }

        fun omniGetTransaction(hash: String): OmniTransaction {
            val block = objectMapper.writeValueAsString(query("omni_gettransaction", hash))
            return objectMapper.readValue(block, OmniTransaction::class.java)
        }

        fun omniGetBalance(address: String, propertyId: String): OmniBalance {
            val balance = objectMapper.writeValueAsString(query("omni_getbalance", address, propertyId.toInt()))
            return objectMapper.readValue(balance, OmniBalance::class.java)
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        class OmniBalance {
            var balance: BigDecimal? = null
            var reserved: BigDecimal? = null
            var frozen: BigDecimal? = null
        }


        @JsonIgnoreProperties(ignoreUnknown = true)
        class OmniTransaction {
            var txid: String? = null
            var sendingaddress: String? = null
            var referenceaddress: String? = null
            var ismine: String? = null
            var confirmations: Int? = null
            var fee: BigDecimal? = null
            var blocktime: Date? = null
            var valid: String? = null
            var positioninblock: String? = null
            var version: String? = null
            var type_int: String? = null
            var type: String? = null
            var propertyid: Long? = null
            var divisible: Boolean? = null
            var amount: BigDecimal? = null
            var blockhash: String? = null
            var block: String? = null
            var invalidreason: String? = null

        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        class BlockWithTx {
            var hash: String? = null

            var confirmations: Long = 0

            var strippedsize: Long = 0

            var size: Long = 0

            var weight: Long = 0

            var height: Long = 0

            var version: Long = 0

            var versionHex: String? = null

            var merkleroot: String? = null

            var tx: List<Tx>? = null

            var time: Long = 0

            var mediantime: Long = 0

            var nonce: Long = 0

            var bits: String? = null

            var difficulty: BigDecimal = BigDecimal.ZERO

            var chainwork: String? = null

            var nTx: Long = 0

            var previousblockhash: String? = null

            var nextblockhash: String? = null
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        class Tx {
            var txid: String? = null

            var hash: String? = null

            var version: Long = 0

            var size: Long = 0

            var vsize: Long = 0

            var weight: Long = 0

            var locktime: Long = 0

            var vin: List<Vin>? = null

            var vout: List<Vout>? = null

            var hex: String? = null
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        class Vin {
            var coinbase: String? = null

            var sequence: Long = 0
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        class Vout {
            var value: BigDecimal = BigDecimal.ZERO

            var n: Long = 0

            var scriptPubKey: ScriptPubkey? = null

        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        class ScriptPubkey {
            var asm: String? = null

            var hex: String? = null

            var reqSigs: Long = 0

            var type: String? = null

            var addresses: List<String>? = null
        }

        companion object {
            val objectMapper = ObjectMapper()
        }

    }
}
