package at.tugraz.tc.cyfile.crypto.exceptions;

public class KeyVaultNotInitializedException extends KeyVaultServiceException {

    public KeyVaultNotInitializedException() {
    }

    public KeyVaultNotInitializedException(String message) {
        super(message);
    }

    public KeyVaultNotInitializedException(String message, Throwable cause) {
        super(message, cause);
    }

    public KeyVaultNotInitializedException(Throwable cause) {
        super(cause);
    }

    public KeyVaultNotInitializedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
