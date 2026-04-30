package com.wallet.webapi.controller

import com.wallet.biz.domain.dict.TokenResponse
import com.wallet.biz.service.ConfigService
import com.wallet.biz.dict.SysConfigKey
import com.wallet.biz.xservice.AdminXService
import com.wallet.entity.domain.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.annotation.Resource
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Admin API", description = "管理后台接口")
@RequestMapping("admin")
class AdminController {

    @GetMapping("config/rpc")
    @Operation(summary = "RPC配置")
    fun getRpcConfig(): TokenResponse<Map<String, String>> {
        val keys = listOf(SysConfigKey.ETH_RPC_URL, SysConfigKey.OMNI_RPC_URL, SysConfigKey.BCH_RPC_URL, SysConfigKey.TRX_API_URL)
        return TokenResponse(keys.associate { it.name to maskSensitive(it.name, findConfig(it.name)) })
    }

    @PutMapping("config/rpc")
    @Operation(summary = "更新RPC配置")
    fun putRpcConfig(key: String, value: String): TokenResponse<Any> {
        saveConfigByKey(key, value)
        return TokenResponse()
    }

    @GetMapping("config/scheduler")
    @Operation(summary = "调度配置")
    fun getSchedulerConfig(): TokenResponse<Map<String, String>> {
        val keys = SysConfigKey.values().filter { it.name.startsWith("SCHEDULER_") }
        return TokenResponse(keys.associate { it.name to findConfig(it.name) })
    }

    @PutMapping("config/scheduler")
    @Operation(summary = "更新调度配置")
    fun putSchedulerConfig(key: String, value: String): TokenResponse<Any> {
        saveConfigByKey(key, value)
        return TokenResponse()
    }

    @GetMapping("get_user")
    @Operation(summary = "获取用户")
    fun getUser(): TokenResponse<User> {
        val user = request.getAttribute("user") as User
        return TokenResponse(user)
    }

    @GetMapping("get_addr_list")
    @Operation(summary = "获取地址清单")
    fun getAddrList(): TokenResponse<List<Address>> {
        val list = adminXService.getAddrList()
        return TokenResponse(list)
    }

    @GetMapping("get_sys_config_list")
    @Operation(summary = "获取配置列表")
    fun getSysConfigList(): TokenResponse<List<Config>> {
        val list = adminXService.getSysConfigList()
        return TokenResponse(list)
    }

    @GetMapping("get_white_list")
    @Operation(summary = "获取白名单列表")
    fun getWhiteList(): TokenResponse<List<White>> {
        val list = adminXService.getWhiteList()
        return TokenResponse(list)
    }

    @GetMapping("get_address_admin_list")
    @Operation(summary = "获取管理员地址列表")
    fun getAddressAdminList(): TokenResponse<List<AddressAdmin>> {
        val list = adminXService.getAddrAdminList()
        return TokenResponse(list)
    }

    @GetMapping("get_transaction_list")
    @Operation(summary = "获取充值列表")
    fun getTransactionList(): TokenResponse<List<Deposit>> {
        val list = adminXService.getTransactionList()
        return TokenResponse(list)
    }

    @GetMapping("get_withdraw_list")
    @Operation(summary = "获取提现列表")
    fun getWithdrawList(): TokenResponse<List<Withdraw>> {
        val list = adminXService.getWithdrawList()
        return TokenResponse(list)
    }

    @GetMapping("get_block_height_list")
    @Operation(summary = "获取区块高度列表")
    fun getBlockHeightList(): TokenResponse<List<BlockHeight>> {
        val list = adminXService.getBlockHeightList()
        return TokenResponse(list)
    }

    @GetMapping("get_token_list")
    @Operation(summary = "获取代币列表")
    fun getTokenList(): TokenResponse<List<Token>> {
        val list = adminXService.getTokenList()
        return TokenResponse(list)
    }

    @GetMapping("get_dashboard")
    @Operation(summary = "获取桌面信息")
    fun getDashboard(): TokenResponse<Map<String, Any>> {
        val map = adminXService.getDashboard()
        return TokenResponse(map)
    }

    @PostMapping("login")
    @Operation(summary = "登陆")
    fun login(name: String, password: String): TokenResponse<Any> {
        val token = adminXService.login(name, password)
        return TokenResponse(token)
    }

    @PostMapping("add_addr_admin")
    @Operation(summary = "增加管理员地址")
    fun addAddrAdmin(type: Int, address: String, chainType: String): TokenResponse<Any> {
        adminXService.addAddrAdmin(type, address, chainType)
        return TokenResponse()
    }

    @PostMapping("del_addr_admin")
    @Operation(summary = "删除管理员地址")
    fun delAddrAdmin(id: Long): TokenResponse<Any> {
        adminXService.delAddrAdmin(id)
        return TokenResponse()
    }

    @PostMapping("edit_addr_admin")
    @Operation(summary = "编辑管理员地址")
    fun editAddrAdmin(walletAddressAdmin: AddressAdmin): TokenResponse<Any> {
        adminXService.editAddrAdmin(walletAddressAdmin)
        return TokenResponse()
    }

    @PostMapping("edit_token")
    @Operation(summary = "编辑代币")
    fun editToken(walletToken: Token): TokenResponse<Any> {
        adminXService.editToken(walletToken)
        return TokenResponse()
    }

    @PostMapping("del_token")
    @Operation(summary = "删除代币")
    fun delToken(id: Long): TokenResponse<Any> {
        adminXService.delToken(id)
        return TokenResponse()
    }

    @PostMapping("edit_block_height")
    @Operation(summary = "编辑区块高度配置")
    fun editBlockHeight(walletBlockHeight: BlockHeight): TokenResponse<Any> {
        adminXService.editBlockHeight(walletBlockHeight)
        return TokenResponse()
    }

    @PostMapping("del_block_height")
    @Operation(summary = "删除区块高度配置")
    fun delBlockHeight(id: Long): TokenResponse<Any> {
        adminXService.delBlockHeight(id)
        return TokenResponse()
    }

    @PostMapping("edit_config")
    @Operation(summary = "编辑系统配置")
    fun editConfig(sysConfig: Config): TokenResponse<Any> {
        adminXService.editConfig(sysConfig)
        return TokenResponse()
    }

    @PostMapping("del_config")
    @Operation(summary = "删除系统配置")
    fun delConfig(id: Long): TokenResponse<Any> {
        adminXService.delConfig(id)
        return TokenResponse()
    }

    @PostMapping("edit_white")
    @Operation(summary = "编辑白名单")
    fun editWhite(white: White): TokenResponse<Any> {
        adminXService.editWhite(white)
        return TokenResponse()
    }

    @PostMapping("del_white")
    @Operation(summary = "删除白名单")
    fun delWhite(id: Long): TokenResponse<Any> {
        adminXService.delWhite(id)
        return TokenResponse()
    }

    @Autowired
    lateinit var adminXService: AdminXService

    @Resource
    lateinit var request: HttpServletRequest

    @Autowired
    lateinit var configService: ConfigService

    private fun findConfig(key: String): String = configService.findAll().firstOrNull { it.configKey == key }?.configValue ?: ""

    private fun saveConfigByKey(key: String, value: String) {
        val item = configService.findAll().firstOrNull { it.configKey == key } ?: Config().also { it.configKey = key }
        if (value.isBlank()) return
        item.configValue = value
        configService.save(item)
    }

    private fun maskSensitive(key: String, value: String): String {
        if (!key.contains("PASSWORD") && !key.contains("KEY")) return value
        if (value.length <= 8) return "****"
        return value.take(4) + "****" + value.takeLast(4)
    }
}
