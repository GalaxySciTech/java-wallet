package com.wallet.biz.domain.dict

class TokenResponse<T>(val code:Int, val msg:String, val data:T?){
    constructor(data:T) : this(200,"success",data)

    constructor():this(200,"success",null)

    constructor(errorCode: ErrorCode):this(errorCode.code,errorCode.msg,null)

}
