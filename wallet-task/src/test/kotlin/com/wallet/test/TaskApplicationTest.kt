package com.wallet.test

import com.wallet.TaskApplication
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [TaskApplication::class])
class TaskApplicationTest {

    @Test
    fun contextLoads() {
    }
}
