package at.tugraz.tc.cyfile.crypto.impl


import at.tugraz.tc.cyfile.crypto.KeyVaultService
import at.tugraz.tc.cyfile.crypto.exceptions.KeyVaultAlreadyInitializedException
import at.tugraz.tc.cyfile.crypto.exceptions.KeyVaultLockedException
import at.tugraz.tc.cyfile.crypto.exceptions.KeyVaultNotInitializedException
import java.security.Key
import java.security.NoSuchAlgorithmException
import javax.crypto.KeyGenerator

class DummyKeyVaultService : KeyVaultService {

    private var initState: Short = 0
    private var unlockState: Short = 1
    private var lockState: Short = 2

    private var currentState: Short = initState

    override fun getEncryptionKey(): Key {
        if (currentState == initState) {
            throw KeyVaultNotInitializedException("Not initialized")
        }

        if (currentState == lockState) {
            throw KeyVaultLockedException("Key vault is locked")
        }

        return key
    }

    lateinit var key: Key

    @Throws(KeyVaultAlreadyInitializedException::class)
    override fun init(passphrase: String) {
        if (currentState != initState) {
            throw KeyVaultAlreadyInitializedException()
        }

        currentState = lockState
    }

    @Throws(KeyVaultNotInitializedException::class)
    override fun unlockVault(passphrase: String) {
        if (currentState == initState) {
            throw KeyVaultNotInitializedException("Key vault must be initialized first.")
        }

        // already unlocked
        if (currentState == unlockState) {
            return
        }

        val keyGenerator: KeyGenerator
        try {
            keyGenerator = KeyGenerator.getInstance("AES")
            keyGenerator.init(128)
            key = keyGenerator.generateKey()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        currentState = unlockState


    }

    override fun lockVault() {
        if (currentState == lockState) {
            return
        }

        if (currentState == initState) {
            throw KeyVaultNotInitializedException("Must be initialized first")
        }

        currentState = lockState
    }

}
