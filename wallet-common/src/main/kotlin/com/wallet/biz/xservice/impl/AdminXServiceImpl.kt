package com.wallet.biz.xservice.impl

import com.wallet.biz.domain.exception.BizException
import com.wallet.biz.service.*
import com.wallet.biz.xservice.AdminXService
import com.wallet.entity.domain.*
import org.apache.commons.codec.digest.Md5Crypt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/** 
 * Created by pie on 2020/12/7 13: 06. 
 */
@Service
class AdminXServiceImpl : AdminXService {

    override fun getAddrList(): List<Address> {
        return addressService.findAll()
    }

    override fun getSysConfigList(): List<Config> {
        return configService.findAll()
    }

    override fun login(name: String, password: String): String {
        val user = userService.getByName(name) ?: throw BizException(-1, "用户名密码错误")
        val enPass = Md5Crypt.md5Crypt(password.toByteArray(), user.salt, "")

        if (user.password != enPass) throw BizException(-1, "用户名密码错误")
        return user.accessToken
    }

    override fun getWhiteList(): List<White> {
        return whiteService.findAll()
    }

    override fun getAddrAdminList(): List<AddressAdmin> {
        return addressAdminService.findAll()
    }

    override fun getTransactionList(): List<Deposit> {
        return depositService.findAll()
    }

    override fun getWithdrawList(): List<Withdraw> {
        return withdrawService.findAll()
    }

    override fun getBlockHeightList(): List<BlockHeight> {
        return blockHeightService.findAll()
    }


    override fun getTokenList(): List<Token> {
        return walletTokenService.findAll()
    }

    override fun getDashboard(): Map<String, Any> {
        val totalAddress = addressService.findAll().size
        val deL = depositService.findAll()
        val totalDeposit = deL.size
        val totalWithdraw = withdrawService.findAll().size
        val totalTx = totalDeposit + totalWithdraw
        val pieGroup = deL.groupBy { it.chainType }
        val pieLabels = pieGroup.keys
        val pieData = pieGroup.values.map { it.size }
        val pieChart = mapOf("labels" to pieLabels, "data" to pieData)
        val areaGroup=deL.groupBy { "${it.createdAt.year}-${it.createdAt.month}" }
        val areaLabels=areaGroup.keys
        val areaData=areaGroup.values.map { it.size }
        val areaChart = mapOf("labels" to areaLabels, "data" to areaData)

        return mapOf(
            "totalAddress" to totalAddress,
            "totalDeposit" to totalDeposit,
            "totalWithdraw" to totalWithdraw,
            "totalTx" to totalTx,
            "pieChart" to pieChart,
            "areaChart" to areaChart
        )
    }

    override fun addAddrAdmin(type: Int, address: String, chainType: String) {
        val addrAdmin= AddressAdmin()
        if(type!=200){
            val find= Address()
            find.address=address
            val wAddr=addressService.findByBean(find).firstOrNull()?:throw BizException(-1,"添加热钱包或者gas钱包必须是在本钱包有生成过的地址")
            addrAdmin.walletCode=wAddr.walletCode
            addressService.delete(wAddr.id)
        }
        addrAdmin.address=address
        addrAdmin.addressType=type
        addrAdmin.chainType=chainType

        addressAdminService.save(addrAdmin)
    }

    override fun delAddrAdmin(id: Long) {
        addressAdminService.del(id)
    }

    override fun editAddrAdmin(walletAddressAdmin: AddressAdmin) {
        addressAdminService.update(walletAddressAdmin)
    }

    override fun editToken(walletToken: Token) {
        walletTokenService.update(walletToken)
    }

    override fun delToken(id: Long) {
        return walletTokenService.del(id)
    }

    override fun editBlockHeight(walletBlockHeight: BlockHeight) {
        blockHeightService.update(walletBlockHeight)
    }

    override fun delBlockHeight(id: Long) {
        blockHeightService.del(id)
    }

    override fun editConfig(config: Config) {
        configService.update(config)
    }

    override fun delConfig(id: Long) {
        configService.del(id)
    }

    override fun editWhite(white: White) {
        whiteService.update(white)
    }

    override fun delWhite(id: Long) {
        whiteService.del(id)
    }

    @Autowired
    lateinit var addressAdminService: AddressAdminService
    @Autowired
    lateinit var depositService: DepositService
    @Autowired
    lateinit var withdrawService: WithdrawService
    @Autowired
    lateinit var blockHeightService: BlockHeightService
    @Autowired
    lateinit var walletTokenService: TokenService
    @Autowired
    lateinit var addressService: AddressService
    @Autowired
    lateinit var configService: ConfigService
    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var cacheService: com.wallet.biz.cache.CacheService
    @Autowired
    lateinit var whiteService: WhiteService
}

fun main() {

    val a=Md5Crypt.md5Crypt("v423hgv".toByteArray(), "32134", "")

    print(a)
}

