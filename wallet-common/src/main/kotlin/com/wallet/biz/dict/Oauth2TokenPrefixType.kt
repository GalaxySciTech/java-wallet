package com.wallet.biz.dict

/**
 * @Author: LvJH
 * @Description:
 * @Data: Created in 2017/12/4 下午1:25
 * @Version: 1.0
 */
enum class Oauth2TokenPrefixType(val prefix: String, val requestHeaderCode:String) {
    WECHAT_USER     ("wechat", "wx_oauth_code"),
    ADMIN_USER      ("h5",  "TODO"),
    APP_USER        ("app",    "app_access_token"),
    H5_USER         ("h5",     "h5_access_token")
}