package com.wallet.biz.service.impl

import com.wallet.biz.service.AddressAdminService
import com.wallet.biz.utils.BasicUtils
import com.wallet.entity.domain.AddressAdmin
import com.wallet.entity.domain.QAddressAdmin
import com.wallet.repository.AddressAdminRepository
import org.consenlabs.tokencore.wallet.model.ChainType
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

@Service
class AddressAdminServiceImpl: AddressAdminService {

    override fun findAll(): List<AddressAdmin> {
        return addressAdminRepository.findAll().toList()
    }

    override fun findAllBitcoinFork(): List<AddressAdmin> {
        var pre = QAddressAdmin.addressAdmin.id.isNotNull
        pre = pre.and(
                QAddressAdmin.addressAdmin.chainType.eq(
                        ChainType.BITCOIN
                ).or(
                        QAddressAdmin.addressAdmin.chainType.eq(ChainType.BITCOINCASH)
                ).or(
                        QAddressAdmin.addressAdmin.chainType.eq(ChainType.BITCOINSV)
                ).or(
                        QAddressAdmin.addressAdmin.chainType.eq(ChainType.DASH)
                ).or(
                        QAddressAdmin.addressAdmin.chainType.eq(ChainType.DOGECOIN)
                ).or(
                        QAddressAdmin.addressAdmin.chainType.eq(ChainType.LITECOIN)
                )
        )
        return addressAdminRepository.findAll(pre).toList()
    }

    override fun del(id: Long) {
        addressAdminRepository.delete(id)
    }

    override fun update(addressAdmin: AddressAdmin) {
        val e = addressAdminRepository.findOne(addressAdmin.id)
        BasicUtils.copyPropertiesIgnoreNull(addressAdmin, e)
        addressAdminRepository.save(e)
    }

    override fun getById(id:Long): AddressAdmin? {
        return addressAdminRepository.findOne(id)
    }

    override fun save(addressAdmin:AddressAdmin): AddressAdmin {
        return addressAdminRepository.saveAndFlush(addressAdmin)
    }

    @Autowired lateinit var addressAdminRepository: AddressAdminRepository
}
