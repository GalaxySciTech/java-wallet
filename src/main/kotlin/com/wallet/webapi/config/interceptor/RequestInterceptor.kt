package com.wallet.webapi.config.interceptor

import com.wallet.biz.domain.PageEntity
import com.wallet.biz.domain.dict.ErrorCode
import com.wallet.biz.domain.exception.BizException
import com.wallet.biz.service.UserService
import com.wallet.biz.service.WhiteService
import com.wallet.entity.domain.White
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
open class RequestInterceptor : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val requestURI = request.requestURI
        if (requestURI.contains("swagger")
            || requestURI.contains("api-docs")
            || requestURI.contains("/error")
            || requestURI.contains("ops")
            || requestURI.contains("admin/login")
        ) {
            return true
        }

        val ip = request.getHeader("X-Real-IP") ?: request.remoteAddr
        val find = PageEntity(White())
        find.entity.ip = ip
        val list = whiteService.findByEntity(find).toList()
        if (list.isEmpty()) throw BizException(ErrorCode.NOT_IN_WHITE_LIST)
        val token = request.getHeader("access_token") ?: throw BizException(-100, "access_token为空 权限不足")
        val u = userService.getByToken(token) ?: throw BizException(-100, "access_token无效 权限不足")
        request.setAttribute("user", u)

        return true
    }

    @Autowired
    lateinit var whiteService: WhiteService

    @Autowired
    lateinit var userService: UserService
}
