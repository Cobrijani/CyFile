package at.tugraz.tc.cyfile.crypto

import at.tugraz.tc.cyfile.BaseUnitTest
import at.tugraz.tc.cyfile.crypto.exceptions.*
import at.tugraz.tc.cyfile.crypto.impl.KeyVaultServiceImpl
import at.tugraz.tc.cyfile.crypto.mocks.MockedKeyGenerator
import at.tugraz.tc.cyfile.crypto.mocks.MockedKeyStore
import at.tugraz.tc.cyfile.logging.CyFileLogger
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import java.security.Key
import java.security.KeyStoreSpi
import java.security.NoSuchAlgorithmException
import java.security.UnrecoverableKeyException
import javax.crypto.KeyGeneratorSpi

class KeyVaultServiceImplUnitTest : BaseUnitTest() {

    private lateinit var keyVaultService: KeyVaultServiceImpl

    @RelaxedMockK
    private lateinit var keyGeneratorSpi: KeyGeneratorSpi

    @RelaxedMockK
    private lateinit var keyStoreSpi: KeyStoreSpi

    @RelaxedMockK
    private lateinit var logger: CyFileLogger

    private val correctPass = "test"
    private val wrongPass = "wrong"

    @Before
    fun setup() {


        val keyGenerator = MockedKeyGenerator(keyGeneratorSpi, mockk(), "test")
        keyGenerator.init(32)

        val keyStore = MockedKeyStore(keyStoreSpi, mockk(), "test")

        every { keyStoreSpi.engineContainsAlias(any()) } returns true

        try {
            every { keyStoreSpi.engineGetKey(KEY_ALIAS, correctPass.toCharArray()) } returns object : Key {
                override fun getEncoded(): ByteArray = ByteArray(0)
                override fun getFormat(): String = "test"
                override fun getAlgorithm(): String = "test"
            }
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: UnrecoverableKeyException) {
            e.printStackTrace()
        }

        keyVaultService = KeyVaultServiceImpl(keyGenerator, keyStore, logger)


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
        every { keyStoreSpi.engineGetKey(any(), any()) } throws UnrecoverableKeyException()

        keyVaultService.init(correctPass)
        keyVaultService.unlockVault(wrongPass)
    }

    @Test(expected = KeyVaultServiceException::class)
    fun keyVaultServiceShouldThrowErrorWhenAliasDoesNotExist() {
        every {
            keyStoreSpi.engineContainsAlias(any())
        } returns false

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
        keyVaultService.getEncryptionKey()
    }

    @Test(expected = KeyVaultLockedException::class)
    fun keyVaultServiceGetKeyShouldThrowErrorWhenLocked() {
        keyVaultService.init(correctPass)

        keyVaultService.getEncryptionKey()
    }

    @Test
    fun keyVaultServiceGetKeyShouldReturnKeyWithProperFlow() {
        keyVaultService.init(correctPass)
        keyVaultService.unlockVault(correctPass)

        keyVaultService.getEncryptionKey()
    }

    @Test(expected = KeyVaultLockedException::class)
    fun keyVaultServiceGetKeyShouldThrowErrorWhenLockedAfterUnlocking() {
        keyVaultService.init(correctPass)
        keyVaultService.unlockVault(correctPass)
        keyVaultService.lockVault()

        keyVaultService.getEncryptionKey()
    }

    @Test
    fun keyVaultServiceGetKeyShouldWorkAfterMultipleLockUnlocks() {
        keyVaultService.init(correctPass)
        keyVaultService.unlockVault(correctPass)

        keyVaultService.getEncryptionKey()

        keyVaultService.lockVault()

        keyVaultService.unlockVault(correctPass)

        keyVaultService.getEncryptionKey()
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
