package com.wallet.biz.service.impl

import com.wallet.biz.service.AddressService
import com.wallet.entity.domain.Address
import com.wallet.entity.domain.QAddress
import com.wallet.repository.AddressRepository
import org.consenlabs.tokencore.wallet.model.ChainType
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

@Service
class AddressServiceImpl: AddressService {

    override fun delete(id: Long) {
        addressRepository.delete(id)
    }

    override fun findAllBitcoinFork(): List<Address> {
        var pre = QAddress.address1.id.isNotNull
        pre = pre.and(
                QAddress.address1.chainType.eq(
                        ChainType.BITCOIN
                ).or(
                        QAddress.address1.chainType.eq(ChainType.BITCOINCASH)
                ).or(
                        QAddress.address1.chainType.eq(ChainType.BITCOINSV)
                ).or(
                        QAddress.address1.chainType.eq(ChainType.DASH)
                ).or(
                        QAddress.address1.chainType.eq(ChainType.DOGECOIN)
                ).or(
                        QAddress.address1.chainType.eq(ChainType.LITECOIN)
                )
        )
        return addressRepository.findAll(pre).toList()
    }

    override fun findByBean(address: Address): List<Address> {
        var pre = QAddress.address1.id.isNotNull
        if (address.address != null)
            pre = pre.and(QAddress.address1.address.eq(address.address))
        if (address.chainType != null)
            pre = pre.and(QAddress.address1.chainType.eq(address.chainType))
        if (address.walletCode != null)
            pre = pre.and(QAddress.address1.walletCode.eq(address.walletCode))
        return addressRepository.findAll(pre).toList()
    }

    override fun saveAll(list: List<Address>): List<Address> {
        return addressRepository.save(list)
    }

    override fun findByType(chainType: String?): List<Address> {
        var pre = QAddress.address1.id.isNotNull
        if (chainType != null)
            pre = pre.and(QAddress.address1.chainType.eq(chainType))
        return addressRepository.findAll(pre).toList()
    }

    override fun findAll(): List<Address> {
        return addressRepository.findAll().toList()
    }

    override fun getById(id:Long): Address? {
        return addressRepository.findOne(id)
    }

    override fun save(address:Address): Address {
        return addressRepository.saveAndFlush(address)
    }

    @Autowired lateinit var addressRepository: AddressRepository
}
