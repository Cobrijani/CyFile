package at.tugraz.tc.cyfile.crypto;

import org.junit.Test;

import at.tugraz.tc.cyfile.crypto.exceptions.InvalidPassPhraseException;
import at.tugraz.tc.cyfile.crypto.exceptions.KeyVaultAlreadyInitializedException;
import at.tugraz.tc.cyfile.crypto.exceptions.KeyVaultLockedException;
import at.tugraz.tc.cyfile.crypto.exceptions.KeyVaultNotInitializedException;

public class KeyVaultServiceImplTest {


    KeyVaultServiceImpl keyVaultService;


    @Test(expected = KeyVaultNotInitializedException.class)
    public void keyVaultShouldThrowExceptionWhenTryingToUnlockNotInitialized() {
        keyVaultService = new KeyVaultServiceImpl();
        keyVaultService.unlockVault("test");
    }

    @Test
    public void keyVaultServiceShouldUnlockWithoutErrorWhenPerformingInitAndUnlock() {
        keyVaultService = new KeyVaultServiceImpl();

        keyVaultService.init("test");
        keyVaultService.unlockVault("test");
    }

    @Test(expected = InvalidPassPhraseException.class)
    public void keyVaultServiceShouldThrowErrorWhenUnlockingWithWrongPassphrase() {
        keyVaultService = new KeyVaultServiceImpl();

        keyVaultService.init("test");
        keyVaultService.unlockVault("wrong");
    }

    @Test(expected = KeyVaultAlreadyInitializedException.class)
    public void keyVaultServiceCannotBeInitializedTwice() {
        keyVaultService = new KeyVaultServiceImpl();

        keyVaultService.init("test");
        keyVaultService.init("test1");
    }

    @Test(expected = KeyVaultNotInitializedException.class)
    public void keyVaultServiceGetKeyShouldThrowErrorWhenNotInitialized() {
        keyVaultService = new KeyVaultServiceImpl();

        keyVaultService.getEncryptionKey();
    }

    @Test(expected = KeyVaultLockedException.class)
    public void keyVaultServiceGetKeyShouldThrowErrorWhenLocked() {
        keyVaultService = new KeyVaultServiceImpl();

        keyVaultService.init("test");

        keyVaultService.getEncryptionKey();
    }

    @Test
    public void keyVaultServiceGetKeyShouldReturnKeyWithProperFlow() {

        keyVaultService = new KeyVaultServiceImpl();

        keyVaultService.init("test");
        keyVaultService.unlockVault("test");

        keyVaultService.getEncryptionKey();
    }

    @Test(expected = KeyVaultLockedException.class)
    public void keyVaultServiceGetKeyShouldThrowErrorWhenLockedAfterUnlocking() {
        keyVaultService = new KeyVaultServiceImpl();

        keyVaultService.init("test");
        keyVaultService.unlockVault("test");
        keyVaultService.lockVault();

        keyVaultService.getEncryptionKey();
    }

    @Test
    public void keyVaultServiceGetKeyShouldWorkAfterMultipleLockUnlocks() {
        keyVaultService = new KeyVaultServiceImpl();

        keyVaultService.init("test");
        keyVaultService.unlockVault("test");

        keyVaultService.getEncryptionKey();

        keyVaultService.lockVault();

        keyVaultService.unlockVault("test");

        keyVaultService.getEncryptionKey();
    }

    @Test(expected = KeyVaultNotInitializedException.class)
    public void keyVaultServiceGetKeyShouldThrowExceptionWhenLockingUninitializedKeyVault() {
        keyVaultService = new KeyVaultServiceImpl();

        keyVaultService.lockVault();
    }

    @Test
    public void keyVaultMultipleLocksWontAffectCode() {
        keyVaultService = new KeyVaultServiceImpl();

        keyVaultService.init("test");
        keyVaultService.unlockVault("test");
        keyVaultService.lockVault();
        keyVaultService.lockVault();
        keyVaultService.lockVault();
        keyVaultService.lockVault();
    }


}
