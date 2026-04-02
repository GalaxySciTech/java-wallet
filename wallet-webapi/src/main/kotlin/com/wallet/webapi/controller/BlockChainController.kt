package com.wallet.webapi.controller

import com.wallet.biz.dict.TokenKey
import com.wallet.biz.domain.dict.TokenResponse
import com.wallet.biz.domain.po.CalculationFeePo
import com.wallet.biz.domain.po.GetAddressBalancePo
import com.wallet.biz.domain.po.GetTransactionPo
import com.wallet.biz.domain.vo.GetRecommendFeeVo
import com.wallet.biz.domain.vo.TransactionVo
import com.wallet.biz.xservice.BlockChainXService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RestController
@Tag(name = "BlockChain API", description = "区块链接口")
@RequestMapping("block_chain/v1")
class BlockChainController {

    @GetMapping("get_transaction")
    @Operation(summary = "查询交易", description = "查询hash/地址交易(链上查询) type 100查询hash 200查询地址 默认10条")
    fun getTransaction(
        @RequestParam(required = false) chain: String?,
        @RequestParam(required = false) tokenAddress: String?,
        @RequestParam(required = false) hash: String?,
        @RequestParam(required = false) address: String?,
        @RequestParam(required = false) type: Int?,
        @RequestParam(required = false) limit: Int?
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
    @Operation(summary = "查询地址余额", description = "链上查询")
    fun getAddressBalance(
        @RequestParam address: String,
        @RequestParam chain: String,
        @RequestParam(required = false) tokenAddress: String?
    ): TokenResponse<BigDecimal> {
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
    @Operation(summary = "获取推荐手续费", description = "获取低中高阶手续费")
    fun getRecommendFee(@RequestParam chain: String): TokenResponse<GetRecommendFeeVo> {
        val vo = blockChainXService.getRecommendFee(chain)
        return TokenResponse(vo)
    }

    @GetMapping("calculation_fee")
    @Operation(summary = "计算手续费数量")
    fun calculationFee(
        @RequestParam gas: Int,
        @RequestParam(required = false) gasLimit: Long?,
        @RequestParam(required = false) from: String?,
        @RequestParam chain: String
    ): TokenResponse<BigDecimal> {
        val po = CalculationFeePo()
        po.chain = chain
        po.from = from
        po.gas = gas
        po.gasLimit = gasLimit
        val fee = blockChainXService.calculationFee(po)
        return TokenResponse(fee)
    }

    @GetMapping("get_support_token")
    @Operation(summary = "获得支持币种")
    fun getSupportToken(): TokenResponse<Map<String, Any>> {
        val chainList = TokenKey.getChainList()
        val symbolList = TokenKey.getSymbolList()
        return TokenResponse(mapOf("chain" to chainList, "symbol" to symbolList))
    }

    @Autowired
    lateinit var blockChainXService: BlockChainXService
}
