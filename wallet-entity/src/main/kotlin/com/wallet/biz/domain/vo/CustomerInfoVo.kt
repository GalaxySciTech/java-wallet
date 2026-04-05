package com.wallet.biz.domain.vo

import io.swagger.v3.oas.annotations.media.Schema

/** 
 * Created by pie on 2019-03-05 11: 55. 
 */
@Schema
class CustomerInfoVo {

    @Schema(description = "用户名")
    var customerName: String? = null

    @Schema(description = "vip等级")
    var vipLevel:Int?=null



}