package com.wallet

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties

@SpringBootApplication(exclude=[DataSourceAutoConfiguration::class,HibernateJpaAutoConfiguration::class])
@EnableConfigurationProperties
open class HsmApplication

fun main() {
    SpringApplication.run(HsmApplication::class.java)
}