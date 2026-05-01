package com.wallet.biz.domain.po

import org.consenlabs.tokencore.wallet.transaction.BitcoinTransaction
import java.math.BigDecimal
import java.util.ArrayList

/** 
 * Created by pie on 2019-04-16 11: 47. 
 */
class SignUsdtCollectPo{

    var walletId: String? = null

    var toAddress: String? = null

    var amount: BigDecimal? = null

    var fee: BigDecimal? = null

    var utxos: ArrayList<BitcoinTransaction.UTXO>?=null

    var feeProviderUtxos: ArrayList<BitcoinTransaction.UTXO>?=null

    var feeProviderWalletId:String?=null
}