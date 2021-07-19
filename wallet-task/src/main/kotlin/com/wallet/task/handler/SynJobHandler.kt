package com.wallet.task.handler

import com.wallet.biz.handler.service.impl.SynServiceImpl
import com.wallet.biz.rpc.RpcClient
import com.xxl.job.core.biz.model.ReturnT
import com.xxl.job.core.handler.annotation.XxlJob
import org.consenlabs.tokencore.wallet.model.ChainType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


/** 
 * Created by pie on 2020/6/29 01: 49. 
 */
@Component
class SynJobHandler {

    @XxlJob("synTRX")
    fun synTRX(param: String): ReturnT<String> {
        synService.synTRX()
        return ReturnT.SUCCESS
    }

    @XxlJob("synETH")
    fun synETH(param: String): ReturnT<String> {
        synService.synETH()
        return ReturnT.SUCCESS
    }

    @XxlJob("synBTC")
    fun synBTC(param: String): ReturnT<String> {
        synService.synBTCOrFork(rpcClient.omniRpc(), ChainType.BITCOIN)
        return ReturnT.SUCCESS
    }

    @XxlJob("synOMNI")
    fun synOMNI(param: String): ReturnT<String> {
        synService.synOMNI()
        return ReturnT.SUCCESS
    }

    @XxlJob("synDASH")
    fun synDASH(param: String): ReturnT<String> {
        synService.synBTCOrFork(rpcClient.dashRpc(), ChainType.DASH)
        return ReturnT.SUCCESS
    }

    @XxlJob("synLTC")
    fun synLTC(param: String): ReturnT<String> {
        synService.synBTCOrFork(rpcClient.ltcRpc(), ChainType.LITECOIN)
        return ReturnT.SUCCESS
    }

    @XxlJob("synBCH")
    fun synBCH(param: String): ReturnT<String> {
        synService.synBTCOrFork(rpcClient.bchRpc(), ChainType.BITCOINCASH)
        return ReturnT.SUCCESS
    }

    @XxlJob("synBSV")
    fun synBSV(param: String): ReturnT<String> {
        synService.synBTCOrFork(rpcClient.bsvRpc(), ChainType.BITCOINSV)
        return ReturnT.SUCCESS
    }

    @XxlJob("synDOGE")
    fun synDOGE(param: String): ReturnT<String> {
        synService.synBTCOrFork(rpcClient.dogeRpc(), ChainType.DOGECOIN)
        return ReturnT.SUCCESS
    }

    @XxlJob("synEOS")
    fun synEOS(param: String): ReturnT<String> {
        synService.synEOS()
        return ReturnT.SUCCESS
    }

    @XxlJob("synDeposit")
    fun synDeposit(param: String): ReturnT<String> {
        synService.synDeposit()
        return ReturnT.SUCCESS
    }

    @XxlJob("synImportAddress")
    fun synImportAddress(param: String): ReturnT<String> {
        synService.synImportAddress()
        return ReturnT.SUCCESS
    }

    @Autowired
    lateinit var synService: SynServiceImpl
    @Autowired
    lateinit var rpcClient: RpcClient
}
