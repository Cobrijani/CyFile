package at.tugraz.tc.cyfile.crypto;

import java.security.InvalidKeyException;
import java.security.Key;

public interface KeyVaultService {
    /**
     * Use this method to unlock the KeyVault. You need to unlock it in order to recieve a
     * Key with getEncryptionKey
     *
     * @param passphrase The Passphrase of the KeVault
     * @param algo The used encryption algorithm
     *
     * @throws InvalidKeyException
     */
    void unlockVault(String passphrase, String algo) throws InvalidKeyException;

    /**
     * To lock the Vault again.
     */
    void lockVault();

    /**
     * To get the Key from KeyVault Storage.
     * You need to call unlockVault before!!
     *
     * @return The key from the KeyVault
     */
    Key getEncryptionKey();
}
