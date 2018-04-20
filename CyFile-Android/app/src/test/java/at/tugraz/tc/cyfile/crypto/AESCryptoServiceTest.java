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

    //TODO: find out Block Size
    private int blockSize = 32;


    @Test
    public void testEncryptDecryptString () {
        String plain = "Hello World!";
        String encrypted = cryptoService.encrypt(plain);
        assertNotSame(plain, encrypted);
        assertTrue(encrypted.length() > 0);
        String decrypted = cryptoService.decrypt(encrypted);
        assertEquals(plain, decrypted);
    }

    @Test
    public void testEncryptEmptyString () {
        String plain = "";
        String encrypted = cryptoService.encrypt(plain);
        assertNotSame(plain, encrypted);
        assertTrue(encrypted.length() > 0);
        String decrypted = cryptoService.decrypt(encrypted);
        assertEquals(plain, decrypted);
    }

    @Test
    public void testEncryptNotBlockSizeAlignedString () {
        //TODO: Find out Block Size for Block Size unaligned String
        String plain = "Hello this is a not Block-Size aligned String!";
        assertNotSame(0, plain.length() % blockSize);
        String encrypted = cryptoService.encrypt(plain);
        assertNotSame(plain, encrypted);
        assertTrue(encrypted.length() > 0);
        String decrypted = cryptoService.decrypt(encrypted);
        assertEquals(plain, decrypted);
    }

    @Test
    public void testEncryptBlockSizeAlignedString () {
        //TODO: Find out Block Size for Block Size aligned String
        String plain = "This is gonna be a Block-Size aligned String, achieved by cropping!" +
                " In order to fit different block sizes, this has to be a quite large String." +
                " If it is too short, just append something at the end.";
        assertTrue(plain.length() >= blockSize);
        plain = plain.substring(0, blockSize);
        assertEquals(0, plain.length() % blockSize);
        String encrypted = cryptoService.encrypt(plain);
        assertNotSame(plain, encrypted);
        assertTrue(encrypted.length() > 0);
        String decrypted = cryptoService.decrypt(encrypted);
        assertEquals(plain, decrypted);
    }

    @Test
    public void testEncryptSameStringMultipleTimes () {
        String plain = "Hello World!";
        String encrypted1 = cryptoService.encrypt(plain);
        assertNotSame(plain, encrypted1);
        String encrypted2 = cryptoService.encrypt(plain);
        assertNotSame(plain, encrypted2);
        assertTrue(encrypted1.length() > 0);
        assertTrue(encrypted2.length() > 0);
        assertTrue(!encrypted1.equals(encrypted2));
    }
}
