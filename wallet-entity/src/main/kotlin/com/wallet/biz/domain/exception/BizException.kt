package com.wallet.biz.domain.exception

import com.wallet.biz.domain.dict.ErrorCode

/** 
 * Created by pie on 2020/7/20 03: 04. 
 */
class BizException(val code: Int, val msg: String?) : RuntimeException(msg) {

    constructor(errorCode: ErrorCode) : this(errorCode.code, errorCode.msg)
}

