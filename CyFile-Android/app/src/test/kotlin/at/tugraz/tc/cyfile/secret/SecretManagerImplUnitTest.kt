package at.tugraz.tc.cyfile.secret

import at.tugraz.tc.cyfile.BaseUnitTest
import at.tugraz.tc.cyfile.secret.impl.PinPatternSecret
import at.tugraz.tc.cyfile.secret.impl.SecretManagerImpl
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SecretManagerImplUnitTest : BaseUnitTest() {

    private lateinit var secretManager: SecretManagerImpl

    @RelaxedMockK
    private lateinit var secretVerifier: SecretVerifier

    @RelaxedMockK
    private lateinit var secretRepository: SecretRepository

    @Before
    fun setup() {
        secretManager = SecretManagerImpl(secretVerifier, secretRepository)
    }

    @Test
    fun verifyShouldDelegateVerificationToSecretVerifier() {
        // given configured secret manager
        every { secretManager.verify(any()) } returns true

        val secret = PinPatternSecret("test")
        //when secret manager to verify is called
        val isValid = secretManager.verify(secret)

        // then verification is true and the call has been delegated to secret verifier
        Assert.assertTrue(isValid)

        verify { secretVerifier.verify(secret) }
    }

    @Test
    fun setSecretShouldSecretWhenSecretIsNull() {
        // given secret is null
        every { secretRepository.retrieveSecret() } returns null

        val secret = PinPatternSecret("test")
        // when set secret is called
        val isSet = secretManager.setSecret(secret)

        // then secret manager will save the secret
        Assert.assertTrue(isSet)
        verify(exactly = 1) { secretRepository.saveSecret(secret) }
    }

    @Test
    fun setSecretShouldNotSetSecretWhenIsNotNull() {
        // given secret is set
        every { secretRepository.retrieveSecret() } returns PinPatternSecret("test")

        // when set secret is called
        val isSet = secretManager.setSecret(PinPatternSecret("another"))

        //then secret manager will not save secret
        verify(exactly = 0) { secretRepository.saveSecret(any()) }
        Assert.assertFalse(isSet)
    }


}
