package at.tugraz.tc.cyfile.crypto

import at.tugraz.tc.cyfile.BaseUnitTest
import at.tugraz.tc.cyfile.crypto.impl.PrefixCryptoService
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Created by mmalte on 05.06.18.
 */

class PrefixCryptoServiceUnitTest : BaseUnitTest() {

    private var cryptoService: PrefixCryptoService? = null

    @Before
    fun setup() {
        cryptoService = PrefixCryptoService()
    }

    @Test
    fun testEncryptWithStringReturnsWithPrefix() {
        val test = "test"
        val actual = cryptoService!!.encrypt(test)
        Assert.assertTrue(actual.contains("test"))
        Assert.assertTrue(actual.contains("PLEASE DON'T READ"))
    }

    @Test
    fun testDecryptWithStringReturnsWithoutPrefix() {
        val test = "PLEASE DON'T READ test"
        val actual = cryptoService!!.decrypt(test)
        Assert.assertTrue(actual.contains("test"))
        Assert.assertFalse(actual.contains("PLEASE DON'T READ"))
    }

}
