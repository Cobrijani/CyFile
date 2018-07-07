package at.tugraz.tc.cyfile.crypto.impl

import at.tugraz.tc.cyfile.crypto.CryptoService

/**
 * Implementation of [CryptoService]
 *
 *
 * that does nothing
 * Created by cobri on 3/21/2018.
 */

class NoOpCryptoService : CryptoService {
    override fun encrypt(data: ByteArray): ByteArray {
        return data
    }

    override fun encrypt(data: String): String {
        return data
    }

    override fun decrypt(cipherData: ByteArray): ByteArray {
        return cipherData
    }

    override fun decrypt(cipherData: String): String {
        return cipherData
    }

}
