package at.tugraz.tc.cyfile.secret

import android.content.Context
import at.tugraz.tc.cyfile.crypto.impl.ApacheCodecBase64
import at.tugraz.tc.cyfile.secret.impl.HashSecretRepository
import at.tugraz.tc.cyfile.secret.impl.HashedSecret
import at.tugraz.tc.cyfile.secret.impl.PinPatternSecret
import io.mockk.mockk
import junit.framework.Assert
import org.junit.Test
import java.io.*

class HashSecretRepositoryUnitTest {

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
        val secret = HashedSecret(PinPatternSecret("111222"), ApacheCodecBase64())
        val mockContext = mockk<Context>(relaxed = true)

        val repository = HashSecretRepository(mockContext,
                "",
                mockk(relaxed = true),
                ApacheCodecBase64(),
                createInputStream(secret))

        Assert.assertEquals(repository.retrieveSecret(), secret)
    }

    @Test
    @Throws(IOException::class, ClassNotFoundException::class)
    fun testWriteSecret() {
        val oldSecret = HashedSecret(PinPatternSecret("332211"), ApacheCodecBase64())
        val newSecret = PinPatternSecret("111222")
        val mockContext = mockk<Context>(relaxed = true)

        val outputStream = createOutputStream()
        val repository = HashSecretRepository(mockContext,
                "",
                mockk(relaxed = true),
                ApacheCodecBase64(),
                inputStream = createInputStream(oldSecret),
                outputStream = outputStream)

        repository.saveSecret(newSecret)

        val buffer = outputStream.toByteArray()
        val `is` = ByteArrayInputStream(buffer)
        val ois = ObjectInputStream(`is`)
        val actual = ois.readObject() as HashedSecret
        Assert.assertEquals(actual, newSecret)
    }
}
