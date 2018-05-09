package at.tugraz.tc.cyfile.crypto.exceptions;

public class KeyVaultServiceException extends CryptoException {

    public KeyVaultServiceException() {
    }

    public KeyVaultServiceException(String message) {
        super(message);
    }

    public KeyVaultServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public KeyVaultServiceException(Throwable cause) {
        super(cause);
    }

}
