package com.wallet.biz.domain.vo

import java.math.BigDecimal

/** 
 * Created by pie on 2020/7/8 16: 08. 
 */
class TransactionVo {

    var from: Any ?= null

    var to: Any ?= null

    var value: BigDecimal? = null

    var fee: BigDecimal ?= BigDecimal.ZERO

    var txid: String? = null

    var pass=true
}

class BTCScript{
    var address:String?=null
    var value:BigDecimal?=null
}