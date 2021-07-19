package com.wallet.webapi.config.filter

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.HandlerExceptionResolver
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/** 
 * Created by pie on 2020/12/7 19: 13. 
 */
@Configuration
open class WebFilter : Filter {

    override fun destroy() {

    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        request as HttpServletRequest
        response as HttpServletResponse
        try {
            chain.doFilter(request, response)
        } catch (e: Exception) { // 异常捕获，发送到error controller
            resolver.resolveException(request, response, null, e);
        }
    }

    override fun init(filterConfig: FilterConfig?) {

    }

    @Autowired
    @Qualifier("handlerExceptionResolver")
    lateinit var resolver: HandlerExceptionResolver
}
