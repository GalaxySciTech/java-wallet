package com.wallet.biz.domain.po

import io.swagger.v3.oas.annotations.media.Schema

/** 
 * Created by pie on 2019-03-08 17: 17. 
 */
@Schema
class RegisteredPo{

    @Schema(description = "手机号(即用户名)", requiredMode = Schema.RequiredMode.REQUIRED)
    var customerName:String?=null

    @Schema(description = "登录密码", requiredMode = Schema.RequiredMode.REQUIRED)
    var password:String?=null

    @Schema(description = "sms验证码", requiredMode = Schema.RequiredMode.REQUIRED)
    var smsCode:Int?=null

    @Schema(description = "邀请码", requiredMode = Schema.RequiredMode.REQUIRED)
    var inviteCode:String?=null

    @Schema(description = "交易密码", requiredMode = Schema.RequiredMode.REQUIRED)
    var tradePassword:String?=null

}