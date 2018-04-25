package at.tugraz.tc.cyfile.crypto;

import java.security.InvalidKeyException;
import java.security.Key;

public interface KeyVaultService {
    boolean unlockVault(String passphrase, String algo) throws InvalidKeyException;

    void lockVault();

    Key getEncryptionKey();
}
