package at.tugraz.tc.cyfile.crypto;

public class DummyKeyVaultService implements KeyVaultService {
    public static final String SUPER_SECURE_PASSWORD = "hunter2";

    @Override
    public boolean unlockVault(String passphrase) {
        return true;
    }

    @Override
    public void lockVault() {
        //nothing to see here, move along citizen!
    }

    @Override
    public byte[] getEncryptionKey() {
        return SUPER_SECURE_PASSWORD.getBytes();
    }
}
