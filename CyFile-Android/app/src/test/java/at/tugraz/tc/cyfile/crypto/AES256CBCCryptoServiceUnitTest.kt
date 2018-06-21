package at.tugraz.tc.cyfile.crypto

import at.tugraz.tc.cyfile.BaseUnitTest
import at.tugraz.tc.cyfile.crypto.exceptions.InvalidCryptoOperationException
import at.tugraz.tc.cyfile.crypto.impl.AES256CBCCryptoService
import at.tugraz.tc.cyfile.crypto.impl.ApacheCodecBase64
import at.tugraz.tc.cyfile.crypto.impl.DummyKeyVaultService
import junit.framework.Assert.*
import org.junit.Test
import org.mockito.Mockito.*
import java.security.InvalidKeyException
import java.security.Key
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.KeyGenerator

/**
 * Created by david on 3/21/2018.
 */

class AES256CBCCryptoServiceUnitTest : BaseUnitTest() {

    private var cryptoService: AES256CBCCryptoService? = null
    private val dummyKeyVaultService = DummyKeyVaultService()


    private val blockSize = 16

    fun setupKVS(keyVaultService: KeyVaultService) {
        cryptoService = spy(AES256CBCCryptoService(keyVaultService, ApacheCodecBase64(), AES256CBCCryptoService.TEST_ALGORITHM))
        keyVaultService.unlockVault("asdf")
    }

    @Test(expected = InvalidCryptoOperationException::class)
    @Throws(InvalidCryptoOperationException::class)
    fun testRelockedVault() {
        setupKVS(dummyKeyVaultService)
        val plain = "Hello World!"
        val encrypted = cryptoService!!.encrypt(plain)

        assertNotSame(plain, encrypted)
        assertTrue(encrypted.length > 0)

        //after we encrypted something we lock the vault
        dummyKeyVaultService.lockVault()
        val decrypted = cryptoService!!.decrypt(encrypted)
        assertNull(decrypted)
    }

    @Test(expected = InvalidCryptoOperationException::class)
    @Throws(InvalidCryptoOperationException::class, NoSuchAlgorithmException::class)
    fun testEncryptChangePasswordDecryptFail() {
        val keyGenerator: KeyGenerator
        keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(128)
        val key = keyGenerator.generateKey()
        val key2 = keyGenerator.generateKey()
        val kvs = mock(KeyVaultService::class.java)
        setupKVS(kvs)
        `when`<Key>(kvs.encryptionKey).thenReturn(key)
        val plain = "Hello World!"
        val encrypted = cryptoService!!.encrypt(plain.toByteArray())
        assertNotSame(plain, encrypted)
        `when`<Key>(kvs.encryptionKey).thenReturn(key2)

        // 16 for iv, then 16 for each started block of 16
        val expectedLength = (plain.length / 16 + 2) * 16
        assertEquals(encrypted.size, expectedLength)

        val decrypted = String(cryptoService!!.decrypt(encrypted))
        assertFalse(plain == decrypted)
    }


    @Test
    @Throws(InvalidCryptoOperationException::class)
    fun testEncryptDecryptString() {
        setupKVS(dummyKeyVaultService)
        val plain = "Hello World!"
        val encrypted = cryptoService!!.encrypt(plain.toByteArray())
        assertNotSame(plain, encrypted)

        // 16 for iv, then 16 for each started block of 16
        val expectedLength = (plain.length / 16 + 2) * 16
        assertEquals(encrypted.size, expectedLength)

        val decrypted = String(cryptoService!!.decrypt(encrypted))
        assertEquals(plain, decrypted)
    }

    @Test
    @Throws(InvalidCryptoOperationException::class)
    fun testEncryptEmptyString() {
        setupKVS(dummyKeyVaultService)
        val plain = ""
        val encrypted = cryptoService!!.encrypt(plain)
        assertNotSame(plain, encrypted)
        assertTrue(encrypted.length > 0)
        val decrypted = cryptoService!!.decrypt(encrypted)
        assertEquals(plain, decrypted)
    }

    @Test
    @Throws(InvalidCryptoOperationException::class)
    fun testEncryptNotBlockSizeAlignedString() {
        setupKVS(dummyKeyVaultService)
        val plain = "Hello this is a not Block-Size aligned String!"
        assertNotSame(0, plain.length % blockSize)
        val encrypted = cryptoService!!.encrypt(plain)
        assertNotSame(plain, encrypted)
        assertTrue(encrypted.length > 0)
        val decrypted = cryptoService!!.decrypt(encrypted)
        assertEquals(plain, decrypted)
    }

    @Test
    @Throws(InvalidCryptoOperationException::class)
    fun testEncryptBlockSizeAlignedString() {
        setupKVS(dummyKeyVaultService)
        var plain = "This is gonna be a Block-Size aligned String, achieved by cropping!" +
                " In order to fit different block sizes, this has to be a quite large String." +
                " If it is too short, just append something at the end."
        assertTrue(plain.length >= blockSize)
        plain = plain.substring(0, blockSize)
        assertEquals(0, plain.length % blockSize)
        val encrypted = cryptoService!!.encrypt(plain)
        assertNotSame(plain, encrypted)
        assertTrue(encrypted.length > 0)
        val decrypted = cryptoService!!.decrypt(encrypted)
        assertEquals(plain, decrypted)
    }

    @Test
    @Throws(InvalidCryptoOperationException::class)
    fun testEncryptSameStringMultipleTimes() {
        setupKVS(dummyKeyVaultService)
        val plain = "Hello World!"
        val encrypted1 = cryptoService!!.encrypt(plain.toByteArray())

        val encrypted2 = cryptoService!!.encrypt(plain.toByteArray())
        assertNotSame(plain, encrypted2)
        assertFalse(Arrays.equals(encrypted1, encrypted2))

        assertTrue(String(cryptoService!!.decrypt(encrypted1)) == plain)
        assertTrue(String(cryptoService!!.decrypt(encrypted2)) == plain)
    }

    @Test
    @Throws(InvalidKeyException::class, InvalidCryptoOperationException::class)
    fun testEncryptDifferentCryptoServiceInstances() {
        setupKVS(dummyKeyVaultService)
        val plain = ""
        val encrypted = cryptoService!!.encrypt(plain)
        assertNotSame(plain, encrypted)
        assertTrue(encrypted.length > 0)
        val cryptoService2 = spy(AES256CBCCryptoService(dummyKeyVaultService, ApacheCodecBase64(), AES256CBCCryptoService.TEST_ALGORITHM))
        val decrypted = cryptoService2.decrypt(encrypted)
        assertEquals(plain, decrypted)
    }

    @Test
    @Throws(InvalidCryptoOperationException::class)
    fun testEncryptLongString() {
        setupKVS(dummyKeyVaultService)
        val plain = "\"Nam vehicula tellus euismod, faucibus enim vitae, feugiat risus. Morbi in\\n\" +\n" +
                "                        \"                pulvinar dolor, vitae ultricies diam. Cras sed turpis nec elit laoreet ultricies non\\n\" +\n" +
                "                        \"                a velit. Cras vel nunc lacinia, malesuada felis ac, sodales mi. Aliquam erat\\n\" +\n" +
                "                        \"                volutpat. Donec varius dui sed diam hendrerit, sed scelerisque libero scelerisque.\\n\" +\n" +
                "                        \"                Maecenas pellentesque dapibus massa, a posuere magna. Nam sit amet aliquet dui.\\n\" +\n" +
                "                        \"                Quisque eget eros finibus, pharetra quam ut, rhoncus dui. Sed non urna et nisl\\n\" +\n" +
                "                        \"                semper rhoncus quis sed mauris. Mauris vitae posuere nibh. Cras rutrum diam quis\\n\" +\n" +
                "                        \"                diam sagittis placerat. Vivamus et auctor eros. Sed blandit ornare varius.\\n\" +\n" +
                "                        \"                Suspendisse consectetur commodo turpis sit amet ornare. Quisque consequat, est sed\\n\" +\n" +
                "                        \"                pharetra semper, metus sapien rutrum massa, ac suscipit ligula tortor id tortor.\\n\" +\n" +
                "                        \"\\n\" +\n" +
                "                        \"                Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis\\n\" +\n" +
                "                        \"                egestas. Nam vitae diam eu eros egestas consectetur ac vel nulla. Donec pellentesque\\n\" +\n" +
                "                        \"                nulla non molestie porta. Quisque dignissim dui felis, id congue erat pharetra eu.\\n\" +\n" +
                "                        \"                Etiam et justo maximus, luctus nisi sed, accumsan lorem. Sed laoreet lacinia ex et\\n\" +\n" +
                "                        \"                dignissim. Etiam ac diam ac leo interdum euismod at at tellus. Aenean vitae nisi ut\\n\" +\n" +
                "                        \"                dolor luctus molestie molestie sed quam. Morbi a vestibulum massa, eu malesuada\\n\" +\n" +
                "                        \"                justo. In feugiat aliquam convallis. Vivamus tincidunt erat at vestibulum accumsan.\\n\" +\n" +
                "                        \"                Suspendisse sed mattis ex. Interdum et malesuada fames ac ante ipsum primis in\\n\" +\n" +
                "                        \"                faucibus. Morbi ut metus eget sapien varius cursus quis eu ante. Sed sollicitudin\\n\" +\n" +
                "                        \"                odio non ligula lacinia, ut dapibus est hendrerit. Ut diam metus, mollis ac\\n\" +\n" +
                "                        \"                tristique a, volutpat et quam.\\n\" +\n" +
                "                        \"\\n\" +\n" +
                "                        \"                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque porta sodales\\n\" +\n" +
                "                        \"                orci id aliquet. Cras turpis massa, tincidunt vel magna dignissim, luctus pulvinar\\n\" +\n" +
                "                        \"                elit. In nisi arcu, finibus non pulvinar at, molestie vitae nisl. Aenean id erat non\\n\" +\n" +
                "                        \"                arcu hendrerit tristique vestibulum sit amet tellus. Aliquam erat volutpat. Donec\\n\" +\n" +
                "                        \"                viverra, ex sed egestas egestas, ante lacus blandit nisi, in sagittis ante dui vel\\n\" +\n" +
                "                        \"                ipsum. Curabitur congue, est eu rutrum porta, nulla risus malesuada purus, id\\n\" +\n" +
                "                        \"                molestie turpis sem ac risus. Etiam tempus orci eu arcu volutpat, ut auctor magna\\n\" +\n" +
                "                        \"                finibus. Nunc risus sem, tincidunt at massa tincidunt, placerat finibus nunc.\\n\" +\n" +
                "                        \"                Aliquam erat volutpat. Suspendisse potenti. Sed sed euismod enim. Morbi dignissim\\n\" +\n" +
                "                        \"                tempor nibh sed vehicula. Etiam condimentum neque a tortor dapibus, et vehicula diam\\n\" +\n" +
                "                        \"                feugiat. Curabitur feugiat augue nec porta vehicula.\"" +
                "\"Nam vehicula tellus euismod, faucibus enim vitae, feugiat risus. Morbi in\\n\" +\n" +
                "                        \"                pulvinar dolor, vitae ultricies diam. Cras sed turpis nec elit laoreet ultricies non\\n\" +\n" +
                "                        \"                a velit. Cras vel nunc lacinia, malesuada felis ac, sodales mi. Aliquam erat\\n\" +\n" +
                "                        \"                volutpat. Donec varius dui sed diam hendrerit, sed scelerisque libero scelerisque.\\n\" +\n" +
                "                        \"                Maecenas pellentesque dapibus massa, a posuere magna. Nam sit amet aliquet dui.\\n\" +\n" +
                "                        \"                Quisque eget eros finibus, pharetra quam ut, rhoncus dui. Sed non urna et nisl\\n\" +\n" +
                "                        \"                semper rhoncus quis sed mauris. Mauris vitae posuere nibh. Cras rutrum diam quis\\n\" +\n" +
                "                        \"                diam sagittis placerat. Vivamus et auctor eros. Sed blandit ornare varius.\\n\" +\n" +
                "                        \"                Suspendisse consectetur commodo turpis sit amet ornare. Quisque consequat, est sed\\n\" +\n" +
                "                        \"                pharetra semper, metus sapien rutrum massa, ac suscipit ligula tortor id tortor.\\n\" +\n" +
                "                        \"\\n\" +\n" +
                "                        \"                Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis\\n\" +\n" +
                "                        \"                egestas. Nam vitae diam eu eros egestas consectetur ac vel nulla. Donec pellentesque\\n\" +\n" +
                "                        \"                nulla non molestie porta. Quisque dignissim dui felis, id congue erat pharetra eu.\\n\" +\n" +
                "                        \"                Etiam et justo maximus, luctus nisi sed, accumsan lorem. Sed laoreet lacinia ex et\\n\" +\n" +
                "                        \"                dignissim. Etiam ac diam ac leo interdum euismod at at tellus. Aenean vitae nisi ut\\n\" +\n" +
                "                        \"                dolor luctus molestie molestie sed quam. Morbi a vestibulum massa, eu malesuada\\n\" +\n" +
                "                        \"                justo. In feugiat aliquam convallis. Vivamus tincidunt erat at vestibulum accumsan.\\n\" +\n" +
                "                        \"                Suspendisse sed mattis ex. Interdum et malesuada fames ac ante ipsum primis in\\n\" +\n" +
                "                        \"                faucibus. Morbi ut metus eget sapien varius cursus quis eu ante. Sed sollicitudin\\n\" +\n" +
                "                        \"                odio non ligula lacinia, ut dapibus est hendrerit. Ut diam metus, mollis ac\\n\" +\n" +
                "                        \"                tristique a, volutpat et quam.\\n\" +\n" +
                "                        \"\\n\" +\n" +
                "                        \"                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque porta sodales\\n\" +\n" +
                "                        \"                orci id aliquet. Cras turpis massa, tincidunt vel magna dignissim, luctus pulvinar\\n\" +\n" +
                "                        \"                elit. In nisi arcu, finibus non pulvinar at, molestie vitae nisl. Aenean id erat non\\n\" +\n" +
                "                        \"                arcu hendrerit tristique vestibulum sit amet tellus. Aliquam erat volutpat. Donec\\n\" +\n" +
                "                        \"                viverra, ex sed egestas egestas, ante lacus blandit nisi, in sagittis ante dui vel\\n\" +\n" +
                "                        \"                ipsum. Curabitur congue, est eu rutrum porta, nulla risus malesuada purus, id\\n\" +\n" +
                "                        \"                molestie turpis sem ac risus. Etiam tempus orci eu arcu volutpat, ut auctor magna\\n\" +\n" +
                "                        \"                finibus. Nunc risus sem, tincidunt at massa tincidunt, placerat finibus nunc.\\n\" +\n" +
                "                        \"                Aliquam erat volutpat. Suspendisse potenti. Sed sed euismod enim. Morbi dignissim\\n\" +\n" +
                "                        \"                tempor nibh sed vehicula. Etiam condimentum neque a tortor dapibus, et vehicula diam\\n\" +\n" +
                "                        \"                feugiat. Curabitur feugiat augue nec porta vehicula.\"" +
                "\"Nam vehicula tellus euismod, faucibus enim vitae, feugiat risus. Morbi in\\n\" +\n" +
                "                        \"                pulvinar dolor, vitae ultricies diam. Cras sed turpis nec elit laoreet ultricies non\\n\" +\n" +
                "                        \"                a velit. Cras vel nunc lacinia, malesuada felis ac, sodales mi. Aliquam erat\\n\" +\n" +
                "                        \"                volutpat. Donec varius dui sed diam hendrerit, sed scelerisque libero scelerisque.\\n\" +\n" +
                "                        \"                Maecenas pellentesque dapibus massa, a posuere magna. Nam sit amet aliquet dui.\\n\" +\n" +
                "                        \"                Quisque eget eros finibus, pharetra quam ut, rhoncus dui. Sed non urna et nisl\\n\" +\n" +
                "                        \"                semper rhoncus quis sed mauris. Mauris vitae posuere nibh. Cras rutrum diam quis\\n\" +\n" +
                "                        \"                diam sagittis placerat. Vivamus et auctor eros. Sed blandit ornare varius.\\n\" +\n" +
                "                        \"                Suspendisse consectetur commodo turpis sit amet ornare. Quisque consequat, est sed\\n\" +\n" +
                "                        \"                pharetra semper, metus sapien rutrum massa, ac suscipit ligula tortor id tortor.\\n\" +\n" +
                "                        \"\\n\" +\n" +
                "                        \"                Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis\\n\" +\n" +
                "                        \"                egestas. Nam vitae diam eu eros egestas consectetur ac vel nulla. Donec pellentesque\\n\" +\n" +
                "                        \"                nulla non molestie porta. Quisque dignissim dui felis, id congue erat pharetra eu.\\n\" +\n" +
                "                        \"                Etiam et justo maximus, luctus nisi sed, accumsan lorem. Sed laoreet lacinia ex et\\n\" +\n" +
                "                        \"                dignissim. Etiam ac diam ac leo interdum euismod at at tellus. Aenean vitae nisi ut\\n\" +\n" +
                "                        \"                dolor luctus molestie molestie sed quam. Morbi a vestibulum massa, eu malesuada\\n\" +\n" +
                "                        \"                justo. In feugiat aliquam convallis. Vivamus tincidunt erat at vestibulum accumsan.\\n\" +\n" +
                "                        \"                Suspendisse sed mattis ex. Interdum et malesuada fames ac ante ipsum primis in\\n\" +\n" +
                "                        \"                faucibus. Morbi ut metus eget sapien varius cursus quis eu ante. Sed sollicitudin\\n\" +\n" +
                "                        \"                odio non ligula lacinia, ut dapibus est hendrerit. Ut diam metus, mollis ac\\n\" +\n" +
                "                        \"                tristique a, volutpat et quam.\\n\" +\n" +
                "                        \"\\n\" +\n" +
                "                        \"                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque porta sodales\\n\" +\n" +
                "                        \"                orci id aliquet. Cras turpis massa, tincidunt vel magna dignissim, luctus pulvinar\\n\" +\n" +
                "                        \"                elit. In nisi arcu, finibus non pulvinar at, molestie vitae nisl. Aenean id erat non\\n\" +\n" +
                "                        \"                arcu hendrerit tristique vestibulum sit amet tellus. Aliquam erat volutpat. Donec\\n\" +\n" +
                "                        \"                viverra, ex sed egestas egestas, ante lacus blandit nisi, in sagittis ante dui vel\\n\" +\n" +
                "                        \"                ipsum. Curabitur congue, est eu rutrum porta, nulla risus malesuada purus, id\\n\" +\n" +
                "                        \"                molestie turpis sem ac risus. Etiam tempus orci eu arcu volutpat, ut auctor magna\\n\" +\n" +
                "                        \"                finibus. Nunc risus sem, tincidunt at massa tincidunt, placerat finibus nunc.\\n\" +\n" +
                "                        \"                Aliquam erat volutpat. Suspendisse potenti. Sed sed euismod enim. Morbi dignissim\\n\" +\n" +
                "                        \"                tempor nibh sed vehicula. Etiam condimentum neque a tortor dapibus, et vehicula diam\\n\" +\n" +
                "                        \"                feugiat. Curabitur feugiat augue nec porta vehicula.\""
        val encrypted = cryptoService!!.encrypt(plain)
        assertNotSame(plain, encrypted)
        assertTrue(encrypted.length > 0)
        val decrypted = cryptoService!!.decrypt(encrypted)
        assertEquals(plain, decrypted)
    }
}
