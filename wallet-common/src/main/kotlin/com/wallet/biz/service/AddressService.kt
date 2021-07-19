package com.wallet.biz.service

import com.wallet.entity.domain.Address

interface AddressService {
    fun getById(id:Long): Address?

    fun findAll():List<Address>

    fun save(address:Address): Address
    fun findByBean(address: Address): List<Address>
    fun findAllBitcoinFork(): List<Address>
    fun delete(id: Long)
    fun findByType(chainType: String?): List<Address>
    fun saveAll(list: List<Address>): List<Address>
}
