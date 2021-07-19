package com.wallet.biz.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*

/** 
 * Created by pie on 2019-04-29 16: 33. 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class ApiDomain{

    var txrefs:List<Txref>?=null

    var unconfirmed_txrefs:List<UnconfirmedTxrefs>?=null

    @JsonIgnoreProperties(ignoreUnknown = true)
    class Txref{

        var tx_hash:String?=null
//        var block_height:Int?=null
//        var tx_input_n:Int?=null
        var tx_output_n:Int?=null
        var value:Long?=null
//        var ref_balance:Long?=null
//        var spent:String?=null
//        var confirmations:String?=null
//        var confirmed:Date?=null
//        var double_spend:String?=null
        var script:String?=null
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    class UnconfirmedTxrefs{
        var address:String?=null
        var tx_hash:String?=null
        var tx_input_n:Int?=null
        var tx_output_n:Int?=null
        var value:Long?=null
        var script:String?=null
    }
}
