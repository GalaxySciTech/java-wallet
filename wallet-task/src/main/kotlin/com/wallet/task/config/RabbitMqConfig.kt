package com.wallet.task.config

import com.wallet.biz.dict.MqKey
import com.wallet.biz.rpc.EthRpc
import com.wallet.biz.rpc.RpcClient
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Bean
import org.springframework.amqp.core.TopicExchange
import org.springframework.beans.factory.annotation.Autowired
import javax.annotation.PostConstruct


/**
 * Created by pie on 2019/2/22 16: 33.
 */
@Configuration
open class RabbitMqConfig {



    //声明队列
    @Bean
    open fun deposit(): Queue {
        return Queue(MqKey.DEPOSIT_QUEUE, true) // true表示持久化该队列
    }

    //声明交互器
    @Bean
    open fun topicExchange(): TopicExchange {
        return TopicExchange(MqKey.TOPIC_EXCHANGE)
    }

    @Bean
    open fun binding1(): Binding {
        return BindingBuilder.bind(deposit()).to(topicExchange())
            .with(MqKey.DEPOSIT_KEY)
    }

//    @Bean
//    open fun binding2(): Binding {
//        return BindingBuilder.bind(Queue("qq2")).to(TopicExchange(MqKey.TOPIC_EXCHANGE))
//            .with("foo.aa.#")
//    }

    @Autowired
    lateinit var rpcClient: RpcClient
}
