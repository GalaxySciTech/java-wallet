package com.wallet.biz.domain.vo

import com.wallet.entity.domain.AddressAdmin
import org.consenlabs.tokencore.wallet.transaction.BitcoinTransaction
import java.math.BigDecimal

/** 
 * Created by pie on 2020/3/19 15: 34. 
 */
class WalletAddressAdminVo : AddressAdmin() {

    var utxos:ArrayList<BitcoinTransaction.UTXO>?=null

    var btcFee:BigDecimal?=null

    var ethGasPrice:Int?=null

    var ethGasLimit:Long?=null


}