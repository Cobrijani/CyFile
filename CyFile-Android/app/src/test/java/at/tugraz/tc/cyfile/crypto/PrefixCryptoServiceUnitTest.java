package at.tugraz.tc.cyfile.crypto;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import at.tugraz.tc.cyfile.BaseUnitTest;
import at.tugraz.tc.cyfile.crypto.impl.AESCryptoService;
import at.tugraz.tc.cyfile.crypto.impl.ApacheCodecBase64;
import at.tugraz.tc.cyfile.crypto.impl.DummyKeyVaultService;
import at.tugraz.tc.cyfile.crypto.impl.PrefixCryptoService;

import static org.mockito.Mockito.spy;

/**
 * Created by mmalte on 05.06.18.
 */

public class PrefixCryptoServiceUnitTest extends BaseUnitTest {

    private PrefixCryptoService cryptoService;

    @Before
    public void setup() {
        cryptoService = new PrefixCryptoService();
    }

    @Test
    public void testEncryptWithStringReturnsWithPrefix() {
        String test = "test";
        String actual = cryptoService.encrypt(test);
        Assert.assertTrue(actual.contains("test"));
        Assert.assertTrue(actual.contains("PLEASE DON'T READ"));
    }

    @Test
    public void testDecryptWithStringReturnsWithoutPrefix() {
        String test = "PLEASE DON'T READ test";
        String actual = cryptoService.decrypt(test);
        Assert.assertTrue(actual.contains("test"));
        Assert.assertFalse(actual.contains("PLEASE DON'T READ"));
    }

}
