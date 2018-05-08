package at.tugraz.tc.cyfile.crypto.exceptions;

public class KeyVaultAlreadyInitializedException extends KeyVaultServiceException {


    public KeyVaultAlreadyInitializedException() {
    }

    public KeyVaultAlreadyInitializedException(String message) {
        super(message);
    }

    public KeyVaultAlreadyInitializedException(String message, Throwable cause) {
        super(message, cause);
    }

    public KeyVaultAlreadyInitializedException(Throwable cause) {
        super(cause);
    }

    public KeyVaultAlreadyInitializedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
