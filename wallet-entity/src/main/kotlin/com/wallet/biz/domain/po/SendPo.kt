package com.wallet.biz.domain.po

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal

/** 
 * Created by pie on 2019-04-12 09: 50. 
 */
@Schema
class SendPo{

    @Schema(description = "发送者地址")
    var from:String?=null
    @Schema(description = "发送者地址code")
    var fromWalletCode:String?=null

    @Schema(description = "接收者地址")
    var to:String?=null
    @Schema(description = "数量")
    var amount:BigDecimal?=null
    @Schema(description = "币种类型  例: ETHEREUM")
    var chain:String?=null
    @Schema(description = "代币类型 例: USDT")
    var symbol :String?=null
    @Schema(description = "手续费价格 gas")
    var gas:Int?=null

    var data:String?=null

    var gasLimit:Long?=null

}
