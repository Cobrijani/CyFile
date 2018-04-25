package at.tugraz.tc.cyfile.crypto;

import java.security.InvalidKeyException;

public interface KeyVaultService {

    boolean unlockVault(String passphrase);

    void lockVault();

    byte[] getEncryptionKey() throws InvalidKeyException;
}
