package com.wallet.biz.utils

import com.wallet.biz.domain.DecodedTransferInput
import com.wallet.biz.rpc.RpcClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Bool
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Type
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.Transaction
import java.math.BigDecimal
import java.math.BigInteger

@Component
class ETHUtils {

    private fun createTransferFunctionData(functionName: String, params: List<Type<*>>): String {
        val function = Function(
            functionName,
            params,
            listOf<TypeReference<*>>(object : TypeReference<Bool>() {})
        )
        return FunctionEncoder.encode(function)
    }

    private fun createViewFunctionData(functionName: String, params: List<Type<*>>): String {
        val function = Function(
            functionName,
            params,
            listOf<TypeReference<*>>(object : TypeReference<Uint256>() {})
        )
        return FunctionEncoder.encode(function)
    }

    private fun ethCall(functionName: String, contractAddress: String, params: List<Type<*>>): String {
        val rpc = rpcClient.ethRpc()
        val data = createViewFunctionData(functionName, params)
        return rpc.handleError(
            rpc.ethCall(
                Transaction.createEthCallTransaction(contractAddress, contractAddress, data),
                DefaultBlockParameterName.PENDING
            ).send()
        ).value
    }

    fun estimateContractGasWithMaxBalance(from: String, to: String, tokenAddress: String): Long {
        val tokenBalance = getContractBalance(tokenAddress, from)
        return estimateContractGas(from, to, tokenBalance, tokenAddress)
    }

    fun estimateContractGas(from: String, to: String, amount: BigDecimal, tokenAddress: String): Long {
        val decimals = getETHContractDecimals(tokenAddress)
        val data = createContractTransferData(amount, to, decimals)
        return estimateGas(from, tokenAddress, data)
    }

    fun estimateGas(from: String, to: String, data: String): Long {
        val rpc = rpcClient.ethRpc()
        val estimateGasTx = Transaction.createEthCallTransaction(from, to, data)
        val res = rpc.handleError(rpc.ethEstimateGas(estimateGasTx).send())
        return res.amountUsed.toLong()
    }

    fun getETHContractDecimals(contractAddress: String): Int {
        val value = ethCall("decimals", contractAddress, listOf())
        if (value == "0x" || value.isNullOrBlank()) return 0
        return try {
            value.removePrefix("0x").toBigInteger(16).toInt()
        } catch (e: NumberFormatException) {
            0
        }
    }

    fun getEthContractBalanceNoDivide(contractAddress: String, address: String): BigDecimal {
        val value = ethCall("balanceOf", contractAddress, listOf(Address(address)))
        if (value == "0x" || value.isNullOrBlank()) return BigDecimal.ZERO
        return BigDecimal(BigInteger(value.replace("0x", ""), 16).toString())
    }

    fun getContractBalance(contractAddress: String, address: String): BigDecimal {
        val amount = getEthContractBalanceNoDivide(contractAddress, address)
        val decimals = getETHContractDecimals(contractAddress)
        if (decimals == 0) return amount
        return amount.divide(BigDecimal.TEN.pow(decimals))
    }

    fun getBalance(address: String): BigDecimal {
        val rpc = rpcClient.ethRpc()
        return rpc.handleError(
            rpc.ethGetBalance(address, DefaultBlockParameterName.PENDING).send()
        ).balance.toBigDecimal().divide(BigDecimal.TEN.pow(18))
    }

    fun decodeTransferInput(inputData: String): DecodedTransferInput {
        val i = DecodedTransferInput()
        try {
            i.methodID = inputData.substring(0, 10)
            val rawHex = inputData.substring(10, 74).removePrefix("0x").removePrefix("0X")
            i.to = "0x${rawHex.substring(24)}"
            val valueHex = inputData.substring(74).removePrefix("0x").removePrefix("0X")
            i.value = BigInteger(valueHex, 16)
            i.pass = true
        } catch (e: Exception) {
            i.pass = false
        }
        return i
    }

    fun createContractTransferData(amount: BigDecimal, to: String, decimals: Int): String {
        return createTransferFunctionData(
            "transfer", listOf(
                Address(to),
                Uint256(amount.multiply(BigDecimal.TEN.pow(decimals)).toBigInteger())
            )
        )
    }

    @Autowired
    lateinit var rpcClient: RpcClient
}
