package at.tugraz.tc.cyfile.crypto.exceptions;

public class KeyVaultLockedException extends KeyVaultServiceException {

    public KeyVaultLockedException() {
    }

    public KeyVaultLockedException(String message) {
        super(message);
    }

    public KeyVaultLockedException(String message, Throwable cause) {
        super(message, cause);
    }

    public KeyVaultLockedException(Throwable cause) {
        super(cause);
    }

}
