package com.wallet.biz.dict

import com.wallet.biz.domain.dict.ErrorCode
import com.wallet.biz.domain.exception.BizException
import org.consenlabs.tokencore.wallet.model.ChainType
import java.math.BigDecimal

/** 
 * Created by pie on 2020/7/24 19: 28. 
 */
enum class TokenKey(
    val tokenSymbol: String,
    val chainType: String,
    var tokenAddress: String?,
    var gasLimit: Long?,
    var minCollect:BigDecimal
) {

    BITCOIN_BITCOIN(ChainType.BITCOIN, ChainType.BITCOIN, null, null,BigDecimal.ZERO),
    USDT_BITCOIN("USDT", ChainType.BITCOIN, "31", null,BigDecimal.ZERO),
    USDT_ETHEREUM("USDT", ChainType.ETHEREUM, "0xdac17f958d2ee523a2206206994597c13d831ec7", 60000L,BigDecimal.ZERO),
    ETHEREUM_ETHEREUM(ChainType.ETHEREUM, ChainType.ETHEREUM, null, null,BigDecimal.ZERO),
    USDT_TRON("USDT", ChainType.TRON, "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t", 5000000,BigDecimal.ZERO),
    TRON_TRON(ChainType.TRON, ChainType.TRON, null, null,BigDecimal.ZERO),
    DASH_DASH(ChainType.DASH, ChainType.DASH, null, null,BigDecimal.ZERO),
    DOGECOIN_DOGECOIN(ChainType.DOGECOIN, ChainType.DOGECOIN, null, null,BigDecimal.ZERO),
    LITECOIN_LITECOIN(ChainType.LITECOIN, ChainType.LITECOIN, null, null,BigDecimal.ZERO),
    BITCOINSV_BITCOINSV(ChainType.BITCOINSV, ChainType.BITCOINSV, null, null,BigDecimal.ZERO),
    BITCOINCASH_BITCOINCASH(ChainType.BITCOINCASH, ChainType.BITCOINCASH, null, null,BigDecimal.ZERO);

    companion object {

        fun getChainList():Set<String>{
            return values().groupBy { it.chainType }.keys
        }

        fun getSymbolList():Set<String>{
            return values().groupBy { it.tokenSymbol }.keys
        }
    }
}
