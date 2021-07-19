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
import java.lang.Exception
import java.math.BigDecimal
import java.math.BigInteger

/** 
 * Created by pie on 2020/7/9 20: 02. 
 */
@Component
class ETHUtils {

    private fun createContractData(functionName: String, params: List<Type<*>>): String {
        val function = Function(
            functionName,
            params,
            listOf<TypeReference<*>>(object : TypeReference<Bool>() {

            })
        )
        return FunctionEncoder.encode(function)
    }

    private fun ethCall(functionName: String, contractAddress: String, params: List<Type<*>>): String {
        val rpc = rpcClient.ethRpc()
        val data = createContractData(functionName, params)
        return rpc.handleError(
            rpc.ethCall(
                Transaction.createEthCallTransaction(contractAddress, contractAddress, data),
                DefaultBlockParameterName.PENDING
            ).send()
        ).value
    }

    fun estimateContractGasWithMaxBalance(from: String, to: String,tokenAddress:String):Long{
        val tokenBalance=getContractBalance(tokenAddress,from)
        return estimateContractGas(from,to,tokenBalance,tokenAddress)
    }

    fun estimateContractGas(from: String, to: String,amount: BigDecimal,tokenAddress:String):Long{
        val decimals = getETHContractDecimals(tokenAddress)
        val data=createContractTransferData(amount,to,decimals)
        return estimateGas(from,tokenAddress,data)
    }

    fun estimateGas(from: String, to: String, data: String): Long {
        val rpc = rpcClient.ethRpc()
        val estimateGasTx = Transaction.createEthCallTransaction(from, to, data)
        val res = rpc.handleError(rpc.ethEstimateGas(estimateGasTx).send())
        return res.amountUsed.toLong()
    }

    fun getETHContractDecimals(contractAddress: String): Int {
        val value = ethCall("decimals", contractAddress, listOf())
        if (value == "0x") return 0
        return value.replaceFirst("0x", "").toInt(16)
    }

    fun getEthContractBalanceNoDivide(contractAddress: String, address: String): BigDecimal {
        val value = ethCall("balanceOf", contractAddress, listOf(Address(address)))
        if (value == "0x") return BigDecimal.ZERO
        return BigDecimal(BigInteger(value.replace("0x", ""), 16).toString())
    }

    fun getContractBalance(contractAddress: String, address: String): BigDecimal {
        val amount = getEthContractBalanceNoDivide(contractAddress, address)
        val decimals = getETHContractDecimals(contractAddress)
        return amount.divide(BigDecimal.TEN.pow(decimals))
    }

    fun getBalance(address: String): BigDecimal {
        val rpc = rpcClient.ethRpc()
        return rpc.handleError(
            rpc.ethGetBalance(
                address,
                DefaultBlockParameterName.PENDING
            ).send()
        ).balance.toBigDecimal().divide(
            BigDecimal.TEN.pow(18)
        )
    }


    fun decodeTransferInput(inputData: String): DecodedTransferInput {
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

    fun createContractTransferData(amount: BigDecimal, to: String, decimals: Int): String {
        return createContractData("transfer",listOf(
            Address(to),
            Uint256(amount.multiply(BigDecimal.TEN.pow(decimals)).toBigInteger())
        ))
    }


    @Autowired
    lateinit var rpcClient: RpcClient

}
