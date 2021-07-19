package com.wallet.task.handler

import com.wallet.biz.handler.service.SendFeeService
import com.xxl.job.core.biz.model.ReturnT
import com.xxl.job.core.handler.annotation.XxlJob
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/** 
 * Created by pie on 2020/6/29 16: 18. 
 */
@Component
class SendFeeJobHandler {

    @XxlJob("sendFeeETH")
    fun sendFeeETH(param: String): ReturnT<String> {
        sendFeeService.sendFeeETH()
        return ReturnT.SUCCESS
    }

    @XxlJob("sendFeeTRX")
    fun sendFeeTRX(param: String): ReturnT<String> {
        sendFeeService.sendFeeTRX()
        return ReturnT.SUCCESS
    }

    @Autowired
    lateinit var sendFeeService: SendFeeService

}
