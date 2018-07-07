package at.tugraz.tc.cyfile.crypto.impl


import at.tugraz.tc.cyfile.crypto.Base64
import at.tugraz.tc.cyfile.crypto.CryptoService
import at.tugraz.tc.cyfile.crypto.KeyVaultService
import at.tugraz.tc.cyfile.crypto.exceptions.InvalidCryptoOperationException
import java.nio.charset.StandardCharsets
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.Key
import java.security.NoSuchAlgorithmException
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.IvParameterSpec

class AES256CBCCryptoService(private val keyVaultService: KeyVaultService, private val encoder: Base64, private val algorithm: String) : CryptoService {

    @Throws(InvalidCryptoOperationException::class)
    override fun encrypt(data: String): String {
        val encryptedBytes = encrypt(data.toByteArray(StandardCharsets.UTF_8))
        return encoder.encode(encryptedBytes)
    }

    @Throws(InvalidCryptoOperationException::class)
    override fun encrypt(data: ByteArray): ByteArray {
        val key: Key?
        try {
            key = keyVaultService.getEncryptionKey()
        } catch (e: Exception) {
            throw InvalidCryptoOperationException()
        }

        val currentIV: ByteArray
        val encCipher: Cipher

        try {
            encCipher = Cipher.getInstance(this.algorithm)
            encCipher.init(Cipher.ENCRYPT_MODE, key)
            currentIV = encCipher.iv
        } catch (e: NoSuchAlgorithmException) {
            throw InvalidCryptoOperationException()
        } catch (e: NoSuchPaddingException) {
            throw InvalidCryptoOperationException()
        } catch (e: InvalidKeyException) {
            throw InvalidCryptoOperationException()
        }

        try {
            val encBytes = encCipher.doFinal(data)
            val ret = ByteArray(encBytes.size + currentIV.size)
            System.arraycopy(currentIV, 0, ret, 0, currentIV.size)
            System.arraycopy(encBytes, 0, ret, currentIV.size, encBytes.size)
            return ret
        } catch (e: IllegalBlockSizeException) {
            throw InvalidCryptoOperationException()
        } catch (e: BadPaddingException) {
            throw InvalidCryptoOperationException()
        }

    }

    @Throws(InvalidCryptoOperationException::class)
    override fun decrypt(cipherData: ByteArray): ByteArray {
        try {
            val key = keyVaultService.getEncryptionKey()
            val iv = ByteArray(BLOCK_SIZE)
            val encryptedData = ByteArray(cipherData.size - BLOCK_SIZE)
            System.arraycopy(cipherData, 0, iv, 0, BLOCK_SIZE)
            System.arraycopy(cipherData, BLOCK_SIZE, encryptedData, 0,
                    cipherData.size - BLOCK_SIZE)

            val ivParameterSpec = IvParameterSpec(iv)
            val decCipher = Cipher.getInstance(this.algorithm)
            decCipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec)

            return decCipher.doFinal(encryptedData)
        } catch (e: IllegalBlockSizeException) {
            throw InvalidCryptoOperationException()
        } catch (e: BadPaddingException) {
            throw InvalidCryptoOperationException()
        } catch (e: NoSuchPaddingException) {
            throw InvalidCryptoOperationException()
        } catch (e: InvalidAlgorithmParameterException) {
            throw InvalidCryptoOperationException()
        } catch (e: InvalidKeyException) {
            throw InvalidCryptoOperationException()
        } catch (e: NoSuchAlgorithmException) {
            throw InvalidCryptoOperationException()
        } catch (e: Exception) {
            throw InvalidCryptoOperationException()
        }

    }

    @Throws(InvalidCryptoOperationException::class)
    override fun decrypt(cipherData: String): String {
        val data = encoder.decode(cipherData)
        val decValue = decrypt(data)
        return String(decValue, StandardCharsets.UTF_8)
    }

    companion object {
        private const val BLOCK_SIZE = 16
        const val DEFAULT_ALGORITHM = "AES/CBC/PKCS7Padding"
        const val TEST_ALGORITHM = "AES/CBC/PKCS5Padding"
    }
}
