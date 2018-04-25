package at.tugraz.tc.cyfile.crypto;

import java.security.Key;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DummyKeyVaultService implements KeyVaultService {
    public static final String SUPER_SECURE_PASSWORD = "hunter2";
    private String algo;

    @Override
    public void unlockVault(String passphrase, String algo) {
        this.algo = algo;
    }

    @Override
    public void lockVault() {
        //nothing to see here, move along citizen!
    }

    @Override
    public Key getEncryptionKey() {
        return new SecretKeySpec(SUPER_SECURE_PASSWORD.getBytes(), algo);
    }
}
