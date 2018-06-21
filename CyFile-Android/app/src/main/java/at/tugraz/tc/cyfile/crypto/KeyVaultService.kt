package at.tugraz.tc.cyfile.crypto

import at.tugraz.tc.cyfile.crypto.exceptions.InvalidPassPhraseException
import at.tugraz.tc.cyfile.crypto.exceptions.KeyVaultAlreadyInitializedException
import at.tugraz.tc.cyfile.crypto.exceptions.KeyVaultNotInitializedException
import java.security.Key


interface KeyVaultService {

    /**
     * To get the Key from KeyVault Storage.
     * You need to call unlockVault before!
     *
     * @return The key from the KeyVault
     * @throws KeyVaultLockedException         when vault is locked
     * @throws KeyVaultNotInitializedException when vault is not initialized
     */
    val encryptionKey: Key?


    /**
     * Initialize key vault with given passphrase, should be done only once
     *
     * @param passphrase passphrase that will used for key vault
     * @throws KeyVaultAlreadyInitializedException when init is already configured
     */
    @Throws(KeyVaultAlreadyInitializedException::class)
    fun init(passphrase: String)

    /**
     * Use this method to unlock the KeyVault. You need to unlock it in order to recieve a
     * Key with getEncryptionKey
     *
     * @param passphrase The Passphrase of the KeVault
     * @throws KeyVaultNotInitializedException when vault is not initialized
     * @throws InvalidPassPhraseException      when wrong passphrase is being passed
     */
    @Throws(KeyVaultNotInitializedException::class, InvalidPassPhraseException::class)
    fun unlockVault(passphrase: String)

    /**
     * To lock the Vault again.
     *
     * @throws KeyVaultNotInitializedException when not initialized
     */
    @Throws(KeyVaultNotInitializedException::class)
    fun lockVault()
}
