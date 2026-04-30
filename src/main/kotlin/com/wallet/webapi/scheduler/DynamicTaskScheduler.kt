package com.wallet.webapi.scheduler

import com.wallet.biz.cache.CacheService
import com.wallet.biz.dict.SysConfigKey
import com.wallet.biz.handler.service.CollectService
import com.wallet.biz.handler.service.SendFeeService
import com.wallet.biz.handler.service.SynService
import com.wallet.webapi.service.RuntimeConfigService
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class DynamicTaskScheduler(
    private val cacheService: CacheService,
    private val synService: SynService,
    private val collectService: CollectService,
    private val sendFeeService: SendFeeService,
    private val runtimeConfigService: RuntimeConfigService
) {
    private val log = LoggerFactory.getLogger(javaClass)
    private val scheduler = ThreadPoolTaskScheduler()

    @PostConstruct
    fun start() {
        scheduler.poolSize = 4
        scheduler.setThreadNamePrefix("wallet-scheduler-")
        scheduler.initialize()
        register("deposit", SysConfigKey.SCHEDULER_DEPOSIT_SCAN_MS, SysConfigKey.SCHEDULER_DEPOSIT_SCAN_ENABLED) { synService.synDeposit() }
        register("sync", SysConfigKey.SCHEDULER_CHAIN_SYNC_MS, SysConfigKey.SCHEDULER_CHAIN_SYNC_ENABLED) {
            synService.synETH(); synService.synOMNI(); synService.synTRX(); synService.synImportAddress()
        }
        register("sweep", SysConfigKey.SCHEDULER_SWEEP_MS, SysConfigKey.SCHEDULER_SWEEP_ENABLED) {
            collectService.collectETH(); collectService.collectOMNI(); collectService.collectTRX(); collectService.collectTRC(); collectService.collectERC()
        }
        register("fee", SysConfigKey.SCHEDULER_FEE_SUPPLY_MS, SysConfigKey.SCHEDULER_FEE_SUPPLY_ENABLED) {
            sendFeeService.sendFeeETH(); sendFeeService.sendFeeTRX()
        }
    }

    private fun register(name: String, intervalKey: SysConfigKey, enabledKey: SysConfigKey, task: () -> Unit) {
        scheduler.scheduleWithFixedDelay({
            val enabled = readEnabled(name, enabledKey)
            if (!enabled) return@scheduleWithFixedDelay
            runCatching(task).onFailure { log.error("dynamic task {} failed: {}", name, it.message) }
        }, Duration.ofMillis(readInterval(name, intervalKey)))
    }

    private fun readEnabled(name: String, fallbackKey: SysConfigKey): Boolean {
        val cfg = runtimeConfigService.schedulerDefault()
        return when (name) {
            "deposit" -> cfg?.depositScanEnabled
            "sweep" -> cfg?.sweepEnabled
            "fee" -> cfg?.feeSupplyEnabled
            else -> null
        } ?: (cacheService.getSysConfig(fallbackKey).lowercase() == "true")
    }

    private fun readInterval(name: String, fallbackKey: SysConfigKey): Long {
        val cfg = runtimeConfigService.schedulerDefault()
        return when (name) {
            "deposit" -> cfg?.depositScanIntervalMs
            "sweep" -> cfg?.sweepIntervalMs
            "fee" -> cfg?.feeSupplyIntervalMs
            else -> null
        } ?: (cacheService.getSysConfig(fallbackKey).toLongOrNull() ?: fallbackKey.defaultValue.toLong())
    }
}
