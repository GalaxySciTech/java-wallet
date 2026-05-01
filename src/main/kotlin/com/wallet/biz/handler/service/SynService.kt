package com.wallet.biz.handler.service

import wf.bitcoin.javabitcoindrpcclient.BitcoinJSONRPCClient

/** 
 * Created by pie on 2020/6/29 16: 21. 
 */
interface SynService {

    fun synOMNI()

    fun synETH()

    fun synDeposit()

    fun synBTCOrFork(rpc: BitcoinJSONRPCClient, chainType: String)

    fun synEOS()

    fun synTRX()

    fun synImportAddress()
}
