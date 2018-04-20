package at.tugraz.tc.cyfile.crypto;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;

/**
 * Created by david on 3/21/2018.
 */

public class AESCryptoServiceTest {

    private AESCryptoService cryptoService;

    @Before
    public void setup() {
        cryptoService = new AESCryptoService();
    }


    @Test
    public void testEncryptDecryptString () {
        String plain = "Hello World!";
        String encrypted = cryptoService.encrypt(plain);
        assertNotSame(plain, encrypted);
        assertTrue(encrypted.length() > 0);
        String decrypted = cryptoService.decrypt(encrypted);
        assertEquals(plain, decrypted);
    }

}
