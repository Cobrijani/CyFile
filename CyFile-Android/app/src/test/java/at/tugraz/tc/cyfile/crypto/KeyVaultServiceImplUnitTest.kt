package at.tugraz.tc.cyfile.crypto

import at.tugraz.tc.cyfile.BaseUnitTest
import at.tugraz.tc.cyfile.crypto.exceptions.*
import at.tugraz.tc.cyfile.crypto.impl.KeyVaultServiceImpl
import at.tugraz.tc.cyfile.crypto.mocks.MockedKeyGenerator
import at.tugraz.tc.cyfile.crypto.mocks.MockedKeyStore
import at.tugraz.tc.cyfile.logging.CyFileLogger
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.security.*
import javax.crypto.KeyGeneratorSpi

class KeyVaultServiceImplUnitTest : BaseUnitTest() {

    private lateinit var keyVaultService: KeyVaultServiceImpl

    @Mock
    private var keyGeneratorSpi: KeyGeneratorSpi? = null

    @Mock
    private var keyStoreSpi: KeyStoreSpi? = null

    @Mock
    private var logger: CyFileLogger? = null

    private val correctPass = "test"
    private val wrongPass = "wrong"

    @Before
    fun setup() {
        val keyGenerator = MockedKeyGenerator(keyGeneratorSpi!!, mock(Provider::class.java), "test")
        keyGenerator.init(32)
        val keyStore = MockedKeyStore(keyStoreSpi!!, mock(Provider::class.java), "test")
        `when`(keyStoreSpi!!.engineContainsAlias(any()))
                .thenReturn(true)

        try {
            `when`(keyStoreSpi!!.engineGetKey(KEY_ALIAS, correctPass.toCharArray()))
                    .thenReturn(object : Key {
                        override fun getAlgorithm(): String {
                            return "test"
                        }

                        override fun getFormat(): String {
                            return "test"
                        }

                        override fun getEncoded(): ByteArray {
                            return ByteArray(0)
                        }
                    })

            `when`(keyStoreSpi!!.engineGetKey(KEY_ALIAS, wrongPass.toCharArray()))
                    .thenThrow(UnrecoverableKeyException::class.java)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: UnrecoverableKeyException) {
            e.printStackTrace()
        }

        keyVaultService = KeyVaultServiceImpl(keyGenerator, keyStore, logger!!)


    }

    @Test(expected = KeyVaultNotInitializedException::class)
    fun keyVaultShouldThrowExceptionWhenTryingToUnlockNotInitialized() {
        keyVaultService.unlockVault(correctPass)
    }

    @Test
    fun keyVaultServiceShouldUnlockWithoutErrorWhenPerformingInitAndUnlock() {
        keyVaultService.init(correctPass)
        keyVaultService.unlockVault(correctPass)
    }

    @Test
    fun keyVaultServiceShouldUnlockWithoutErrorWhenPerformingInitAndMultipleUnlock() {
        keyVaultService.init(correctPass)
        keyVaultService.unlockVault(correctPass)
        keyVaultService.unlockVault(correctPass)
    }

    @Test(expected = InvalidPassPhraseException::class)
    fun keyVaultServiceShouldThrowErrorWhenUnlockingWithWrongPassphrase() {
        keyVaultService.init(correctPass)
        keyVaultService.unlockVault(wrongPass)
    }

    @Test(expected = KeyVaultServiceException::class)
    fun keyVaultServiceShouldThrowErrorWhenAliasDoesNotExist() {
        `when`(keyStoreSpi!!.engineContainsAlias(any()))
                .thenReturn(false)
        keyVaultService.init(correctPass)
        keyVaultService.unlockVault(correctPass)
    }

    @Test(expected = KeyVaultAlreadyInitializedException::class)
    fun keyVaultServiceCannotBeInitializedTwice() {
        keyVaultService.init(correctPass)
        keyVaultService.init(correctPass)
    }

    @Test(expected = KeyVaultNotInitializedException::class)
    fun keyVaultServiceGetKeyShouldThrowErrorWhenNotInitialized() {
        keyVaultService.encryptionKey
    }

    @Test(expected = KeyVaultLockedException::class)
    fun keyVaultServiceGetKeyShouldThrowErrorWhenLocked() {
        keyVaultService.init(correctPass)

        keyVaultService.encryptionKey
    }

    @Test
    fun keyVaultServiceGetKeyShouldReturnKeyWithProperFlow() {
        keyVaultService.init(correctPass)
        keyVaultService.unlockVault(correctPass)

        keyVaultService.encryptionKey
    }

    @Test(expected = KeyVaultLockedException::class)
    fun keyVaultServiceGetKeyShouldThrowErrorWhenLockedAfterUnlocking() {
        keyVaultService.init(correctPass)
        keyVaultService.unlockVault(correctPass)
        keyVaultService.lockVault()

        keyVaultService.encryptionKey
    }

    @Test
    fun keyVaultServiceGetKeyShouldWorkAfterMultipleLockUnlocks() {
        keyVaultService.init(correctPass)
        keyVaultService.unlockVault(correctPass)

        keyVaultService.encryptionKey

        keyVaultService.lockVault()

        keyVaultService.unlockVault(correctPass)

        keyVaultService.encryptionKey
    }

    @Test(expected = KeyVaultNotInitializedException::class)
    fun keyVaultServiceGetKeyShouldThrowExceptionWhenLockingUninitializedKeyVault() {
        keyVaultService.lockVault()
    }

    @Test
    fun keyVaultMultipleLocksWontAffectCode() {
        keyVaultService.init(correctPass)
        keyVaultService.unlockVault(correctPass)
        keyVaultService.lockVault()
        keyVaultService.lockVault()
        keyVaultService.lockVault()
        keyVaultService.lockVault()
    }

    companion object {
        private const val KEY_ALIAS = "cyfile-encryption-key"
    }


}
