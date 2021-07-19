package com.wallet.biz.domain.vo

import java.math.BigDecimal
import java.util.*

/** 
 * Created by pie on 2019/12/7 16: 45. 
 */
class TransactionLogVo {
    var chainType:String?=null
    var amount: BigDecimal?=null
    var hash:String?=null
    var time: Date?=null
    var from:String?=null
    var to:String?=null
    var tokenSymbol:String?=null
    var type:Int?=null
}

class PageRes{
    var content:Any?=null
    var totalElements:Long?=null
    var totalPages:Int?=null
    var number:Int?=null
    var size:Int?=null

}