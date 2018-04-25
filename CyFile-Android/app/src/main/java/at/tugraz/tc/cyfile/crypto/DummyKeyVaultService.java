package at.tugraz.tc.cyfile.crypto;

import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DummyKeyVaultService implements KeyVaultService {
    private String algo;
    private Key key;

    @Override
    public boolean unlockVault(String passphrase, String algo) {
        this.algo = algo;
        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            key = keyGenerator.generateKey();
            return true;
        } catch (NoSuchAlgorithmException e) {
            key = null;
            e.printStackTrace();
        }
        return false;
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
