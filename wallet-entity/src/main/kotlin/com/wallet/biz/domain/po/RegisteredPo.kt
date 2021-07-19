package com.wallet.biz.domain.po

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/** 
 * Created by pie on 2019-03-08 17: 17. 
 */
@ApiModel
class RegisteredPo{

    @ApiModelProperty("手机号(即用户名)",required = true)
    var customerName:String?=null

    @ApiModelProperty("登录密码",required = true)
    var password:String?=null

    @ApiModelProperty("sms验证码",required = true)
    var smsCode:Int?=null

    @ApiModelProperty("邀请码",required = true)
    var inviteCode:String?=null

    @ApiModelProperty("交易密码",required = true)
    var tradePassword:String?=null

}