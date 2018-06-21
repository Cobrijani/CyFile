package at.tugraz.tc.cyfile.crypto.impl

import at.tugraz.tc.cyfile.crypto.CryptoService

/**
 * "Encrypts" text by adding a prefix.
 * this is not actual encryption, it's only purpose is for unit-testing
 */
class PrefixCryptoService : CryptoService {
    override fun encrypt(data: ByteArray): ByteArray {
        throw NotImplementedError()
    }

    override fun encrypt(data: String): String {
        return PREFIX + data
    }

    override fun decrypt(cipherData: ByteArray): ByteArray {
        throw NotImplementedError()
    }

    override fun decrypt(cipherData: String): String {
        return cipherData.replace(PREFIX.toRegex(), "")
    }

    companion object {
        const val PREFIX = "PLEASE DON'T READ "
    }

}
