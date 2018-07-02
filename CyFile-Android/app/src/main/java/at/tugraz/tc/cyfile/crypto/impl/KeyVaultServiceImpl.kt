package at.tugraz.tc.cyfile.crypto.impl

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import at.tugraz.tc.cyfile.crypto.KeyVaultService
import at.tugraz.tc.cyfile.crypto.exceptions.*
import at.tugraz.tc.cyfile.logging.CyFileLogger
import java.io.IOException
import java.security.*
import java.security.cert.CertificateException
import javax.crypto.KeyGenerator

class KeyVaultServiceImpl : KeyVaultService {


    private var secretKey: Key? = null
    private var internalState = State.INIT

    private var logger: CyFileLogger
    private var generator: KeyGenerator
    private var keyStore: KeyStore

    private enum class State {
        INIT,
        UNLOCKED,
        LOCKED
    }

    constructor(log: CyFileLogger) {
        this.secretKey = null
        this.logger = log
        try {
            this.keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER)
        } catch (e: KeyStoreException) {
            throw KeyVaultServiceException("KeyStore cannot be initialized")
        }
        try {
            generator = KeyGenerator
                    .getInstance(KeyProperties.KEY_ALGORITHM_AES, KEYSTORE_PROVIDER)
        } catch (e: NoSuchAlgorithmException) {
            throw KeyVaultServiceException("Configuration failure")
        } catch (e: NoSuchProviderException) {
            throw KeyVaultServiceException("Configuration failure")
        }
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setKeySize(256)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .build()
        try {
            generator.init(keyGenParameterSpec)
        } catch (e: InvalidAlgorithmParameterException) {
            throw KeyVaultServiceException("Wrong parameter configuration")
        }
    }

    constructor(keyGenerator: KeyGenerator, keyStore: KeyStore, logger: CyFileLogger) {
        this.logger = logger
        this.generator = keyGenerator
        this.keyStore = keyStore
    }

    @Throws(KeyVaultAlreadyInitializedException::class)
    override fun init(passphrase: String) {
        if (internalState != State.INIT) {
            throw KeyVaultAlreadyInitializedException()
        }


        try {
            this.keyStore.load(null)
            if (!keyStore.containsAlias(KEY_ALIAS)) {
                this.logger.d("KeyVaultService", "Generating key")
                this.secretKey = generator.generateKey()
            }
            internalState = State.LOCKED
        } catch (e: KeyStoreException) {
            e.printStackTrace()

            // if error occurs than just leave it in init state
            internalState = State.INIT

        } catch (e: CertificateException) {
            e.printStackTrace()
            internalState = State.INIT
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            internalState = State.INIT
        } catch (e: IOException) {
            e.printStackTrace()
            internalState = State.INIT
        }

    }

    @Throws(KeyVaultNotInitializedException::class, InvalidPassPhraseException::class)
    override fun unlockVault(passphrase: String) {
        if (internalState == State.INIT) {
            throw KeyVaultNotInitializedException("Key vault must be initialized first.")
        }

        // already unlocked
        if (internalState == State.UNLOCKED) {
            return
        }

        try {
            this.keyStore.load(null)
            if (this.keyStore.containsAlias(KEY_ALIAS)) {
                // load key from keystore
                secretKey = this.keyStore.getKey(KEY_ALIAS, passphrase.toCharArray())
            } else {
                throw KeyVaultServiceException("Configuration is invalid - contact programmer!")
            }
        } catch (e: IOException) {
            throw InvalidPassPhraseException("Passphrase is invalid", e)
        } catch (e: NoSuchAlgorithmException) {
            throw InvalidPassPhraseException("Passphrase is invalid", e)
        } catch (e: CertificateException) {
            throw InvalidPassPhraseException("Passphrase is invalid", e)
        } catch (e: KeyStoreException) {
            throw InvalidPassPhraseException("Passphrase is invalid", e)
        } catch (e: UnrecoverableKeyException) {
            throw InvalidPassPhraseException("Passphrase is invalid", e)
        }

        logger.d("KeyVaultService", "KVS unlocked")
        internalState = State.UNLOCKED

    }

    @Throws(KeyVaultNotInitializedException::class)
    override fun lockVault() {
        if (internalState == State.LOCKED) {
            return
        }

        if (internalState == State.INIT) {
            throw KeyVaultNotInitializedException("Must be initialized first")
        }

        internalState = State.LOCKED
    }

    override fun getEncryptionKey(): Key? {
        if (internalState == State.INIT) {
            throw KeyVaultNotInitializedException("Not initialized")
        }

        if (internalState == State.LOCKED) {
            throw KeyVaultLockedException("Key vault is locked")
        }

        return secretKey
    }


    companion object {
        private const val KEY_ALIAS = "cyfile-encryption-key"
        private const val KEYSTORE_PROVIDER = "AndroidKeyStore"
    }

    init {

    }
}
