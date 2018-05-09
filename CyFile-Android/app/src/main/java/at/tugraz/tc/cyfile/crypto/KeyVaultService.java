package at.tugraz.tc.cyfile.crypto;

import java.security.Key;

import at.tugraz.tc.cyfile.crypto.exceptions.InvalidPassPhraseException;
import at.tugraz.tc.cyfile.crypto.exceptions.KeyVaultAlreadyInitializedException;
import at.tugraz.tc.cyfile.crypto.exceptions.KeyVaultLockedException;
import at.tugraz.tc.cyfile.crypto.exceptions.KeyVaultNotInitializedException;

public interface KeyVaultService {


    /**
     * Initialize key vault with given passphrase, should be done only once
     *
     * @param passphrase passphrase that will used for key vault
     * @throws KeyVaultAlreadyInitializedException when init is already configured
     */
    void init(String passphrase) throws KeyVaultAlreadyInitializedException;

    /**
     * Use this method to unlock the KeyVault. You need to unlock it in order to recieve a
     * Key with getEncryptionKey
     *
     * @param passphrase The Passphrase of the KeVault
     * @throws KeyVaultNotInitializedException when vault is not initialized
     * @throws InvalidPassPhraseException      when wrong passphrase is being passed
     */
    void unlockVault(String passphrase) throws KeyVaultNotInitializedException, InvalidPassPhraseException;

    /**
     * To lock the Vault again.
     *
     * @throws KeyVaultNotInitializedException when not initialized
     */
    void lockVault() throws KeyVaultNotInitializedException;

    /**
     * To get the Key from KeyVault Storage.
     * You need to call unlockVault before!
     *
     * @return The key from the KeyVault
     * @throws KeyVaultLockedException         when vault is locked
     * @throws KeyVaultNotInitializedException when vault is not initialized
     */
    Key getEncryptionKey() throws KeyVaultLockedException, KeyVaultNotInitializedException;
}
