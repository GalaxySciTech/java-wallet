package com.wallet.biz.dict

import com.wallet.biz.domain.dict.ErrorCode
import com.wallet.biz.domain.exception.BizException
import org.consenlabs.tokencore.wallet.model.ChainType

/** 
 * Created by pie on 2019-04-14 11: 27. 
 */
enum class AddressAdminKey(val chainType: String, val addressType: Int) {

    BTC_SEND(ChainType.BITCOIN, 100),
    BTC_COLLECT(ChainType.BITCOIN, 200),
    BTC_GAS(ChainType.BITCOIN, 300),

    ETH_SEND(ChainType.ETHEREUM, 100),
    ETH_COLLECT(ChainType.ETHEREUM, 200),
    ETH_GAS(ChainType.ETHEREUM, 300),

    DASH_SEND(ChainType.DASH, 100),
    DASH_COLLECT(ChainType.DASH, 200),

    LTC_SEND(ChainType.LITECOIN, 100),
    LTC_COLLECT(ChainType.LITECOIN, 200),

    BCH_SEND(ChainType.BITCOINCASH, 100),
    BCH_COLLECT(ChainType.BITCOINCASH, 200),

    BSV_SEND(ChainType.BITCOINSV, 100),
    BSV_COLLECT(ChainType.BITCOINSV, 200),

    DOGE_SEND(ChainType.DOGECOIN, 100),
    DOGE_COLLECT(ChainType.DOGECOIN, 200),

//    EOS_SEND(ChainType.EOS,100),
//    EOS_COLLECT(ChainType.EOS,200),

    TRON_SEND(ChainType.TRON, 100),
    TRON_COLLECT(ChainType.TRON, 200),
    TRON_GAS(ChainType.TRON, 300);

    companion object {
        val map = values().associateBy { "${it.chainType}_${it.addressType}" }

        fun getCollect(chainType: String): AddressAdminKey {
            return map["${chainType}_200"] ?: throw BizException(ErrorCode.COLLECT_ADDRESS_NOT_FOUND)
        }
    }


}
