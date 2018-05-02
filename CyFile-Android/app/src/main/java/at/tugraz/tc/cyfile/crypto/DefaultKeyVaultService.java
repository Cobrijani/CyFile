package at.tugraz.tc.cyfile.crypto;

import java.security.InvalidKeyException;
import java.security.Key;

import kotlin.NotImplementedError;

public class DefaultKeyVaultService implements KeyVaultService {
    @Override
    public void unlockVault(String passphrase, String algo) throws InvalidKeyException {
        throw new NotImplementedError();
    }

    @Override
    public void lockVault() {
        throw new NotImplementedError();
    }

    @Override
    public Key getEncryptionKey(){
        throw new NotImplementedError();
    }
}
