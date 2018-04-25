package at.tugraz.tc.cyfile.crypto;

import java.security.InvalidKeyException;

import kotlin.NotImplementedError;

public class DefaultKeyVaultService implements KeyVaultService {
    @Override
    public boolean unlockVault(String passphrase) {
        throw new NotImplementedError();
    }

    @Override
    public void lockVault() {
        throw new NotImplementedError();
    }

    @Override
    public byte[] getEncryptionKey() throws InvalidKeyException {
        throw new NotImplementedError();
    }
}
