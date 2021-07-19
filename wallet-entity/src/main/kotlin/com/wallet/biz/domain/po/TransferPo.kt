package com.wallet.biz.domain.po

import io.swagger.annotations.ApiModel
import java.math.BigDecimal

/** 
 * Created by pie on 2019-03-08 17: 03. 
 */
@ApiModel
class TransferPo{

    var chainType:String?=null

    var fee:BigDecimal?=null

    var value:BigDecimal?=null

    var to:String?=null

}