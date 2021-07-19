package com.wallet.biz.service.impl

import com.wallet.biz.service.UserService
import com.wallet.entity.domain.QUser
import com.wallet.entity.domain.User
import com.wallet.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

@Service
class UserServiceImpl: UserService {

    override fun getById(id:Long): User? {
        return userRepository.findOne(id)
    }

    override fun save(user: User): User {
        return userRepository.saveAndFlush(user)
    }

    override fun getByName(name: String): User? {
        val pre= QUser.user.name.eq(name)
        return userRepository.findAll(pre).firstOrNull()
    }

    override fun getByToken(token: String): User? {
        val pre= QUser.user.accessToken.eq(token)
        return userRepository.findAll(pre).firstOrNull()
    }

    @Autowired lateinit var userRepository: UserRepository
}
