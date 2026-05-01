package com.wallet.biz.domain.po

import java.math.BigDecimal
import java.math.BigInteger

/** 
 * Created by pie on 2019-03-05 13: 24. 
 */
class SignEthereumPo{

    var walletId: String? = null

    var toAddress: String? = null

    var amount: BigDecimal? = null

    var gasPrice: BigDecimal? = null

    var gasLimit:Long?=null

    var nonce:Int?=null

    var data:String?=null

}