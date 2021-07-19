package com.wallet.biz.domain.po

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel
class LoginPo{
    @ApiModelProperty("用户名")
    var customerName:String?=null
    @ApiModelProperty("密码")
    var password:String?=null
}