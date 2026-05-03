package com.wallet.biz.signing

enum class SigningBackend {
    TOKENCORE,
    EXTERNAL;

    companion object {
        fun parse(raw: String?, defaultBackend: SigningBackend): SigningBackend {
            if (raw.isNullOrBlank()) return defaultBackend
            return when (raw.uppercase()) {
                "EXTERNAL" -> EXTERNAL
                else -> TOKENCORE
            }
        }
    }
}
