package com.wallet.test

import com.wallet.WebApiApplication
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [WebApiApplication::class])
class WebApiApplicationTest {

    @Test
    fun contextLoads() {
    }
}
