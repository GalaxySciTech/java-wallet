package com.wallet.biz.domain.vo

import io.swagger.v3.oas.annotations.media.Schema

/** 
 * Created by pie on 2019-03-08 17: 00. 
 */
@Schema
class DepositEosVo{

    var depositAddress:String?=null

    var memo:String?=null

}