package com.wallet.biz.xservice

import com.wallet.biz.domain.po.CalculationFeePo
import com.wallet.biz.domain.po.GetTransactionPo
import com.wallet.biz.domain.vo.GetRecommendFeeVo
import com.wallet.biz.domain.vo.TransactionVo
import java.math.BigDecimal

/** 
 * Created by pie on 2020/7/11 16: 10. 
 */
interface BlockChainXService {
    fun getTransaction(getTransactionByHashPo: GetTransactionPo): List<TransactionVo>
    fun getAddressBalance(chainType: String, address: String, tokenAddress: String?): BigDecimal

    fun getRecommendFee(chain: String): GetRecommendFeeVo

    fun calculationFee(po: CalculationFeePo): BigDecimal
}
