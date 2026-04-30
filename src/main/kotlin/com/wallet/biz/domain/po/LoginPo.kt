package com.wallet.biz.domain.po

import io.swagger.v3.oas.annotations.media.Schema

@Schema
class LoginPo{
    @Schema(description = "用户名")
    var customerName:String?=null
    @Schema(description = "密码")
    var password:String?=null
}