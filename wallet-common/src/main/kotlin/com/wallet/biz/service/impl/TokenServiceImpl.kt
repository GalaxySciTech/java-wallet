package com.wallet.biz.service.impl

import com.wallet.biz.service.TokenService
import com.wallet.biz.utils.BasicUtils
import com.wallet.entity.domain.QToken
import com.wallet.entity.domain.Token
import com.wallet.repository.TokenRepository
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

@Service
class TokenServiceImpl: TokenService {

    override fun getByBean(token: Token): List<Token> {
        var pre = QToken.token.id.isNotNull
        if (token.tokenSymbol != null)
            pre = pre.and(QToken.token.tokenSymbol.eq(token.tokenSymbol))
        if (token.chainType != null)
            pre = pre.and(QToken.token.chainType.eq(token.chainType))
        return tokenRepository.findAll(pre).toList()
    }

    override fun findAll(): List<Token> {
        return tokenRepository.findAll()
    }

    override fun update(token: Token) {
        val e=tokenRepository.findOne(token.id)
        BasicUtils.copyPropertiesIgnoreNull(token,e)
        tokenRepository.save(e)
    }

    override fun del(id: Long) {
        return tokenRepository.delete(id)
    }

    override fun getById(id:Long): Token? {
        return tokenRepository.findOne(id)
    }

    override fun save(token:Token): Token {
        return tokenRepository.saveAndFlush(token)
    }

    @Autowired lateinit var tokenRepository: TokenRepository
}
