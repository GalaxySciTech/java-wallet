package com.wallet.biz.domain.po

import io.eblock.eos4j.api.vo.SignParam
import java.math.BigDecimal

/** 
 * Created by pie on 2019-03-05 13: 24. 
 */
class SignEosPo {

    var fromAddress: String? = null

    var toAddress: String? = null

    var amount: BigDecimal? = null

    var signParam: SignParam? = null

    var walletId: String? = null

}