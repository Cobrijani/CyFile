package at.tugraz.tc.cyfile.crypto;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import java.security.InvalidKeyException;

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

public class AESCryptoServiceTest {

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


    //TODO this won't work until we reinitialize the cipher on every encrypt/decrypt
    @Ignore
    @Test
    public void testRelockedVault() {
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

    @Test
    public void testFailedUnlock() throws Exception {
        KeyVaultService svc = mock(KeyVaultService.class);
        Mockito.doThrow(new InvalidKeyException())
                .when(svc).unlockVault(any(), any());
        when(svc.getEncryptionKey()).thenReturn(null);

        cryptoService = new AESCryptoService(svc);
        try {
            cryptoService.init("any pass");
        } catch (InvalidKeyException e) {
            return;
        }
        fail("Exception should have been thrown");
    }

    //TODO tests with byte-arrays and not strings

    @Test
    public void testEncryptDecryptString() {
        setup(dummyKeyVaultService);
        String plain = "Hello World!";
        String encrypted = cryptoService.encrypt(plain);
        assertNotSame(plain, encrypted);
        assertTrue(encrypted.length() > 0);
        String decrypted = cryptoService.decrypt(encrypted);
        assertEquals(plain, decrypted);
    }

    @Test
    public void testEncryptEmptyString() {
        setup(dummyKeyVaultService);
        String plain = "";
        String encrypted = cryptoService.encrypt(plain);
        assertNotSame(plain, encrypted);
        assertTrue(encrypted.length() > 0);
        String decrypted = cryptoService.decrypt(encrypted);
        assertEquals(plain, decrypted);
    }

    @Test
    public void testEncryptNotBlockSizeAlignedString() {
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
    public void testEncryptBlockSizeAlignedString() {
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

    @Ignore
    @Test
    public void testEncryptSameStringMultipleTimes() {
        setup(dummyKeyVaultService);
        // TODO imho with a block-cipher this should return different strings.
        // i guess the .doFinal "resets" the cipher
        // will look into this
        String plain = "Hello World!";
        String encrypted1 = cryptoService.encrypt(plain);
        assertNotSame(plain, encrypted1);
        String encrypted2 = cryptoService.encrypt(plain);
        assertNotSame(plain, encrypted2);
        assertTrue(encrypted1.length() > 0);
        assertTrue(encrypted2.length() > 0);
        assertFalse(encrypted1.equals(encrypted2));
    }

    @Ignore
    @Test
    public void testEncryptDifferentCryptoServiceInstances () throws InvalidKeyException {
        //TODO this can't work until we initialize the iv for every encryption
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
    public void testEncryptLongString() {
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
