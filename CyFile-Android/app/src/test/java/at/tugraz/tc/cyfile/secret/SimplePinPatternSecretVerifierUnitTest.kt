package at.tugraz.tc.cyfile.secret

import at.tugraz.tc.cyfile.BaseUnitTest
import at.tugraz.tc.cyfile.secret.impl.PinPatternSecret
import at.tugraz.tc.cyfile.secret.impl.SimplePinPatternSecretVerifier
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import junit.framework.Assert
import org.junit.Before
import org.junit.Test

class SimplePinPatternSecretVerifierUnitTest : BaseUnitTest() {

    private var simplePinPatternSecretVerifier: SimplePinPatternSecretVerifier? = null

    @MockK
    private lateinit var secretRepository: SecretRepository

    @Before
    fun setup() {
        simplePinPatternSecretVerifier = SimplePinPatternSecretVerifier(secretRepository)
    }


    @Test
    fun verifyShouldReturnTrueIfTheValuesAreEqual() {
        every { secretRepository.retrieveSecret() } returns PinPatternSecret("test")

        val isValid = simplePinPatternSecretVerifier!!.verify(PinPatternSecret("test"))

        Assert.assertTrue(isValid)

        verify { secretRepository.retrieveSecret() }
    }

    @Test
    fun verifyShouldReturnFalseWhenValuesAreNotEqual() {
        every { secretRepository.retrieveSecret() } returns PinPatternSecret("test")

        val isValid = simplePinPatternSecretVerifier!!.verify(PinPatternSecret("invalidpin"))

        Assert.assertFalse(isValid)

        verify { secretRepository.retrieveSecret() }
    }

    @Test
    fun verifyShouldReturnFalseWhenSecretNotSet() {
        every { secretRepository.retrieveSecret() } returns null

        val isValid = simplePinPatternSecretVerifier!!.verify(PinPatternSecret("test"))

        Assert.assertFalse(isValid)
        verify { secretRepository.retrieveSecret() }
    }
}
