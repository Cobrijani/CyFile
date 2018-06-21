package at.tugraz.tc.cyfile.crypto.impl

import at.tugraz.tc.cyfile.crypto.KeyVaultService
import java.security.Key
import java.security.NoSuchAlgorithmException
import javax.crypto.KeyGenerator


import at.tugraz.tc.cyfile.crypto.exceptions.KeyVaultAlreadyInitializedException
import at.tugraz.tc.cyfile.crypto.exceptions.KeyVaultNotInitializedException

class DummyKeyVaultService : KeyVaultService {
    override val encryptionKey: Key
        get() = key


    lateinit var key: Key

    @Throws(KeyVaultAlreadyInitializedException::class)
    override fun init(passphrase: String) {

    }

    @Throws(KeyVaultNotInitializedException::class)
    override fun unlockVault(passphrase: String) {
        val keyGenerator: KeyGenerator
        try {
            keyGenerator = KeyGenerator.getInstance("AES")
            keyGenerator.init(128)
            key = keyGenerator.generateKey()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

    }

    override fun lockVault() {
    }

}
