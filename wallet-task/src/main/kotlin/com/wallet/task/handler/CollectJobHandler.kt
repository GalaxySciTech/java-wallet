package com.wallet.task.handler

import com.wallet.biz.handler.service.impl.CollectServiceImpl
import com.wallet.biz.rpc.RpcClient
import com.xxl.job.core.biz.model.ReturnT
import com.xxl.job.core.handler.annotation.XxlJob
import org.consenlabs.tokencore.wallet.model.ChainType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/** 
 * Created by pie on 2020/6/29 01: 53. 
 */
@Component
class CollectJobHandler {

    @XxlJob("collectTRX")
    fun collectTRX(param: String): ReturnT<String> {
        collectService.collectTRX()
        return ReturnT.SUCCESS
    }

    @XxlJob("collectTRC")
    fun collectTRC(param: String): ReturnT<String> {
        collectService.collectTRC()
        return ReturnT.SUCCESS
    }

    @XxlJob("collectOMNI")
    fun collectOMNI(param: String): ReturnT<String> {
        collectService.collectOMNI()
        return ReturnT.SUCCESS
    }

    @XxlJob("collectETH")
    fun collectETH(param: String): ReturnT<String> {
        collectService.collectETH()
        return ReturnT.SUCCESS
    }

    @XxlJob("collectERC")
    fun collectERC(param: String): ReturnT<String> {
        collectService.collectERC()
        return ReturnT.SUCCESS
    }

    @XxlJob("collectBTC")
    fun collectBTC(param: String): ReturnT<String> {
        collectService.collectBTCOrFork(rpcClient.omniRpc(),ChainType.BITCOIN)
        return ReturnT.SUCCESS
    }

    @XxlJob("collectDASH")
    fun collectDASH(param: String): ReturnT<String> {
        collectService.collectBTCOrFork(rpcClient.dashRpc(),ChainType.DASH)
        return ReturnT.SUCCESS
    }

    @XxlJob("collectLTC")
    fun collectLTC(param: String): ReturnT<String> {
        collectService.collectBTCOrFork(rpcClient.ltcRpc(),ChainType.LITECOIN)
        return ReturnT.SUCCESS
    }

    @XxlJob("collectBCH")
    fun collectBCH(param: String): ReturnT<String> {
        collectService.collectBCHOrFork(rpcClient.bchRpc(),ChainType.BITCOINCASH)
        return ReturnT.SUCCESS
    }

    @XxlJob("collectBSV")
    fun collectBSV(param: String): ReturnT<String> {
        collectService.collectBCHOrFork(rpcClient.bsvRpc(),ChainType.BITCOINSV)
        return ReturnT.SUCCESS
    }

    @XxlJob("collectDOGE")
    fun collectDOGE(param: String): ReturnT<String> {
        collectService.collectBTCOrFork(rpcClient.dogeRpc(),ChainType.DOGECOIN)
        return ReturnT.SUCCESS
    }

    @XxlJob("scanAllCoin")
    fun scanAllCoin(param: String): ReturnT<String> {
        collectService.scanAllCoin()
        return ReturnT.SUCCESS
    }

    @Autowired
    lateinit var collectService: CollectServiceImpl
    @Autowired
    lateinit var rpcClient: RpcClient
}
