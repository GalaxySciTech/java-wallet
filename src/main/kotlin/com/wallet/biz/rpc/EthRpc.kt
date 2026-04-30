package com.wallet.biz.rpc

import com.wallet.biz.domain.exception.BizException
import org.web3j.protocol.core.JsonRpc2_0Web3j
import org.web3j.protocol.core.Response
import org.web3j.protocol.http.HttpService

/** 
 * Created by pie on 2019-03-20 18: 02. 
 */
class EthRpc(url: String) : JsonRpc2_0Web3j(HttpService(url)) {

    fun <T : Response<*>> handleError(t: T):T{
        if (t.hasError()) throw BizException(t.error.code,t.error.message)
        return t
    }

}
