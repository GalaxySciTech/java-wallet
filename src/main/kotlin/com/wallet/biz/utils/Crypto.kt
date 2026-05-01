package com.wallet.biz.utils

import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object Crypto {
    @JvmStatic
    fun aesEncrypt(v: String, secretKey: String) = AES256.encrypt(v, secretKey)

    @JvmStatic
    fun aesDecrypt(v: String, secretKey: String) = AES256.decrypt(v, secretKey)
}

private object AES256 {
    private val encoder = Base64.getEncoder()
    private val decoder = Base64.getDecoder()
    private val secureRandom = SecureRandom()

    private fun getKeySpec(secretKey: String): SecretKeySpec {
        if (secretKey.length != 32) throw RuntimeException("SecretKey length is not 32 chars")
        return SecretKeySpec(secretKey.toByteArray(Charsets.UTF_8), "AES")
    }

    fun encrypt(str: String, secretKey: String): String {
        val iv = ByteArray(16)
        secureRandom.nextBytes(iv)
        val ivSpec = IvParameterSpec(iv)
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, getKeySpec(secretKey), ivSpec)
        val encrypted = cipher.doFinal(str.toByteArray(Charsets.UTF_8))
        val combined = ByteArray(iv.size + encrypted.size)
        System.arraycopy(iv, 0, combined, 0, iv.size)
        System.arraycopy(encrypted, 0, combined, iv.size, encrypted.size)
        return String(encoder.encode(combined))
    }

    fun decrypt(str: String, secretKey: String): String {
        val combined = decoder.decode(str.toByteArray(Charsets.UTF_8))
        if (combined.size < 16) {
            return decryptLegacy(str, secretKey)
        }
        return try {
            val iv = combined.copyOfRange(0, 16)
            val encrypted = combined.copyOfRange(16, combined.size)
            val ivSpec = IvParameterSpec(iv)
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, getKeySpec(secretKey), ivSpec)
            String(cipher.doFinal(encrypted))
        } catch (e: Exception) {
            decryptLegacy(str, secretKey)
        }
    }

    private fun decryptLegacy(str: String, secretKey: String): String {
        val byteStr = decoder.decode(str.toByteArray(Charsets.UTF_8))
        val iv = IvParameterSpec(secretKey.substring(0, 16).toByteArray(Charsets.UTF_8))
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, getKeySpec(secretKey), iv)
        return String(cipher.doFinal(byteStr))
    }
}
