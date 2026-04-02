package com.wallet.task.config

import com.wallet.biz.dict.MqKey
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class RabbitMqConfig {

    @Bean
    open fun deposit(): Queue {
        return Queue(MqKey.DEPOSIT_QUEUE, true)
    }

    @Bean
    open fun topicExchange(): TopicExchange {
        return TopicExchange(MqKey.TOPIC_EXCHANGE)
    }

    @Bean
    open fun binding1(): Binding {
        return BindingBuilder.bind(deposit()).to(topicExchange())
            .with(MqKey.DEPOSIT_KEY)
    }
}
