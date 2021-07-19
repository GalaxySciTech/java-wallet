package com.wallet.biz.domain

import com.fasterxml.jackson.databind.ObjectMapper


/**
 * Created by richardchen on 10/2/17.
 */

/**
 * Usage: 通过code换取网页授权access_token和微信用户openid
 *
 * 请求方法：  https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
 *
 */
class UserToken {

    var wx_access_token: String? = null
    var wx_refresh_token: String? = null

    var app_access_token: String? = null
    var app_refresh_token: String? = null

    var h5_access_token: String? = null
    var h5_refresh_token: String? = null

    var expires_in: Long? = null
    var openid: String? = null
    var scope: String? = null

    companion object {
        @Throws(Exception::class)
        fun toObject(jsonString: String): UserToken {
            return obj.readValue(jsonString, UserToken::class.java)
        }
        val obj=ObjectMapper()
    }

}
