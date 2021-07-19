package com.wallet.test

import com.wallet.TaskApplication
import com.wallet.biz.dict.SysConfigKey
import com.wallet.biz.rpc.RpcClient
import com.wallet.biz.rpc.TrxApi
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal

/** 
 * Created by pie on 2019-04-12 16: 06. 
 */
@RunWith(SpringRunner::class)
@SpringBootTest(classes = [TaskApplication::class])
class Test {



    @Autowired
    lateinit var restTemplate: RestTemplate

}
