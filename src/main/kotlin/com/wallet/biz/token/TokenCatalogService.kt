package com.wallet.biz.token

import com.wallet.repository.TokenRepository
import org.springframework.stereotype.Service

/** Chain and token lists driven by the `token` table (E2). */
@Service
class TokenCatalogService(
    private val tokenRepository: TokenRepository
) {
    fun distinctChains(): Set<String> =
        tokenRepository.findAll().mapNotNull { it.chainType }.filter { it.isNotBlank() }.toSortedSet()

    fun distinctSymbols(): Set<String> =
        tokenRepository.findAll().mapNotNull { it.tokenSymbol }.filter { it.isNotBlank() }.toSortedSet()
}
