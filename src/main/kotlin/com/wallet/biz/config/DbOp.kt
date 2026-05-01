package com.wallet.biz.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class DbOp {

    fun select(sql: String, vararg args: Any): List<Map<String, Any>> {
        return if (args.isEmpty()) {
            jdbcTemplate.queryForList(sql)
        } else {
            jdbcTemplate.queryForList(sql, *args)
        }
    }

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate
}
