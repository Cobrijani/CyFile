package at.tugraz.tc.cyfile.crypto

import at.tugraz.tc.cyfile.crypto.exceptions.InvalidCryptoOperationException


/**
 * Service responsible for handling various cryptographic operations
 * Created by cobri on 3/21/2018.
 */
interface CryptoService {

    /**
     * Perform a encryption of plain data
     *
     * @param data data represented in bytes to be encrypted
     * @return cipher text in form of byte array
     */
    @Throws(InvalidCryptoOperationException::class)
    fun encrypt(data: ByteArray): ByteArray

    /**
     * Encrypt data and perform base64 encoding
     *
     * @param data data to be encrypted
     * @return cipher data in encoded format
     */
    @Throws(InvalidCryptoOperationException::class)
    fun encrypt(data: String): String

    /**
     * Decrypt cipher data
     *
     * @param cipherData data to be decrypted
     * @return decrypted text in a form of byte array
     */
    @Throws(InvalidCryptoOperationException::class)
    fun decrypt(cipherData: ByteArray): ByteArray

    /**
     * Decrypt data
     *
     * @param cipherData data to be decrypted
     * @return String representation of data
     */
    @Throws(InvalidCryptoOperationException::class)
    fun decrypt(cipherData: String): String

}
