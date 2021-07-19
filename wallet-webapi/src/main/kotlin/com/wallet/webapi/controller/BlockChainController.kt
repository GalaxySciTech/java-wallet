package com.wallet.webapi.controller

import com.wallet.biz.dict.TokenKey
import com.wallet.biz.domain.dict.TokenResponse
import com.wallet.biz.domain.po.CalculationFeePo
import com.wallet.biz.domain.po.GetAddressBalancePo
import com.wallet.biz.domain.po.GetTransactionPo
import com.wallet.biz.domain.vo.GetRecommendFeeVo
import com.wallet.biz.domain.vo.TransactionVo
import com.wallet.biz.xservice.BlockChainXService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

/** 
 * Created by pie on 2020/7/11 16: 05. 
 */
@RestController
@Api(description = "区块链接口")
@RequestMapping("block_chain/v1")
class BlockChainController {

    @GetMapping("get_transaction")
    @ApiOperation("查询hash/地址 查询交易(链上查询) type 100查询hash 200查询地址 默认10条")
    fun getTransaction(
            chain: String?,
            tokenAddress: String?,
            hash: String?,
            address: String?,
            type: Int?,
            limit: Int?
    ): TokenResponse<List<TransactionVo>> {
        val getTransactionPo = GetTransactionPo()
        getTransactionPo.chain = chain
        getTransactionPo.address = address
        getTransactionPo.hash = hash
        getTransactionPo.tokenAddress = tokenAddress
        getTransactionPo.type = type
        getTransactionPo.limit = limit
        val list = blockChainXService.getTransaction(getTransactionPo)
        return TokenResponse(list)
    }

    @GetMapping("get_address_balance")
    @ApiOperation("查询地址余额(链上查询)")
    fun getAddressBalance(address: String?, chain: String?, tokenAddress: String?): TokenResponse<BigDecimal> {
        val getAddressBalancePo = GetAddressBalancePo()
        getAddressBalancePo.address = address
        getAddressBalancePo.chain = chain
        getAddressBalancePo.tokenAddress = tokenAddress
        val any = blockChainXService.getAddressBalance(
                getAddressBalancePo.chain!!,
                getAddressBalancePo.address!!,
                getAddressBalancePo.tokenAddress
        )
        return TokenResponse(any)
    }

    @GetMapping("get_recommend_fee")
    @ApiOperation("获取低中高阶手续费")
    fun getRecommendFee(chain: String): TokenResponse<GetRecommendFeeVo> {
        val vo = blockChainXService.getRecommendFee(chain)
        return TokenResponse(vo)
    }

    @GetMapping("calculation_fee")
    @ApiOperation("计算手续费数量")
    fun calculationFee(gas: Int, gasLimit: Long?, from: String?, chain: String): TokenResponse<BigDecimal> {
        val po = CalculationFeePo()
        po.chain = chain
        po.from = from
        po.gas = gas
        po.gasLimit = gasLimit
        val fee = blockChainXService.calculationFee(po)
        return TokenResponse(fee)
    }

    @GetMapping("get_support_token")
    @ApiOperation("获得支持币种")
    fun getSupportToken(): TokenResponse<Map<String, Any>> {
        val chainList = TokenKey.getChainList()
        val symbolList = TokenKey.getSymbolList()
        return TokenResponse(mapOf("chain" to chainList, "symbol" to symbolList))
    }


    @Autowired
    lateinit var blockChainXService: BlockChainXService
}
