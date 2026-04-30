package com.wallet.webapi.controller

import com.wallet.biz.domain.dict.TokenResponse
import com.wallet.biz.service.ConfigService
import com.wallet.biz.dict.SysConfigKey
import com.wallet.biz.xservice.AdminXService
import com.wallet.entity.domain.*
import com.wallet.entity.domain.WalletAdminAuditLog
import com.wallet.biz.domain.exception.BizException
import com.wallet.biz.domain.dict.ErrorCode
import com.wallet.repository.WalletAdminAuditLogRepository
import com.wallet.repository.WalletRpcConfigRepository
import com.wallet.repository.WalletSchedulerConfigRepository
import com.wallet.repository.WalletChainConfigRepository
import com.wallet.repository.WalletFeeSupplyConfigRepository
import com.wallet.repository.WalletSecurityConfigRepository
import com.wallet.repository.WalletSweepConfigRepository
import com.wallet.repository.WalletWithdrawConfigRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.annotation.Resource
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Admin API", description = "管理后台接口")
@RequestMapping("admin")
class AdminController {
    private val auditLogs = java.util.Collections.synchronizedList(mutableListOf<Map<String, String>>())

    @GetMapping("config/rpc")
    @Operation(summary = "RPC配置")
    fun getRpcConfig(): TokenResponse<Map<String, String>> {
        val map = walletRpcConfigRepository.findAll().associate { cfg ->
            cfg.chain to maskSensitive("${cfg.chain}_RPC_URL", cfg.rpcUrl ?: "")
        }
        return TokenResponse(map)
    }

    @GetMapping("config/rpc/{chain}")
    @Operation(summary = "按链获取RPC配置")
    fun getRpcConfigByChain(@PathVariable chain: String): TokenResponse<Map<String, String>> {
        val cfg = walletRpcConfigRepository.findByChain(chain)
        return TokenResponse(mapOf(
            "chain" to chain,
            "rpcUrl" to maskSensitive("RPC_URL", cfg?.rpcUrl ?: ""),
            "rpcUsername" to (cfg?.rpcUsername ?: ""),
            "rpcPassword" to maskSensitive("RPC_PASSWORD", cfg?.rpcPassword ?: ""),
            "rpcApiKey" to maskSensitive("RPC_API_KEY", cfg?.rpcApiKey ?: "")
        ))
    }

    @PutMapping("config/rpc")
    @Operation(summary = "更新RPC配置")
    fun putRpcConfig(@RequestParam chain: String, @RequestParam rpcUrl: String): TokenResponse<Any> {
        val sec = walletSecurityConfigRepository.findAll().firstOrNull()
        if (sec != null && sec.allowUpdateRpcByAdmin == false) {
            throw BizException(ErrorCode.ERROR_PARAM.code, "rpc update forbidden by security policy")
        }
        val item = walletRpcConfigRepository.findByChain(chain) ?: WalletRpcConfig().also { it.chain = chain }
        item.rpcUrl = rpcUrl
        walletRpcConfigRepository.save(item)
        audit("UPDATE_RPC_CONFIG", chain, rpcUrl)
        return TokenResponse()
    }

    @GetMapping("config/scheduler")
    @Operation(summary = "调度配置")
    fun getSchedulerConfig(): TokenResponse<Map<String, String>> {
        val map = walletSchedulerConfigRepository.findAll().associate { cfg ->
            cfg.chain to "deposit:${cfg.depositScanEnabled}/${cfg.depositScanIntervalMs},sweep:${cfg.sweepEnabled}/${cfg.sweepIntervalMs},fee:${cfg.feeSupplyEnabled}/${cfg.feeSupplyIntervalMs}"
        }
        return TokenResponse(map)
    }

    @GetMapping("config/scheduler/{chain}")
    @Operation(summary = "按链获取调度配置")
    fun getSchedulerConfigByChain(@PathVariable chain: String): TokenResponse<Map<String, String>> {
        val cfg = walletSchedulerConfigRepository.findByChain(chain)
        return TokenResponse(mapOf(
            "chain" to chain,
            "depositScanEnabled" to (cfg?.depositScanEnabled?.toString() ?: "false"),
            "depositScanIntervalMs" to (cfg?.depositScanIntervalMs?.toString() ?: "0"),
            "sweepEnabled" to (cfg?.sweepEnabled?.toString() ?: "false"),
            "sweepIntervalMs" to (cfg?.sweepIntervalMs?.toString() ?: "0"),
            "feeSupplyEnabled" to (cfg?.feeSupplyEnabled?.toString() ?: "false"),
            "feeSupplyIntervalMs" to (cfg?.feeSupplyIntervalMs?.toString() ?: "0")
        ))
    }

    @PutMapping("config/scheduler")
    @Operation(summary = "更新调度配置")
    fun putSchedulerConfig(
        @RequestParam chain: String,
        @RequestParam depositScanEnabled: Boolean,
        @RequestParam depositScanIntervalMs: Long,
        @RequestParam sweepEnabled: Boolean,
        @RequestParam sweepIntervalMs: Long,
        @RequestParam feeSupplyEnabled: Boolean,
        @RequestParam feeSupplyIntervalMs: Long
    ): TokenResponse<Any> {
        val item = walletSchedulerConfigRepository.findByChain(chain) ?: WalletSchedulerConfig().also { it.chain = chain }
        item.depositScanEnabled = depositScanEnabled
        item.depositScanIntervalMs = depositScanIntervalMs
        item.sweepEnabled = sweepEnabled
        item.sweepIntervalMs = sweepIntervalMs
        item.feeSupplyEnabled = feeSupplyEnabled
        item.feeSupplyIntervalMs = feeSupplyIntervalMs
        walletSchedulerConfigRepository.save(item)
        audit("UPDATE_SCHEDULER_CONFIG", chain, "$depositScanEnabled,$depositScanIntervalMs,$sweepEnabled,$sweepIntervalMs,$feeSupplyEnabled,$feeSupplyIntervalMs")
        return TokenResponse()
    }

    

    @GetMapping("config/chains")
    @Operation(summary = "链配置")
    fun getChainConfig(): TokenResponse<Map<String, String>> {
        val map = walletChainConfigRepository.findAll().associate { cfg ->
            cfg.chain to "enabled:${cfg.enabled},deposit:${cfg.depositScanEnabled},withdraw:${cfg.withdrawEnabled},confirm:${cfg.confirmations},start:${cfg.startBlock},current:${cfg.currentBlock},batch:${cfg.scanBatchSize},interval:${cfg.scanIntervalMs}"
        }
        return TokenResponse(map)
    }

    @PutMapping("config/chains")
    @Operation(summary = "更新链配置")
    fun putChainConfig(
        @RequestParam chain: String,
        @RequestParam enabled: Boolean,
        @RequestParam depositScanEnabled: Boolean,
        @RequestParam withdrawEnabled: Boolean,
        @RequestParam confirmations: Int,
        @RequestParam startBlock: Long,
        @RequestParam currentBlock: Long,
        @RequestParam scanBatchSize: Int,
        @RequestParam scanIntervalMs: Long
    ): TokenResponse<Any> {
        val item = walletChainConfigRepository.findByChain(chain) ?: WalletChainConfig().also { it.chain = chain }
        item.enabled = enabled
        item.depositScanEnabled = depositScanEnabled
        item.withdrawEnabled = withdrawEnabled
        item.confirmations = confirmations
        item.startBlock = startBlock
        item.currentBlock = currentBlock
        item.scanBatchSize = scanBatchSize
        item.scanIntervalMs = scanIntervalMs
        walletChainConfigRepository.save(item)
        audit("UPDATE_CHAIN_CONFIG", chain, "$enabled,$depositScanEnabled,$withdrawEnabled,$confirmations,$startBlock,$currentBlock,$scanBatchSize,$scanIntervalMs")
        return TokenResponse()
    }

    @GetMapping("config/security")
    @Operation(summary = "安全配置")
    fun getSecurityConfig(): TokenResponse<Map<String, String>> {
        val cfg = walletSecurityConfigRepository.findAll().firstOrNull()
        return TokenResponse(mapOf(
            "allowExportPrivateKey" to (cfg?.allowExportPrivateKey?.toString() ?: "false"),
            "exportPrivateKeyRequire2fa" to (cfg?.exportPrivateKeyRequire2fa?.toString() ?: "true"),
            "allowUpdateRpcByAdmin" to (cfg?.allowUpdateRpcByAdmin?.toString() ?: "true")
        ))
    }

    @PutMapping("config/security")
    @Operation(summary = "更新安全配置")
    fun putSecurityConfig(
        @RequestParam allowExportPrivateKey: Boolean,
        @RequestParam exportPrivateKeyRequire2fa: Boolean,
        @RequestParam allowUpdateRpcByAdmin: Boolean
    ): TokenResponse<Any> {
        val cfg = walletSecurityConfigRepository.findAll().firstOrNull() ?: WalletSecurityConfig()
        cfg.allowExportPrivateKey = allowExportPrivateKey
        cfg.exportPrivateKeyRequire2fa = exportPrivateKeyRequire2fa
        cfg.allowUpdateRpcByAdmin = allowUpdateRpcByAdmin
        walletSecurityConfigRepository.save(cfg)
        audit("UPDATE_SECURITY_CONFIG", "GLOBAL", "$allowExportPrivateKey,$exportPrivateKeyRequire2fa,$allowUpdateRpcByAdmin")
        return TokenResponse()
    }

    @GetMapping("config/audit-logs")
    @Operation(summary = "审计日志")
    fun getAuditLogs(): TokenResponse<List<Map<String, String>>> {
        val dbLogs = walletAdminAuditLogRepository.findAll().takeLast(200).reversed().map {
            mapOf(
                "action" to (it.action ?: ""),
                "key" to (it.targetKey ?: ""),
                "value" to (it.afterValue ?: ""),
                "operator" to (it.operator ?: "system"),
                "time" to (it.createTime?.toInstant()?.toString() ?: "")
            )
        }
        return TokenResponse((dbLogs + auditLogs).take(200))
    }


    @GetMapping("config/sweep")
    @Operation(summary = "归集配置")
    fun getSweepConfig(): TokenResponse<Map<String, String>> {
        return TokenResponse(walletSweepConfigRepository.findAll().associate {
            it.chain to "enabled:${it.sweepEnabled},to:${maskSensitive("SWEEP_TO_ADDRESS", it.sweepToAddress ?: "")},min:${it.minSweepAmount},reserve:${it.reserveAmount}"
        })
    }

    @GetMapping("config/sweep/{chain}")
    @Operation(summary = "按链获取归集配置")
    fun getSweepConfigByChain(@PathVariable chain: String): TokenResponse<Map<String, String>> {
        val cfg = walletSweepConfigRepository.findByChain(chain)
        return TokenResponse(mapOf(
            "chain" to chain,
            "sweepEnabled" to (cfg?.sweepEnabled?.toString() ?: "false"),
            "sweepToAddress" to maskSensitive("SWEEP_TO_ADDRESS", cfg?.sweepToAddress ?: ""),
            "minSweepAmount" to (cfg?.minSweepAmount ?: "0"),
            "reserveAmount" to (cfg?.reserveAmount ?: "0")
        ))
    }

    @PutMapping("config/sweep")
    @Operation(summary = "更新归集配置")
    fun putSweepConfig(@RequestParam chain: String, @RequestParam sweepEnabled: Boolean, @RequestParam sweepToAddress: String, @RequestParam minSweepAmount: String, @RequestParam reserveAmount: String): TokenResponse<Any> {
        val cfg = walletSweepConfigRepository.findByChain(chain) ?: WalletSweepConfig().also { it.chain = chain }
        cfg.sweepEnabled = sweepEnabled
        cfg.sweepToAddress = sweepToAddress
        cfg.minSweepAmount = minSweepAmount
        cfg.reserveAmount = reserveAmount
        walletSweepConfigRepository.save(cfg)
        audit("UPDATE_SWEEP_CONFIG", chain, "$sweepEnabled,$sweepToAddress,$minSweepAmount,$reserveAmount")
        return TokenResponse()
    }

    @GetMapping("config/withdraw")
    @Operation(summary = "提现配置")
    fun getWithdrawConfig(): TokenResponse<Map<String, String>> {
        return TokenResponse(walletWithdrawConfigRepository.findAll().associate {
            it.chain to "enabled:${it.withdrawEnabled},review:${it.manualReviewEnabled},max:${it.maxAutoWithdrawAmount},daily:${it.dailyWithdrawLimit}"
        })
    }

    @GetMapping("config/withdraw/{chain}")
    @Operation(summary = "按链获取提现配置")
    fun getWithdrawConfigByChain(@PathVariable chain: String): TokenResponse<Map<String, String>> {
        val cfg = walletWithdrawConfigRepository.findByChain(chain)
        return TokenResponse(mapOf(
            "chain" to chain,
            "withdrawEnabled" to (cfg?.withdrawEnabled?.toString() ?: "false"),
            "manualReviewEnabled" to (cfg?.manualReviewEnabled?.toString() ?: "true"),
            "maxAutoWithdrawAmount" to (cfg?.maxAutoWithdrawAmount ?: "0"),
            "dailyWithdrawLimit" to (cfg?.dailyWithdrawLimit ?: "0")
        ))
    }

    @PutMapping("config/withdraw")
    @Operation(summary = "更新提现配置")
    fun putWithdrawConfig(@RequestParam chain: String, @RequestParam withdrawEnabled: Boolean, @RequestParam manualReviewEnabled: Boolean, @RequestParam maxAutoWithdrawAmount: String, @RequestParam dailyWithdrawLimit: String): TokenResponse<Any> {
        val cfg = walletWithdrawConfigRepository.findByChain(chain) ?: WalletWithdrawConfig().also { it.chain = chain }
        cfg.withdrawEnabled = withdrawEnabled
        cfg.manualReviewEnabled = manualReviewEnabled
        cfg.maxAutoWithdrawAmount = maxAutoWithdrawAmount
        cfg.dailyWithdrawLimit = dailyWithdrawLimit
        walletWithdrawConfigRepository.save(cfg)
        audit("UPDATE_WITHDRAW_CONFIG", chain, "$withdrawEnabled,$manualReviewEnabled,$maxAutoWithdrawAmount,$dailyWithdrawLimit")
        return TokenResponse()
    }

    @GetMapping("config/fee-supply")
    @Operation(summary = "手续费补充配置")
    fun getFeeSupplyConfig(): TokenResponse<Map<String, String>> {
        return TokenResponse(walletFeeSupplyConfigRepository.findAll().associate {
            it.chain to "enabled:${it.feeSupplyEnabled},from:${maskSensitive("FEE_SUPPLY_FROM_ADDRESS", it.feeSupplyFromAddress ?: "")},min:${it.minGasBalance},target:${it.targetGasBalance}"
        })
    }

    @GetMapping("config/fee-supply/{chain}")
    @Operation(summary = "按链获取手续费补充配置")
    fun getFeeSupplyConfigByChain(@PathVariable chain: String): TokenResponse<Map<String, String>> {
        val cfg = walletFeeSupplyConfigRepository.findByChain(chain)
        return TokenResponse(mapOf(
            "chain" to chain,
            "feeSupplyEnabled" to (cfg?.feeSupplyEnabled?.toString() ?: "false"),
            "feeSupplyFromAddress" to maskSensitive("FEE_SUPPLY_FROM_ADDRESS", cfg?.feeSupplyFromAddress ?: ""),
            "minGasBalance" to (cfg?.minGasBalance ?: "0"),
            "targetGasBalance" to (cfg?.targetGasBalance ?: "0")
        ))
    }

    @PutMapping("config/fee-supply")
    @Operation(summary = "更新手续费补充配置")
    fun putFeeSupplyConfig(@RequestParam chain: String, @RequestParam feeSupplyEnabled: Boolean, @RequestParam feeSupplyFromAddress: String, @RequestParam minGasBalance: String, @RequestParam targetGasBalance: String): TokenResponse<Any> {
        val cfg = walletFeeSupplyConfigRepository.findByChain(chain) ?: WalletFeeSupplyConfig().also { it.chain = chain }
        cfg.feeSupplyEnabled = feeSupplyEnabled
        cfg.feeSupplyFromAddress = feeSupplyFromAddress
        cfg.minGasBalance = minGasBalance
        cfg.targetGasBalance = targetGasBalance
        walletFeeSupplyConfigRepository.save(cfg)
        audit("UPDATE_FEE_SUPPLY_CONFIG", chain, "$feeSupplyEnabled,$feeSupplyFromAddress,$minGasBalance,$targetGasBalance")
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

    @Autowired
    lateinit var walletAdminAuditLogRepository: WalletAdminAuditLogRepository

    @Autowired
    lateinit var walletRpcConfigRepository: WalletRpcConfigRepository

    @Autowired
    lateinit var walletSchedulerConfigRepository: WalletSchedulerConfigRepository

    @Autowired
    lateinit var walletChainConfigRepository: WalletChainConfigRepository
    @Autowired
    lateinit var walletSweepConfigRepository: WalletSweepConfigRepository
    @Autowired
    lateinit var walletWithdrawConfigRepository: WalletWithdrawConfigRepository
    @Autowired
    lateinit var walletFeeSupplyConfigRepository: WalletFeeSupplyConfigRepository
    @Autowired
    lateinit var walletSecurityConfigRepository: WalletSecurityConfigRepository

    private fun findConfig(key: String): String = configService.findAll().firstOrNull { it.configKey == key }?.configValue ?: ""

    private fun saveConfigByKey(key: String, value: String) {
        val item = configService.findAll().firstOrNull { it.configKey == key } ?: Config().also { it.configKey = key }
        if (value.isBlank()) return
        item.configValue = value
        configService.save(item)
    }

    private fun audit(action: String, key: String, value: String) {
        val masked = maskSensitive(key, value)
        auditLogs.add(mapOf("action" to action, "key" to key, "value" to masked, "operator" to ((request.getHeader("X-Admin-User") ?: "system")), "time" to java.time.Instant.now().toString()))
        runCatching {
            val log = WalletAdminAuditLog()
            log.action = action
            log.targetKey = key
            log.afterValue = masked
            log.operator = request.getHeader("X-Admin-User") ?: "system"
            log.requestIp = request.remoteAddr
            log.userAgent = request.getHeader("User-Agent") ?: ""
            walletAdminAuditLogRepository.save(log)
        }
    }

    private fun maskSensitive(key: String, value: String): String {
        if (!key.contains("PASSWORD") && !key.contains("KEY")) return value
        if (value.length <= 8) return "****"
        return value.take(4) + "****" + value.takeLast(4)
    }
}
