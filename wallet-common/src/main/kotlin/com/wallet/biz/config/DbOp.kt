package com.wallet.biz.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component


/** 
 * Created by pie on 2020/11/1 16: 13. 
 */
@Component
class DbOp {

    fun select(sql: String): List<Map<String, Any>> {
        return jdbcTemplate.queryForList(sql)
    }

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate


}
