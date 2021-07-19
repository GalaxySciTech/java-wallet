package com.wallet.biz.domain.vo

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/** 
 * Created by pie on 2019-03-05 11: 55. 
 */
@ApiModel
class CustomerInfoVo {

    @ApiModelProperty("用户名")
    var customerName: String? = null

    @ApiModelProperty("vip等级")
    var vipLevel:Int?=null



}