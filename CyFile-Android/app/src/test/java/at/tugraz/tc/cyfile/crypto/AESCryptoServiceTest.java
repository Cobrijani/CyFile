package at.tugraz.tc.cyfile.crypto;

import org.junit.Test;
import org.mockito.Mockito;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.KeyGenerator;

import at.tugraz.tc.cyfile.BaseUnitTest;
import at.tugraz.tc.cyfile.crypto.exceptions.InvalidCryptoOperationException;
import at.tugraz.tc.cyfile.crypto.exceptions.InvalidPassPhraseException;
import at.tugraz.tc.cyfile.crypto.impl.AESCryptoService;
import at.tugraz.tc.cyfile.crypto.impl.DummyKeyVaultService;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by david on 3/21/2018.
 */

public class AESCryptoServiceTest extends BaseUnitTest {

    private AESCryptoService cryptoService;
    private KeyVaultService dummyKeyVaultService = new DummyKeyVaultService();

    public void setup(KeyVaultService keyVaultService) {
        cryptoService = new AESCryptoService(keyVaultService);
        try {
            cryptoService.init("any pass");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    private int blockSize = 16;


    @Test(expected = InvalidCryptoOperationException.class)
    public void testRelockedVault()  throws InvalidCryptoOperationException{
        setup(dummyKeyVaultService);
        String plain = "Hello World!";
        String encrypted = cryptoService.encrypt(plain);

        assertNotSame(plain, encrypted);
        assertTrue(encrypted.length() > 0);

        //after we encrypted something we lock the vault
        dummyKeyVaultService.lockVault();
        String decrypted = cryptoService.decrypt(encrypted);
        assertNull(decrypted);
    }

    @Test (expected = InvalidPassPhraseException.class)
    public void testFailedUnlock() throws Exception {
        KeyVaultService svc = mock(KeyVaultService.class);
        Mockito.doThrow(new InvalidPassPhraseException())
                .when(svc).unlockVault(any());
        when(svc.getEncryptionKey()).thenReturn(null);

        cryptoService = new AESCryptoService(svc);
        cryptoService.init("any pass");
    }

    @Test(expected = InvalidCryptoOperationException.class)
    public void testEncryptChangePasswordDecryptFail() throws InvalidCryptoOperationException,
            NoSuchAlgorithmException {
        KeyGenerator keyGenerator;
        keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        Key key = keyGenerator.generateKey();
        Key key2 = keyGenerator.generateKey();
        KeyVaultService kvs = mock(KeyVaultService.class);
        setup(kvs);
        when(kvs.getEncryptionKey()).thenReturn(key);
        String plain = "Hello World!";
        byte[] encrypted = cryptoService.encrypt(plain.getBytes());
        assertNotSame(plain, encrypted);
        when(kvs.getEncryptionKey()).thenReturn(key2);

        // 16 for iv, then 16 for each started block of 16
        int expectedLength = (plain.length() / 16 + 2) * 16;
        assertEquals(encrypted.length, expectedLength);

        String decrypted = new String(cryptoService.decrypt(encrypted));
        assertFalse(plain.equals(decrypted));
    }


    @Test
    public void testEncryptDecryptString() throws InvalidCryptoOperationException {
        setup(dummyKeyVaultService);
        String plain = "Hello World!";
        byte[] encrypted = cryptoService.encrypt(plain.getBytes());
        assertNotSame(plain, encrypted);

        // 16 for iv, then 16 for each started block of 16
        int expectedLength = (plain.length() / 16 + 2) * 16;
        assertEquals(encrypted.length, expectedLength);

        String decrypted = new String(cryptoService.decrypt(encrypted));
        assertEquals(plain, decrypted);
    }

    @Test
    public void testEncryptEmptyString() throws InvalidCryptoOperationException {
        setup(dummyKeyVaultService);
        String plain = "";
        String encrypted = cryptoService.encrypt(plain);
        assertNotSame(plain, encrypted);
        assertTrue(encrypted.length() > 0);
        String decrypted = cryptoService.decrypt(encrypted);
        assertEquals(plain, decrypted);
    }

    @Test
    public void testEncryptNotBlockSizeAlignedString() throws InvalidCryptoOperationException {
        setup(dummyKeyVaultService);
        String plain = "Hello this is a not Block-Size aligned String!";
        assertNotSame(0, plain.length() % blockSize);
        String encrypted = cryptoService.encrypt(plain);
        assertNotSame(plain, encrypted);
        assertTrue(encrypted.length() > 0);
        String decrypted = cryptoService.decrypt(encrypted);
        assertEquals(plain, decrypted);
    }

    @Test
    public void testEncryptBlockSizeAlignedString() throws InvalidCryptoOperationException {
        setup(dummyKeyVaultService);
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
    public void testEncryptSameStringMultipleTimes() throws InvalidCryptoOperationException {
        setup(dummyKeyVaultService);
        String plain = "Hello World!";
        byte[] encrypted1 = cryptoService.encrypt(plain.getBytes());

        byte[] encrypted2 = cryptoService.encrypt(plain.getBytes());
        assertNotSame(plain, encrypted2);
        assertFalse(Arrays.equals(encrypted1, encrypted2));

        assertTrue(new String(cryptoService.decrypt(encrypted1))
                .equals(plain));
        assertTrue(new String(cryptoService.decrypt(encrypted2))
                .equals(plain));
    }

    @Test(expected = InvalidCryptoOperationException.class)
    public void testEncryptDifferentCryptoServiceInstances () throws InvalidKeyException, InvalidCryptoOperationException {
        setup(dummyKeyVaultService);
        String plain = "";
        String encrypted = cryptoService.encrypt(plain);
        assertNotSame(plain, encrypted);
        assertTrue(encrypted.length() > 0);
        AESCryptoService cryptoService2 = new AESCryptoService(dummyKeyVaultService);
        cryptoService2.init("any pass");
        String decrypted = cryptoService2.decrypt(encrypted);
        assertEquals(plain, decrypted);
    }

    @Test
    public void testEncryptLongString() throws InvalidCryptoOperationException {
        setup(dummyKeyVaultService);
        String plain = "\"Nam vehicula tellus euismod, faucibus enim vitae, feugiat risus. Morbi in\\n\" +\n" +
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
                "                        \"                feugiat. Curabitur feugiat augue nec porta vehicula.\"";
        String encrypted = cryptoService.encrypt(plain);
        assertNotSame(plain, encrypted);
        assertTrue(encrypted.length() > 0);
        String decrypted = cryptoService.decrypt(encrypted);
        assertEquals(plain, decrypted);
    }
}
