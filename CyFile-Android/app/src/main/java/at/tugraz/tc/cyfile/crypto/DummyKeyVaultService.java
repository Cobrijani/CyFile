package at.tugraz.tc.cyfile.crypto;

import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;

import at.tugraz.tc.cyfile.crypto.exceptions.KeyVaultAlreadyInitializedException;
import at.tugraz.tc.cyfile.crypto.exceptions.KeyVaultNotInitializedException;

public class DummyKeyVaultService implements KeyVaultService {
    private Key key;

    @Override
    public void init(String passphrase) throws KeyVaultAlreadyInitializedException {

    }

    @Override
    public void unlockVault(String passphrase) throws KeyVaultNotInitializedException {
        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            key = keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            key = null;
            e.printStackTrace();
        }
    }

    @Override
    public void lockVault() {
        key = null;
    }

    @Override
    public Key getEncryptionKey() {
        return key;
    }
}
