package com.wallet.api.service

import com.wallet.entity.domain.*
import com.wallet.repository.*
import org.springframework.stereotype.Service

@Service
class RuntimeConfigService(
    private val schedulerRepo: WalletSchedulerConfigRepository,
    private val chainRepo: WalletChainConfigRepository,
    private val rpcRepo: WalletRpcConfigRepository,
    private val sweepRepo: WalletSweepConfigRepository,
    private val withdrawRepo: WalletWithdrawConfigRepository,
    private val feeRepo: WalletFeeSupplyConfigRepository,
    private val securityRepo: WalletSecurityConfigRepository
) {
    fun schedulerDefault(): WalletSchedulerConfig? = schedulerRepo.findByChain("DEFAULT")
    fun chain(chain: String): WalletChainConfig? = chainRepo.findByChain(chain)
    fun rpc(chain: String): WalletRpcConfig? = rpcRepo.findByChain(chain)
    fun sweep(chain: String): WalletSweepConfig? = sweepRepo.findByChain(chain)
    fun withdraw(chain: String): WalletWithdrawConfig? = withdrawRepo.findByChain(chain)
    fun fee(chain: String): WalletFeeSupplyConfig? = feeRepo.findByChain(chain)
    fun security(): WalletSecurityConfig? = securityRepo.findAll().firstOrNull()
}
