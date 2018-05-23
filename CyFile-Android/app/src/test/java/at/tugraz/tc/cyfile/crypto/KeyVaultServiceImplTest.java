package at.tugraz.tc.cyfile.crypto;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreSpi;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.UnrecoverableKeyException;

import javax.crypto.KeyGenerator;
import javax.crypto.KeyGeneratorSpi;

import at.tugraz.tc.cyfile.BaseUnitTest;
import at.tugraz.tc.cyfile.crypto.exceptions.InvalidPassPhraseException;
import at.tugraz.tc.cyfile.crypto.exceptions.KeyVaultAlreadyInitializedException;
import at.tugraz.tc.cyfile.crypto.exceptions.KeyVaultLockedException;
import at.tugraz.tc.cyfile.crypto.exceptions.KeyVaultNotInitializedException;
import at.tugraz.tc.cyfile.crypto.exceptions.KeyVaultServiceException;
import at.tugraz.tc.cyfile.crypto.impl.KeyVaultServiceImpl;
import at.tugraz.tc.cyfile.crypto.mocks.MockedKeyGenerator;
import at.tugraz.tc.cyfile.crypto.mocks.MockedKeyStore;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class KeyVaultServiceImplTest extends BaseUnitTest {

    KeyVaultServiceImpl keyVaultService;

    @Mock
    KeyGeneratorSpi keyGeneratorSpi;

    @Mock
    KeyStoreSpi keyStoreSpi;

    private final String correctPass = "test";
    private final String wrongPass = "wrong";
    private static final String KEY_ALIAS = "cyfile-encryption-key";

    @Before
    public void setup() {
        KeyGenerator keyGenerator = new MockedKeyGenerator(keyGeneratorSpi, mock(Provider.class), "test");
        keyGenerator.init(32);
        KeyStore keyStore = new MockedKeyStore(keyStoreSpi, mock(Provider.class), "test");
        when(keyStoreSpi.engineContainsAlias(any()))
                .thenReturn(true);

        try {
            when(keyStoreSpi.engineGetKey(KEY_ALIAS, correctPass.toCharArray()))
                    .thenReturn(new Key() {
                        @Override
                        public String getAlgorithm() {
                            return "test";
                        }

                        @Override
                        public String getFormat() {
                            return "test";
                        }

                        @Override
                        public byte[] getEncoded() {
                            return new byte[0];
                        }
                    });

            when(keyStoreSpi.engineGetKey(KEY_ALIAS, wrongPass.toCharArray()))
                    .thenThrow(UnrecoverableKeyException.class);
        } catch (NoSuchAlgorithmException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }

        keyVaultService = new KeyVaultServiceImpl(keyGenerator, keyStore);


    }

    @Test(expected = KeyVaultNotInitializedException.class)
    public void keyVaultShouldThrowExceptionWhenTryingToUnlockNotInitialized() {
        keyVaultService.unlockVault(correctPass);
    }

    @Test
    public void keyVaultServiceShouldUnlockWithoutErrorWhenPerformingInitAndUnlock() {
        keyVaultService.init(correctPass);
        keyVaultService.unlockVault(correctPass);
    }

    @Test
    public void keyVaultServiceShouldUnlockWithoutErrorWhenPerformingInitAndMultipleUnlock() {
        keyVaultService.init(correctPass);
        keyVaultService.unlockVault(correctPass);
        keyVaultService.unlockVault(correctPass);
    }

    @Test(expected = InvalidPassPhraseException.class)
    public void keyVaultServiceShouldThrowErrorWhenUnlockingWithWrongPassphrase() {
        keyVaultService.init(correctPass);
        keyVaultService.unlockVault(wrongPass);
    }

    @Test(expected = KeyVaultServiceException.class)
    public void keyVaultServiceShouldThrowErrorWhenAliasDoesNotExist() {
        when(keyStoreSpi.engineContainsAlias(any()))
                .thenReturn(false);
        keyVaultService.init(correctPass);
        keyVaultService.unlockVault(correctPass);
    }

    @Test(expected = KeyVaultAlreadyInitializedException.class)
    public void keyVaultServiceCannotBeInitializedTwice() {
        keyVaultService.init(correctPass);
        keyVaultService.init(correctPass);
    }

    @Test(expected = KeyVaultNotInitializedException.class)
    public void keyVaultServiceGetKeyShouldThrowErrorWhenNotInitialized() {
        keyVaultService.getEncryptionKey();
    }

    @Test(expected = KeyVaultLockedException.class)
    public void keyVaultServiceGetKeyShouldThrowErrorWhenLocked() {
        keyVaultService.init(correctPass);

        keyVaultService.getEncryptionKey();
    }

    @Test
    public void keyVaultServiceGetKeyShouldReturnKeyWithProperFlow() {
        keyVaultService.init(correctPass);
        keyVaultService.unlockVault(correctPass);

        keyVaultService.getEncryptionKey();
    }

    @Test(expected = KeyVaultLockedException.class)
    public void keyVaultServiceGetKeyShouldThrowErrorWhenLockedAfterUnlocking() {
        keyVaultService.init(correctPass);
        keyVaultService.unlockVault(correctPass);
        keyVaultService.lockVault();

        keyVaultService.getEncryptionKey();
    }

    @Test
    public void keyVaultServiceGetKeyShouldWorkAfterMultipleLockUnlocks() {
        keyVaultService.init(correctPass);
        keyVaultService.unlockVault(correctPass);

        keyVaultService.getEncryptionKey();

        keyVaultService.lockVault();

        keyVaultService.unlockVault(correctPass);

        keyVaultService.getEncryptionKey();
    }

    @Test(expected = KeyVaultNotInitializedException.class)
    public void keyVaultServiceGetKeyShouldThrowExceptionWhenLockingUninitializedKeyVault() {
        keyVaultService.lockVault();
    }

    @Test
    public void keyVaultMultipleLocksWontAffectCode() {
        keyVaultService.init(correctPass);
        keyVaultService.unlockVault(correctPass);
        keyVaultService.lockVault();
        keyVaultService.lockVault();
        keyVaultService.lockVault();
        keyVaultService.lockVault();
    }


}
