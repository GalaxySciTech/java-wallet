package com.wallet.biz.signing

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.wallet.biz.config.WalletProperties
import com.wallet.biz.domain.dict.ErrorCode
import com.wallet.biz.domain.exception.BizException
import com.wallet.biz.domain.po.SignBitcoinPo
import com.wallet.biz.domain.po.SignEthereumPo
import com.wallet.biz.domain.po.SignUsdtCollectPo
import com.wallet.biz.domain.po.SignUsdtPo
import com.wallet.hsm.xservice.HsmXService
import com.wallet.repository.WalletChainConfigRepository
import org.consenlabs.tokencore.wallet.transaction.TxSignResult
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

/**
 * Routes signing to tokencore in-process or HTTP `wallet.external-signer`.
 * POST `{baseUrl}/v1/sign` body: `{ "chainType", "kind", "payload" }` where kind matches the Po type.
 * Response: `{ "signedTx", "txHash?", "wtxID?" }` or `{ "mode": "fallback" }` to use tokencore.
 */
@Service
class ChainSigningService(
    private val hsmXService: HsmXService,
    private val chainConfigRepository: WalletChainConfigRepository,
    private val walletProperties: WalletProperties,
    private val objectMapper: ObjectMapper,
    private val restTemplate: RestTemplate
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun signUsdt(chainType: String, po: SignUsdtPo): TxSignResult {
        val tc = { hsmXService.signUsdtTransaction(po) }
        return if (resolveBackend(chainType) == SigningBackend.TOKENCORE) tc()
        else timed(chainType) { postExternal(chainType, "signUsdt", po, tc) }
    }

    fun signUsdtCollect(chainType: String, po: SignUsdtCollectPo): TxSignResult {
        val tc = { hsmXService.signUsdtCollectTransaction(po) }
        return if (resolveBackend(chainType) == SigningBackend.TOKENCORE) tc()
        else timed(chainType) { postExternal(chainType, "signUsdtCollect", po, tc) }
    }

    fun signBitcoin(chainType: String, po: SignBitcoinPo): TxSignResult {
        val tc = { hsmXService.signBitcoinTransaction(po) }
        return if (resolveBackend(chainType) == SigningBackend.TOKENCORE) tc()
        else timed(chainType) { postExternal(chainType, "signBitcoin", po, tc) }
    }

    fun signEthereum(chainType: String, po: SignEthereumPo): TxSignResult {
        val tc = { hsmXService.signEthereumTransaction(po) }
        return if (resolveBackend(chainType) == SigningBackend.TOKENCORE) tc()
        else timed(chainType) { postExternal(chainType, "signEthereum", po, tc) }
    }

    private fun timed(chainType: String, block: () -> TxSignResult): TxSignResult {
        val started = System.nanoTime()
        return try {
            block()
        } finally {
            log.info("external_sign chain={} took_ms={}", chainType, (System.nanoTime() - started) / 1_000_000)
        }
    }

    private fun resolveBackend(chainType: String): SigningBackend {
        val def = SigningBackend.parse(walletProperties.signing.defaultBackend, SigningBackend.TOKENCORE)
        val raw = runCatching { chainConfigRepository.findByChain(chainType)?.signingBackend }.getOrNull()
        return SigningBackend.parse(raw, def)
    }

    private fun postExternal(chainType: String, kind: String, payload: Any, tokencore: () -> TxSignResult): TxSignResult {
        val base = walletProperties.externalSigner.baseUrl.trimEnd('/')
        if (base.isBlank()) {
            throw BizException(
                ErrorCode.ERROR_PARAM.code,
                "EXTERNAL signing for chain=$chainType but wallet.external-signer.base-url is empty"
            )
        }
        val body = objectMapper.createObjectNode().apply {
            put("chainType", chainType)
            put("kind", kind)
            set<JsonNode>("payload", objectMapper.valueToTree(payload))
        }
        val headers = HttpHeaders().apply { contentType = MediaType.APPLICATION_JSON }
        val response = restTemplate.postForEntity(
            "$base/v1/sign",
            HttpEntity(body, headers),
            JsonNode::class.java
        ).body ?: throw BizException(ErrorCode.ERROR_PARAM.code, "Empty response from external signer")
        if ("fallback".equals(response.path("mode").asText(null), ignoreCase = true)) {
            log.warn("external signer fallback for chain={} kind={}", chainType, kind)
            return tokencore()
        }
        val signedTx = response.path("signedTx").asText(null)
            ?: throw BizException(ErrorCode.ERROR_PARAM.code, "external signer response missing signedTx")
        val txHash = response.path("txHash").asText(null)
        val wtxId = response.path("wtxID").asText(null)
        return if (txHash != null) TxSignResult(wtxId, signedTx, txHash) else TxSignResult(wtxId, signedTx)
    }
}
