package com.wallet.biz.domain.po

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.math.BigDecimal

/** 
 * Created by pie on 2019-04-12 09: 50. 
 */
@ApiModel
class SendPo{

    @ApiModelProperty("发送者地址")
    var from:String?=null
    @ApiModelProperty("发送者地址code")
    var fromWalletCode:String?=null

    @ApiModelProperty("接收者地址")
    var to:String?=null
    @ApiModelProperty("数量")
    var amount:BigDecimal?=null
    @ApiModelProperty("币种类型  例: ETHEREUM")
    var chain:String?=null
    @ApiModelProperty("代币类型 例: USDT")
    var symbol :String?=null
    @ApiModelProperty("手续费价格 gas")
    var gas:Int?=null

    var data:String?=null

    var gasLimit:Long?=null

}
