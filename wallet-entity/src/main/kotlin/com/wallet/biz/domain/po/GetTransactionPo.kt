package com.wallet.biz.domain.po

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
/** 
 * Created by pie on 2020/7/8 04: 34. 
 */
@ApiModel
class GetTransactionPo {

    var chain:String?=null

    var hash:String?=null

    var address:String?=null

    var type:Int?=null

    @ApiModelProperty("合约地址")
    var tokenAddress:String?=null

    var limit:Int?=null
}


