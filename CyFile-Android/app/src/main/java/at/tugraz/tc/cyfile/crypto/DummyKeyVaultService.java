package at.tugraz.tc.cyfile.crypto;

import java.security.Key;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DummyKeyVaultService implements KeyVaultService {
    public static final String SUPER_SECURE_PASSWORD = "hunter2";
    public static final String ALGO = "";

    @Override
    public boolean unlockVault(String passphrase) {
        return true;
    }

    @Override
    public void lockVault() {
        //nothing to see here, move along citizen!
    }

    @Override
    public Key getEncryptionKey(String algo) {
        return new SecretKeySpec(SUPER_SECURE_PASSWORD.getBytes(), algo);
    }
}
