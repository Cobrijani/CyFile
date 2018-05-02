package at.tugraz.tc.cyfile.crypto;

import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;

public class DummyKeyVaultService implements KeyVaultService {
    private Key key;

    @Override
    public void unlockVault(String passphrase, String algo) {
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
