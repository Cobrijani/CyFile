package at.tugraz.tc.cyfile.secret

import at.tugraz.tc.cyfile.BaseUnitTest
import at.tugraz.tc.cyfile.crypto.impl.ApacheCodecBase64
import at.tugraz.tc.cyfile.file.FileHandler
import at.tugraz.tc.cyfile.secret.impl.HashSecretRepository
import at.tugraz.tc.cyfile.secret.impl.HashedSecret
import at.tugraz.tc.cyfile.secret.impl.PinPatternSecret
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import junit.framework.Assert
import org.junit.Before
import org.junit.Test
import java.io.*

class HashSecretRepositoryUnitTest : BaseUnitTest() {

    @RelaxedMockK
    private lateinit var fileHandler: FileHandler
    private val defaultSecretValue = "111222"
    private val defaultSecret = HashedSecret(PinPatternSecret(defaultSecretValue), ApacheCodecBase64())

    private lateinit var repository: HashSecretRepository

    @Before
    fun setup() {
        every { fileHandler.getOutputStream() } returns createOutputStream()
        every { fileHandler.getInputStream() } returns createInputStream(
                defaultSecret)
        repository = HashSecretRepository(fileHandler, mockk(relaxed = true), ApacheCodecBase64())
    }

    @Throws(IOException::class)
    private fun createOutputStream(): ByteArrayOutputStream {
        return ByteArrayOutputStream(2048)
    }

    @Throws(IOException::class)
    private fun createInputStream(secret: HashedSecret): InputStream {
        val bas = ByteArrayOutputStream(2048)
        val oos = ObjectOutputStream(bas)
        oos.writeObject(secret)

        val buffer = bas.toByteArray()
        return ByteArrayInputStream(buffer)
    }

    @Test
    @Throws(IOException::class)
    fun testReadSecret() {
        Assert.assertEquals(repository.retrieveSecret(), defaultSecret)
    }

    @Test
    @Throws(IOException::class, ClassNotFoundException::class)
    fun testWriteSecret() {
        val newSecret = PinPatternSecret("332211")

        repository.saveSecret(newSecret)

        val buffer = (fileHandler.getOutputStream() as ByteArrayOutputStream).toByteArray()
        val `is` = ByteArrayInputStream(buffer)
        val ois = ObjectInputStream(`is`)
        val actual = ois.readObject() as HashedSecret
        Assert.assertEquals(actual, newSecret)
    }
}
