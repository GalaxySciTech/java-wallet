package com.wallet.biz.handler.service

import wf.bitcoin.javabitcoindrpcclient.BitcoinJSONRPCClient
import java.math.BigDecimal

/** 
 * Created by pie on 2020/6/29 16: 21. 
 */
interface CollectService {

    fun collectOMNI()

    fun collectETH()

    fun collectERC()

    fun collectTRX()

    fun collectTRC()

    fun collectBTCOrFork(rpc: BitcoinJSONRPCClient, chainType: String)

    fun collectBCHOrFork(rpc: BitcoinJSONRPCClient, chainType: String)

    fun scanAllCoin()

}
