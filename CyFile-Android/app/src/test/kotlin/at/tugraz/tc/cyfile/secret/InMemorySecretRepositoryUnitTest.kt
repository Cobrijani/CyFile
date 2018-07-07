package at.tugraz.tc.cyfile.secret

import at.tugraz.tc.cyfile.BaseUnitTest
import at.tugraz.tc.cyfile.secret.impl.InMemorySecretRepository
import at.tugraz.tc.cyfile.secret.impl.PinPatternSecret
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class InMemorySecretRepositoryUnitTest : BaseUnitTest() {

    private lateinit var memorySecretRepository: InMemorySecretRepository

    @Before
    fun setup() {
        memorySecretRepository = InMemorySecretRepository()

    }

    @Test
    fun getSecretShouldReturnSecretIfSecretSet() {
        // given secret is set
        memorySecretRepository = InMemorySecretRepository()
        memorySecretRepository.saveSecret(object : Secret {
            override val secretValue: String
                get() = "test"
        })

        // when secret is retrieved
        val secret = memorySecretRepository.retrieveSecret()!!

        // then secret value is correct
        Assert.assertEquals(secret.secretValue, "test")
    }

    @Test
    fun getSecretShouldReturnNullIfSecretNotSet() {
        // given secret is not set
        memorySecretRepository = InMemorySecretRepository()

        // when secret is retrieved
        val secret = memorySecretRepository.retrieveSecret()

        // then secret value is null
        Assert.assertNull(secret)

    }

    @Test
    fun setSecretShouldSetSecretIfNotNull() {
        //given initialized state

        //when secret is being set
        val isSet = memorySecretRepository.saveSecret(PinPatternSecret("secret"))

        //then secret is saved
        Assert.assertTrue(isSet)

    }
}

