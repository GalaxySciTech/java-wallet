package com.wallet.biz.service

import com.wallet.entity.domain.AddressAdmin

interface AddressAdminService {
    fun getById(id:Long): AddressAdmin?

    fun save(addressAdmin:AddressAdmin): AddressAdmin
    fun findAll(): List<AddressAdmin>
    fun findAllBitcoinFork(): List<AddressAdmin>
    fun del(id: Long)
    fun update(addressAdmin: AddressAdmin)
}
