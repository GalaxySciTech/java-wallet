package com.wallet.biz.domain

import com.wallet.biz.env.Environment
import java.util.*

data class AdminUserToken (
    var access_token: String,
    var refresh_token: String,
    var openid: String? = null,
    var access_expires_in: Long = Date().time + Environment.ADMIN_ACCESS_TOKEN_EXPIRE_SECONDS*1000,
    var refresh_expires_in: Long = Date().time + Environment.ADMIN_REFRESH_TOKEN_EXPIRE_SECONDS*1000)
