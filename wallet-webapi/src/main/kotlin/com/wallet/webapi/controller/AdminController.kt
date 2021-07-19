package com.wallet.webapi.controller


import com.wallet.biz.domain.dict.TokenResponse
import com.wallet.biz.xservice.AdminXService
import com.wallet.entity.domain.*
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

/** 
 * Created by pie on 2020/12/7 13: 05. 
 */
@RestController
@Api(description = "管理后台接口")
@RequestMapping("admin")
class AdminController {

    @GetMapping("get_user")
    @ApiOperation("获取用户")
    fun getUser(): TokenResponse<User> {
        val user = request.getAttribute("user") as User
        return TokenResponse(user)
    }

    @GetMapping("get_addr_list")
    @ApiOperation("获取地址清单")
    fun getAddrList(): TokenResponse<List<Address>> {
        val list = adminXService.getAddrList()
        return TokenResponse(list)
    }

    @GetMapping("get_sys_config_list")
    @ApiOperation("获取配置列表")
    fun getSysConfigList(): TokenResponse<List<Config>> {
        val list = adminXService.getSysConfigList()
        return TokenResponse(list)
    }

    @GetMapping("get_white_list")
    @ApiOperation("获取白名单列表")
    fun getWhiteList(): TokenResponse<List<White>> {
        val list = adminXService.getWhiteList()
        return TokenResponse(list)
    }

    @GetMapping("get_address_admin_list")
    @ApiOperation("获取管理员地址列表")
    fun getAddressAdminList(): TokenResponse<List<AddressAdmin>> {
        val list = adminXService.getAddrAdminList()
        return TokenResponse(list)
    }

    @GetMapping("get_transaction_list")
    @ApiOperation("获取充值列表")
    fun getTransactionList(): TokenResponse<List<Deposit>> {
        val list = adminXService.getTransactionList()
        return TokenResponse(list)
    }

    @GetMapping("get_withdraw_list")
    @ApiOperation("获取提现列表")
    fun getWithdrawList(): TokenResponse<List<Withdraw>> {
        val list = adminXService.getWithdrawList()
        return TokenResponse(list)
    }

    @GetMapping("get_block_height_list")
    @ApiOperation("获取区块高度列表")
    fun getBlockHeightList(): TokenResponse<List<BlockHeight>> {
        val list = adminXService.getBlockHeightList()
        return TokenResponse(list)
    }


    @GetMapping("get_token_list")
    @ApiOperation("获取代币列表")
    fun getTokenList(): TokenResponse<List<Token>> {
        val list = adminXService.getTokenList()
        return TokenResponse(list)
    }

    @GetMapping("get_addr_admin_list")
    @ApiOperation("获得管理员地址列表")
    fun getAddrAminList(): TokenResponse<List<AddressAdmin>> {
        val list = adminXService.getAddrAdminList()
        return TokenResponse(list)
    }

    @GetMapping("get_dashboard")
    @ApiOperation("获取桌面信息")
    fun getDashboard(): TokenResponse<Map<String,Any>> {
        val map = adminXService.getDashboard()
        return TokenResponse(map)
    }

    @PostMapping("login")
    @ApiOperation("登陆")
    fun login(name: String, password: String): TokenResponse<Any> {
        val token = adminXService.login(name, password)
        return TokenResponse(token)
    }

    @PostMapping("add_addr_admin")
    @ApiOperation("增加管理员地址")
    fun addAddrAdmin(type:Int,address:String,chainType:String): TokenResponse<Any> {
        adminXService.addAddrAdmin(type,address,chainType)
        return TokenResponse()
    }

    @PostMapping("del_addr_admin")
    @ApiOperation("删除管理员地址")
    fun delAddrAdmin(id:Long): TokenResponse<Any> {
        adminXService.delAddrAdmin(id)
        return TokenResponse()
    }

    @PostMapping("edit_addr_admin")
    @ApiOperation("编辑管理员地址")
    fun editAddrAdmin(walletAddressAdmin: AddressAdmin): TokenResponse<Any> {
        adminXService.editAddrAdmin(walletAddressAdmin)
        return TokenResponse()
    }

    @PostMapping("edit_token")
    @ApiOperation("编辑代币")
    fun editToken(walletToken: Token): TokenResponse<Any> {
        adminXService.editToken(walletToken)
        return TokenResponse()
    }

    @PostMapping("del_token")
    @ApiOperation("删除代币")
    fun delToken(id:Long): TokenResponse<Any> {
        adminXService.delToken(id)
        return TokenResponse()
    }

    @PostMapping("edit_block_height")
    @ApiOperation("编辑区块高度配置")
    fun editBlockHeight(walletBlockHeight: BlockHeight): TokenResponse<Any> {
        adminXService.editBlockHeight(walletBlockHeight)
        return TokenResponse()
    }

    @PostMapping("del_block_height")
    @ApiOperation("删除区块高度配置")
    fun delBlockHeight(id:Long): TokenResponse<Any> {
        adminXService.delBlockHeight(id)
        return TokenResponse()
    }

    @PostMapping("edit_config")
    @ApiOperation("编辑区块高度配置")
    fun editConfig(sysConfig: Config): TokenResponse<Any> {
        adminXService.editConfig(sysConfig)
        return TokenResponse()
    }

    @PostMapping("del_config")
    @ApiOperation("删除系统配置")
    fun delConfig(id:Long): TokenResponse<Any> {
        adminXService.delConfig(id)
        return TokenResponse()
    }

    @PostMapping("edit_white")
    @ApiOperation("编辑白名单")
    fun editWhite(white: White): TokenResponse<Any> {
        adminXService.editWhite(white)
        return TokenResponse()
    }

    @PostMapping("del_white")
    @ApiOperation("删除白名单")
    fun delWhite(id:Long): TokenResponse<Any> {
        adminXService.delWhite(id)
        return TokenResponse()
    }


    @Autowired
    lateinit var adminXService: AdminXService
    @Resource
    lateinit var request: HttpServletRequest
}
