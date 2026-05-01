package com.wallet.biz.domain

import com.wallet.biz.dict.Oauth2TokenPrefixType


/**
 * Created by zjk on 2018/1/31
 */
class AccessTokenBean(
        val accessToken: String? = null,
        val oauth2TokenPrefixType: Oauth2TokenPrefixType? = null
)