package com.wallet.biz.domain.po

import io.eblock.eos4j.api.vo.SignParam
import org.consenlabs.tokencore.wallet.transaction.BitcoinTransaction
import java.math.BigDecimal
import java.util.ArrayList

/** 
 * Created by pie on 2019-04-13 16: 08. 
 */
class SignUsdtPo{

    var walletId: String? = null

    var toAddress: String? = null

    var amount: BigDecimal? = null

    var fee: BigDecimal? = null

    var utxos: ArrayList<BitcoinTransaction.UTXO>?=null

}