package com.wallet.biz.domain.po

import io.swagger.v3.oas.annotations.media.Schema
/** 
 * Created by pie on 2020/7/8 04: 34. 
 */
@Schema
class GetTransactionPo {

    var chain:String?=null

    var hash:String?=null

    var address:String?=null

    var type:Int?=null

    @Schema(description = "合约地址")
    var tokenAddress:String?=null

    var limit:Int?=null
}


