package com.wallet.biz.domain

import java.math.BigInteger

/** 
 * Created by pie on 2020/6/9 16: 24. 
 */
class DecodedTransferInput {
    var methodID:String?=null
    var to:String?=null
    var value:BigInteger?=null

    var pass:Boolean=true
}